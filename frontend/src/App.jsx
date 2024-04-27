import CssBaseline from '@mui/material/CssBaseline';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import * as React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { useState } from 'react';
import TopNav from './components/TopNav';
import LogIn from './components/LogIn';
import Home from './pages/Home';
import Watchlist from './pages/Watchlist';
import { grey } from '@mui/material/colors';
import IsAuthContext from './contexts/IsAuthContext';

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

  const [isAuth, setIsAuth] = useState(false); 
  
  return (
  <>
    <ThemeProvider theme={darkTheme}> 
      <IsAuthContext.Provider value={{isAuth, setIsAuth}}>
        <CssBaseline />

        <Router > 

            <TopNav />
            <QueryClientProvider client={queryClient}>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/dashboard" element={<Home />} />
                <Route path="/login" element={<LogIn />} />
                <Route path="/watchlist" element={<Watchlist />} /> 
              </Routes>
            </QueryClientProvider>
        </Router>
      </IsAuthContext.Provider> 
    </ThemeProvider>  
  </>
  );
}

export default App;