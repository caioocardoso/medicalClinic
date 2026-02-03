import React, { useState, useEffect } from 'react';
import api from '../services/api';
import AppointmentCard from '../components/AppointmentCard';
import Sidebar from '../components/Sidebar';
import Modal from '../components/Modal';
import DoctorsList from '../components/DoctorsList';
import UserProfile from '../components/UserProfile';
import DoctorProfile from '../components/DoctorProfile';
import DoctorRequests from '../components/DoctorRequests';
import AdminPatientList from '../components/AdminPatientList';
import AdminDoctorList from '../components/AdminDoctorList';
import BecomeDoctorForm from '../components/BecomeDoctorForm';
import BecomePatientForm from '../components/BecomePatientForm';
import { useNavigate } from 'react-router-dom';
import { useToast } from '../components/Toast';
import { handleApiError } from '../utils/errorHandler';
import './Home.css';

const Home = () => {
    const [activeTab, setActiveTab] = useState('appointments');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const [appointments, setAppointments] = useState([]); 
    const [doctorAppointments, setDoctorAppointments] = useState([]); 
    const [doctors, setDoctors] = useState({});
    const [patients, setPatients] = useState({});
    const [doctorList, setDoctorList] = useState([]); 
    const [loading, setLoading] = useState(true);
    const [roles, setRoles] = useState([]);
    
    const [scheduleDate, setScheduleDate] = useState('');
    const [selectedDoctorId, setSelectedDoctorId] = useState('');
    
    const navigate = useNavigate();
    const { showToast } = useToast();
    const patientId = localStorage.getItem('patientId');
    const doctorId = localStorage.getItem('doctorId');

    useEffect(() => {
        if (!localStorage.getItem('token')) {
            navigate('/');
            return;
        }
        
        try {
            const rolesStr = localStorage.getItem('userRoles');
            const parsedRoles = rolesStr ? JSON.parse(rolesStr) : [];
            setRoles(parsedRoles);
            
            if (parsedRoles.includes('ROLE_ADMIN')) {
                setActiveTab('admin-requests');
            } else if (parsedRoles.includes('ROLE_DOCTOR') && !parsedRoles.includes('ROLE_PATIENT')) {
                setActiveTab('doctor-appointments');
            }
        } catch (e) {
            console.error("Erro ao ler roles", e);
        }

        fetchData();
    }, [navigate]);

    const fetchData = async () => {
        try {
            setLoading(true);
            const rolesStr = localStorage.getItem('userRoles');
            const currentRoles = rolesStr ? JSON.parse(rolesStr) : [];

            const doctorsResponse = await api.get('/medico');
            const docs = doctorsResponse.data.content || [];
            
            const docMap = {};
            docs.forEach(doc => {
                docMap[doc.id] = doc.userData?.name || doc.name; 
            });
            setDoctors(docMap);
            setDoctorList(docs);

            if (currentRoles.includes('ROLE_PATIENT') && patientId) {
                const appointmentsResponse = await api.get('/consulta/paciente');
                setAppointments(appointmentsResponse.data);
            }

            if (currentRoles.includes('ROLE_DOCTOR') && doctorId) {
                const docAppsResponse = await api.get(`/consulta/medico/${doctorId}`);
                setDoctorAppointments(docAppsResponse.data);

                try {
                    const patientsResponse = await api.get('/paciente');
                    const pats = patientsResponse.data.content || [];
                    const patMap = {};
                    pats.forEach(p => {
                        patMap[p.id] = p.userData?.name || p.name;
                    });
                    setPatients(patMap);
                } catch (err) {
                    console.error("Erro ao buscar pacientes", err);
                }
            }
            
        } catch (err) {
            console.error(err);
            if (err.response && err.response.status === 403) {
                showToast("Sessão expirada ou sem permissão. Faça login novamente.", "error");
                setTimeout(() => navigate('/'), 1500);
            } else {
                handleApiError(err, showToast);
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSchedule = async (e) => {
        e.preventDefault();
        
        if (!patientId) {
            showToast('ID do paciente não encontrado. Faça login novamente.', 'error');
            return;
        }

        // Validate year
        const year = parseInt(scheduleDate.split('-')[0]);
        if (year > 9999) {
             showToast('Ano inválido. Por favor insira um ano com 4 dígitos.', 'warning');
             return;
        }

        try {
            // ISO 8601 strict format: YYYY-MM-DDTHH:mm:ss
            const dateTimePayload = scheduleDate.length <= 16 ? scheduleDate + ":00" : scheduleDate;

            const payload = {
                patientId: parseInt(patientId),
                doctorId: selectedDoctorId ? parseInt(selectedDoctorId) : null,
                dateTime: dateTimePayload
            };
            
            const response = await api.post('/consulta', payload);
            const successMessage = response.data.message || 'Consulta agendada com sucesso!';
            showToast(successMessage, 'success');
            
            // Reset and Close
            setScheduleDate('');
            setSelectedDoctorId('');
            setIsModalOpen(false);
            fetchData(); 
        } catch (err) {
            handleApiError(err, showToast);
        }
    };

    const handleCancel = async (appointmentId) => {
        if (!window.confirm('Tem certeza que deseja cancelar esta consulta?')) return;

        try {
            const payload = {
                appointmentId: appointmentId,
                reason: 'PATIENT_GAVE_UP' // Or generic reason. Doctor canceling technically might send different reason.
            };
            
            const response = await api.delete('/consulta', { data: payload });
            const successMessage = response.data.message || 'Consulta cancelada com sucesso!';
            showToast(successMessage, 'success');
            fetchData();
        } catch (err) {
            handleApiError(err, showToast);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('patientId');
        localStorage.removeItem('doctorId');
        localStorage.removeItem('userRoles');
        navigate('/');
    };

    const getAppointmentsByStatus = (list, status) => {
        return list.filter(app => app.status === status);
    };

    const renderContent = () => {
        if (loading) return <div>Carregando...</div>;

        switch (activeTab) {
            case 'doctors':
                return <DoctorsList doctors={doctorList} />;
            case 'profile':
                return <UserProfile />;
            case 'appointments':
                return (
                    <>
                        <div className="header-actions">
                            <button onClick={() => setIsModalOpen(true)} className="btn btn-primary">
                                + Cadastrar Consulta
                            </button>
                        </div>

                        {['SCHEDULED', 'COMPLETED', 'CANCELLED'].map(status => {
                            const statusApps = getAppointmentsByStatus(appointments, status);
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
                                                    entityName={doctors[app.doctorId] || `Médico #${app.doctorId}`}
                                                    entityLabel="Médico"
                                                />
                                            ))}
                                        </div>
                                    )}
                                </section>
                            );
                        })}
                    </>
                );

            case 'doctor-profile':
                return <DoctorProfile />;
            case 'doctor-appointments':
                return (
                    <>
                        <div className="header-actions">
                            <h2>Minha Agenda Médica</h2>
                        </div>

                        {['SCHEDULED', 'COMPLETED', 'CANCELLED'].map(status => {
                            const statusApps = getAppointmentsByStatus(doctorAppointments, status);
                            if (statusApps.length === 0 && status !== 'SCHEDULED') return null;

                            return (
                                <section key={status} className="status-section">
                                    <h3 className={`status-title ${status.toLowerCase()}`}>
                                        {status === 'SCHEDULED' ? 'Agendadas' : 
                                         status === 'COMPLETED' ? 'Realizadas' : 'Canceladas'}
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
                                                    entityName={patients[app.patientId] || `Paciente #${app.patientId}`}
                                                    entityLabel="Paciente"
                                                />
                                            ))}
                                        </div>
                                    )}
                                </section>
                            );
                        })}
                    </>
                );

            case 'admin-requests':
                return <DoctorRequests />;
            case 'admin-patients':
                return <AdminPatientList />;
            case 'admin-doctors':
                return <AdminDoctorList />;
            case 'become-doctor':
                return <BecomeDoctorForm />;
            case 'become-patient':
                return <BecomePatientForm onSuccess={(data) => {
                    const currentRoles = roles.includes('ROLE_PATIENT') ? roles : [...roles, 'ROLE_PATIENT'];
                    setRoles(currentRoles);
                    localStorage.setItem('userRoles', JSON.stringify(currentRoles));
                    fetchData();
                    setActiveTab('appointments');
                }} />;

            default:
                return <div>Selecione uma opção no menu.</div>;
        }
    };

    return (
        <div className="app-container">
            <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} onLogout={handleLogout} roles={roles} />
            
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
                                    {doc.userData?.name || doc.name} - {doc.speciality || 'Clínico Geral'}
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