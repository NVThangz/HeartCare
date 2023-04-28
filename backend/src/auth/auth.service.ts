import { LoginUserInput, User } from 'src/graphql';
import { Injectable } from '@nestjs/common';
import { UserService } from 'src/user/user.service';
import { JwtService } from '@nestjs/jwt';
import * as bycrypt from 'bcrypt';

@Injectable()
export class AuthService {
  constructor(
    private userService: UserService,
    private jwtService: JwtService
  ) {}

  async validateUser(username: string, password: string): Promise<any> {
    const user = await this.userService.findOne(username);
    const valid = await bycrypt.compare(password, user?.password);
    if (user && valid) {
      const { password, ...result } = user;
      return result;
    }
    return null;
  }

  async login(user: User) {
    console.log('user', user);
    const { password , ...result } = user;
    return {
      access_token: this.jwtService.sign({username: user.email, sub: user.id }),
      user: result,
    };
  }

  async signup(loginUserInput: LoginUserInput) {
    const password = await bycrypt.hash(loginUserInput.password, 10);

    return await this.userService.createUser({ ...loginUserInput, password})
  }

}
