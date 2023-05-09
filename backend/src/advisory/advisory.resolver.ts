import { Resolver, Mutation, Args } from '@nestjs/graphql';
import { AdvisoryService } from './advisory.service';
import { AdvisoryInput } from 'src/graphql';

@Resolver('Advisory')
export class AdvisoryResolver {
  constructor(private readonly advisoryService: AdvisoryService) {}

  @Mutation('getAdvisory')
  getAdvisory(@Args('advisoryInput') advisoryInput: AdvisoryInput) {
    return this.advisoryService.getAdvisory(advisoryInput.email, advisoryInput.question);
  }
  
}
