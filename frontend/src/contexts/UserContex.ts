import { UserInfo } from '../types';
import axios from 'axios';
import { createContext } from 'react';


const UserContex = createContext <{
    user: UserInfo | null;
    setUser: React.Dispatch<React.SetStateAction<UserInfo | null>>;
} > ({
    user: null,
    setUser: () => {}
})


export default UserContex;