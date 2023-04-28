
/*
 * -------------------------------------------------------
 * THIS FILE WAS AUTOMATICALLY GENERATED (DO NOT MODIFY)
 * -------------------------------------------------------
 */

/* tslint:disable */
/* eslint-disable */

export class LoginUserInput {
    username: string;
    password: string;
}

export class UpdateProfileInput {
    email: string;
    name?: Nullable<string>;
    sex?: Nullable<string>;
    dob?: Nullable<string>;
    phone?: Nullable<string>;
    address?: Nullable<string>;
    nationalID?: Nullable<string>;
}

export class UpdateRecordInput {
    email: string;
    height?: Nullable<number>;
    weight?: Nullable<number>;
    BMT?: Nullable<number>;
    bloodType?: Nullable<string>;
}

export class CreateUserInput {
    username: string;
    password: string;
}

export class UpdateUserInput {
    email?: Nullable<string>;
}

export class LoginResponse {
    access_token: string;
    user: User;
}

export abstract class IMutation {
    abstract signup(loginUserInput?: Nullable<LoginUserInput>): User | Promise<User>;

    abstract login(loginUserInput?: Nullable<LoginUserInput>): LoginResponse | Promise<LoginResponse>;

    abstract createProfile(userId: number): Profile | Promise<Profile>;

    abstract updateProfile(updateProfileInput: UpdateProfileInput): Profile | Promise<Profile>;

    abstract createRecord(userId: number): Record | Promise<Record>;

    abstract updateRecord(updateRecordInput: UpdateRecordInput): Record | Promise<Record>;

    abstract createUser(createUserInput: CreateUserInput): User | Promise<User>;

    abstract updateUser(updateUserInput: UpdateUserInput): User | Promise<User>;

    abstract removeUser(id: number): Nullable<User> | Promise<Nullable<User>>;
}

export class Profile {
    name?: Nullable<string>;
    sex?: Nullable<string>;
    dob?: Nullable<DateTime>;
    phone?: Nullable<string>;
    address?: Nullable<string>;
    nationalID?: Nullable<string>;
    userId?: Nullable<number>;
}

export abstract class IQuery {
    abstract profile(email: string): Nullable<Profile> | Promise<Nullable<Profile>>;

    abstract record(email: string): Nullable<Record> | Promise<Nullable<Record>>;

    abstract users(): Nullable<User>[] | Promise<Nullable<User>[]>;

    abstract user(email: string): User | Promise<User>;
}

export class Record {
    height?: Nullable<number>;
    weight?: Nullable<number>;
    BMT?: Nullable<number>;
    bloodType?: Nullable<string>;
    userId?: Nullable<number>;
}

export class User {
    id: number;
    email: string;
    password: string;
    createdAt?: Nullable<DateTime>;
    updatedAt?: Nullable<DateTime>;
    profile?: Nullable<Profile>;
    record?: Nullable<Record>;
}

export type DateTime = any;
type Nullable<T> = T | null;
