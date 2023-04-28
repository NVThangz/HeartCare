import { Module } from '@nestjs/common';
import { UserService } from './user.service';
import { UserResolver } from './user.resolver';
import { ProfilesModule } from 'src/profiles/profiles.module';
import { RecordsModule } from 'src/records/records.module';

@Module({
  providers: [UserResolver, UserService],
  exports: [UserService],
})
export class UserModule {}
