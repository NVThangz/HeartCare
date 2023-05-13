import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { UserService } from './user.service';
import { CreateUserInput, User } from 'src/graphql';
import { UseGuards } from '@nestjs/common';
import { IsEmail } from 'class-validator';
import { AuthGuard } from '@nestjs/passport';

@Resolver('User')
export class UserResolver {
  constructor(private readonly userService: UserService) {}

  @Query(() => [User], {name: 'users'})
  findAll() {
    return this.userService.findAll();
  }

  @Query(() => User, {name: 'user'})
  findOne(@Args('email') email: string) {
    return this.userService.findOne(email);
  }


  
}
