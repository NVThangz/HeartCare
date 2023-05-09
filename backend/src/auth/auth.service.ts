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
      throw new ForbiddenError('Email không hợp lệ');
    }
    const password = await this.hashData(authInput.password);

    const isEmailExist = await this.userService.findOne(authInput.username);
    if (isEmailExist) {
      throw new ForbiddenError('Email đã tồn tại');
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

  async logout(userId: number) {
    const user = await this.userService.deleteRefreshToken(userId);
    if (user.count) return true;
    return false;
  }

  async refresh(userId: number, rt: string) {
    const user = await this.userService.findById(userId);
    if (!user) {
      throw new ForbiddenError('User không tồn tại');
    }
    if (!user.refreshToken) {
      throw new ForbiddenError('Refresh token không tồn tại');
    }
    const hashRt = createHash('sha256').update(rt).digest('hex');
    const valid = await bcrypt.compare(hashRt, user.refreshToken);
    console.log(valid);
    if (!valid) {
      throw new ForbiddenError('Refresh token không hợp lệ');
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
      throw new ForbiddenError('Email không tồn tại');
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
      throw new ForbiddenError('Email không tồn tại');
    }
    const tokenStore = this.tokenStore[email];
    if (!tokenStore) {
      throw new ForbiddenError('Token không tồn tại');
    }
    if (tokenStore.value !== token) {
      throw new ForbiddenError('Token không hợp lệ');
    }
    if (tokenStore.expire < new Date()) {
      throw new ForbiddenError('Token đã hết hạn');
    }
    delete this.tokenStore[email];
    return true;
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
    const valid = await bcrypt.compare(authInput.password, user.password);
    if (user && valid) {
      const { password, ...result } = user;
      return result;
    } else throw new ForbiddenError('user not found');
  }
}
