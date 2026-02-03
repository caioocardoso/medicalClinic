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
import './Home.css';

const Home = () => {
    // Layout State
    const [activeTab, setActiveTab] = useState('appointments');
    const [isModalOpen, setIsModalOpen] = useState(false);

    // Data State
    const [appointments, setAppointments] = useState([]); // Patient appointments
    const [doctorAppointments, setDoctorAppointments] = useState([]); // Doctor appointments
    const [doctors, setDoctors] = useState({}); // Map ID -> Name
    const [patients, setPatients] = useState({}); // Map ID -> Name (for doctors view)
    const [doctorList, setDoctorList] = useState([]); // List for dropdown/display
    const [loading, setLoading] = useState(true);
    const [roles, setRoles] = useState([]);
    
    // Form State
    const [scheduleDate, setScheduleDate] = useState('');
    const [selectedDoctorId, setSelectedDoctorId] = useState('');
    
    const navigate = useNavigate();
    const patientId = localStorage.getItem('patientId');
    const doctorId = localStorage.getItem('doctorId');

    useEffect(() => {
        if (!localStorage.getItem('token')) {
            navigate('/');
            return;
        }
        
        // Parse roles
        try {
            const rolesStr = localStorage.getItem('userRoles');
            const parsedRoles = rolesStr ? JSON.parse(rolesStr) : [];
            setRoles(parsedRoles);
            
            // Set initial tab based on role if needed, but defaults to 'appointments' (Patient)
            // If only doctor, switch to doctor-appointments
            // If admin, switch to admin-requests
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

            // 1. Fetch Doctors (Needed for Patient view to schedule/list, and mapping names)
            // Even doctors might want to see other doctors? Maybe not strictly required but harmless.
            const doctorsResponse = await api.get('/medico');
            const docs = doctorsResponse.data.content || [];
            
            const docMap = {};
            docs.forEach(doc => {
                docMap[doc.id] = doc.userData?.name || doc.name; 
            });
            setDoctors(docMap);
            setDoctorList(docs);

            // 2. If Patient, fetch Patient Appointments
            if (currentRoles.includes('ROLE_PATIENT') && patientId) {
                const appointmentsResponse = await api.get('/consulta/paciente');
                setAppointments(appointmentsResponse.data);
            }

            // 3. If Doctor, fetch Doctor Appointments and Patients (for mapping)
            if (currentRoles.includes('ROLE_DOCTOR') && doctorId) {
                const docAppsResponse = await api.get(`/consulta/medico/${doctorId}`);
                setDoctorAppointments(docAppsResponse.data);

                // Fetch patients to map names
                // Note: In a real large app, we wouldn't fetch ALL patients. We'd fetch by ID or the endpoint would return names.
                // For this scope, we'll fetch page 1 (or all if possible) or just show ID if name missing.
                try {
                    const patientsResponse = await api.get('/paciente'); // Returns Page
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
                doctorId: selectedDoctorId ? parseInt(selectedDoctorId) : null,
                dateTime: dateTimePayload
            };
            
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
                reason: 'PATIENT_GAVE_UP' // Or generic reason. Doctor canceling technically might send different reason.
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
        localStorage.removeItem('doctorId');
        localStorage.removeItem('userRoles');
        navigate('/');
    };

    const getAppointmentsByStatus = (list, status) => {
        return list.filter(app => app.status === status);
    };

    // Render Content based on activeTab
    const renderContent = () => {
        if (loading) return <div>Carregando...</div>;

        switch (activeTab) {
            // --- PATIENT VIEWS ---
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

            // --- DOCTOR VIEWS ---
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

            // --- ADMIN VIEWS ---
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
                    // Atualiza os roles do usuário
                    const currentRoles = roles.includes('ROLE_PATIENT') ? roles : [...roles, 'ROLE_PATIENT'];
                    setRoles(currentRoles);
                    localStorage.setItem('userRoles', JSON.stringify(currentRoles));
                    // Recarrega os dados
                    fetchData();
                    // Muda para a aba de consultas
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