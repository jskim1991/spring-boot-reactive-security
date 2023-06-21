import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const RedirectHandler = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const code = new URL(window.location.href).searchParams.get("code");
    const state = new URL(window.location.href).searchParams.get("state");

    axios.get(`/login/token?code=${code}&state=${state}`).then((response) => {
      localStorage.setItem("token", response.data.accessToken);
      navigate("/me");
    });
  }, []);

  return (
    <div>
      <div>Loading...</div>
    </div>
  );
};

export default RedirectHandler;
