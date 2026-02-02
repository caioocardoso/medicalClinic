import React from 'react';

const Sidebar = ({ activeTab, setActiveTab, onLogout }) => {
    const menuItems = [
        { id: 'appointments', label: 'Minhas Consultas', icon: '📅' },
        { id: 'doctors', label: 'Médicos Disponíveis', icon: '👨‍⚕️' },
        { id: 'profile', label: 'Meu Perfil', icon: '👤' },
    ];

    return (
        <div className="sidebar">
            <div className="sidebar-header">
                <h2>Medical Clinic</h2>
            </div>
            <nav className="sidebar-nav">
                {menuItems.map(item => (
                    <button
                        key={item.id}
                        className={`sidebar-item ${activeTab === item.id ? 'active' : ''}`}
                        onClick={() => setActiveTab(item.id)}
                    >
                        <span className="icon">{item.icon}</span>
                        {item.label}
                    </button>
                ))}
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
