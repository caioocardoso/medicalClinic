import React, { useState, useEffect } from 'react';
import api from '../services/api';

const AdminDoctorList = () => {
    const [doctors, setDoctors] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDoctors = async () => {
            try {
                const response = await api.get('/medico');
                setDoctors(response.data.content || []);
            } catch (error) {
                console.error("Erro ao buscar médicos:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchDoctors();
    }, []);

    if (loading) return <div>Carregando médicos...</div>;

    return (
        <div>
            <h2>Todos os Médicos</h2>
            <div className="cards-grid">
                {doctors.map(doctor => (
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
                            <p><strong>Email:</strong> {doctor.userData?.email || doctor.email}</p>
                            <p><strong>Telefone:</strong> {doctor.phone}</p>
                            <p><strong>Status:</strong> {doctor.active ? <span style={{color: 'var(--success-color)'}}>Ativo</span> : <span style={{color: 'var(--error-color)'}}>Inativo</span>}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default AdminDoctorList;
