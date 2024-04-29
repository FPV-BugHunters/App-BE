import * as React from 'react';
import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {Symbol, SymbolResponse} from '../types';
import Chart from 'react-apexcharts'
import { getSymbols } from '../api/SymbolApi';
import SymbolDataTableChart from './SymbolDataTableChart';
import { Style } from '@mui/icons-material';


const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.primary.main,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover ,
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}));

export type SymbolResponse = {
    id: number;
    name: string;
    symbol: string;
    rank: number;
    priceUSD: number;
    circulatingSupply: number;
    marketCap: number;
    volume: number;
    h1: number;
    h24: number;
    d7: number;
}

export default function SymbolDataTable() {
    
  const [open, setOpen] = React.useState(false);

  const queryClient = useQueryClient();

  const { data, isError, isLoading, isSuccess } = useQuery({
    queryKey: ["symbol"],
    queryFn: getSymbols
  });
  
  React.useEffect(() => {
    console.log(data);
  }, [data]);

  // const { mutate } = useMutation(deleteCar, {
  //   onSuccess: () => {
  //     setOpen(true);
  //     queryClient.invalidateQueries({ queryKey: ['cars'] });
  //   },
  //   onError: (err) => {
  //     console.error(err);
  //   },
  // }); 
  // 
  return (
  
    <TableContainer component={Paper} sx={{ padding: 4, borderRadius: { topLeft: 2, topRight: 1, bottomRight: 2, bottomLeft: 1 } }}>
      <Table sx={{ minWidth: 700 }} aria-label="customized table">
        <TableHead>
          <TableRow>

            <StyledTableCell>ID</StyledTableCell>
            <StyledTableCell>Name</StyledTableCell>
            <StyledTableCell>Price </StyledTableCell>
            <StyledTableCell>1h %</StyledTableCell>
            <StyledTableCell>24h %</StyledTableCell>
            <StyledTableCell>7d %</StyledTableCell>
            <StyledTableCell>Market Cap</StyledTableCell>
            <StyledTableCell>Volume(24h)</StyledTableCell>
            <StyledTableCell>Circulating Supply</StyledTableCell>
            <StyledTableCell>Chart</StyledTableCell>

          </TableRow>
        </TableHead>
        <TableBody>
          {data && data.map((row) => (
            <StyledTableRow key={row.name}>
              <StyledTableCell>{row.id}</StyledTableCell>
              <StyledTableCell>{row.name}</StyledTableCell>
              <StyledTableCell>${parseFloat(row.priceUSD).toFixed(2)}</StyledTableCell>
              <StyledTableCell>{parseFloat(row.h1).toFixed(2)}%</StyledTableCell>
              <StyledTableCell>{parseFloat(row.h24).toFixed(2)}%</StyledTableCell>
              <StyledTableCell>{parseFloat(row.d7).toFixed(2)}%</StyledTableCell>
              <StyledTableCell>${parseFloat(row.marketCap).toFixed(0)}</StyledTableCell>
              <StyledTableCell>${parseFloat(row.volume).toFixed(0)}</StyledTableCell>
              <StyledTableCell>{parseFloat(row.circulatingSupply).toFixed(2) + ' ' + row.symbol}</StyledTableCell>


              {/* <SymbolDataTableChart symbol={row} /> */}

            </StyledTableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}