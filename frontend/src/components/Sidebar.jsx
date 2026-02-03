import React from 'react';

const Sidebar = ({ activeTab, setActiveTab, onLogout, roles = [] }) => {
    const isPatient = roles.includes('ROLE_PATIENT');
    const isDoctor = roles.includes('ROLE_DOCTOR');
    const isAdmin = roles.includes('ROLE_ADMIN');

    const renderItems = (items) => (
        items.map(item => (
            <button
                key={item.id}
                className={`sidebar-item ${activeTab === item.id ? 'active' : ''}`}
                onClick={() => setActiveTab(item.id)}
            >
                <span className="icon">{item.icon}</span>
                {item.label}
            </button>
        ))
    );

    return (
        <div className="sidebar">
            <div className="sidebar-header">
                <h2>Medical Clinic</h2>
                <div style={{ fontSize: '0.8rem', opacity: 0.7, marginTop: '5px' }}>
                    {isAdmin ? 'Administrador' : isPatient && isDoctor ? 'Médico & Paciente' : isDoctor ? 'Área do Médico' : 'Área do Paciente'}
                </div>
            </div>
            <nav className="sidebar-nav">
                {isAdmin && (
                    <>
                         <div style={{ padding: '0 1rem', fontSize: '0.75rem', textTransform: 'uppercase', color: 'rgba(255,255,255,0.5)', marginTop: '1rem', marginBottom: '0.5rem' }}>Administração</div>
                        {renderItems([
                            { id: 'admin-requests', label: 'Solicitações', icon: '📥' },
                            { id: 'admin-patients', label: 'Todos Pacientes', icon: '👥' },
                            { id: 'admin-doctors', label: 'Todos Médicos', icon: '👨‍⚕️' }
                        ])}
                    </>
                )}

                {isPatient && (
                    <>
                        {(isDoctor || isAdmin) && <div style={{ padding: '0 1rem', fontSize: '0.75rem', textTransform: 'uppercase', color: 'rgba(255,255,255,0.5)', marginTop: '1rem', marginBottom: '0.5rem' }}>Paciente</div>}
                        {renderItems([
                            { id: 'appointments', label: 'Minhas Consultas', icon: '📅' },
                            { id: 'doctors', label: 'Médicos Disponíveis', icon: '👨‍⚕️' },
                            { id: 'profile', label: 'Meu Perfil', icon: '👤' },
                            // Show "Become a Doctor" only if patient is NOT a doctor yet
                            ...(!isDoctor ? [{ id: 'become-doctor', label: 'Virar Médico', icon: '🩺' }] : [])
                        ])}
                    </>
                )}

                {isDoctor && (
                    <>
                        {isPatient && <div style={{ padding: '0 1rem', fontSize: '0.75rem', textTransform: 'uppercase', color: 'rgba(255,255,255,0.5)', marginTop: '1rem', marginBottom: '0.5rem' }}>Médico</div>}
                        {renderItems([
                            { id: 'doctor-appointments', label: 'Agenda Médica', icon: '🩺' },
                            { id: 'doctor-profile', label: 'Dados do Médico', icon: '📋' }
                        ])}
                    </>
                )}

                {!isPatient && !isAdmin && (
                    <>
                        {isDoctor && <div style={{ padding: '0 1rem', fontSize: '0.75rem', textTransform: 'uppercase', color: 'rgba(255,255,255,0.5)', marginTop: '1rem', marginBottom: '0.5rem' }}>Ações</div>}
                        {renderItems([
                            { id: 'become-patient', label: 'Virar Paciente', icon: '👤' }
                        ])}
                    </>
                )}
            </nav>
            <div className="sidebar-footer">
                <button onClick={onLogout} className="logout-btn-sidebar">
                    🚪 Sair
                </button>
            </div>
        </div>
    );
};

export default Sidebar;
