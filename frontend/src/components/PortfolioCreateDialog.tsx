import * as React from 'react';
import {
    Dialog, DialogTitle, List, ListItem, ListItemButton, ListItemAvatar, Avatar,
    ListItemText, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Box, tableCellClasses, Button, Grid, TextField,
    Typography
} from '@mui/material';
import ClearIcon from '@mui/icons-material/Clear';

import { createUserPortfolio } from '../api/SymbolApi';

export interface PorfolioCreateDialogProps {
    open: boolean;
    onClose: () => void;
}


export default function PorfolioCreateDialog(props: PorfolioCreateDialogProps) {

    const { onClose, open } = props;

    const handleClose = () => {
        onClose();
    };

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {

        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        const name = formData.get('name') as string;

        createUserPortfolio(name).then(() => {
            onClose();
        });
        onClose();
    };

    return (

        <Dialog onClose={handleClose} open={open} sx={{ maxHeight: '70%', justifyContent: 'center', marginTop: 10 }}>
            <Box sx={{ margin: 5, }}>

                <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}><ClearIcon onClick={() => handleClose()} sx={{ cursor: 'pointer' }} /></Box>
                <Typography variant="h6" component="div">Create Portfolio</Typography>
                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                    <TextField margin="normal" required fullWidth id="name" label="Portfolio name" name="name" autoComplete="name" autoFocus sx={{borderColor: 'white'}} />
                    <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }} >submit</Button>
                </Box>
            </Box>
        </Dialog>
    );

}