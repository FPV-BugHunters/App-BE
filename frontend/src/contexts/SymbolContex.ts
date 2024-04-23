
import { getAxiosConfig } from '../config/AxiosConfig';
import axios from 'axios';
import { SymbolResponse } from '../types';


export const getSymbols = async (): Promise<SymbolResponse[]> => {
//   const response = await axios.get(`${import.meta.env.API_URL}/api/cars`, getAxiosConfig());
//   return response.data._embedded.cars;
  const mockData: SymbolResponse[] = Array.from({ length: 10 }, (_, id) => ({
    id: id + 1,
    name: `Symbol ${id + 1}`,
    price: Math.random() * 1000,
    _1h: Math.random() * 100,
    _24h: Math.random() * 100,
    _7h: Math.random() * 100,
    marketCap: Math.random() * 1000000,
    volume: Math.random() * 1000000,
    circulatingSupply: Math.random() * 1000000,
  }));
  
  return mockData;
}