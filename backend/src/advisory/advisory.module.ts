import { Module } from '@nestjs/common';
import { AdvisoryService } from './advisory.service';
import { AdvisoryResolver } from './advisory.resolver';
import { ProfilesModule } from 'src/profiles/profiles.module';
import { RecordsModule } from 'src/records/records.module';

@Module({
  imports: [ProfilesModule, RecordsModule],
  providers: [AdvisoryResolver, AdvisoryService],
})
export class AdvisoryModule {}
