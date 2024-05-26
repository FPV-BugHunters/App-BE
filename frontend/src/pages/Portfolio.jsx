import React from 'react';
import WatchlistTable from '../components/WatchlistTable';
import {
    Button, Container, Grid, Paper, Box, useTheme,
    FormControl, InputLabel, Select, MenuItem, Alert, Typography
} from '@mui/material';
import WatchlistAddDialog from '../components/WatchlistAddDialog';
import { listUserPortfolio, getBalance } from '../api/SymbolApi';
import { useQuery } from '@tanstack/react-query';
import PortfolioCreateDialog  from '../components/PortfolioCreateDialog';
import PortfolioCreateTransactionDialog from '../components/PortfolioCreateTransactionDialog';

export default function Portfolio () {
    const theme = useTheme();
    const [ createPorfolioDialogOpen, setCreatePortfolioDialogOpen ] = React.useState(false);
    const [ selectedPortfolio, setSelectedPortfolio ] = React.useState('');
    
    const [ createTransactionDialogOpen, setCreateTransactionDialogOpen ] = React.useState(false);
    const [ transactionType, setTransactionType ] = React.useState('buy');


    // list user portfolios
    const { data: listuserPortfolioData, isError, isLoading, isSuccess, refetch } = useQuery({
        queryKey: [ "userportfolio" ],
        queryFn: listUserPortfolio
    });
    
    React.useEffect(() => {
        if (listuserPortfolioData && listuserPortfolioData.length > 0) {
            setSelectedPortfolio(listuserPortfolioData[ 0 ].id);
        }
    }, [ listuserPortfolioData ]);

    // list crypto from user portfolio
    // 
    
    const { data: portfolioCrypto } = useQuery({
        queryKey: [ "po" ],
        queryFn: listUserPortfolio
    });
    

    const { data: balance } = useQuery({
        queryKey: [ "balance" ],
        queryFn: getBalance,
    });
    

    
    const handleSelectPortfolio = (e) => {
        setSelectedPortfolio(e.target.value);
    }

    return (
        <>
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: '20px', height:'50px' }}>
                    <Button viariant="contained" color="primary" onClick={() => setCreateTransactionDialogOpen(true) && setTransactionType("buy")} sx={{ bgcolor: theme.palette.primary.main, color: 'white' }}>Buy</Button>
                    <Button viariant="contained" color="primary" onClick={() => setCreateTransactionDialogOpen(true) && setTransactionType("sell")} sx={{ bgcolor: theme.palette.primary.main, color: 'white' }}>Sell</Button>
                    {listuserPortfolioData && listuserPortfolioData.length === 0 && <Alert variant="filled" severity="error"> Create a portfolio to get started </Alert> }
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
                    <PortfolioCreateTransactionDialog
                        selectedPortfolio={selectedPortfolio}
                        open={createTransactionDialogOpen} onClose={() => setCreateTransactionDialogOpen(false)}
                        type={transactionType}
                        balance={balance}
                    ></PortfolioCreateTransactionDialog>
                </Box>
            </Container>
        

            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Grid container spacing={3}>

                    <Grid item xs={12} md={8} lg={9}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }} >
                        </Paper>
                    </Grid>

                    <Grid item xs={12} md={4} lg={3}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }} >
                            <Typography variant="h6" component="h2" gutterBottom>Your balance:</Typography>
                            <Typography variant="h6" component="h2" gutterBottom>${balance && balance.toFixed(2)} </Typography>
                        </Paper>
                    </Grid>

                    <Grid item xs={12} md={8} lg={9}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }} >
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={4} lg={3}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }} >

                        </Paper>
                    </Grid>

                    <Grid item xs={12}>
                        <Paper sx={{ display: 'flex', flexDirection: 'column' }}>
                            {/* <WatchlistTable data={data} isError={isError} isLoading={isLoading}  ></WatchlistTable> */}
                        </Paper>
                    </Grid>
                </Grid>
            </Container>

        </>
    );
}