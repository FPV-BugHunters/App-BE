import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AdbIcon from '@mui/icons-material/Adb';
import { useLocation } from 'react-router-dom';
import { useEffect, useState, useContext } from 'react';
import IsAuthContext from '../contexts/IsAuthContext';
import UserContext from '../contexts/UserContex';
import { logoutUser } from '../api/UserApi';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

function TopNav () {
  const [ anchorElNav, setAnchorElNav ] = useState(null);
  const [ anchorElUser, setAnchorElUser ] = useState(null);

  const { isAuth, setIsAuth } = useContext(IsAuthContext);
  const { user, setUser } = useContext(UserContext);

  const navigate = useNavigate();
  const AuthRequiredUrl = [ '/watchlist', '/portfolio' ];

  useEffect(() => {
    const actualUrl = window.location.pathname;
    if (AuthRequiredUrl.includes(actualUrl) && !isAuth) {
      navigate('/login');
    }
  }, [ isAuth, navigate ]);

  const handleAccountClick = () => {
    console.log('Account');
    navigate('/user');
  };
  
  const location = useLocation();

  const handlePageChange = () => {
    const pageName = location.pathname;
    console.log('Current page:', pageName);
    // Do something with pageName...
  };

  useEffect(() => {
    handlePageChange();
  });

  const handleOpenNavMenu = (event) => {
    // console.log(event.currentTarget);
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    console.log(event.currentTarget);
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = (e) => {
    console.log('close', e);
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = (e) => {
    console.log('close', e);
    setAnchorElUser(null);
  };

  const handleLogout = () => {
    console.log('Logout');
    setUser(null);
    setIsAuth(false);
    logoutUser();
  }

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <AdbIcon sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }} />
          <Typography
            variant="h6"
            noWrap
            component="a"
            // href="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            <Link to="/" style={{ textDecoration: 'none', color: 'white', display: 'block', marginRight: 10 }}>TRADER</Link>
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
              >
                <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                  <Link to="/dashboard" style={{ textDecoration: 'none', color: 'white', padding: '5px 15px' }}>Dashboard</Link>
                  <Link to="/watchlist" style={{ textDecoration: 'none', color: 'white', padding: '5px 15px' }}>Watchlist</Link>
                  <Link to="/portfolio" style={{ textDecoration: 'none', color: 'white', padding: '5px 15px' }}>Portfolio</Link>
                </Box>
            </Menu>
          </Box>
          <AdbIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
          <Typography
            variant="h5"
            noWrap
            component="a"
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            <Link to="/" style={{ textDecoration: 'none', color: 'white', display: 'block', marginRight: 10 }}>TRADER</Link>
          </Typography>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            <Link to="/dashboard" style={{ textDecoration: 'none', color: 'white', display: 'block', marginRight: 20 }}>Dashboard</Link>
            <Link to="/watchlist" style={{ textDecoration: 'none', color: 'white', display: 'block', marginRight: 20 }}>Watchlist</Link>
            <Link to="/portfolio" style={{ textDecoration: 'none', color: 'white', display: 'block', marginRight: 20 }}>Portfolio</Link>

          </Box>


          {isAuth ? (

            <Box sx={{ flexGrow: 0 }}>
              <Tooltip title="Open settings">
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                  <Avatar alt={user && user.firstName} />
                </IconButton>
              </Tooltip>

              <Menu
                sx={{ mt: '45px' }}
                id="menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                open={Boolean(anchorElUser)}
                onClose={handleCloseUserMenu}
              >
                <MenuItem >
                  <Typography textAlign="center" onClick={()=>handleAccountClick()} >Account</Typography>
                </MenuItem>

                <MenuItem onClick={handleLogout}>
                  <Typography textAlign="center">Logout</Typography>
                </MenuItem>

              </Menu>
            </Box>
          ) : (
            <Link to="/login" style={{ textDecoration: 'none', color: 'white', display: 'block', marginRight: 10 }}>Login</Link>
          )}

        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default TopNav;