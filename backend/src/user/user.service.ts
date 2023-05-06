import { Injectable } from '@nestjs/common';
import { CreateUserInput } from 'src/graphql';
import { PrismaService } from 'src/prisma/prisma.service';
import * as bycrypt from 'bcrypt';

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
      },
    });
  }

  findAll() {
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

  deleteRefreshToken(id: number) {
    return this.prisma.user.updateMany({
      where: {
        id,
        refreshToken: {
          not: null,
        }
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

  resetPasswordConfirmed(email: string, password: string) {
    const hassPassword = bycrypt.hashSync(password, 10);
    return this.prisma.user.update({
      where: {
        email,
      },
      data: {
        password: hassPassword,
      },
    });
  }
        

  // update(id: number, updateUserInput: UpdateUserInput) {
  //   return `This action updates a #${id} user`;
  // }

  // remove(id: number) {
  //   return `This action removes a #${id} user`;
  // }
}
