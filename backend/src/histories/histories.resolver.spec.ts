import { Test, TestingModule } from '@nestjs/testing';
import { HistoriesResolver } from './histories.resolver';
import { HistoriesService } from './histories.service';

describe('HistoriesResolver', () => {
  let resolver: HistoriesResolver;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [HistoriesResolver, HistoriesService],
    }).compile();

    resolver = module.get<HistoriesResolver>(HistoriesResolver);
  });

  it('should be defined', () => {
    expect(resolver).toBeDefined();
  });
});
