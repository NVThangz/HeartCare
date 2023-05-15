import { Module } from '@nestjs/common';
import { NotesService } from './notes.service';
import { NotesResolver } from './notes.resolver';

@Module({
  providers: [NotesResolver, NotesService],
  exports: [NotesService]
})
export class NotesModule {}
