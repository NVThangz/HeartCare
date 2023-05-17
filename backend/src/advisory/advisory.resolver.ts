import { Resolver, Mutation, Args } from '@nestjs/graphql';
import { AdvisoryService } from './advisory.service';
import { AdvisoryInput } from 'src/graphql';

@Resolver('Advisory')
export class AdvisoryResolver {
  constructor(private readonly advisoryService: AdvisoryService) {}

  @Mutation('getAdvisoryFirst')
  getAdvisoryFirst(@Args('email') email: string, @Args('lang') lang: string) {
    return this.advisoryService.getAdvisoryFirst(email, lang);
  }

  @Mutation('getAdvisory')
  getAdvisory(@Args('email') email: string, @Args('question') question: string) {
    return this.advisoryService.getAdvisory(email, question);
  }
  
}
