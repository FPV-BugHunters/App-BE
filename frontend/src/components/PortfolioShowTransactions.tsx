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

export type Transaction = {
  id: number;
  amount: number;
  pricePerUnit: number;
  totalPrice: number;
  userId: number;
  cryptoId: number;
  cryptoName: string;
  cryptoSymbol: string;
  type: string;
  dateTime: number;

}

export interface PortfolioShowTransactionsProps {
  refresh: () => void;
  transactions: Transaction[];
}


export default function PortfolioShowTransactions(props: PortfolioShowTransactionsProps) {

  const [open, setOpen] = React.useState(false);
  const { transactions } = props;
  const formatDate = (timestamp: number) => {
    const date = new Date(timestamp);
    return date.toLocaleString('en-GB', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };


  //   {
  //     "id": 153,
  //     "amount": 42,
  //     "pricePerUnit": 0.166716929698325,
  //     "totalPrice": 7.00211104732965,
  //     "userId": 1,
  //     "cryptoId": 74,
  //     "cryptoName": "Dogecoin",
  //     "cryptoSymbol": "DOGE",
  //     "type": "BUY",
  //     "dateTime": 1716765731539
  // }



  return (
    <>
      {/* <Box sx={{ display: 'flex', justifyContent: 'center', }}> */}
      <TableContainer >

        <Table stickyHeader aria-label="sticky table">
          <TableHead>
            <TableRow>
              <StyledTableCell>ID</StyledTableCell>
              <StyledTableCell>Amount</StyledTableCell>
              <StyledTableCell>Total Price</StyledTableCell>
              <StyledTableCell>Price Per Unit</StyledTableCell>
              <StyledTableCell>Crypto Name</StyledTableCell>
              <StyledTableCell>Crypto Symbol</StyledTableCell>
              <StyledTableCell>Type</StyledTableCell>
              <StyledTableCell>Date Time</StyledTableCell>

              {/* <StyledTableCell>Chart</StyledTableCell> */}
            </TableRow>
          </TableHead>
          <TableBody>
            {transactions && transactions.length > 0 && transactions
              .map((row) => {
                return (
                  <TableRow hover role="checkbox" tabIndex={-1} key={row.cryptoId}>
                    <StyledTableCell>{row.id}</StyledTableCell>
                    <StyledTableCell>{row.amount}</StyledTableCell>
                    <StyledTableCell>{row.totalPrice.toFixed(2)}</StyledTableCell>
                    <StyledTableCell>{row.pricePerUnit.toFixed(2)}</StyledTableCell>
                    <StyledTableCell>{row.cryptoName}</StyledTableCell>
                    <StyledTableCell>{row.cryptoSymbol}</StyledTableCell>
                    <StyledTableCell>{row.type}</StyledTableCell>
                    <StyledTableCell>{formatDate(row.dateTime)}</StyledTableCell>

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