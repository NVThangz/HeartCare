import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { PrismaService } from './prisma/prisma.service';
import { GraphQLModule } from '@nestjs/graphql';
import { ApolloDriver, ApolloDriverConfig } from '@nestjs/apollo';
import { ApolloServerPluginLandingPageLocalDefault } from '@apollo/server/plugin/landingPage/default';
import { join } from 'path';
import { UserModule } from './user/user.module';
import { AuthModule } from './auth/auth.module';
import { PrismaModule } from './prisma/prisma.module';
import { RecordsModule } from './records/records.module';
import { ProfilesModule } from './profiles/profiles.module';
import {
  constraintDirective,
  constraintDirectiveTypeDefs,
} from 'graphql-constraint-directive';
import { JwtAuthGuard } from './common/guards/jwt-auth.guard';
import { MailModule } from './mail/mail.module';
import { ConfigModule } from '@nestjs/config';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
    }),
    GraphQLModule.forRoot<ApolloDriverConfig>({
      driver: ApolloDriver,
      playground: false,
      plugins: [ApolloServerPluginLandingPageLocalDefault()],
      typePaths: ['./**/*.graphql'],
      definitions: {
        path: join(process.cwd(), 'src/graphql.ts'),
        outputAs: 'class',
      },
      typeDefs: constraintDirectiveTypeDefs,
      transformSchema: constraintDirective(),
    }),
    PrismaModule,
    UserModule,
    AuthModule,
    RecordsModule,
    ProfilesModule,
    MailModule,
  ],
  controllers: [AppController],
  providers: [
    AppService,
    PrismaService,
    // ,{
    //   provide: 'APP_GUARD',
    //   useClass: JwtAuthGuard,
    // }
  ],
})
export class AppModule {}
