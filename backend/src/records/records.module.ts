import { Module } from '@nestjs/common';
import { RecordsService } from './records.service';
import { RecordsResolver } from './records.resolver';

@Module({
  providers: [RecordsResolver, RecordsService],
  exports: [RecordsService]
})
export class RecordsModule {}
