import { AuthInput, User } from 'src/graphql';
import { Injectable } from '@nestjs/common';
import { UserService } from 'src/user/user.service';
import { JwtService } from '@nestjs/jwt';
import * as bcrypt from 'bcrypt';
import { ForbiddenError } from '@nestjs/apollo';
import { createHash } from 'crypto';

@Injectable()
export class AuthService {
  constructor(
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
    const password = await this.hashData(authInput.password);

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
      throw new ForbiddenError('user not found');
    }
    if (!user.refreshToken) {
      throw new ForbiddenError('Refresh token not found');
    }
    const hashRt = createHash('sha256').update(rt).digest('hex');
    const valid = await bcrypt.compare(hashRt, user.refreshToken);
    console.log(valid);
    if (!valid) {
      throw new ForbiddenError('Refresh token not valid');
    }
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

  async validateUser(username: string, password: string): Promise<any> {
    const user = await this.userService.findOne(username);
    const valid = await bcrypt.compare(password, user?.password);
    if (user && valid) {
      const { password, ...result } = user;
      return result;
    } else throw new ForbiddenError('user not found');
  }
}
