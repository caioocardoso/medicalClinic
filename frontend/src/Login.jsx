import { useState } from "react";
import api from "./services/api";
import { useNavigate } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await api.post("/auth/login", {
        email: email,
        password: password
      });

      const { token } = response.data;

      localStorage.setItem("token", token);

      alert("Login realizado com sucesso! Token guardado.");
      
      navigate("/dashboard")
      
    } catch (err) {
      console.error(err);
      setError("Falha no login. Verifique suas credenciais.");
    }
  };

  return (
    <div style={{ display: "flex", justifyContent: "center", marginTop: "50px" }}>
      <form onSubmit={handleLogin} style={{ display: "flex", flexDirection: "column", width: "300px", gap: "10px" }}>
        <h2>Acesse a Clínica</h2>
        
        <input
          type="email"
          placeholder="Seu e-mail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          style={{ padding: "10px" }}
        />
        
        <input
          type="password"
          placeholder="Sua senha"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={{ padding: "10px" }}
        />
        
        {error && <p style={{ color: "red" }}>{error}</p>}
        
        <button type="submit" style={{ padding: "10px", cursor: "pointer", backgroundColor: "#007BFF", color: "white", border: "none" }}>
          Entrar
        </button>
      </form>
    </div>
  );
}

export default Login;