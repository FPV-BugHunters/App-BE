import CssBaseline from '@mui/material/CssBaseline';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import * as React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import Login from './components/Login';
import TopNav from './components/TopNav';
import { deepOrange, grey, indigo } from '@mui/material/colors';
import SymbolDataTable from './components/SymbolDataTable';

const darkTheme = createTheme({
  palette: {
    primary: {
      main: '#3f51b5',
    },
    background: {
      default: '#0b2948',
      paper: '#0b2948'
    },
    text: {
      primary: '#fff',
      secondary: grey[500],
    },
  },
  
});

const queryClient = new QueryClient();

function App() {
  return (
  <>
    <ThemeProvider theme={darkTheme}>

      <CssBaseline />
      <Router >
        <TopNav />
      </Router>
      <QueryClientProvider client={queryClient}>
        <SymbolDataTable />
        {/* <Login /> */}
      </QueryClientProvider>

    </ThemeProvider> 
  </>
  );
}

export default App;