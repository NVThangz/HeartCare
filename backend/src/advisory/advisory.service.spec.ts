import { Test, TestingModule } from '@nestjs/testing';
import { AdvisoryService } from './advisory.service';

describe('AdvisoryService', () => {
  let service: AdvisoryService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [AdvisoryService],
    }).compile();

    service = module.get<AdvisoryService>(AdvisoryService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});
