import React from 'react';
import WatchlistTable from '../components/WatchlistTable';
import { Button, Container, Grid, Paper, Box, useTheme } from '@mui/material';
import WatchlistAddDialog from '../components/WatchlistAddDialog';
import { addSymbolToWatchlist, removeSymbolFromWatchlist, getWatchlist } from '../api/SymbolApi';
import { useQuery } from '@tanstack/react-query';

// const darkTheme = createTheme({
//   palette: {
//     primary: {
//       main: '#3f51b5',
//     },
//     background: {
//       default: '#0b2948',
//       paper: '#0b2948'
//     },
//     text: {
//       primary: '#fff',
//       secondary: grey[500],
//     },
//   },
  
// });

export default function Watchlist () {
    const theme = useTheme();

    const [ watchlistDialogOpen, setWatchlistDialogOpen ] = React.useState(false);
    const [ watchlistDialogCrypto, setWatchlistDialogCrypto ] = React.useState(null);


    const handlewatchlistDialogOpen = () => {
        setWatchlistDialogOpen(true);
    }
    // addSymbolToWatchlist
    const handleAddCryptoClose = (value) => {
        setWatchlistDialogOpen(false);
        setWatchlistDialogCrypto(value);
        addSymbolToWatchlist(value);
        setTimeout(refetch, 100);
    }

    const { data, isError, isLoading, isSuccess, refetch } = useQuery({
        queryKey: [ "watchlist" ],
        queryFn: getWatchlist
    });

    const handleRemoveCrypto = (value) => {

        removeSymbolFromWatchlist(value).then(() => {
            refetch();
        });
    }

    return (
        <>

            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
                    <Button viariant="contained" color="primary" onClick={handlewatchlistDialogOpen} sx={{bgcolor:theme.palette.primary.main, color: 'white', mb:2}}>Add Crypto</Button>
                    <WatchlistAddDialog open={watchlistDialogOpen} onClose={handleAddCryptoClose} ></WatchlistAddDialog>
                </Box>
                <WatchlistTable data={data} isError={isError} isLoading={isLoading} isSuccess={isSuccess} handleRemoveCrypto={handleRemoveCrypto} ></WatchlistTable>
            </Container>






            {/* <Grid container spacing={3}>
                    <Grid item xs={12} md={8} lg={9}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }} >
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={4} lg={3}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }} >

                        </Paper>
                    </Grid>
                    <Grid item xs={12}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>

                        </Paper>
                    </Grid>
                </Grid> */}

        </>
    );
}