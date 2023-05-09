import { Test, TestingModule } from '@nestjs/testing';
import { AdvisoryResolver } from './advisory.resolver';
import { AdvisoryService } from './advisory.service';

describe('AdvisoryResolver', () => {
  let resolver: AdvisoryResolver;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [AdvisoryResolver, AdvisoryService],
    }).compile();

    resolver = module.get<AdvisoryResolver>(AdvisoryResolver);
  });

  it('should be defined', () => {
    expect(resolver).toBeDefined();
  });
});
