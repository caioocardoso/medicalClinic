import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useToast } from "../components/Toast";
import { handleApiError } from "../utils/errorHandler";

function Register() {
  const navigate = useNavigate();
  const { showToast } = useToast();
  const [loading, setLoading] = useState(false);
  const [userType, setUserType] = useState("patient"); // 'patient' or 'doctor'
  const [formData, setFormData] = useState({
    userData: {
      name: "",
      email: "",
      password: "",
      phone: "",
      address: {
        publicPlace: "",
        number: "",
        complement: "",
        neighborhood: "",
        city: "",
        uf: "",
        zipCode: ""
      }
    },
    patientData: { cpf: "" },
    doctorData: { crm: "", speciality: "ORTOPEDIA" } // Default speciality
  });

  const specialities = [
    "ORTOPEDIA",
    "CARDIOLOGIA",
    "GINECOLOGIA",
    "DERMATOLOGIA"
  ];

  // Funções de formatação
  const formatCPF = (value) => {
    const numbers = value.replace(/\D/g, '');
    if (numbers.length <= 11) {
      return numbers
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    }
    return value.slice(0, 14); // Limita ao tamanho máximo formatado
  };

  const formatPhone = (value) => {
    const numbers = value.replace(/\D/g, '');
    if (numbers.length <= 11) {
      if (numbers.length <= 10) {
        // Telefone fixo: (XX) XXXX-XXXX
        return numbers
          .replace(/(\d{2})(\d)/, '($1) $2')
          .replace(/(\d{4})(\d)/, '$1-$2');
      } else {
        // Celular: (XX) XXXXX-XXXX
        return numbers
          .replace(/(\d{2})(\d)/, '($1) $2')
          .replace(/(\d{5})(\d)/, '$1-$2');
      }
    }
    return value.slice(0, 15); // Limita ao tamanho máximo formatado
  };

  const formatCEP = (value) => {
    const numbers = value.replace(/\D/g, '');
    if (numbers.length <= 8) {
      return numbers.replace(/(\d{5})(\d)/, '$1-$2');
    }
    return value.slice(0, 9); // Limita ao tamanho máximo formatado
  };

  const handleChange = (e, section, field) => {
    let value = e.target.value;
    
    // Aplicar formatação específica
    if (field === "cpf") {
      value = formatCPF(value);
    } else if (field === "phone") {
      value = formatPhone(value);
    } else if (field === "zipCode") {
      value = formatCEP(value);
    } else if (field === "uf") {
      value = value.toUpperCase().slice(0, 2);
    }
    
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
    setLoading(true);
    
    try {
      // Use raw axios to avoid sending Authorization header from interceptor
      const api = axios.create({ baseURL: 'http://localhost:8084/medicalclinic' });
      
      let response;
      if (userType === "patient") {
        const payload = {
            userData: formData.userData,
            patientData: formData.patientData
        };
        response = await api.post("/auth/register/patient", payload);
      } else {
        const payload = {
            userData: formData.userData,
            doctorData: formData.doctorData
        };
        response = await api.post("/medico/cadastrar-novo", payload);
      }
      
      // Exibir mensagem retornada pela API
      const successMessage = response.data.message || "Cadastro realizado com sucesso!";
      showToast(successMessage, "success");
      
      // Pequeno delay para o usuário ver o toast antes de navegar
      setTimeout(() => {
        navigate("/");
      }, 1500);
    } catch (err) {
      handleApiError(err, showToast);
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ display: "flex", justifyContent: "center", paddingTop: "2rem", paddingBottom: "2rem" }}>
      <div className="card" style={{ width: "100%", maxWidth: "800px" }}>
        <h2 className="text-center mb-2" style={{ color: "var(--primary-color)" }}>Crie sua conta</h2>
        <p className="text-center mb-4" style={{ color: "var(--text-secondary)" }}>Escolha o tipo de perfil e preencha seus dados</p>
        
        <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem', justifyContent: 'center' }}>
            <button 
                type="button"
                className={`btn ${userType === 'patient' ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => setUserType('patient')}
                style={{ minWidth: '150px' }}
            >
                Sou Paciente
            </button>
            <button 
                type="button"
                className={`btn ${userType === 'doctor' ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => setUserType('doctor')}
                style={{ minWidth: '150px' }}
            >
                Sou Médico
            </button>
        </div>

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
                <input className="form-control" placeholder="(XX) XXXXX-XXXX" value={formData.userData.phone} onChange={e => handleChange(e, "userData", "phone")} maxLength="15" required />
              </div>

              {userType === 'patient' ? (
                  <div className="form-group">
                    <label className="form-label">CPF</label>
                    <input className="form-control" placeholder="000.000.000-00" value={formData.patientData.cpf} onChange={e => handleChange(e, "patientData", "cpf")} maxLength="14" required />
                  </div>
              ) : (
                  <>
                    <div className="form-group">
                        <label className="form-label">CRM</label>
                        <input className="form-control" placeholder="12345/SP" onChange={e => handleChange(e, "doctorData", "crm")} required />
                    </div>
                    <div className="form-group">
                        <label className="form-label">Especialidade</label>
                        <select className="form-control" onChange={e => handleChange(e, "doctorData", "speciality")} required>
                            {specialities.map(s => (
                                <option key={s} value={s}>{s}</option>
                            ))}
                        </select>
                    </div>
                  </>
              )}
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
                  <input className="form-control" placeholder="123" value={formData.userData.address.number} onChange={e => handleChange(e, "address", "number")} required />
              </div>
          </div>

          <div className="form-group">
              <label className="form-label">Complemento</label>
              <input className="form-control" placeholder="Apto, Bloco, Casa..." value={formData.userData.address.complement} onChange={e => handleChange(e, "address", "complement")} />
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
                  <input className="form-control" placeholder="SP" value={formData.userData.address.uf} maxLength="2" onChange={e => handleChange(e, "address", "uf")} style={{textTransform: 'uppercase'}} required />
              </div>
              
              <div className="form-group">
                  <label className="form-label">CEP</label>
                  <input className="form-control" placeholder="00000-000" value={formData.userData.address.zipCode} onChange={e => handleChange(e, "address", "zipCode")} maxLength="9" required />
              </div>
          </div>
          
          <div style={{ display: "flex", gap: "1rem", marginTop: "2rem" }}>
              <button 
                type="submit" 
                className="btn btn-primary" 
                style={{ flex: 1 }}
                disabled={loading}
              >
                {loading ? 'Processando...' : userType === 'patient' ? 'Registrar Paciente' : 'Solicitar Cadastro Médico'}
              </button>
              <button 
                type="button" 
                onClick={() => navigate("/")} 
                className="btn btn-secondary" 
                style={{ flex: 1, backgroundColor: "#999" }}
                disabled={loading}
              >
                Voltar
              </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Register;
