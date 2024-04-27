import axios, { AxiosRequestConfig } from 'axios';

export const getAxiosConfig = (): AxiosRequestConfig => {
  
  const token = sessionStorage.getItem("token");
  return {
    headers: {
      'Authorization': token,
      'Content-Type': 'application/json',
    },
  };
};