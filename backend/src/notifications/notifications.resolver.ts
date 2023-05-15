import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { NotificationsService } from './notifications.service';
import { CreateNotificationInput, UpdateNotificationInput } from 'src/graphql';


@Resolver('Notification')
export class NotificationsResolver {
  constructor(private readonly notificationsService: NotificationsService) {}

  @Mutation('createNotification')
  create(@Args('createNotificationInput') createNotificationInput: CreateNotificationInput) {
    return this.notificationsService.create(createNotificationInput);
  }

  @Mutation('createNotificationAll')
  createNotificationAll(@Args('title') title: string, @Args('content') content: string, @Args('createdAt') createdAt: Date) {
    return this.notificationsService.createNotificationAll(title, content, createdAt);
  }

  @Query('notifications')
  findAll() {
    return this.notificationsService.findAll();
  }


  @Query('findNotificationsWithEmail')
  findNotificationsWithEmail(@Args('email') email: string) {
    return this.notificationsService.findNotificationsWithEmail(email);
  }

  @Query('findNotificationsAll')
  findNotificationsAll() {
    return this.notificationsService.findNotificationsAll();
  }

  @Mutation('updateNotification')
  update(@Args('updateNotificationInput') updateNotificationInput: UpdateNotificationInput) {
    return this.notificationsService.update(updateNotificationInput.id, updateNotificationInput);
  }

  @Mutation('removeNotification')
  remove(@Args('id') id: number) {
    return this.notificationsService.remove(id);
  }
}
