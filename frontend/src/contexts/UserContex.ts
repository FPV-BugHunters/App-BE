import { getAxiosConfig } from '../config/AxiosConfig';
import { User } from '../types';
import axios from 'axios';

export const getUser = async (): Promise<User> => {
  const response = await axios.get(`${import.meta.env.API_URL}/api/cars`, getAxiosConfig());
  return response.data._embedded.cars;
}