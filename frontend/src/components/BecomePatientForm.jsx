import { useState } from "react";
import api from "../services/api";
import { useToast } from "./Toast";
import { handleApiError } from "../utils/errorHandler";

function BecomePatientForm({ onSuccess }) {
  const { showToast } = useToast();
  const [formData, setFormData] = useState({
    cpf: ""
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    let value = e.target.value.replace(/\D/g, '');
    
    if (value.length <= 11) {
      value = value.replace(/(\d{3})(\d)/, '$1.$2');
      value = value.replace(/(\d{3})(\d)/, '$1.$2');
      value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    }
    
    setFormData({
      ...formData,
      cpf: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Remove formatação do CPF para enviar ao backend
    const cpfUnformatted = formData.cpf.replace(/\D/g, '');
    
    if (cpfUnformatted.length !== 11) {
      showToast("CPF deve conter 11 dígitos", "warning");
      return;
    }

    setLoading(true);
    try {
      const response = await api.post("/paciente/perfil", { cpf: cpfUnformatted });
      
      if (response.data.id) {
        localStorage.setItem('patientId', response.data.id);
      }
      
      const successMessage = response.data.message || "Cadastro como paciente realizado com sucesso!";
      showToast(successMessage, "success");
      
      if (onSuccess) {
        onSuccess(response.data);
      }
    } catch (err) {
      console.error(err);
      handleApiError(err, showToast);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card" style={{ maxWidth: '600px', margin: '0 auto' }}>
      <h3 style={{ marginBottom: '1.5rem', color: 'var(--primary-color)' }}>Cadastrar como Paciente</h3>
      <p style={{ marginBottom: '2rem', color: 'var(--text-secondary)' }}>
        Como você já possui cadastro de usuário, precisamos apenas do seu CPF para vincular o perfil de paciente.
      </p>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
            <label className="form-label">CPF</label>
            <input 
                className="form-control" 
                name="cpf"
                placeholder="000.000.000-00" 
                value={formData.cpf}
                onChange={handleChange}
                maxLength="14"
                required 
            />
            <small style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
              Digite apenas números, a formatação é automática
            </small>
        </div>

        <button 
          type="submit" 
          className="btn btn-primary" 
          style={{ width: '100%', marginTop: '1rem' }}
          disabled={loading}
        >
            {loading ? 'Cadastrando...' : 'Cadastrar como Paciente'}
        </button>
      </form>
    </div>
  );
}

export default BecomePatientForm;
