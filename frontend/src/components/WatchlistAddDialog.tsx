import * as React from 'react';
import { styled } from '@mui/material/styles';
import {
    Dialog, DialogTitle, List, ListItem, ListItemButton, ListItemAvatar, Avatar,
    ListItemText, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Box
} from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import AddIcon from '@mui/icons-material/Add';
import { blue } from '@mui/material/colors';
import { getSymbolsNotInWatchlist } from '../api/SymbolApi';
import { useQuery } from '@tanstack/react-query';

export interface WatchlistAddDialogProps {
    open: boolean;
    onClose: (value) => void;
}




export default function WatchlistAddDialog(props: WatchlistAddDialogProps) {

    const { onClose, open } = props;

    const handleClose = () => {
        // onClose();
    };

    const handleListItemClick = (value) => {
        onClose(value);
    };


    const { data, isError, isLoading, isSuccess } = useQuery({
        queryFn: getSymbolsNotInWatchlist
    });

    if (isLoading) {
        return <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>Loading...</Box>;
    }

    if (isError || !data) {
        return <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '10vh' }}>
            Error fetching data
        </Box>;
    }

    return (
        <Dialog onClose={handleClose} open={open} sx={{ maxHeight: '70%', justifyContent: 'center', marginTop:10 }}>
            <Box sx={{ margin: 5, }}>
                <DialogTitle>Add Crypto to your Watchlist</DialogTitle>
                <Box sx={{}}>
                    <TableContainer sx={{ maxHeight: 440 }}>
                        <Table stickyHeader aria-label="sticky table">
                            <TableHead>
                                <TableRow>
                                    <TableCell > ID </TableCell>
                                    <TableCell > Name </TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data
                                    .map((row) => {
                                        return (
                                            <TableRow hover role="checkbox" tabIndex={-1} key={row.crypto_id} onClick={() => handleListItemClick(row.crypto_id)}>
                                                <TableCell>{row.crypto_id}</TableCell>
                                                <TableCell>{row.name}</TableCell>
                                            </TableRow>
                                        );
                                    })}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </Box>
            {/* <List sx={{ pt: 0 }}>
                {data.map((crypto) => (
                    <ListItem disableGutters key={crypto}>
                        <ListItemButton onClick={() => handleListItemClick(crypto)}>
                        
                            <ListItemText primary={crypto.id} secondary={crypto.name} />
                        </ListItemButton>
                    </ListItem>
                ))}
                <ListItem disableGutters>
                    <ListItemButton
                        autoFocus
                        onClick={() => handleListItemClick('addAccount')}
                    >
                        <ListItemAvatar>
                            <Avatar>
                                <AddIcon />
                            </Avatar>
                        </ListItemAvatar>
                        <ListItemText primary="Add account" />
                    </ListItemButton>
                </ListItem>
            </List> */}
        </Dialog>
    );
}