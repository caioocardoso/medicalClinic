import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./Login";
import Dashboard from "./Dashboard";
import RegisterPatient from "./RegisterPatient";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/registro" element={<RegisterPatient />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;