import * as React from 'react';
import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';


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
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}));

export type Portfolio = {
  cryptoId: number;
  rank: number;
  name: string;
  slug: string;
  totalPrice: number;
  price: number;
  circulatingSupply: number;
  marketCap: number;
  volume: number;
  amount: number;
  h1: number;
  h24: number;
  d7: number;
}

export interface PortfolioShowTableProps {
  selectedPortfolio: string;
  listPortfolio: Portfolio[];
  refresh: () => void;
}


export default function PortfolioShowTable(props: PortfolioShowTableProps) {

  const [open, setOpen] = React.useState(false);
  const { selectedPortfolio, refresh, listPortfolio } = props;


  
  
  



  return (
    <>
      {/* <Box sx={{ display: 'flex', justifyContent: 'center', }}> */}
      <TableContainer >

        <Table stickyHeader aria-label="sticky table">
          <TableHead>
            <TableRow>
              {/* <StyledTableCell>ID</StyledTableCell> */}
              <StyledTableCell>Name</StyledTableCell>
              <StyledTableCell>Amount </StyledTableCell>
              <StyledTableCell>Price </StyledTableCell>
              <StyledTableCell>Market Cap</StyledTableCell>
              <StyledTableCell>Volume(24h)</StyledTableCell>
              {/* <StyledTableCell>Chart</StyledTableCell> */}
            </TableRow>
          </TableHead>
          <TableBody>
            {listPortfolio && listPortfolio.length > 0 && listPortfolio
              .map((row) => {
                return (
                  <TableRow hover role="checkbox" tabIndex={-1} key={row.cryptoId}>
                    {/* <StyledTableCell>{row.id}</StyledTableCell> */}
                    <StyledTableCell>{row.name}</StyledTableCell>
                    <StyledTableCell>{row.amount.toFixed(2)}</StyledTableCell>
                    <StyledTableCell>{row.price.toFixed(2)}</StyledTableCell>
                    <StyledTableCell>{row.marketCap.toFixed(2)}</StyledTableCell>
                    <StyledTableCell>{row.volume.toFixed(2)}</StyledTableCell>
                    {/* <StyledTableCell><DashboardSymbolDataTableChart cryptoId={row.cryptoId} /></StyledTableCell> */}
                  </TableRow>
                );
              })}
          </TableBody>
        </Table>
      </TableContainer>
      {/* </Box> */}
    </>
  );
}