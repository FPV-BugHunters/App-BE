import * as React from 'react';
import { styled } from '@mui/material/styles';
import {
    Dialog, DialogTitle, List, ListItem, ListItemButton, ListItemAvatar, Avatar, Autocomplete,
    ListItemText, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Box, tableCellClasses, Button, Grid, TextField,
    Typography,
    Input
} from '@mui/material';
import { withdrawBalance } from '../api/SymbolApi';
import { sellTransaction, buyTransaction, getSymbols } from '../api/SymbolApi';
import { useQuery } from '@tanstack/react-query';

export interface PortfolioWithdrawDialogProps {
    type: string;
    open: boolean;
    onClose: () => void;
    selectedPortfolio: number;
    balance: number;
}


export default function PortfolioWithdrawDialog(props: PortfolioWithdrawDialogProps) {

    const { onClose, open, type, selectedPortfolio, balance } = props;

    const handleClose = () => {
        onClose();
    };

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        withdrawBalance(Number(formData.get('amount')))
        handleClose();
    };


    return (

        <Dialog onClose={handleClose} open={open} sx={{ maxHeight: '70%', justifyContent: 'center', marginTop: 10 }}>
            <Box sx={{ margin: 5, }}>
                <Typography variant="h4" component="div">Withdraw</Typography>
                <Typography variant="h6" component="div" sx={{marginTop:"10px"}}>{balance && "Balance: $" + balance.toFixed(2)}</Typography>

                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                    <TextField type="number" margin="normal" label="Amount" name="amount"> </TextField>

                    <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }} >Buy</Button>
                </Box>
            </Box>
        </Dialog>
    );

}