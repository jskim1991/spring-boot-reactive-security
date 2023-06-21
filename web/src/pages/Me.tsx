import { useEffect, useState } from "react";
import axios from "axios";

type User = {
  id: string;
  naverId: string;
  name: string;
  nickname: string;
  email: string;
};

const Me = () => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    let config = {};
    if (token) {
      config = {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      };
    }
    axios.get("/users/me", config).then((response) => {
      setUser(response.data);
    });
  }, []);

  if (!user) {
    return <h3>No user info found ...</h3>;
  }

  return (
    <div>
      <h3>Name</h3>
      <div>{user.id}</div>
      <h3>Naver ID</h3>
      <div>{user?.naverId}</div>
      <h3>Name</h3>
      <div>{user?.name}</div>
      <h3>Nickname</h3>
      <div>{user?.nickname}</div>
      <h3>Email</h3>
      <div>{user?.email}</div>
    </div>
  );
};

export default Me;
