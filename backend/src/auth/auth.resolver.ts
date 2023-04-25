import { Args, Context, Mutation, Resolver } from '@nestjs/graphql';
import { AuthService } from './auth.service';
import { LoginResponse, LoginUserInput, User } from 'src/graphql';
import { UseGuards } from '@nestjs/common';
import { GqlAuthGuard } from './dto/gql-auth.guard';

// import { LoginUserInput } from 'src/graphql';

@Resolver()
export class AuthResolver {
    constructor(private authService: AuthService) {}

    @Mutation(() => LoginResponse)
    @UseGuards(GqlAuthGuard)
    login(@Args('loginUserInput') loginUserInput: LoginUserInput, @Context() context) {
        return this.authService.login(context.user);
    }

    @Mutation('signup')
    signup(@Args('loginUserInput') loginUserInput: LoginUserInput) {
        return this.authService.signup(loginUserInput);
    }


}
