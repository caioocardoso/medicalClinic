import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useToast } from './Toast';
import { handleApiError } from '../utils/errorHandler';

const AdminPatientList = () => {
    const { showToast } = useToast();
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [inactivatingId, setInactivatingId] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetchPatients();
    }, []);

    const fetchPatients = async () => {
        try {
            setLoading(true);
            const response = await api.get('/paciente');
            setPatients(response.data.content || []);
        } catch (error) {
            console.error("Erro ao buscar pacientes:", error);
            handleApiError(error, showToast);
        } finally {
            setLoading(false);
        }
    };

    const handleInactivate = async (patientId) => {
        if (!window.confirm('Tem certeza que deseja inativar este paciente?')) {
            return;
        }

        setInactivatingId(patientId);
        try {
            const response = await api.delete(`/paciente/${patientId}`);
            const successMessage = response.data.message || "Paciente inativado com sucesso!";
            showToast(successMessage, "success");
            
            // Atualizar a lista
            fetchPatients();
        } catch (error) {
            handleApiError(error, showToast);
        } finally {
            setInactivatingId(null);
        }
    };

    if (loading) return <div>Carregando pacientes...</div>;

    const filteredPatients = patients.filter(patient => {
        const name = patient.userData?.name || patient.name || '';
        const cpf = patient.cpf || '';
        const email = patient.userData?.email || '';
        const term = searchTerm.toLowerCase();
        
        return name.toLowerCase().includes(term) || 
               cpf.includes(term) ||
               email.toLowerCase().includes(term);
    });

    return (
        <div>
            <h2>Todos os Pacientes</h2>
            
            <div className="search-container">
                <span className="search-icon">🔍</span>
                <input
                    type="text"
                    className="search-input"
                    placeholder="Buscar paciente por nome, CPF ou email..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="cards-grid">
                {filteredPatients.map(patient => (
                    <div key={patient.id} className="card appointment-card">
                        <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                            <span style={{ fontSize: '2rem', marginRight: '1rem' }}>👤</span>
                            <div>
                                <h3>{patient.userData?.name || patient.name}</h3>
                                <p style={{ color: 'var(--text-secondary)' }}>Paciente #{patient.id}</p>
                            </div>
                        </div>
                        <div className="patient-details">
                            <p><strong>Email:</strong> {patient.email}</p>
                            <p><strong>CPF:</strong> {patient.cpf}</p>
                            <p><strong>Status:</strong> {patient.active ? <span style={{color: 'var(--success-color)'}}>Ativo</span> : <span style={{color: 'var(--error-color)'}}>Inativo</span>}</p>
                        </div>
                        
                        {patient.active && (
                            <button 
                                className="btn" 
                                onClick={() => handleInactivate(patient.id)}
                                disabled={inactivatingId === patient.id}
                                style={{ 
                                    width: '100%', 
                                    marginTop: '1rem', 
                                    backgroundColor: 'var(--error-color)', 
                                    color: 'white' 
                                }}
                            >
                                {inactivatingId === patient.id ? 'Inativando...' : '🚫 Inativar Paciente'}
                            </button>
                        )}
                    </div>
                ))}
                {filteredPatients.length === 0 && (
                    <div className="no-results">
                        <p>Nenhum paciente encontrado com o termo "{searchTerm}".</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminPatientList;
