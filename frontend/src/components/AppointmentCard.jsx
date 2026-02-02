import React from 'react';

const AppointmentCard = ({ appointment, onCancel, doctors }) => {
    const { id, doctorId, dateTime, status, cancellationReason } = appointment;
    
    // Se doctors for um Map/Objeto, acessa pelo ID. Se for lista, find.
    const doctorName = doctors[doctorId] || (doctorId ? `Médico ID: ${doctorId}` : "Sem médico preferencial");
    const formattedDate = new Date(dateTime).toLocaleString();

    const getStatusColor = (status) => {
        switch(status) {
            case 'SCHEDULED': return 'var(--info-color)'; 
            case 'COMPLETED': return 'var(--success-color)'; 
            case 'CANCELLED': return 'var(--error-color)'; 
            default: return '#999';
        }
    };

    const statusColor = getStatusColor(status);

    return (
        <div className="card" style={{ borderLeft: `5px solid ${statusColor}`, transition: 'transform 0.2s' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <span style={{ fontWeight: 'bold', fontSize: '1.1rem' }}>Consulta #{id}</span>
                <span style={{
                    backgroundColor: statusColor,
                    color: 'white',
                    padding: '4px 12px',
                    borderRadius: '20px',
                    fontSize: '0.8em',
                    fontWeight: '600'
                }}>
                    {status}
                </span>
            </div>
            
            <div style={{ marginBottom: '1rem' }}>
                <p style={{ margin: '5px 0' }}><strong style={{color: 'var(--text-secondary)'}}>Médico:</strong> {doctorName}</p>
                <p style={{ margin: '5px 0' }}><strong style={{color: 'var(--text-secondary)'}}>Data:</strong> {formattedDate}</p>
                
                {status === 'CANCELLED' && cancellationReason && (
                    <p style={{ margin: '5px 0', color: 'var(--error-color)' }}><strong>Motivo:</strong> {cancellationReason}</p>
                )}
            </div>

            {status === 'SCHEDULED' && (
                <button 
                    onClick={() => onCancel(id)}
                    className="btn btn-danger"
                    style={{ width: '100%', fontSize: '0.9rem', marginTop: '10px' }}
                >
                    Cancelar Consulta
                </button>
            )}
        </div>
    );
};

export default AppointmentCard;
