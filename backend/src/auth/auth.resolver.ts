import { Args, Context, Mutation, Resolver } from '@nestjs/graphql';
import { AuthService } from './auth.service';
import { LoginResponse, AuthInput, User } from 'src/graphql';
import { Req, UseGuards, Headers, HttpCode, HttpStatus } from '@nestjs/common';
import { GqlAuthGuard } from '../common/guards/gql-auth.guard';
import { AuthGuard } from '@nestjs/passport';
import { JwtAuthGuard } from 'src/common/guards/jwt-auth.guard';
import { JwtRefreshAuthGuard } from 'src/common/guards/jwt-refresh-auth.guard';
import { Public } from 'src/common/decorator/public.decorator';

// import { LoginUserInput } from 'src/graphql';

@Resolver()
export class AuthResolver {
  constructor(private authService: AuthService) {}

  @Public()
  @Mutation(() => LoginResponse)
  @UseGuards(GqlAuthGuard)
  @HttpCode(HttpStatus.CREATED)
  login(@Args('AuthInput') authInput: AuthInput, @Context() context) {
    return this.authService.login(context.user);
  }

  @Public()
  @Mutation('signup')
  @HttpCode(HttpStatus.OK)
  signup(@Args('AuthInput') authInput: AuthInput) {
    return this.authService.signup(authInput);
  }

  @Mutation('logout')
  @UseGuards(JwtAuthGuard)
  @HttpCode(HttpStatus.OK)
  logout(@Context() context) {
    return this.authService.logout(context.req.user['sub']);
  }

  @Public()
  @UseGuards(JwtRefreshAuthGuard)
  @Mutation('refresh')
  @HttpCode(HttpStatus.OK)
  refresh(@Context() context) {
    const rt = context.req.headers['authorization'].split(' ')[1];
    return this.authService.refresh(context.req.user['sub'], rt);
  }
}
