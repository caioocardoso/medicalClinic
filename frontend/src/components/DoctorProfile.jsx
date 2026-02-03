import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useToast } from './Toast';
import { handleApiError } from '../utils/errorHandler';

const DoctorProfile = () => {
    const { showToast } = useToast();
    const [doctor, setDoctor] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDoctorData = async () => {
            try {
                const response = await api.get('/medico/me');
                setDoctor(response.data);
            } catch (error) {
                console.error("Erro ao buscar dados do médico", error);
                handleApiError(error, showToast);
            } finally {
                setLoading(false);
            }
        };

        fetchDoctorData();
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    if (loading) return <div>Carregando perfil...</div>;
    if (!doctor) return <div>Erro ao carregar perfil.</div>;

    const address = doctor.address || {};

    return (
        <div style={{ maxWidth: '800px', margin: '0 auto' }}>
            <h2 style={{ marginBottom: '2rem', color: 'var(--primary-color)' }}>Dados do Médico</h2>
            <div className="card">
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '2rem', paddingBottom: '1rem', borderBottom: '2px solid #eee' }}>
                    <div style={{ fontSize: '4rem' }}>👨‍⚕️</div>
                    <div style={{ flex: 1 }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                            <h3 style={{ margin: '0 0 0.5rem 0', color: 'var(--secondary-color)' }}>{doctor.name || 'Nome não informado'}</h3>
                            <span style={{ 
                                backgroundColor: doctor.active ? 'var(--success-color)' : 'var(--error-color)', 
                                color: 'white', 
                                padding: '0.25rem 0.75rem', 
                                borderRadius: '12px', 
                                fontSize: '0.75rem',
                                fontWeight: 'bold'
                            }}>
                                {doctor.active ? 'ATIVO' : 'INATIVO'}
                            </span>
                        </div>
                        <p style={{ margin: 0, color: 'var(--text-secondary)' }}>{doctor.email || 'Email não informado'}</p>
                    </div>
                </div>
                <div style={{ display: 'grid', gap: '1rem' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                        <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>CRM:</label>
                        <span>{doctor.crm}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                        <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>Especialidade:</label>
                        <span>{doctor.speciality}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                        <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>Telefone:</label>
                        <span>{doctor.phone || 'Não informado'}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                        <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>Endereço:</label>
                        <span>
                            {address.publicPlace && address.number 
                                ? `${address.publicPlace}, ${address.number} - ${address.neighborhood || ''}, ${address.city || ''} - ${address.uf || ''}` 
                                : 'Não informado'}
                        </span>
                    </div>
                    {address.zipCode && (
                        <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                            <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>CEP:</label>
                            <span>{address.zipCode}</span>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default DoctorProfile;