import CssBaseline from '@mui/material/CssBaseline';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ThemeProvider, createTheme } from '@mui/material/styles';
// import * as React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useState } from 'react';
import TopNav from './components/TopNav';
import LogIn from './components/LogIn';
import Home from './pages/Home';
import { grey } from '@mui/material/colors';
import IsAuthContext from './contexts/IsAuthContext';
import UserContex from './contexts/UserContex';
import CheckUser from './components/CheckUser';

import Watchlist from './pages/Watchlist';
import Portfolio from './pages/Portfolio';
import User from './pages/User';

const darkTheme = createTheme({
 palette: {
    mode: 'dark',
    primary: {
      main: '#3f51b5',
    },
    secondary: {
      main: '#f50057',
    },
  },
// 0b2948
  
});

const queryClient = new QueryClient();

function App() {

  const [ isAuth, setIsAuth ] = useState(false); 
  const [ user, setUser] = useState({});

  

  
  return (
  <>
    <ThemeProvider theme={darkTheme}> 
      <IsAuthContext.Provider value={{isAuth, setIsAuth}}>
        <UserContex.Provider value={{user, setUser}}>
          <CssBaseline />
          <CheckUser />
          <Router > 
              <TopNav />
              <QueryClientProvider client={queryClient}>
                <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/dashboard" element={<Home />} />
                  <Route path="/login" element={<LogIn />} />
                  <Route path="/watchlist" element={<Watchlist />} /> 
                  <Route path="/portfolio" element={<Portfolio/>} />
                  <Route path="/user" element={<User/>} />
                </Routes>
              </QueryClientProvider>
          </Router>

        </UserContex.Provider>
      </IsAuthContext.Provider> 
    </ThemeProvider>  
  </>
  );
}

export default App;