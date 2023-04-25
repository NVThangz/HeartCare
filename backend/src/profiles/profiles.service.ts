import { Injectable } from '@nestjs/common';
import { UpdateProfileInput } from 'src/graphql';
import { PrismaService } from 'src/prisma/prisma.service';

@Injectable()
export class ProfilesService {
  constructor(private prisma: PrismaService) {}
  
  create(userId: number) {
    return this.prisma.profile.create({
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

    return this.prisma.profile.findUnique({
      where: {
        userId: user.id,
      },
    });
  }

  async update(updateProfileInput: UpdateProfileInput) {
    const { email, ...update } = updateProfileInput;
    
    const user = await this.prisma.user.findUnique({
      where: {
        email,
      },
    });

    return this.prisma.profile.update({
      where: {
        userId: user.id,
      },
      data: {
        ...update,
      },
    });
  }

}
