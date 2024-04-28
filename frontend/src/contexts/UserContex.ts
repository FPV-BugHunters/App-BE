import { UserInfo } from '../types';
import axios from 'axios';
import { createContext } from 'react';


const UserContex = createContext<UserInfo | null>(null);

export default UserContex;