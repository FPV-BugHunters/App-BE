
import { getAxiosConfig } from '../config/AxiosConfig';
import axios from 'axios';
import { SymbolResponse, UserPortfolio } from '../types';



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

export const listUserPortfolio = (): Promise<UserPortfolio[]> => {
  return axios.get(`${import.meta.env.VITE_API_URL}/api/user/user-portfolio`, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const createUserPortfolio = (name: string): Promise<boolean> => {
  return axios.post(`${import.meta.env.VITE_API_URL}/api/user/user-portfolio`, name, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}


export const listPortfolio = (userPortfolioId: number): Promise<UserPortfolio[]> => { 
  return axios.get(`${import.meta.env.VITE_API_URL}/api/user/user-portfolio/${userPortfolioId}`, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}


export const sellTransaction = (cryptoId: number, userPortfolioId: number, amount: number): Promise<boolean> => {
  return axios.post(`${import.meta.env.VITE_API_URL}/api/user/transaction/sell`, { cryptoId, userPortfolioId, amount }, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const buyTransaction = (cryptoId: number, userPortfolioId: number, amount: number): Promise<boolean> => {
  return axios.post(`${import.meta.env.VITE_API_URL}/api/user/transaction/buy`, { cryptoId, userPortfolioId, amount }, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const getBalance = (): Promise<number> => {
  return axios.get(`${import.meta.env.VITE_API_URL}/api/user/balance`, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}

export const getTransactionByPortfolioId = (userPortfolioId: number): Promise<UserPortfolio[]> => {
  return axios.get(`${import.meta.env.VITE_API_URL}/api/user/user-transactions/portfolio/${userPortfolioId}`, getAxiosConfig())
    .then(response => response.data)
    .catch(error => { throw error; });
}


