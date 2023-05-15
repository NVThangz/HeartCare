import { Injectable } from '@nestjs/common';
import { CreateHistoryInput } from './dto/create-history.input';
import { UpdateHistoryInput } from './dto/update-history.input';
import { PrismaService } from 'src/prisma/prisma.service';
import { UserService } from 'src/user/user.service';
import { copyFileSync } from 'fs';

@Injectable()
export class HistoriesService {
  constructor(
    private prisma: PrismaService,
    private userService: UserService,
  ) {}

  create(email: string, bpm: number) {
    return this.prisma.history.create({
      data: {
        bpm,
        user: {
          connect: {
            email,
          },
        },
        // createdAt: new Date(),
      },
    });
  }

  findOne(email: string) {
    return this.prisma.history.findMany({
      where: {
        user: {
          email,
        },
      },
    });
  }

  findLatest(email: string) {
    return this.prisma.history.findMany({
      where: {
        user: {
          email,
        },
      },
      orderBy: {
        createdAt: 'desc',
      },
      take: 1,
    });
  }

  findTodayHistory(email: string) {
  const now = new Date();
  const offsetInMs = now.getTimezoneOffset() * 60 * 1000;
  const startOfDayUtc = new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate()));
  const endOfDayUtc = new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate() + 1));
  const startOfDayUtcPlus7 = new Date(startOfDayUtc.getTime() + offsetInMs);
  const endOfDayUtcPlus7 = new Date(endOfDayUtc.getTime() + offsetInMs);
  // console.log(startOfDayUtcPlus7, endOfDayUtcPlus7);
    return this.prisma.history.findMany({
      where: {
        user: {
          email,
        },
        createdAt: {
          gte: startOfDayUtcPlus7,
          lte: endOfDayUtcPlus7,
        },
      },
    });
  }
  
  findWeekHistory(email: string) {
    const now = new Date();
    const firstDay = new Date(now.setDate(now.getDate() - (now.getDay() || 7) + 1));
    const offsetInMs = now.getTimezoneOffset() * 60 * 1000;
    const startOfWeekUtc = new Date(Date.UTC(firstDay.getFullYear(), firstDay.getMonth(), firstDay.getDate() - firstDay.getDay()+1));
    const endOfWeekUtc = new Date(Date.UTC(firstDay.getFullYear(), firstDay.getMonth(), firstDay.getDate() - firstDay.getDay() + 8));
    const startOfWeekUtcPlus7 = new Date(startOfWeekUtc.getTime() + offsetInMs);
    const endOfWeekUtcPlus7 = new Date(endOfWeekUtc.getTime() + offsetInMs);
    // console.log(startOfWeekUtcPlus7, endOfWeekUtcPlus7);
    return this.prisma.history.findMany({
      where: {
        user: {
          email,
        },
        createdAt: {
          gte: startOfWeekUtcPlus7,
          lte: endOfWeekUtcPlus7,
        },
      },
    });
  }
}
