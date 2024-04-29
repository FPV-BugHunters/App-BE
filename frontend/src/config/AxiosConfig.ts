import axios, { AxiosRequestConfig } from 'axios';

export const getAxiosConfig = (): AxiosRequestConfig => {
  
  const token = sessionStorage.getItem("token");
  console.log(token);
  return {
    headers: {
      'Authorization': token,
      'Content-Type': 'application/json',
    },
  };
};