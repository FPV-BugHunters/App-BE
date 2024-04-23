import axios, { AxiosRequestConfig } from 'axios';

export const getAxiosConfig = (): AxiosRequestConfig => {
  const token = sessionStorage.getItem("jwt");
  return {
    headers: {
      'Authorization': token,
      'Content-Type': 'application/json',
    },
  };
};