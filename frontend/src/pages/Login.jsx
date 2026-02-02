import { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

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
      
      // Decodificar o token para extrair informações do usuário
      try {
          const decoded = jwtDecode(token);
          
          // Armazenar roles
          if (decoded.roles) {
              localStorage.setItem("userRoles", JSON.stringify(decoded.roles));
          }

          // Armazenar IDs específicos baseado nas roles
          if (decoded.patientId) {
              localStorage.setItem("patientId", decoded.patientId);
          } else {
              localStorage.removeItem("patientId");
          }

          if (decoded.doctorId) {
              localStorage.setItem("doctorId", decoded.doctorId);
          } else {
              localStorage.removeItem("doctorId");
          }

      } catch (decodeErr) {
          console.error("Erro ao processar token:", decodeErr);
      }

      navigate("/home");
      
    } catch (err) {
      console.error(err);
      setError("Falha no login. Verifique suas credenciais.");
    }
  };

  return (
    <div className="container" style={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "80vh" }}>
      <div className="card" style={{ width: "100%", maxWidth: "400px", padding: "2rem" }}>
        <h2 className="text-center mb-2" style={{ color: "var(--primary-color)" }}>Bem-vindo</h2>
        <p className="text-center mb-2" style={{ color: "var(--text-secondary)" }}>Faça login para gerenciar suas consultas</p>
        
        <form onSubmit={handleLogin}>
            <div className="form-group">
                <label className="form-label">E-mail</label>
                <input
                    type="email"
                    className="form-control"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    placeholder="seu@email.com"
                />
            </div>
            
            <div className="form-group">
                <label className="form-label">Senha</label>
                <input
                    type="password"
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    placeholder="Sua senha"
                />
            </div>
            
            {error && <div style={{ color: "var(--error-color)", marginBottom: "1rem", textAlign: "center" }}>{error}</div>}
            
            <button type="submit" className="btn btn-primary" style={{ width: "100%", marginBottom: "1rem" }}>
              Entrar
            </button>
            
            <button 
                type="button" 
                onClick={() => navigate("/registro")} 
                className="btn btn-outline"
                style={{ width: "100%" }}
            >
                Criar Nova Conta
            </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
