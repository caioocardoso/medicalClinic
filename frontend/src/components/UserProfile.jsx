import React, { useEffect, useState } from 'react';
import { jwtDecode } from "jwt-decode"; // Precisamos verificar se existe, se não faremos manual

const UserProfile = () => {
    const [user, setUser] = useState({
        email: '',
        patientId: '',
        roles: []
    });

    useEffect(() => {
        const token = localStorage.getItem('token');
        const pId = localStorage.getItem('patientId');
        
        if (token) {
            try {
                const decoded = jwtDecode(token);
                setUser({
                    email: decoded.sub || '',
                    roles: decoded.roles || [], // Ajustar conforme payload do token
                    patientId: pId
                });
            } catch (e) {
                console.error("Erro ao decodificar token", e);
            }
        }
    }, []);

    return (
        <div className="profile-container">
            <h2>Minhas Informações</h2>
            <div className="card profile-card">
                <div className="profile-header">
                    <div className="profile-avatar">👤</div>
                    <div>
                        <h3>Paciente</h3>
                        <p className="email">{user.email}</p>
                    </div>
                </div>
                <div className="profile-details">
                    <div className="detail-item">
                        <label>ID do Paciente:</label>
                        <span>{user.patientId || 'Não vinculado'}</span>
                    </div>
                    <div className="detail-item">
                        <label>Status:</label>
                        <span className="badge-active">Ativo</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default UserProfile;
