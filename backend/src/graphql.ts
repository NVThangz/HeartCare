
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

export class NoteInput {
    email?: Nullable<string>;
    content?: Nullable<string>;
    startDate?: Nullable<DateTime>;
    endDate?: Nullable<DateTime>;
}

export class NoteUpdateInput {
    id?: Nullable<number>;
    content?: Nullable<string>;
    startDate?: Nullable<DateTime>;
    endDate?: Nullable<DateTime>;
}

export class CreateNotificationInput {
    email?: Nullable<string>;
    title?: Nullable<string>;
    content?: Nullable<string>;
    createdAt?: Nullable<DateTime>;
}

export class UpdateNotificationInput {
    id?: Nullable<number>;
    title?: Nullable<string>;
    content?: Nullable<string>;
    createdAt?: Nullable<DateTime>;
}

export class UpdateProfileInput {
    email: string;
    name?: Nullable<string>;
    sex?: Nullable<string>;
    dob?: Nullable<string>;
    phone?: Nullable<string>;
    address?: Nullable<string>;
    nationalID?: Nullable<string>;
    avatar?: Nullable<string>;
}

export class UpdateRecordInput {
    email: string;
    height?: Nullable<number>;
    weight?: Nullable<number>;
    BMI?: Nullable<number>;
    bloodType?: Nullable<string>;
    HealthProblems?: Nullable<string>;
}

export class CreateUserInput {
    username: string;
    password: string;
}

export class UpdateUserInput {
    email?: Nullable<string>;
}

export abstract class IMutation {
    abstract getAdvisoryFirst(email: string, lang: string): string | Promise<string>;

    abstract getAdvisory(email: string, question: string): string | Promise<string>;

    abstract login(authInput?: Nullable<AuthInput>): LoginResponse | Promise<LoginResponse>;

    abstract signup(authInput?: Nullable<AuthInput>): LoginResponse | Promise<LoginResponse>;

    abstract logout(email: string): boolean | Promise<boolean>;

    abstract refresh(): LoginResponse | Promise<LoginResponse>;

    abstract forgotPassword(email: string): boolean | Promise<boolean>;

    abstract confirmForgotPassword(email: string, token: string): boolean | Promise<boolean>;

    abstract resetPasswordConfirmed(email: string, newPassword: string): LoginResponse | Promise<LoginResponse>;

    abstract changePassword(email: string, oldPassword: string, newPassword: string): boolean | Promise<boolean>;

    abstract registerWithSocial(email: string, name?: Nullable<string>): LoginResponse | Promise<LoginResponse>;

    abstract createHistory(email: string, bpm: number): Nullable<History> | Promise<Nullable<History>>;

    abstract createNote(noteInput: NoteInput): Nullable<Note> | Promise<Nullable<Note>>;

    abstract updateNote(noteUpdateInput: NoteUpdateInput): Nullable<Note> | Promise<Nullable<Note>>;

    abstract deleteNote(id: number): Nullable<boolean> | Promise<Nullable<boolean>>;

    abstract createNotification(createNotificationInput: CreateNotificationInput): Notification | Promise<Notification>;

    abstract createNotificationAll(title: string, content: string, createdAt?: Nullable<DateTime>): Notification | Promise<Notification>;

    abstract updateNotification(updateNotificationInput: UpdateNotificationInput): Notification | Promise<Notification>;

    abstract removeNotification(id: number): Nullable<Notification> | Promise<Nullable<Notification>>;

    abstract updateProfile(updateProfileInput: UpdateProfileInput): Profile | Promise<Profile>;

    abstract updateRecord(updateRecordInput: UpdateRecordInput): Record | Promise<Record>;
}

export class LoginResponse {
    access_token: string;
    refresh_token: string;
    user: User;
}

export class History {
    id?: Nullable<number>;
    userId?: Nullable<number>;
    bpm?: Nullable<number>;
    createdAt?: Nullable<DateTime>;
}

export class HistoryStatistics {
    average?: Nullable<number>;
    max?: Nullable<number>;
    min?: Nullable<number>;
    chartData?: Nullable<Nullable<ChartData>[]>;
}

export class ChartData {
    value?: Nullable<number>;
    bpm?: Nullable<number>;
}

export abstract class IQuery {
    abstract history(email: string): Nullable<Nullable<History>[]> | Promise<Nullable<Nullable<History>[]>>;

    abstract todayHistoryStatistics(email: string): Nullable<HistoryStatistics> | Promise<Nullable<HistoryStatistics>>;

    abstract weekHistoryStatistics(email: string): Nullable<HistoryStatistics> | Promise<Nullable<HistoryStatistics>>;

    abstract note(email: string): Nullable<Nullable<Note>[]> | Promise<Nullable<Nullable<Note>[]>>;

    abstract noteById(id: number): Nullable<Note> | Promise<Nullable<Note>>;

    abstract findNotes(email: string, date: DateTime): Nullable<Nullable<Note>[]> | Promise<Nullable<Nullable<Note>[]>>;

    abstract notifications(): Nullable<Notification>[] | Promise<Nullable<Notification>[]>;

    abstract findNotificationsWithEmail(email: string): Nullable<Nullable<Notification>[]> | Promise<Nullable<Nullable<Notification>[]>>;

    abstract findNotificationsAll(): Nullable<Nullable<Notification>[]> | Promise<Nullable<Nullable<Notification>[]>>;

    abstract profile(email: string): Nullable<Profile> | Promise<Nullable<Profile>>;

    abstract record(email: string): Nullable<Record> | Promise<Nullable<Record>>;

    abstract users(): Nullable<User>[] | Promise<Nullable<User>[]>;

    abstract user(email: string): User | Promise<User>;
}

export class Note {
    id?: Nullable<number>;
    userId?: Nullable<number>;
    content?: Nullable<string>;
    startDate?: Nullable<DateTime>;
    endDate?: Nullable<DateTime>;
}

export class Notification {
    id?: Nullable<number>;
    userId?: Nullable<number>;
    title?: Nullable<string>;
    content?: Nullable<string>;
    createdAt?: Nullable<DateTime>;
}

export class Profile {
    name?: Nullable<string>;
    sex?: Nullable<string>;
    dob?: Nullable<DateTime>;
    phone?: Nullable<string>;
    address?: Nullable<string>;
    nationalID?: Nullable<string>;
    userId?: Nullable<number>;
    avatar?: Nullable<string>;
}

export class Record {
    height?: Nullable<number>;
    weight?: Nullable<number>;
    BMI?: Nullable<number>;
    bloodType?: Nullable<string>;
    userId?: Nullable<number>;
    HealthProblems?: Nullable<string>;
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
