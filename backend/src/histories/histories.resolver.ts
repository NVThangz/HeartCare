import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { HistoriesService } from './histories.service';
import { CreateHistoryInput } from './dto/create-history.input';
import { UpdateHistoryInput } from './dto/update-history.input';

@Resolver('History')
export class HistoriesResolver {
  constructor(private readonly historiesService: HistoriesService) {}

  @Mutation('createHistory')
  create(@Args('email') email: string, @Args('bpm') bpm: number) {
    return this.historiesService.create(email, bpm);
  }
  
  @Query('history')
  findOne(@Args('email') email: string) {
    return this.historiesService.findOne(email);
  }
}
