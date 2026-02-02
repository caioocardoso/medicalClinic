import { useState } from "react";
import api from "../services/api";

function BecomeDoctorForm() {
  const [formData, setFormData] = useState({
    crm: "",
    speciality: "ORTOPEDIA"
  });

  const specialities = [
    "ORTOPEDIA",
    "CARDIOLOGIA",
    "GINECOLOGIA",
    "DERMATOLOGIA"
  ];

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/medico/solicitar-cadastro", formData);
      alert("Solicitação enviada com sucesso! Aguarde a aprovação.");
    } catch (err) {
      console.error(err);
      alert("Erro ao enviar solicitação: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="card" style={{ maxWidth: '600px', margin: '0 auto' }}>
      <h3 style={{ marginBottom: '1.5rem', color: 'var(--primary-color)' }}>Solicitar Cadastro como Médico</h3>
      <p style={{ marginBottom: '2rem', color: 'var(--text-secondary)' }}>
        Como você já possui cadastro de usuário, precisamos apenas das informações profissionais.
      </p>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
            <label className="form-label">CRM</label>
            <input 
                className="form-control" 
                name="crm"
                placeholder="12345/SP" 
                value={formData.crm}
                onChange={handleChange} 
                required 
            />
        </div>

        <div className="form-group">
            <label className="form-label">Especialidade</label>
            <select 
                className="form-control" 
                name="speciality"
                value={formData.speciality}
                onChange={handleChange} 
                required
            >
                {specialities.map(s => (
                    <option key={s} value={s}>{s}</option>
                ))}
            </select>
        </div>

        <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '1rem' }}>
            Enviar Solicitação
        </button>
      </form>
    </div>
  );
}

export default BecomeDoctorForm;
