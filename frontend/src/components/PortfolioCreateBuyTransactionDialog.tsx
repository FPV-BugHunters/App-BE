import * as React from 'react';
import { styled } from '@mui/material/styles';
import {
    Dialog, DialogTitle, List, ListItem, ListItemButton, ListItemAvatar, Avatar, Autocomplete,
    ListItemText, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Box, tableCellClasses, Button, Grid, TextField,
    Typography,
    Input
} from '@mui/material';

import { sellTransaction, buyTransaction, getSymbols } from '../api/SymbolApi';
import { useQuery } from '@tanstack/react-query';

export interface PortfolioCreateTransactionDialogProps {
    type: string;
    open: boolean;
    onClose: () => void;
    selectedPortfolio: number;
    balance: number;
}

type CryptoList = {
    id: number;
    symbol: string;
    label: string;
    priceUSD: number;
}

export default function PortfolioCreateBuyTransactionDialog(props: PortfolioCreateTransactionDialogProps) {

    const { onClose, open, type, selectedPortfolio, balance } = props;
    const [cryptoList2, setCryptoList2] = React.useState<CryptoList[]>([]);
    const [selectedCrypto, setSelectedCrypto] = React.useState<CryptoList | null>(null);
    const [availableAmount, setAvailableAmount] = React.useState<number>(0);
    const [amount, setAmount] = React.useState<string>(0);
    

    React.useEffect(() => {
        setSelectedCrypto(null);
        setAvailableAmount(0);
        setAmount("0");
    },[open])

    const handleClose = () => {
        onClose();
    };

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);

        if ( selectedCrypto && amount) {
            const data = {
                id: selectedCrypto.id,
                amount: Number(formData.get("amount")),
                portfolio: selectedPortfolio
            };
            buyTransaction(data.id, data.portfolio, data.amount).then(() => {
                console.log("buy success");
                onClose();
            }).then(() => {
                console.log("buy failed");
            });

        }
    };

    const { data: cryptoList, isError, isLoading, isSuccess } = useQuery({
        queryKey: ["symbol2"],
        queryFn: getSymbols
    });

    const handleAmountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log(event.target.value);
        const value = parseFloat(event.target.value);
        console.log('handleAmountChange', value);   
        if ((value >= 0 && value <= availableAmount) || event.target.value == "") {
            
            setAmount(event.target.value)
        }

        // if (value >= 0 && value <= availableAmount) {
        //     setAmount(value.toString());
        // }
    }

    React.useEffect(() => {
        if (cryptoList) {
            const newCryptoList2 = cryptoList.map((crypto) => ({
                id: crypto.id,
                symbol: crypto.symbol,
                label: crypto.symbol,
                priceUSD: crypto.priceUSD
            }));
            setCryptoList2(newCryptoList2);
        }
    }, [cryptoList]);


    const setCrypo = (event: React.ChangeEvent<HTMLInputElement>, value) => {
        setSelectedCrypto(value);
        const availableAmount2 = (balance / value.priceUSD) * 0.99;
        setAvailableAmount(availableAmount2);
        availableAmount2 && setAmount(availableAmount2.toFixed(2));
    }



    return (

        <Dialog onClose={handleClose} open={open} sx={{ maxHeight: '70%', justifyContent: 'center', marginTop: 10 }}>
            <Box sx={{ margin: 5, }}>
                <Typography variant="h4" component="div">Buy crypto</Typography>
                <Typography variant="h6" component="div" sx={{marginTop:"10px"}}>{balance && "Balance: $" + balance.toFixed(2)}</Typography>

                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                    <Autocomplete
                        // disablePortal
                        required
                        id="combo-box-demo"
                        options={cryptoList2}
                        sx={{ width: 300 }}
                        onChange={setCrypo}
                        renderInput={(params) => <TextField {...params} label="Crypto" />}
                    />
                    <Box sx={{margin: '10px 0'}}>
                        <Typography variant="h6" component="div">{selectedCrypto && "Price per unit: $" + selectedCrypto.priceUSD.toFixed(2)}</Typography>
                        <Typography variant="h6" component="div">{selectedCrypto && "Available amount: " + availableAmount.toFixed(2)}</Typography>
                    </Box>
                    <TextField
                        margin="normal"
                        label="Amount"
                        name="amount"
                        value={amount}
                        onChange={handleAmountChange}
                    />

                    <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }} >Buy</Button>
                </Box>
            </Box>
        </Dialog>
    );

}