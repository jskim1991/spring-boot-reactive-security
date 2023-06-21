import React from "react";
import "./App.css";
import axios from "axios";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Me from "./pages/Me";
import RedirectHandler from "./pages/RedirectHandler";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/me" element={<Me />} />
        <Route path="/redirect" element={<RedirectHandler />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
