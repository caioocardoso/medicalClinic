import { useEffect, useState } from "react";
import api from "./services/api";
import { useNavigate } from "react-router-dom";

function Dashboard() {
  const [consultas, setConsultas] = useState([]);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      try {
        // 1. O backend já possui o usuário no Token, mas vamos buscar os dados 
        // Você pode criar um endpoint /auth/me ou usar os dados do login.
        // Por agora, vamos focar em buscar as consultas do paciente logado:
        const resConsultas = await api.get("/consulta/paciente"); // Endpoint do AppointmentController
        setConsultas(resConsultas.data);
        
        setLoading(false);
      } catch (err) {
        console.error("Erro ao carregar dashboard", err);
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  function handleLogout() {
    localStorage.removeItem("token");
    navigate("/");
  }

  if (loading) return <p>Carregando dados...</p>;

  return (
    <div style={{ padding: "30px", maxWidth: "800px", margin: "0 auto", fontFamily: "sans-serif" }}>
      <header style={{ display: "flex", justifyContent: "space-between", borderBottom: "2px solid #eee", pb: "10px" }}>
        <h1>Minha Área (Paciente)</h1>
        <button onClick={handleLogout} style={{ height: "40px", backgroundColor: "#ff4d4d", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Sair</button>
      </header>

      {/* Seção de Consultas */}
      <section style={{ marginTop: "30px" }}>
        <h2>Minhas Consultas Marcadas</h2>
        {consultas.length === 0 ? (
          <p>Você ainda não possui consultas agendadas.</p>
        ) : (
          <div style={{ display: "grid", gap: "15px" }}>
            {consultas.map((c) => (
              <div key={c.id} style={{ padding: "15px", border: "1px solid #ddd", borderRadius: "8px", backgroundColor: "#f9f9f9" }}>
                <p><strong>Data/Hora:</strong> {new Date(c.dateTime).toLocaleString()}</p>
                <p><strong>Status:</strong> <span style={{ color: c.status === 'CANCELLED' ? 'red' : 'green' }}>{c.status}</span></p>
                {c.cancellationReason && <p><strong>Motivo Cancelamento:</strong> {c.cancellationReason}</p>}
              </div>
            ))}
          </div>
        )}
      </section>

      {/* Botão para nova consulta (Opcional para o futuro) */}
      <button 
        style={{ marginTop: "20px", padding: "12px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}
        onClick={() => alert("Funcionalidade de agendamento em breve!")}
      >
        Agendar Nova Consulta
      </button>
    </div>
  );
}

export default Dashboard;