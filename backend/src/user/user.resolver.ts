import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { UserService } from './user.service';
import { CreateUserInput, User } from 'src/graphql';
import { UseGuards } from '@nestjs/common';
import { IsEmail } from 'class-validator';
import { AuthGuard } from '@nestjs/passport';

@Resolver('User')
export class UserResolver {
  constructor(private readonly userService: UserService) {}

  @Mutation('createUser')
  createUser(@Args('createUserInput') createUserInput: CreateUserInput) {
    return this.userService.createUser(createUserInput);
  }

  // @Mutation('createUserDoctor')
  // createUserDoctor(@Args('createUserInput') createUserInput: CreateUserInput) {
  //   return this.userService.createUserDoctor(createUserInput);
  // }

  @Query(() => [User], {name: 'users'})
  findAll() {
    return this.userService.findAll();
  }

  @Query(() => User, {name: 'user'})
  findOne(@Args('email') email: string) {
    return this.userService.findOne(email);
  }

  // @Mutation('updateUser')
  // update(@Args('updateUserInput') updateUserInput: UpdateUserInput) {
  //   return this.userService.update(updateUserInput.id, updateUserInput);
  // }

  // @Mutation('removeUser')
  // remove(@Args('id') id: number) {
  //   return this.userService.remove(id);
  // }
}
