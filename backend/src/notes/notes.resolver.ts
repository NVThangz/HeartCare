import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { NotesService } from './notes.service';
import { NoteInput, NoteUpdateInput } from 'src/graphql';

@Resolver('Note')
export class NotesResolver {
  constructor(private readonly notesService: NotesService) {}

  @Mutation('createNote')
  create(@Args('noteInput') noteInput: NoteInput) {
    return this.notesService.create(noteInput);
  }
  
  @Query('note')
  findOne(@Args('email') email: string) {
    return this.notesService.findOne(email);
  }

  @Query('findNotesToday')
  findNotesToday(@Args('email') email: string) {
    return this.notesService.findNotesToday(email);
  }

  @Mutation('updateNote')
  update(@Args('noteUpdateInput') noteUpdateInput: NoteUpdateInput) {
    return this.notesService.update(noteUpdateInput);
  }

  @Mutation('deleteNote')
  delete(@Args('id') id: number) {
    return this.notesService.delete(id);
  }
}
