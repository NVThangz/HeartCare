import { Module } from '@nestjs/common';
import { HistoriesService } from './histories.service';
import { HistoriesResolver } from './histories.resolver';
import { UserModule } from 'src/user/user.module';

@Module({
  imports: [UserModule],
  providers: [HistoriesResolver, HistoriesService],
  exports: [HistoriesService]
})
export class HistoriesModule {}
