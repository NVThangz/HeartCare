import { Injectable } from '@nestjs/common';
import { NoteInput, NoteUpdateInput } from 'src/graphql';
import { PrismaService } from 'src/prisma/prisma.service';

@Injectable()
export class NotesService {
  constructor(private prisma: PrismaService) {}

  create(noteInput: NoteInput) {
    return this.prisma.note.create({
      data: {
        content: noteInput.content,
        startDate: new Date(noteInput.startDate),
        endDate: new Date(noteInput.endDate),
        user: {
          connect: {
            email: noteInput.email,
          },
        },
        // createdAt: new Date(),
      },
    });
  }

  findOne(email: string) {
    return this.prisma.note.findMany({
      where: {
        user: {
          email,
        },
      },
    });
  }
  
  findNotesToday(email: string) {
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

    return this.prisma.note.findMany({
      where: {
        user: {
          email,
        },
        startDate: {
          gte: startOfDayUtcPlus7,
          lte: endOfDayUtcPlus7,
        }
      },
    });
  }

  update(noteUpdateInput: NoteUpdateInput) {
    console.log(noteUpdateInput);
    return this.prisma.note.update({
      where: {
        id: noteUpdateInput.id,
      },
      data: {
        content: noteUpdateInput.content,
        startDate: new Date(noteUpdateInput.startDate),
        endDate: new Date(noteUpdateInput.endDate),
      },
    });
  }

  async delete(id: number) {
    const note = await this.prisma.note.findUnique({
      where: {
        id,
      },
    });
    if (!note) {
      throw new Error('Note not found');
    }
    await this.prisma.note.delete({
      where: {
        id,
      },
    });
    return true;
  }
}
