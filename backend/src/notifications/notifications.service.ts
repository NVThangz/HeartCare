import { Injectable } from '@nestjs/common';
import { CreateNotificationInput, UpdateNotificationInput } from 'src/graphql';
import { PrismaService } from 'src/prisma/prisma.service';

@Injectable()
export class NotificationsService {
  constructor(private prisma: PrismaService) {}

  create(createNotificationInput: CreateNotificationInput) {
    return this.prisma.notification.create({
      data: {
        title: createNotificationInput.title,
        content: createNotificationInput.content,
        createdAt: createNotificationInput.createdAt,
        user: {
          connect: {
            email: createNotificationInput.email,
          },
        },
      },
    });
  }
  

  createNotificationAll(title: string, content: string, createdAt: Date) {
    return this.prisma.notification.create({
      data: {
        title,
        content,
        createdAt,
      },
    });
  }

  async findAll() {
    const notifications = await this.prisma.notification.findMany();
    return notifications.sort((a, b) => {return b.createdAt.getTime() - a.createdAt.getTime()});
  }

  async findNotificationsWithEmail(email: string) {
    const notifications = await this.prisma.notification.findMany({
      where: {
        OR: [
          {
            user: {
              email,
            },
          },
          {
            userId: null,
          },
        ],
      },
    });
    return notifications.sort((a, b) => {return b.createdAt.getTime() - a.createdAt.getTime()});
  }

  async findNotificationsAll() {
    const notifications = await this.prisma.notification.findMany({
      where: {
        userId: null,
      },
    });
    return notifications.sort((a, b) => {return b.createdAt.getTime() - a.createdAt.getTime()});
  }

  update(id: number, updateNotificationInput: UpdateNotificationInput) {
    return this.prisma.notification.update({
      where: {
        id,
      },
      data: {
        title: updateNotificationInput.title,
        content: updateNotificationInput.content,
        createdAt: updateNotificationInput.createdAt,
      },
    });
  }

  remove(id: number) {
    return this.prisma.notification.delete({
      where: {
        id,
      },
    });
  }
}
