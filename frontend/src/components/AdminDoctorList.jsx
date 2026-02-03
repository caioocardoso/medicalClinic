import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useToast } from './Toast';
import { handleApiError } from '../utils/errorHandler';

const AdminDoctorList = () => {
    const { showToast } = useToast();
    const [doctors, setDoctors] = useState([]);
    const [loading, setLoading] = useState(true);
    const [inactivatingId, setInactivatingId] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetchDoctors();
    }, []);

    const fetchDoctors = async () => {
        try {
            setLoading(true);
            const response = await api.get('/medico');
            setDoctors(response.data.content || []);
        } catch (error) {
            console.error("Erro ao buscar médicos:", error);
            handleApiError(error, showToast);
        } finally {
            setLoading(false);
        }
    };

    const handleInactivate = async (doctorId) => {
        if (!window.confirm('Tem certeza que deseja inativar este médico?')) {
            return;
        }

        setInactivatingId(doctorId);
        try {
            const response = await api.delete(`/medico/${doctorId}`);
            const successMessage = response.data.message || "Médico inativado com sucesso!";
            showToast(successMessage, "success");
            
            // Atualizar a lista
            fetchDoctors();
        } catch (error) {
            handleApiError(error, showToast);
        } finally {
            setInactivatingId(null);
        }
    };

    if (loading) return <div>Carregando médicos...</div>;

    const filteredDoctors = doctors.filter(doctor => {
        const name = doctor.userData?.name || doctor.name || '';
        const specialty = doctor.speciality || '';
        const crm = doctor.crm || '';
        const term = searchTerm.toLowerCase();
        
        return name.toLowerCase().includes(term) || 
               specialty.toLowerCase().includes(term) ||
               crm.toLowerCase().includes(term);
    });

    return (
        <div>
            <h2>Todos os Médicos</h2>
            
            <div className="search-container">
                <span className="search-icon">🔍</span>
                <input
                    type="text"
                    className="search-input"
                    placeholder="Buscar médico por nome, especialidade ou CRM..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="cards-grid">
                {filteredDoctors.map(doctor => (
                    <div key={doctor.id} className="card appointment-card">
                        <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                            <span style={{ fontSize: '2rem', marginRight: '1rem' }}>👨‍⚕️</span>
                            <div>
                                <h3>{doctor.userData?.name || doctor.name}</h3>
                                <p style={{ color: 'var(--text-secondary)' }}>CRM: {doctor.crm}</p>
                            </div>
                        </div>
                        <div className="doctor-details">
                            <p><strong>Especialidade:</strong> {doctor.speciality}</p>
                            <p><strong>Email:</strong> {doctor.email}</p>
                            <p><strong>Status:</strong> {doctor.active ? <span style={{color: 'var(--success-color)'}}>Ativo</span> : <span style={{color: 'var(--error-color)'}}>Inativo</span>}</p>
                        </div>
                        
                        {doctor.active && (
                            <button 
                                className="btn" 
                                onClick={() => handleInactivate(doctor.id)}
                                disabled={inactivatingId === doctor.id}
                                style={{ 
                                    width: '100%', 
                                    marginTop: '1rem', 
                                    backgroundColor: 'var(--error-color)', 
                                    color: 'white' 
                                }}
                            >
                                {inactivatingId === doctor.id ? 'Inativando...' : '🚫 Inativar Médico'}
                            </button>
                        )}
                    </div>
                ))}
                {filteredDoctors.length === 0 && (
                    <div className="no-results">
                        <p>Nenhum médico encontrado com o termo "{searchTerm}".</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default AdminDoctorList;
