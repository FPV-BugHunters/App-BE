
import axios, { AxiosRequestConfig } from 'axios';
import {UserInfo} from '../types/User';
import { getAxiosConfig } from '../config/AxiosConfig';

export const getUserInfo = async (): Promise<UserInfo> => {
  const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/user`, getAxiosConfig());
  return response.data._embedded.cars;
}



