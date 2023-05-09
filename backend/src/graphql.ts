
/*
 * -------------------------------------------------------
 * THIS FILE WAS AUTOMATICALLY GENERATED (DO NOT MODIFY)
 * -------------------------------------------------------
 */

/* tslint:disable */
/* eslint-disable */

export class AdvisoryInput {
    email: string;
    question?: Nullable<string>;
}

export class AuthInput {
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
    BMI?: Nullable<number>;
    bloodType?: Nullable<string>;
}

export class CreateUserInput {
    username: string;
    password: string;
}

export class UpdateUserInput {
    email?: Nullable<string>;
}

export abstract class IMutation {
    abstract getAdvisory(advisoryInput: AdvisoryInput): string | Promise<string>;

    abstract login(authInput?: Nullable<AuthInput>): LoginResponse | Promise<LoginResponse>;

    abstract signup(authInput?: Nullable<AuthInput>): LoginResponse | Promise<LoginResponse>;

    abstract logout(): boolean | Promise<boolean>;

    abstract refresh(): LoginResponse | Promise<LoginResponse>;

    abstract forgotPassword(email: string): boolean | Promise<boolean>;

    abstract confirmForgotPassword(email: string, token: string): boolean | Promise<boolean>;

    abstract updateProfile(updateProfileInput: UpdateProfileInput): Profile | Promise<Profile>;

    abstract updateRecord(updateRecordInput: UpdateRecordInput): Record | Promise<Record>;

    abstract resetPasswordConfirmed(email: string, Password: string): User | Promise<User>;
}

export class LoginResponse {
    access_token: string;
    refresh_token: string;
    user: User;
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
    BMI?: Nullable<number>;
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
    refreshToken?: Nullable<string>;
}

export type DateTime = any;
type Nullable<T> = T | null;
