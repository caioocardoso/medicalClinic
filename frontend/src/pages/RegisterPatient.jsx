import { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function RegisterPatient() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    userData: {
      name: "",
      email: "",
      password: "",
      phone: "",
      address: {
        publicPlace: "",
        number: "",
        neighborhood: "",
        city: "",
        uf: "",
        zipCode: ""
      }
    },
    patientData: { cpf: "" }
  });

  const handleChange = (e, section, field) => {
    const value = e.target.value;
    if (section === "address") {
      setFormData(prev => ({
        ...prev,
        userData: {
          ...prev.userData,
          address: { ...prev.userData.address, [field]: value }
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [section]: { ...prev[section], [field]: value }
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/auth/register/patient", formData);
      alert("Registro realizado! Agora você pode fazer login.");
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Erro no registro: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="container" style={{ display: "flex", justifyContent: "center", paddingTop: "2rem", paddingBottom: "2rem" }}>
      <div className="card" style={{ width: "100%", maxWidth: "800px" }}>
        <h2 className="text-center mb-2" style={{ color: "var(--primary-color)" }}>Cadastro de Paciente</h2>
        <p className="text-center mb-2" style={{ color: "var(--text-secondary)" }}>Preencha seus dados para começar</p>
        
        <form onSubmit={handleSubmit}>
          
          <h4 style={{ margin: "1.5rem 0 1rem", color: "var(--secondary-color)", borderBottom: "1px solid #eee", paddingBottom: "0.5rem" }}>
              Dados Pessoais
          </h4>
          
          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "1rem" }}>
              <div className="form-group" style={{ gridColumn: "span 2" }}>
                <label className="form-label">Nome Completo</label>
                <input className="form-control" placeholder="Seu nome" onChange={e => handleChange(e, "userData", "name")} required />
              </div>
              
              <div className="form-group">
                <label className="form-label">E-mail</label>
                <input className="form-control" type="email" placeholder="email@exemplo.com" onChange={e => handleChange(e, "userData", "email")} required />
              </div>
              
              <div className="form-group">
                <label className="form-label">Senha</label>
                <input className="form-control" type="password" placeholder="********" onChange={e => handleChange(e, "userData", "password")} required />
              </div>
              
              <div className="form-group">
                <label className="form-label">Telefone</label>
                <input className="form-control" placeholder="(XX) XXXXX-XXXX" onChange={e => handleChange(e, "userData", "phone")} required />
              </div>
              
              <div className="form-group">
                <label className="form-label">CPF</label>
                <input className="form-control" placeholder="000.000.000-00" onChange={e => handleChange(e, "patientData", "cpf")} required />
              </div>
          </div>

          <h4 style={{ margin: "1.5rem 0 1rem", color: "var(--secondary-color)", borderBottom: "1px solid #eee", paddingBottom: "0.5rem" }}>
              Endereço
          </h4>
          
          <div style={{ display: "grid", gridTemplateColumns: "2fr 1fr", gap: "1rem" }}>
              <div className="form-group">
                  <label className="form-label">Logradouro</label>
                  <input className="form-control" placeholder="Rua, Avenida..." onChange={e => handleChange(e, "address", "publicPlace")} required />
              </div>
              
              <div className="form-group">
                  <label className="form-label">Número</label>
                  <input className="form-control" placeholder="123" onChange={e => handleChange(e, "address", "number")} required />
              </div>
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "1rem" }}>
              <div className="form-group">
                  <label className="form-label">Bairro</label>
                  <input className="form-control" placeholder="Bairro" onChange={e => handleChange(e, "address", "neighborhood")} required />
              </div>
              
              <div className="form-group">
                  <label className="form-label">Cidade</label>
                  <input className="form-control" placeholder="Cidade" onChange={e => handleChange(e, "address", "city")} required />
              </div>
          </div>
          
          <div style={{ display: "grid", gridTemplateColumns: "1fr 2fr", gap: "1rem" }}>
              <div className="form-group">
                  <label className="form-label">UF</label>
                  <input className="form-control" placeholder="SP" maxLength="2" onChange={e => handleChange(e, "address", "uf")} required />
              </div>
              
              <div className="form-group">
                  <label className="form-label">CEP</label>
                  <input className="form-control" placeholder="00000-000" onChange={e => handleChange(e, "address", "zipCode")} required />
              </div>
          </div>
          
          <div style={{ display: "flex", gap: "1rem", marginTop: "2rem" }}>
              <button type="submit" className="btn btn-primary" style={{ flex: 1 }}>Registrar</button>
              <button type="button" onClick={() => navigate("/")} className="btn btn-secondary" style={{ flex: 1, backgroundColor: "#999" }}>Voltar</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default RegisterPatient;
