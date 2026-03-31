import { useState,useEffect} from 'react';
import { useAuth } from './context/AuthContext';

export default function ThemeToggle(){
    const [isDarkMode, setIsDarkMode] = useState(true);
    const { logout } = useAuth();
    const { token } = useAuth();
   


    useEffect(()=>{
        if(isDarkMode){
            document.documentElement.classList.remove('light-mode');
        }else{
            document.documentElement.classList.add('light-mode');

        }
    },[isDarkMode])

    return (
        <div>
        {token?<button onClick={logout}>Log out</button>:''}
        <button onClick={()=>setIsDarkMode(!isDarkMode)}>
             { isDarkMode?'Light':'Dark'} Mode
        </button>
        </div>
    )
}