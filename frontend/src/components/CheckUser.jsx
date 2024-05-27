import * as React from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import IsAuthContext from '../contexts/IsAuthContext';
import { useNavigate } from "react-router-dom";
import { userInfo } from '../api/UserApi';
import { useEffect, useContext } from 'react';
import UserContext from '../contexts/UserContex';


export default function CheckUser () {

  const { isAuth, setIsAuth } = useContext(IsAuthContext);
  const { user, setUser } = useContext(UserContext);
  console.log('Checking if user is logged in')

  useEffect(() => {

    userInfo()
      .then((response) => {
        setUser(response.data);
        setIsAuth(true);

      })
      .catch((error) => {
        setUser(null);
        setIsAuth(false);
      });
  }, []);
  
};


