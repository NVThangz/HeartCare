import { AuthInput, User } from 'src/graphql';
import { Injectable } from '@nestjs/common';
import { UserService } from 'src/user/user.service';
import { JwtService } from '@nestjs/jwt';
import * as bcrypt from 'bcrypt';
import { ForbiddenError } from '@nestjs/apollo';
import { createHash } from 'crypto';
import { MailService } from 'src/mail/mail.service';

interface TokenStore {
  [email: string]: { value: string; expire: Date };
}

@Injectable()
export class AuthService {
  private tokenStore: TokenStore = {};

  constructor(
    private mailService: MailService,
    private userService: UserService,
    private jwtService: JwtService,
  ) {}

  async login(user: User) {
    // const user = await this.validateUser(authInput);
    const { password, ...result } = user;
    const token = await this.getToken(user.id, user.email);
    await this.updateRefreshToken(user.id, token.refresh_token);
    return {
      access_token: token.access_token,
      refresh_token: token.refresh_token,
      user: result,
    };
  }

  async signup(authInput: AuthInput) {
    if (!this.checkEmailRegex(authInput.username)) {
      throw new ForbiddenError('Email invalid');
    }
    const password = await this.hashData(authInput.password);

    const isEmailExist = await this.userService.findOne(authInput.username);
    if (isEmailExist) {
      throw new ForbiddenError('Email does exist');
    }

    const user = await this.userService.createUser({ ...authInput, password });
    const token = await this.getToken(user.id, user.email);
    await this.updateRefreshToken(user.id, token.refresh_token);
    return {
      access_token: token.access_token,
      refresh_token: token.refresh_token,
      user: user,
    };
  }

  // async logout(userId: number) {
  //   const user = await this.userService.deleteRefreshToken(userId);
  //   if (user.count) return true;
  //   return false;
  // }

  async logout(email: string) {
    const user = await this.userService.deleteRefreshToken(email);
    if (user.count) return true;
    return false;
  }

  async refresh(userId: number, rt: string) {
    const user = await this.userService.findById(userId);
    if (!user) {
      throw new ForbiddenError('User does not exist');
    }
    if (!user.refreshToken) {
      throw new ForbiddenError('Refresh token does not exist');
    }
    const hashRt = createHash('sha256').update(rt).digest('hex');
    const valid = await bcrypt.compare(hashRt, user.refreshToken);
    if (!valid) {
      throw new ForbiddenError('Refresh token invalid');
    }
    const token = await this.getToken(user.id, user.email);
    await this.updateRefreshToken(user.id, token.refresh_token);
    return {
      access_token: token.access_token,
      refresh_token: token.refresh_token,
      user: user,
    };
  }

  async forgotPassword(email: string) {
    const user = await this.userService.findOne(email);
    if (!user) {
      throw new ForbiddenError('Email not exist');
    }
    const token = Math.floor(1000 + Math.random() * 9000).toString();
    const expire = new Date();
    expire.setMinutes(expire.getMinutes() + 5);
    this.tokenStore[email] = { value: token, expire };
    await this.mailService.sendUserConfirmation(email, token);
    return true;
  }

  async confirmForgotPassword(email: string, token: string) {
    const user = await this.userService.findOne(email);
    if (!user) {
      throw new ForbiddenError('Email not exist');
    }
    const tokenStore = this.tokenStore[email];
    if (!tokenStore) {
      throw new ForbiddenError('Otp code not exist');
    }
    if (tokenStore.value !== token) {
      throw new ForbiddenError('Otp code is incorrect');
    }
    if (tokenStore.expire < new Date()) {
      throw new ForbiddenError('Otp code expired');
    }
    delete this.tokenStore[email];
    return true;
  }

  async resetPasswordConfirmed(email: string, newPassword: string) {
    const user = await this.userService.changePassword(email, newPassword);
    if (!user) {
      throw new ForbiddenError('Email not exist');
    }
    const token = await this.getToken(user.id, user.email);
    await this.updateRefreshToken(user.id, token.refresh_token);
    return {
      access_token: token.access_token,
      refresh_token: token.refresh_token,
      user,
    };
  }

  async changePassword(
    email: string,
    oldPassword: string,
    newPassword: string,
  ) {
    if (oldPassword === newPassword) {
      throw new ForbiddenError(
        'New password must be different from old password',
      );
    }
    if (!(await this.userService.checkPassword(email, oldPassword))) {
      throw new ForbiddenError('Current password is incorrect');
    }
    const user = await this.userService.changePassword(email, newPassword);
    if (!user) {
      throw new ForbiddenError('Email not exist');
    }
    return true;
  }

  async registerWithSocial(email: string, name: string) {
    const emailExist = await this.userService.findOne(email);
    if (emailExist) {
      const token = await this.getToken(emailExist.id, emailExist.email);
      await this.updateRefreshToken(emailExist.id, token.refresh_token);
      return {
        access_token: token.access_token,
        refresh_token: token.refresh_token,
        user: emailExist,
      };
    }
    const user = await this.userService.createUserSocial(email, name);
    const token = await this.getToken(user.id, user.email);
    await this.updateRefreshToken(user.id, token.refresh_token);
    return {
      access_token: token.access_token,
      refresh_token: token.refresh_token,
      user: user,
    };
  }

  hashData(data: string) {
    return bcrypt.hash(data, 10);
  }

  encryptData(data: string) {
    const hash = createHash('sha256').update(data).digest('hex');
    return bcrypt.hashSync(hash, 10);
  }

  async updateRefreshToken(userId: number, rt: string) {
    const refreshToken = await this.encryptData(rt);
    return this.userService.updateRefreshToken(userId, refreshToken);
  }

  async getToken(userId: number, email: string) {
    const [at, rt] = await Promise.all([
      this.jwtService.signAsync(
        { sub: userId, email },
        { secret: process.env.SECRET_AT_KEY, expiresIn: '1h' },
      ),
      this.jwtService.signAsync(
        { sub: userId, email },
        { secret: process.env.SECRET_RT_KEY, expiresIn: '7d' },
      ),
    ]);
    return {
      access_token: at,
      refresh_token: rt,
    };
  }

  checkEmailRegex(email: string) {
    const regex = new RegExp('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$');
    return regex.test(email);
  }

  async validateUser(authInput: AuthInput): Promise<any> {
    const user = await this.userService.findOne(authInput.username);
    if (!user) {
      throw new ForbiddenError('Email not exist');
    }
    if (user.password === null) {
      throw new ForbiddenError(
        'Email is social account, please login with social',
      );
    }
    const valid = await bcrypt.compare(authInput.password, user.password);
    if (!valid) {
      throw new ForbiddenError('Password is incorrect');
    }
    const { password, ...result } = user;
    return result;
  }
}
