import * as React from 'react';
import { styled } from '@mui/material/styles';
import {
    Dialog, DialogTitle, List, ListItem, ListItemButton, ListItemAvatar, Avatar,
    ListItemText, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Box, tableCellClasses
} from '@mui/material';
import PersonIcon from '@mui/icons-material/Person';
import AddIcon from '@mui/icons-material/Add';
import { blue } from '@mui/material/colors';
import { getSymbolsNotInWatchlist } from '../api/SymbolApi';
import { useQuery } from '@tanstack/react-query';
import { Style } from '@mui/icons-material';
import ClearIcon from '@mui/icons-material/Clear';


export interface WatchlistAddDialogProps {
    open: boolean;
    onClose: (value) => void;
}

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
                <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}> <DialogTitle>Add Crypto to your Watchlist</DialogTitle> <ClearIcon onClick={() => handleListItemClick(null)} sx={{ cursor: 'pointer' }} /></Box>
                
                <Box sx={{}}>
                    <TableContainer sx={{ maxHeight: 440 }}>
                    
                        <Table stickyHeader aria-label="sticky table">
                            <TableHead>
                                <TableRow>
                                    <StyledTableCell>ID</StyledTableCell>
                                    <StyledTableCell>Name</StyledTableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data
                                    .map((row) => {
                                        return (
                                            <TableRow hover role="checkbox" tabIndex={-1} key={row.crypto_id} onClick={() => handleListItemClick(row.crypto_id)}>
                                                <StyledTableCell>{row.crypto_id}</StyledTableCell>
                                                <StyledTableCell>{row.name}</StyledTableCell>
                                            </TableRow>
                                        );
                                    })}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            </Box>
        </Dialog>
    );
}