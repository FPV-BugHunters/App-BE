
import DashboardSymbolDataTable from '../components/DashboardSymbolDataTable'
import { Button, Container, Grid, Paper, Box, Typography } from '@mui/material';


import IsAuthContext from '../contexts/IsAuthContext';
import UserContext from '../contexts/UserContex';
import { useContext } from 'react';

export default function User () {

    const { isAuth, setIsAuth } = useContext(IsAuthContext);
    const { user, setUser } = useContext(UserContext);

    return (
        <>
            {user && (
                
                <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <Typography variant="h6" component="h1" gutterBottom>Name: {user.firstName} {user.lastName}</Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="h6" component="h1" gutterBottom>Username: {user.username}</Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="h6" component="h1" gutterBottom>Email: {user.email}</Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <Typography variant="h6" component="h1" gutterBottom>Phone: {user.phoneNumber}</Typography>
                        </Grid>
                    </Grid>
                </Container>
                )
            }
        </>
    );

}