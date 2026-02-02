import React, { useState, useEffect } from 'react';
import api from '../services/api';

const DoctorRequests = () => {
    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [processingId, setProcessingId] = useState(null);

    const fetchRequests = async () => {
        try {
            const response = await api.get('/medico/listar-solicitacoes');
            // The API returns a Page object, so we access .content
            setRequests(response.data.content || []);
        } catch (error) {
            console.error("Erro ao buscar solicitações:", error);
            alert("Erro ao carregar solicitações.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchRequests();
    }, []);

    const handleApproval = async (id, isApproved) => {
        setProcessingId(id);
        try {
            await api.post('/medico/aceitar-cadastro', {
                id: id,
                isApproved: isApproved
            });
            // Refresh list after action
            fetchRequests();
            alert(isApproved ? "Médico aprovado com sucesso!" : "Solicitação negada.");
        } catch (error) {
            console.error("Erro ao processar solicitação:", error);
            alert("Erro ao processar solicitação. Tente novamente.");
        } finally {
            setProcessingId(null);
        }
    };

    const pendingRequests = requests.filter(req => !req.isFinished);
    const acceptedRequests = requests.filter(req => req.isFinished && req.isAccepted);
    const deniedRequests = requests.filter(req => req.isFinished && !req.isAccepted);

    const RequestCard = ({ req, showActions }) => (
        <div className="card request-card" style={{ borderLeft: `5px solid ${showActions ? 'var(--info-color)' : req.isAccepted ? 'var(--success-color)' : 'var(--error-color)'}` }}>
            <div className="card-header">
                <h3>{req.userDTO?.name || "Usuário Desconhecido"}</h3>
                <span className="badge" style={{ 
                    backgroundColor: showActions ? '#f0ad4e' : req.isAccepted ? 'var(--success-color)' : 'var(--error-color)',
                    color: 'white', padding: '4px 8px', borderRadius: '4px', fontSize: '0.8rem'
                }}>
                    {showActions ? 'Pendente' : req.isAccepted ? 'Aprovado' : 'Negado'}
                </span>
            </div>
            <div className="card-body" style={{ marginTop: '1rem' }}>
                <p><strong>Email:</strong> {req.userDTO?.email}</p>
                <p><strong>CRM:</strong> {req.doctorRegistrationData?.crm}</p>
                <p><strong>Especialidade:</strong> {req.doctorRegistrationData?.speciality}</p>
                <p><strong>Data Solicitação:</strong> {new Date(req.createdAt).toLocaleDateString()}</p>
            </div>
            {showActions && (
                <div className="card-actions" style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
                    <button 
                        className="btn-success" 
                        onClick={() => handleApproval(req.id, true)}
                        disabled={processingId === req.id}
                        style={{ flex: 1, backgroundColor: 'var(--success-color)', color: 'white', border: 'none', padding: '0.5rem', borderRadius: '4px', cursor: 'pointer' }}
                    >
                        {processingId === req.id ? '...' : '✅ Aceitar'}
                    </button>
                    <button 
                        className="btn-danger" 
                        onClick={() => handleApproval(req.id, false)}
                        disabled={processingId === req.id}
                        style={{ flex: 1, backgroundColor: 'var(--error-color)', color: 'white', border: 'none', padding: '0.5rem', borderRadius: '4px', cursor: 'pointer' }}
                    >
                        {processingId === req.id ? '...' : '❌ Negar'}
                    </button>
                </div>
            )}
        </div>
    );

    if (loading) return <div>Carregando solicitações...</div>;

    return (
        <div className="doctor-requests-container">
            <h2>Solicitações de Cadastro de Médicos</h2>
            
            <section className="requests-section">
                <h3 style={{ borderBottom: '2px solid var(--info-color)', paddingBottom: '0.5rem', marginBottom: '1rem' }}>Pendentes ({pendingRequests.length})</h3>
                {pendingRequests.length === 0 ? <p>Nenhuma solicitação pendente.</p> : (
                    <div className="cards-grid">
                        {pendingRequests.map(req => <RequestCard key={req.id} req={req} showActions={true} />)}
                    </div>
                )}
            </section>

            <section className="requests-section" style={{ marginTop: '2rem' }}>
                <h3 style={{ borderBottom: '2px solid var(--success-color)', paddingBottom: '0.5rem', marginBottom: '1rem' }}>Aprovados Recentemente</h3>
                <div className="cards-grid">
                    {acceptedRequests.map(req => <RequestCard key={req.id} req={req} showActions={false} />)}
                </div>
            </section>

            <section className="requests-section" style={{ marginTop: '2rem' }}>
                <h3 style={{ borderBottom: '2px solid var(--error-color)', paddingBottom: '0.5rem', marginBottom: '1rem' }}>Negados Recentemente</h3>
                <div className="cards-grid">
                    {deniedRequests.map(req => <RequestCard key={req.id} req={req} showActions={false} />)}
                </div>
            </section>
        </div>
    );
};

export default DoctorRequests;
