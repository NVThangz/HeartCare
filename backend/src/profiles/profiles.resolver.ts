import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { ProfilesService } from './profiles.service';
import { UpdateProfileInput } from 'src/graphql';

@Resolver('Profile')
export class ProfilesResolver {
  constructor(private readonly profilesService: ProfilesService) {}

  @Mutation('createProfile')
  create(@Args('userId') userId: number) {
    return this.profilesService.create(userId);
  }

  @Query('profile')
  findOne(@Args('email') email: string) {
    return this.profilesService.findOne(email);
  }

  @Mutation('updateProfile')
  update(@Args('updateProfileInput') updateProfileInput: UpdateProfileInput) {
    return this.profilesService.update(updateProfileInput);
  }
}
