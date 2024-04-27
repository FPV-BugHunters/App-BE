import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useState } from 'react';
import axios from 'axios';
import IsAuthContext from '../contexts/IsAuthContext';
import { useNavigate } from "react-router-dom";

function Copyright(props) {
  return (
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://mui.com/">
        Your Website
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

// TODO remove, this demo shouldn't need to reset the theme.
const defaultTheme = createTheme();

export default function LogIn() {
  let navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [isLoginInPage, setIsLoginInPage] = useState(true);

  const { isAuth, setIsAuth } = React.useContext(IsAuthContext);
  // const history = useHistory();

  const handleSubmit = (event) => {

    event.preventDefault();

    const data = new FormData(event.currentTarget);

    if (isLoginInPage) {
      handleLogIn({
        username: data.get('username'),
        password: data.get('password')
      });
    }else if (!isLoginInPage) {
      handleSignUp({
        username: data.get('username'),
        password: data.get('password')
      });
    }

  }
  

  const handleLogIn = (data) => {

    axios.post(import.meta.env.VITE_API_URL + "/login", data, {
      headers: { 'Content-Type': 'application/json' }
    })

    .then(res => {
      const token = res.headers.authorization;
      if (token !== null) {
        sessionStorage.setItem("token", token);
        setIsAuth(true);
        navigate("/");
      }
    })

    .catch(() => setOpen(true));
  }

  const handleSignUp = (data) => {
    axios.post(import.meta.env.VITE_API_URL + "/registration", data, {
      headers: { 'Content-Type': 'application/json' }
    })

    .then(res => {
      setIsLoginInPage(true);
    })

    .catch(() => setOpen(true));
  }
    



  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            {isLoginInPage ? "Login in" : "Sign up"}
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="username"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            {/* <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            /> */}
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
              </Grid>
              <Grid item>
                <Button onClick={() => setIsLoginInPage(!isLoginInPage)}>{isLoginInPage ? "Don't have an account? Sign Up" : "Do you have an account? Log in"}</Button>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Copyright sx={{ mt: 8, mb: 4 }} />
      </Container>
    </ThemeProvider>
  );
}
