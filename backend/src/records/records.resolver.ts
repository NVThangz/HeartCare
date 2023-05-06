import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { RecordsService } from './records.service';
import { UpdateRecordInput } from 'src/graphql';

@Resolver('Record')
export class RecordsResolver {
  constructor(private readonly recordsService: RecordsService) {}
  
  @Query('record')
  findOne(@Args('email') email: string) {
    return this.recordsService.findOne(email);
  }

  @Mutation('updateRecord')
  update(@Args('updateRecordInput') updateRecordInput: UpdateRecordInput) {
    return this.recordsService.update(updateRecordInput);
  }

}
