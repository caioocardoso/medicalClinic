import React, { useState } from 'react';

const DoctorsList = ({ doctors }) => {
    const [searchTerm, setSearchTerm] = useState('');

    const filteredDoctors = doctors.filter(doctor => {
        const name = doctor.userData?.name || doctor.name || '';
        const specialty = doctor.specialty || '';
        const term = searchTerm.toLowerCase();
        
        return name.toLowerCase().includes(term) || 
               specialty.toLowerCase().includes(term);
    });

    return (
        <div className="doctors-container">
            <h2>Médicos da Clínica</h2>
            
            <div className="search-container">
                <span className="search-icon">🔍</span>
                <input
                    type="text"
                    className="search-input"
                    placeholder="Buscar médico por nome ou especialidade..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="cards-grid">
                {filteredDoctors.map(doctor => (
                    <div key={doctor.id} className="doctor-card card">
                        <div className="doctor-avatar">👨‍⚕️</div>
                        <h3>{doctor.userData?.name || doctor.name}</h3>
                        <p className="specialty">{doctor.specialty || 'Clínico Geral'}</p>
                        <p className="crm">CRM: {doctor.crm || 'N/A'}</p>
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

export default DoctorsList;
