import axios from "axios";

const Login = () => {
  const naverLogin = async () => {
    const authorizeUrl = await axios.get("/login/authorize");
    window.location.replace(authorizeUrl.data);
  };

  return <button onClick={naverLogin}>Login</button>;
};

export default Login;
