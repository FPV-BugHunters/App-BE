import React from 'react';
import WatchlistTable from '../components/WatchlistTable';
import {
    Button, Container, Grid, Paper, Box, useTheme,
    FormControl, InputLabel, Select, MenuItem, Alert, Typography
} from '@mui/material';
import { listUserPortfolio, getBalance, listPortfolio, getTransactionByPortfolioId, getBalanceHistory, getPortfolioValueHistory,  getPortfolioValue } from '../api/SymbolApi';
import { useQuery } from '@tanstack/react-query';
import PortfolioCreateDialog from '../components/PortfolioCreateDialog';
import PortfolioCreateSellTransactionDialog from '../components/PortfolioCreateSellTransactionDialog';
import PortfolioCreateBuyTransactionDialog from '../components/PortfolioCreateBuyTransactionDialog';
import PortfolioShowTable from '../components/PortfolioShowTable';
import PortfolioShowTransactions from '../components/PortfolioShowTransactions';
import PortfolioBalanceHistoryChart from '../components/PortfolioBalanceHistoryChart';
import PortfolioValueHistoryChart from '../components/PortfolioValueHistoryChart';
import PortfolioDepositDialog from '../components/PortfolioDepositDialog';
import PortfolioWithdrawDialog from '../components/PortfolioWithdrawDialog';

export default function Portfolio () {
    const theme = useTheme();
    const [ createPorfolioDialogOpen, setCreatePortfolioDialogOpen ] = React.useState(false);
    const [ selectedPortfolio, setSelectedPortfolio ] = React.useState('');

    const [ createBuyTransactionDialogOpen, setCreateBuyTransactionDialogOpen ] = React.useState(false);
    const [ createSellTransactionDialogOpen, setCreateSellTransactionDialogOpen ] = React.useState(false);
    
    const [ createDepositDialogOpen, setCreateDepositDialogOpen ] = React.useState(false);
    const [ createWithdrawDialogOpen, setCreateWithdrawDialogOpen ] = React.useState(false);
    
    // const [ portfolioValue, setPortfolioValue ] = React.useState(0);
    



    // list user portfolios
    const { data: listuserPortfolioData, refetch:listUserPortfolioRefetch } = useQuery({
        queryKey: [ "userportfolio" ],
        queryFn: listUserPortfolio
    });

    const { data: listPortfolioData, refetch: listPortfolioRefetch  } = useQuery({
        queryKey: [ "portfolio" ],
        queryFn: () => {
            if(!selectedPortfolio || selectedPortfolio === ''|| selectedPortfolio == 0) return Promise.resolve(null);
            const portfolioId = Number(selectedPortfolio);
            return isNaN(portfolioId) ? Promise.resolve(null) : listPortfolio(portfolioId);
        },
    });
    
    const { data: balance, refetch: balanceRefetch } = useQuery({
        queryKey: [ "balance" ],
        queryFn: getBalance,
    });
    
    const { data: transactions, refetch: transactionsRefetch } = useQuery({
        queryKey: [ "transactions" ],
        queryFn: () => {
            if(!selectedPortfolio || selectedPortfolio === ''|| selectedPortfolio == 0) return Promise.resolve(null);
            const portfolioId = Number(selectedPortfolio);
            return isNaN(portfolioId) ? Promise.resolve(null) : getTransactionByPortfolioId(portfolioId);
        },
    });

    const { data: balanceHistory, refetch: balanceHistoryRefetch } = useQuery({
        queryKey: [ "balanceHistory" ],
        queryFn: getBalanceHistory,
    });

    const { data: portfolioValueHistory, refetch: portfolioValueHistoryRefetch } = useQuery({
        queryKey: [ "portfolioValueHistory" ],
        queryFn: () => {
            if (!selectedPortfolio || selectedPortfolio === '' || selectedPortfolio == 0) return Promise.resolve(null);
            const portfolioId = Number(selectedPortfolio);
            return isNaN(portfolioId) ? Promise.resolve(null) : getPortfolioValueHistory(portfolioId);
        }
    });

    const { data: portfolioValueData, refetch: portfolioValueRefetch } = useQuery({
        queryKey: [ "portfolioValue" ],
        queryFn: () => {
            if (!selectedPortfolio || selectedPortfolio === '' || selectedPortfolio == 0 ) return Promise.resolve(null);
            const portfolioId = Number(selectedPortfolio);
            return isNaN(portfolioId) ? Promise.resolve(null) : getPortfolioValue(portfolioId);
        }
    });
    
// getPortfolioValueHistory
    
    const refresh = async () => {
        setTimeout(() => {
            listUserPortfolioRefetch();
            balanceRefetch(); 
            listPortfolioRefetch();
            transactionsRefetch();
            balanceHistoryRefetch();
            portfolioValueHistoryRefetch();
            portfolioValueRefetch();
            console.log(balanceHistory)
        }, 300);
    }
    



    React.useEffect(() => {
        if (listuserPortfolioData && listuserPortfolioData.length > 0) {
            setSelectedPortfolio(listuserPortfolioData[ 0 ].id);
            refresh();
        }
    }, [ listuserPortfolioData ]);





    const handleSelectPortfolio = (e) => {
        setSelectedPortfolio(e.target.value);
        refresh();
    }

    return (
        <>
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: '20px', height: '50px' }}>
        
                    <Button viariant="contained" color="primary" onClick={() => { setCreateDepositDialogOpen(true); refresh() ; }} sx={{ bgcolor: theme.palette.primary.main, color: 'white' }}>Deposit</Button>
                    <Button viariant="contained" color="primary" onClick={() => { setCreateWithdrawDialogOpen(true); refresh(); }} sx={{ bgcolor: theme.palette.primary.main, color: 'white' }}>Withdraw</Button>

                    <Button viariant="contained" color="primary" onClick={() => { setCreateBuyTransactionDialogOpen(true); refresh(); }} sx={{ bgcolor: theme.palette.primary.main, color: 'white' }}>Buy</Button>
                    <Button viariant="contained" color="primary" onClick={() => { setCreateSellTransactionDialogOpen(true); refresh(); }} sx={{ bgcolor: theme.palette.primary.main, color: 'white' }}>Sell</Button>

                    {listuserPortfolioData && listuserPortfolioData.length === 0 && <Alert variant="filled" severity="error"> Create a portfolio to get started </Alert>}
                    <FormControl sx={{ minWidth: 100 }} >

                        <InputLabel id="demo-simple-select-label">Select Portfolio</InputLabel>

                        <Select labelId="demo-simple-select-label" id="demo-simple-select" label="Select Crypto" onChange={(e) => handleSelectPortfolio(e)} value={selectedPortfolio} >
                            {listuserPortfolioData && listuserPortfolioData.map((portfolio) => (
                                <MenuItem key={portfolio.id} value={portfolio.id}>{portfolio.name}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <Button viariant="contained" color="primary" onClick={() => setCreatePortfolioDialogOpen(true)} sx={{ bgcolor: theme.palette.primary.main, color: 'white' }}>Create portfolio</Button>
                    <PortfolioCreateDialog open={createPorfolioDialogOpen} onClose={() => setCreatePortfolioDialogOpen(false)} ></PortfolioCreateDialog>
                    <PortfolioCreateBuyTransactionDialog
                        selectedPortfolio={selectedPortfolio}
                        open={createBuyTransactionDialogOpen} onClose={() => {setCreateBuyTransactionDialogOpen(false) ; refresh();}}
                        balance={balance}
                    ></PortfolioCreateBuyTransactionDialog>
                    <PortfolioCreateSellTransactionDialog
                        selectedPortfolio={selectedPortfolio}
                        balance={balance}
                        listPortfolioData={listPortfolioData}
                        open={createSellTransactionDialogOpen} onClose={() => { setCreateSellTransactionDialogOpen(false); refresh();}}
                    ></PortfolioCreateSellTransactionDialog>
                    <PortfolioDepositDialog open={createDepositDialogOpen} onClose={() => { setCreateDepositDialogOpen(false); refresh(); }} balance={balance} ></PortfolioDepositDialog>
                    <PortfolioWithdrawDialog open={createWithdrawDialogOpen} onClose={() => { setCreateWithdrawDialogOpen(false); refresh();}} balance={balance}></PortfolioWithdrawDialog>

                </Box>
            </Container>


            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Grid container spacing={3}>

                    <Grid item xs={12} md={8} lg={6}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection:'column', height: 240, }} >
                            <Typography variant="h6" component="h2" gutterBottom>Total Portfolio Value:  ${portfolioValueData?.value?.toFixed(2)}</Typography>
                            <PortfolioValueHistoryChart data={portfolioValueHistory}></PortfolioValueHistoryChart>
                        </Paper>
                    </Grid>

                    <Grid item xs={12} md={4} lg={6}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }} >
                            <Typography variant="h6" component="h2" gutterBottom>Available Balance: ${balance && balance.toFixed(2)}</Typography>
                            <PortfolioBalanceHistoryChart data={balanceHistory}></PortfolioBalanceHistoryChart>
                        </Paper>
                    </Grid>

                    <Grid item xs={12} md={12} lg={12}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', maxHeight: 800, }} >
                            <Typography variant="h6" component="h2" gutterBottom>Portfolio:</Typography>
                            <PortfolioShowTable selectedPortfolio={selectedPortfolio} listPortfolio={listPortfolioData}></PortfolioShowTable>
                        </Paper>
                    </Grid>

                    <Grid item xs={12} md={12} lg={12}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', maxHeight: 800, }} >
                            <Typography variant="h6" component="h2" gutterBottom>Transactions: </Typography>
                            <PortfolioShowTransactions selectedPortfolio={selectedPortfolio} transactions={transactions}></PortfolioShowTransactions>
                            {/* <PortfolioShowTable selectedPortfolio={selectedPortfolio} ></PortfolioShowTable> */}
                        </Paper>
                    </Grid>

                </Grid>
            </Container>

        </>
    );
}