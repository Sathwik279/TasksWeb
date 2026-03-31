import {createContext, useContext, useState} from 'react';

const AuthContext = createContext();

export function AuthProvider({children}){
    const [token,setToken] = useState(()=>localStorage.getItem('jwt_token'));

    const login = (jwt)=>{
        setToken(jwt);
        localStorage.setItem('jwt_token',jwt)
    }

    const logout = ()=>{
        setToken(null)
        localStorage.removeItem('jwt_token');
    }

    return (
        <AuthContext.Provider value = {{token,login,logout}}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth(){
    return useContext(AuthContext);
}
