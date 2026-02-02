import React from 'react';

const AppointmentCard = ({ appointment, onCancel, entityName, entityLabel = "Médico" }) => {
    const { id, dateTime, status, cancellationReason } = appointment;
    
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
        <div className="appointment-card" style={{ borderLeft: `5px solid ${statusColor}` }}>
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
                <p style={{ margin: '5px 0' }}><strong style={{color: 'var(--text-secondary)'}}>{entityLabel}:</strong> {entityName}</p>
                <p style={{ margin: '5px 0' }}><strong style={{color: 'var(--text-secondary)'}}>Data:</strong> {formattedDate}</p>
                
                {status === 'CANCELLED' && cancellationReason && (
                    <p style={{ margin: '5px 0', color: 'var(--error-color)' }}><strong>Motivo:</strong> {cancellationReason}</p>
                )}
            </div>

            {status === 'SCHEDULED' && (
                <button 
                    onClick={() => onCancel(id)}
                    className="cancel-btn"
                >
                    Cancelar Consulta
                </button>
            )}
        </div>
    );
};

export default AppointmentCard;
