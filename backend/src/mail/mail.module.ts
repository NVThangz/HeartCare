import { MailerModule } from '@nestjs-modules/mailer';
import { HandlebarsAdapter } from '@nestjs-modules/mailer/dist/adapters/handlebars.adapter';
import { Module } from '@nestjs/common';
import { MailService } from './mail.service';
import { UserModule } from 'src/user/user.module';
import { ConfigService } from '@nestjs/config';

@Module({
  imports: [
    UserModule,
    // MailerModule.forRoot({
    //   transport: {
    //     host: 'smtp.example.com',
    //     secure: false,
    //     auth: {
    //       user: 'user@example.com',
    //       pass: 'topsecret',
    //     },
    //   },
    //   defaults: {
    //     from: '"No Reply" <noreply@example.com>',
    //   },
    //   template: {
    //     dir: join(__dirname, 'templates'),
    //     adapter: new HandlebarsAdapter(), // or new PugAdapter() or new EjsAdapter()
    //     options: {
    //       strict: true,
    //     },
    //   },
    // }),
    MailerModule.forRoot({
      transport: 'smtps://user@domain.com:pass@smtp.domain.com',
      template: {
        dir: process.cwd() + '/src/mail/templates/',
        adapter: new HandlebarsAdapter(),
        options: {
          strict: true,
        },
      },
    }),
  ],
  providers: [MailService, ConfigService],
  exports: [MailService], // 👈 export for DI
})

export class MailModule {}
