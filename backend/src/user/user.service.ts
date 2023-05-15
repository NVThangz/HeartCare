import { Injectable } from '@nestjs/common';
import { CreateUserInput } from 'src/graphql';
import { PrismaService } from 'src/prisma/prisma.service';
import * as bycrypt from 'bcrypt';
import validate from 'deep-email-validator';

@Injectable()
export class UserService {
  constructor(private prisma: PrismaService) {}

  createUser({ username, password }: CreateUserInput) {
    return this.prisma.user.create({
      data: {
        email: username,
        password,
        profile: {
          create: {},
        },
        record: {
          create: {},
        },
        createdAt: new Date(),
      },
    });
  }

  async findAll() {
    return this.prisma.user.findMany({
      include: {
        profile: true,
        record: true,
      },
    });
  }

  findOne(email: string) {
    return this.prisma.user.findUnique({
      where: {
        email,
      },
      include: {
        profile: true,
        record: true,
      },
    });
  }

  updateRefreshToken(id: number, refreshToken: string) {
    return this.prisma.user.update({
      where: {
        id,
      },
      data: {
        refreshToken,
      },
    });
  }

  // deleteRefreshToken(id: number) {
  //   return this.prisma.user.updateMany({
  //     where: {
  //       id,
  //       refreshToken: {
  //         not: null,
  //       },
  //     },
  //     data: {
  //       refreshToken: null,
  //     },
  //   });
  // }

  deleteRefreshToken(email: string) {
    return this.prisma.user.updateMany({
      where: {
        email,
        refreshToken: {
          not: null,
        },
      },
      data: {
        refreshToken: null,
      },
    });
  }

  findById(id: number) {
    return this.prisma.user.findUnique({
      where: {
        id,
      },
    });
  }

  changePassword(email: string, password: string) {
    const hashPassword = bycrypt.hashSync(password, 10);
    return this.prisma.user.update({
      where: {
        email,
      },
      data: {
        password: hashPassword,
      },
    });
  }

  async checkPassword(email: string, password: string) {
    const user = await this.prisma.user.findUnique({
      where: {
        email,
      },
    });
    return bycrypt.compareSync(password, user.password);
  }
}
