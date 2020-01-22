export interface User {
    id: number;
    username: string;
    ime: string;
    prezime: string;
    role:string;
    token?: string;
    expiresIn?:number;
}