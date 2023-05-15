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

  async todayHistoryStatistics(email: string) {
    const now = new Date();
    const offsetInMs = now.getTimezoneOffset() * 60 * 1000;
    const startOfDayUtc = new Date(
      Date.UTC(now.getFullYear(), now.getMonth(), now.getDate()),
    );
    const endOfDayUtc = new Date(
      Date.UTC(now.getFullYear(), now.getMonth(), now.getDate() + 1),
    );
    const startOfDayUtcPlus7 = new Date(startOfDayUtc.getTime() + offsetInMs);
    const endOfDayUtcPlus7 = new Date(endOfDayUtc.getTime() + offsetInMs);
    // console.log(startOfDayUtcPlus7, endOfDayUtcPlus7);
    const histories = await this.prisma.history.findMany({
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
    
    const bpmValues = histories.map((item) => item.bpm);
    const bpmAvg = Math.round(bpmValues.reduce((a, b) => a + b, 0) / bpmValues.length);
    const bpmMax = Math.max(...bpmValues);
    const bpmMin = Math.min(...bpmValues);
    const stats = {
      average: bpmAvg,
      max: bpmMax,
      min: bpmMin,
      chartData: histories.map((item) => ({
        value: () => {
          const date = new Date(item.createdAt);
          return date.getHours() + Number((date.getMinutes()/ 60).toFixed(2));
        },
        bpm: item.bpm,
      })).sort((a, b) => a.value() - b.value()),
    };
    return stats;
  }

  async weekHistoryStatistics(email: string) {
    const now = new Date();
    const firstDay = new Date(
      now.setDate(now.getDate() - (now.getDay() || 7) + 1),
    );
    const offsetInMs = now.getTimezoneOffset() * 60 * 1000;
    const startOfWeekUtc = new Date(
      Date.UTC(
        firstDay.getFullYear(),
        firstDay.getMonth(),
        firstDay.getDate() - firstDay.getDay() + 1,
      ),
    );
    const endOfWeekUtc = new Date(
      Date.UTC(
        firstDay.getFullYear(),
        firstDay.getMonth(),
        firstDay.getDate() - firstDay.getDay() + 8,
      ),
    );
    const startOfWeekUtcPlus7 = new Date(startOfWeekUtc.getTime() + offsetInMs);
    const endOfWeekUtcPlus7 = new Date(endOfWeekUtc.getTime() + offsetInMs);
    // console.log(startOfWeekUtcPlus7, endOfWeekUtcPlus7);
    const histories = await this.prisma.history.findMany({
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
    const bpmValues = histories.map((item) => item.bpm);
    const bpmAvg = Math.round(bpmValues.reduce((a, b) => a + b, 0) / bpmValues.length);
    const bpmMax = Math.max(...bpmValues);
    const bpmMin = Math.min(...bpmValues);
    const stats = {
      average: bpmAvg,
      max: bpmMax,
      min: bpmMin,
      chartData: histories.map((item) => ({
        value: () => {
          const date = new Date(item.createdAt);
          return date.getDay() - 1 + Number((date.getHours() / 24).toFixed(2));
        },
        bpm: item.bpm,
      })).sort((a, b) => a.value() - b.value()),
    };
    return stats;
  }
}
