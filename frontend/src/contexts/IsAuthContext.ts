import React from 'react';

const IsAuthContext = React.createContext <{
    isAuth: boolean;
    setIsAuth: React.Dispatch<React.SetStateAction<boolean>>;
} > ({
    isAuth: false,
    setIsAuth: () => { }
})


export default IsAuthContext;