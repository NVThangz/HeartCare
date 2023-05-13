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
}
