
import DashboardSymbolDataTable from '../components/DashboardSymbolDataTable'
import { Button, Container, Grid, Paper, Box } from '@mui/material';

export default function Home(){
    return (
        <div>
            <Container  maxWidth="lg" sx={{ mt: 10, mb: 4,  }}>
                <DashboardSymbolDataTable />
            </Container>
        </div>
    );
}