import { Injectable } from '@nestjs/common';
import { UpdateRecordInput } from 'src/graphql';
import { PrismaService } from 'src/prisma/prisma.service';

@Injectable()
export class RecordsService {
  constructor(private prisma: PrismaService) {}

  create(userId: number) {
    return this.prisma.record.create({
      data: {
        userId,
      },
    });
  }

  async findOne(email: string) {
    const user = await this.prisma.user.findUnique({
      where: {
        email,
      },
    });
    
    return this.prisma.record.findUnique({
      where: {
        userId: user.id,
      },
    });
  }

  async update(updateRecordInput: UpdateRecordInput) {
    const { email, ...update } = updateRecordInput;
    
    const user = await this.prisma.user.findUnique({
      where: {
        email,
      },
    });

    return this.prisma.record.update({
      where: {
        userId: user.id,
      },
      data: {
        ...update,
      },
    });
  }

}
