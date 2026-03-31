import './App.css'

import Login from './components/Login.jsx'
import Register from './components/Register.jsx'
import TodoPage from './components/TodoPage.jsx'
import { useAuth } from './context/AuthContext.jsx'
import { createBrowserRouter } from 'react-router-dom'
import { Navigate } from 'react-router-dom'
import { RouterProvider } from 'react-router-dom'
import ThemeToggle from './ThemeToggle.jsx'
import { Outlet } from 'react-router-dom'
import { NotificationProvider } from './context/NotificationContext.jsx'
import { TodoProvider } from './context/TodoContext.jsx'

const ProtectedRoute = ({children})=>{
  const { token } = useAuth();
  return token ? children : <Navigate to='/login'/>;
};



// 1. Create a Layout component
const Layout = () => (
  <div>
    <nav>
      <h1>My Todo App</h1>
      
      <ThemeToggle /> {/* Toggle stays here globally */}
      
    </nav>
    {/* Outlet is where the current page (Login or Todos) renders */}
    <Outlet /> 
  </div>
);

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />, // The parent
    children: [
       { 
        //This is the 'default' child of "/"
        index: true, 
        element: (
          <ProtectedRoute>
            <TodoPage />
          </ProtectedRoute>
        ) 
      },
      { path: "login", element: <Login /> },
      { path: "register", element: <Register/>},
      { path: "todos", element: <ProtectedRoute><TodoPage /></ProtectedRoute> },
    ],
  },
]);

function App() {
  
  return (
    <NotificationProvider>
      <TodoProvider>
        <RouterProvider router = {router}/>
      </TodoProvider>
    </NotificationProvider>
  )
}

export default App
