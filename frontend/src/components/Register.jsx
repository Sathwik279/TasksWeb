import registerUser from '../services/registerUser'
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

export default function Register() {
  const { login } = useAuth();
  const navigate = useNavigate();
    const handleSubmit = async(e)=>{
        e.preventDefault();

        const data = {
            userName: e.target.userName.value,
            email: e.target.email.value,
            password: e.target.password.value
        }

        try{
            const response = await registerUser(data);
            login(response.token); //store the token globally
            navigate('/todos')
        }catch(error){
            alert(error.message)
        }
    }
  return (
    <div className='vertical'>
      <h1>Registration page</h1>
      <form onSubmit={handleSubmit}>
        <div className="vertical">
          <div className="nearAdjust">
            <label htmlFor="userName">Enter Name:</label>
            <input id="userName"></input>
          </div>
          <br></br>
          <div className="nearAdjust">
            <label htmlFor="email">Enter email:</label>
            <input id="email"></input>
          </div>
          <br></br>
          <div className="nearAdjust">
            <label htmlFor="password">Enter Password:</label>
            <input id="password" type='password'></input>
          </div>
          <br></br>
          <div>
            <button>Submit</button>
          </div>
          <br></br>
          <h3>Already Registered? <Link to='/login'>Login</Link></h3>
        </div>
      </form>
    </div>
  );
}
