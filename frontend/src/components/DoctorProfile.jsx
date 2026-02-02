import React, { useEffect, useState } from 'react';
import api from '../services/api';

const DoctorProfile = () => {
    const [doctor, setDoctor] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDoctorData = async () => {
            const doctorId = localStorage.getItem('doctorId');
            if (!doctorId) return;

            try {
                const response = await api.get(`/medico/${doctorId}`);
                setDoctor(response.data);
            } catch (error) {
                console.error("Erro ao buscar dados do médico", error);
            } finally {
                setLoading(false);
            }
        };

        fetchDoctorData();
    }, []);

    if (loading) return <div>Carregando perfil...</div>;
    if (!doctor) return <div>Erro ao carregar perfil.</div>;

    return (
        <div className="profile-container">
            <h2>Dados do Médico</h2>
            <div className="card profile-card">
                <div className="profile-header">
                    <div className="profile-avatar">👨‍⚕️</div>
                    <div>
                        <h3>{doctor.name}</h3>
                        <p className="email">{doctor.email}</p>
                    </div>
                </div>
                <div className="profile-details">
                    <div className="detail-item">
                        <label>CRM:</label>
                        <span>{doctor.crm}</span>
                    </div>
                    <div className="detail-item">
                        <label>Especialidade:</label>
                        <span>{doctor.speciality}</span>
                    </div>
                    <div className="detail-item">
                        <label>Telefone:</label>
                        <span>{doctor.phone || 'Não informado'}</span>
                    </div>
                    <div className="detail-item">
                        <label>Endereço:</label>
                        <span>{doctor.address ? `${doctor.address.street}, ${doctor.address.number}` : 'Não informado'}</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DoctorProfile;