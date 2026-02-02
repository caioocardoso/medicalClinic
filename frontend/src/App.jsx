import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import RegisterPatient from "./pages/RegisterPatient";
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/dashboard" element={<Home />} /> {/* Compatibility redirect */}
        <Route path="/registro" element={<RegisterPatient />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
