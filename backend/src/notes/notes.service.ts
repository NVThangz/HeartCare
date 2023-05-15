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
        startDate: noteInput.startDate,
        endDate: noteInput.endDate,
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

  update(noteUpdateInput: NoteUpdateInput) {
    console.log(noteUpdateInput);
    return this.prisma.note.update({
      where: {
        id: noteUpdateInput.id,
      },
      data: {
        content: noteUpdateInput.content,
        startDate: noteUpdateInput.startDate,
        endDate: noteUpdateInput.endDate,
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
