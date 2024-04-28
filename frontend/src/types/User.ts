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

export type UserInfo = {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    roles: string[];
}


