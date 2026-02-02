import React, { useState, useEffect } from 'react';
import api from '../services/api';

const AdminPatientList = () => {
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPatients = async () => {
            try {
                const response = await api.get('/paciente');
                setPatients(response.data.content || []);
            } catch (error) {
                console.error("Erro ao buscar pacientes:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchPatients();
    }, []);

    if (loading) return <div>Carregando pacientes...</div>;

    return (
        <div>
            <h2>Todos os Pacientes</h2>
            <div className="cards-grid">
                {patients.map(patient => (
                    <div key={patient.id} className="card appointment-card">
                        <div style={{ display: 'flex', alignItems: 'center', marginBottom: '1rem' }}>
                            <span style={{ fontSize: '2rem', marginRight: '1rem' }}>👤</span>
                            <div>
                                <h3>{patient.userData?.name || patient.name}</h3>
                                <p style={{ color: 'var(--text-secondary)' }}>Paciente #{patient.id}</p>
                            </div>
                        </div>
                        <div className="patient-details">
                            <p><strong>Email:</strong> {patient.userData?.email}</p>
                            <p><strong>CPF:</strong> {patient.cpf}</p>
                            <p><strong>Telefone:</strong> {patient.phone}</p>
                            <p><strong>Status:</strong> {patient.active ? <span style={{color: 'var(--success-color)'}}>Ativo</span> : <span style={{color: 'var(--error-color)'}}>Inativo</span>}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default AdminPatientList;
