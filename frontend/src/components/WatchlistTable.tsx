import * as React from 'react';
import { styled } from '@mui/material/styles';
// import Table from '@mui/material/Table';
// import TableBody from '@mui/material/TableBody';
// import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import { Alert, Typography, Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, tableCellClasses } from '@mui/material';
import { Symbol, SymbolResponse } from '../types';
import DeleteIcon from '@mui/icons-material/Delete';
import DashboardSymbolDataTableChart from './DashboardSymbolDataTableChart';


const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.primary.dark,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
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

export default function WatchlistTable({ data, isError, isLoading, isSuccess, handleRemoveCrypto }){

  if (isLoading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>Loading...</Box>;
  }

  if (isError || !data) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
      Error fetching data
    </Box>;
  }

  return (
    <>
      <TableContainer component={Paper} >
        <Table /* sx={{ minWidth: 700 }} */ aria-label="customized table">
          <TableHead >
            <TableRow /* sx={{ bgcolor: 'black', height: '50px' }} */>

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
              <StyledTableCell></StyledTableCell>
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
                <StyledTableCell style={{ paddingTop:3, paddingBotton:3 }}>
                  <DashboardSymbolDataTableChart data={row.priceHistoryUSD}></DashboardSymbolDataTableChart>
                </StyledTableCell>

                <StyledTableCell onClick={() => handleRemoveCrypto(row.id)}><DeleteIcon sx={{ color: 'white' }} /></StyledTableCell>
              </StyledTableRow>
              ))}
          </TableBody>

        </Table>

      </TableContainer>

      {data.length === 0 &&
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
          Your watch list is empty
        </Box>
      }

    </>
  );
}