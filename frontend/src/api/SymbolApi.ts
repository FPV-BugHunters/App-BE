
import { getAxiosConfig } from '../config/AxiosConfig';
import axios from 'axios';
import { SymbolResponse } from '../types';



export const getSymbols = (): Promise<SymbolResponse[]> => {
  return axios.get(`${import.meta.env.VITE_API_URL}/api/cryptos`, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const getWatchlist = (): Promise<SymbolResponse[]> => {
  return axios.get(`${import.meta.env.VITE_API_URL}/api/user/watchlist`, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const getSymbolsNotInWatchlist = (): Promise<SymbolResponse[]> => {
  return axios.get(`${import.meta.env.VITE_API_URL}/api/user/watchlist/not-in-watchlist`, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const addSymbolToWatchlist = (symbolId: number): Promise<boolean> => {
  return axios.post(`${import.meta.env.VITE_API_URL}/api/user/watchlist`, symbolId, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const removeSymbolFromWatchlist = (symbolId: number): Promise<boolean> => {
  return axios.delete(`${import.meta.env.VITE_API_URL}/api/user/watchlist`, { data: symbolId, ...getAxiosConfig() })
    .then(response => response.data)
    .catch(error => { throw error; });
}
