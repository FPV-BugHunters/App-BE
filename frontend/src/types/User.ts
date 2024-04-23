export type User = {
    firstName: string;
    lastName: string;
    email: string;
    role: 'admin' | 'user';
}

export type UserLogin = {
    email: string;
    password: string;
}