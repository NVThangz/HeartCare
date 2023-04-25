import { Injectable } from '@nestjs/common';
import { CreateUserInput } from 'src/graphql';
import { PrismaService } from 'src/prisma/prisma.service';

@Injectable()
export class UserService {
  constructor(
    private prisma: PrismaService
    ) {}

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
    return this.prisma.user.findMany();
  }

  findOne(email: string) {
    return this.prisma.user.findUnique({
      where: {
        email,
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
