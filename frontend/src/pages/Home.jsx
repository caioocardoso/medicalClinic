import React, { useState, useEffect } from 'react';
import api from '../services/api';
import AppointmentCard from '../components/AppointmentCard';
import Sidebar from '../components/Sidebar';
import Modal from '../components/Modal';
import DoctorsList from '../components/DoctorsList';
import UserProfile from '../components/UserProfile';
import { useNavigate } from 'react-router-dom';
import './Home.css';

const Home = () => {
    // Layout State
    const [activeTab, setActiveTab] = useState('appointments');
    const [isModalOpen, setIsModalOpen] = useState(false);

    // Data State
    const [appointments, setAppointments] = useState([]);
    const [doctors, setDoctors] = useState({}); 
    const [doctorList, setDoctorList] = useState([]); 
    const [loading, setLoading] = useState(true);
    
    // Form State
    const [scheduleDate, setScheduleDate] = useState('');
    const [selectedDoctorId, setSelectedDoctorId] = useState('');
    
    const navigate = useNavigate();
    const patientId = localStorage.getItem('patientId');

    useEffect(() => {
        if (!localStorage.getItem('token')) {
            navigate('/');
            return;
        }
        fetchData();
    }, [navigate]);

    const fetchData = async () => {
        try {
            setLoading(true);
            
            // 1. Fetch Doctors
            const doctorsResponse = await api.get('/medico');
            const docs = doctorsResponse.data.content || [];
            
            const docMap = {};
            docs.forEach(doc => {
                docMap[doc.id] = doc.userData?.name || doc.name; 
            });
            setDoctors(docMap);
            setDoctorList(docs);

            // 2. Fetch Appointments
            const appointmentsResponse = await api.get('/consulta/paciente');
            setAppointments(appointmentsResponse.data);
            
        } catch (err) {
            console.error(err);
            if (err.response && err.response.status === 403) {
                alert("Sessão expirada ou sem permissão.");
                navigate('/');
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSchedule = async (e) => {
        e.preventDefault();
        
        if (!patientId) {
            alert('Erro: ID do paciente não encontrado. Faça login novamente.');
            return;
        }

        // Validate year
        const year = parseInt(scheduleDate.split('-')[0]);
        if (year > 9999) {
             alert('Ano inválido. Por favor insira um ano com 4 dígitos.');
             return;
        }

        try {
            // ISO 8601 strict format: YYYY-MM-DDTHH:mm:ss
            const dateTimePayload = scheduleDate.length <= 16 ? scheduleDate + ":00" : scheduleDate;

            const payload = {
                patientId: parseInt(patientId),
                doctorId: selectedDoctorId ? parseInt(selectedDoctorId) : null, // Assuming backend accepts null or ID
                dateTime: dateTimePayload
            };

        console.log("Enviando Payload:", JSON.stringify(payload));
            // Se o doctorId for nulo e o backend reclamar, podemos ter que remover a chave
            // Mas o AppointmentRequest tem doctorId opcional.
            
            await api.post('/consulta', payload);
            alert('Consulta agendada com sucesso!');
            
            // Reset and Close
            setScheduleDate('');
            setSelectedDoctorId('');
            setIsModalOpen(false);
            fetchData(); 
        } catch (err) {
            console.error(err);
            alert('Erro ao agendar: ' + (err.response?.data?.message || err.message));
        }
    };

    const handleCancel = async (appointmentId) => {
        if (!window.confirm('Tem certeza que deseja cancelar esta consulta?')) return;

        try {
            const payload = {
                appointmentId: appointmentId,
                reason: 'PATIENT_GAVE_UP' 
            };
            
            await api.delete('/consulta', { data: payload });
            alert('Consulta cancelada!');
            fetchData();
        } catch (err) {
            console.error(err);
            alert('Erro ao cancelar.');
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('patientId');
        navigate('/');
    };

    const getAppointmentsByStatus = (status) => {
        return appointments.filter(app => app.status === status);
    };

    // Render Content based on activeTab
    const renderContent = () => {
        if (loading) return <div>Carregando...</div>;

        switch (activeTab) {
            case 'doctors':
                return <DoctorsList doctors={doctorList} />;
            case 'profile':
                return <UserProfile />;
            case 'appointments':
            default:
                return (
                    <>
                        <div className="header-actions">
                            <button onClick={() => setIsModalOpen(true)} className="btn btn-primary">
                                + Cadastrar Consulta
                            </button>
                        </div>

                        {['SCHEDULED', 'COMPLETED', 'CANCELLED'].map(status => {
                            const statusApps = getAppointmentsByStatus(status);
                            if (statusApps.length === 0 && status !== 'SCHEDULED') return null;

                            return (
                                <section key={status} className="status-section">
                                    <h3 className={`status-title ${status.toLowerCase()}`}>
                                        {status === 'SCHEDULED' ? 'Próximas Consultas' : 
                                         status === 'COMPLETED' ? 'Histórico' : 'Canceladas'}
                                    </h3>
                                    
                                    {statusApps.length === 0 ? (
                                        <p>Nenhuma consulta encontrada.</p>
                                    ) : (
                                        <div className="cards-grid">
                                            {statusApps.map(app => (
                                                <AppointmentCard 
                                                    key={app.id} 
                                                    appointment={app} 
                                                    onCancel={handleCancel}
                                                    doctors={doctors}
                                                />
                                            ))}
                                        </div>
                                    )}
                                </section>
                            );
                        })}
                    </>
                );
        }
    };

    return (
        <div className="app-container">
            <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} onLogout={handleLogout} />
            
            <main className="main-content">
                {renderContent()}
            </main>

            <Modal 
                isOpen={isModalOpen} 
                onClose={() => setIsModalOpen(false)}
                title="Agendar Nova Consulta"
            >
                <form onSubmit={handleSchedule}>
                    <div className="form-group">
                        <label className="form-label">Data e Hora:</label>
                        <input 
                            type="datetime-local" 
                            className="form-control"
                            value={scheduleDate}
                            onChange={(e) => setScheduleDate(e.target.value)}
                            required 
                            max="9999-12-31T23:59"
                        />
                    </div>
                    
                    <div className="form-group">
                        <label className="form-label">Médico (Opcional):</label>
                        <select 
                            className="form-control"
                            value={selectedDoctorId}
                            onChange={(e) => setSelectedDoctorId(e.target.value)}
                        >
                            <option value="">Qualquer Médico Disponível</option>
                            {doctorList.map(doc => (
                                <option key={doc.id} value={doc.id}>
                                    {doc.userData?.name || doc.name} - {doc.specialty || 'Clínico Geral'}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group" style={{marginTop: '20px'}}>
                         <button type="submit" className="btn btn-primary" style={{width: '100%'}}>Confirmar Agendamento</button>
                    </div>
                </form>
            </Modal>
        </div>
    );
};

export default Home;
