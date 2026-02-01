import { useState } from "react";
import api from "./services/api";
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
      // Endpoint definido no seu AuthenticationController
      await api.post("/auth/register/patient", formData);
      alert("Registro realizado! Agora você pode fazer login.");
      navigate("/");
    } catch (err) {
      console.error(err);
      alert("Erro no registro. Verifique os dados (formato do CPF ou Email já existente).");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "500px", margin: "auto" }}>
      <h2>Cadastro de Paciente</h2>
      <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
        <input placeholder="Nome Completo" onChange={e => handleChange(e, "userData", "name")} required />
        <input type="email" placeholder="E-mail" onChange={e => handleChange(e, "userData", "email")} required />
        <input type="password" placeholder="Senha" onChange={e => handleChange(e, "userData", "password")} required />
        <input placeholder="Telefone (ex: 11999999999)" onChange={e => handleChange(e, "userData", "phone")} required />
        <input placeholder="CPF (000.000.000-00)" onChange={e => handleChange(e, "patientData", "cpf")} required />
        
        <h4>Endereço</h4>
        <input placeholder="Logradouro" onChange={e => handleChange(e, "address", "publicPlace")} required />
        <div style={{ display: "flex", gap: "5px" }}>
          <input placeholder="Nº" style={{ width: "20%" }} onChange={e => handleChange(e, "address", "number")} required />
          <input placeholder="Bairro" style={{ width: "80%" }} onChange={e => handleChange(e, "address", "neighborhood")} required />
        </div>
        <input placeholder="Cidade" onChange={e => handleChange(e, "address", "city")} required />
        <div style={{ display: "flex", gap: "5px" }}>
          <input placeholder="UF (2 letras)" maxLength="2" onChange={e => handleChange(e, "address", "uf")} required />
          <input placeholder="CEP (00000-000)" onChange={e => handleChange(e, "address", "zipCode")} required />
        </div>
        
        <button type="submit" style={{ padding: "10px", backgroundColor: "#28a745", color: "#fff", border: "none" }}>Registrar</button>
        <button type="button" onClick={() => navigate("/")} style={{ background: "none", border: "none", color: "blue", cursor: "pointer" }}>Já tenho conta</button>
      </form>
    </div>
  );
}

export default RegisterPatient;