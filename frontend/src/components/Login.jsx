import loginUser from '../services/loginUser'
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const handleSubmit = async(e)=>{
        e.preventDefault();

        const data = {
            email: e.target.email.value,
            password: e.target.password.value
        }

        try{
            const response = await loginUser(data);
            login(response.token); //store the token globally
            // alert("Logged In Successfully");
            navigate('/todos')
        }catch(error){
            alert(error.message)
        }
  }


  return (
    <div className='vertical'>
      <h1>Login page</h1>
      <form onSubmit={handleSubmit}>
        <div className="vertical">
          <div className="nearAdjust">
            <label htmlFor="email">Enter Email:</label>
            <input id="email"></input>
          </div>
          <br></br>
          <div className="nearAdjust">
            <label htmlFor="password">Enter Password:</label>
            <input id="password" type='password'></input>
          </div>
          <br></br>
          <br></br>
          <br></br>
          <div>
            <button>Login</button>
          </div>
        </div>
      </form>
      <h3>Not having Account? <Link to='/register'>Register</Link></h3>
      {/* <p>{token}</p> */}
    </div>
  );
}
