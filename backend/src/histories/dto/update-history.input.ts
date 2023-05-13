import { CreateHistoryInput } from './create-history.input';
import { PartialType } from '@nestjs/mapped-types';

export class UpdateHistoryInput extends PartialType(CreateHistoryInput) {
  id: number;
}
