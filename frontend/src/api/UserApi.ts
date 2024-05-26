
import axios, { AxiosRequestConfig } from 'axios';
import {UserInfo} from '../types/User';
import { getAxiosConfig } from '../config/AxiosConfig';

export const userInfo = async (): Promise<UserInfo> => {
  return axios.get(`${import.meta.env.VITE_API_URL}/api/user`, getAxiosConfig());
}

export const logoutUser = async (): Promise<void> => {
  return new Promise((resolve) => {
    sessionStorage.removeItem('token');
    resolve();
  });
}



