import { Resolver, Query, Mutation, Args } from '@nestjs/graphql';
import { ProfilesService } from './profiles.service';
import { UpdateProfileInput } from 'src/graphql';
import {
  FileTypeValidator,
  MaxFileSizeValidator,
  ParseFilePipe,
  UploadedFile,
  UseInterceptors,
} from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import path from 'path';
import uuid4 from "uuid4";

@Resolver('Profile')
export class ProfilesResolver {
  constructor(private readonly profilesService: ProfilesService) {}

  @Query('profile')
  findOne(@Args('email') email: string) {
    return this.profilesService.findOne(email);
  }

  @Mutation('updateProfile')
  update(@Args('updateProfileInput') updateProfileInput: UpdateProfileInput) {
    return this.profilesService.update(updateProfileInput);
  }


  // @Mutation('uploadFile')
  // @UseInterceptors(
  //   FileInterceptor('file', {
  //     dest: './uploads/avatar',
  //     fileFilter: (req, file, cb) => {
  //       const fileName = path.parse(file.originalname).name.replace(/\s/g, '') + uuid4();
  //       const fileExtName = path.parse(file.originalname).ext;
  //       file.originalname = fileName + fileExtName;
  //       cb(null, true);
  //     }
  //   }),
  // )
  // uploadFile(
  //   @UploadedFile(
  //     new ParseFilePipe({
  //       validators: [
  //         // new MaxFileSizeValidator({ maxSize: 1000 }),
  //         new FileTypeValidator({ fileType: 'image' }),
  //       ],
  //     }),
  //   )
  //   file: Express.Multer.File,
  // ) {
  //   console.log(file);
  //   return file.path;
  // }
}
