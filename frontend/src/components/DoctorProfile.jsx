import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useToast } from './Toast';
import { handleApiError } from '../utils/errorHandler';
import { useNavigate } from 'react-router-dom';

const DoctorProfile = () => {
    const { showToast } = useToast();
    const navigate = useNavigate();
    const [doctor, setDoctor] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        phone: '',
        address: {
            publicPlace: '',
            number: '',
            complement: '',
            neighborhood: '',
            city: '',
            uf: '',
            zipCode: ''
        }
    });

    useEffect(() => {
        const fetchDoctorData = async () => {
            try {
                const response = await api.get('/medico/me');
                const data = response.data;
                setDoctor(data);
                setFormData({
                    name: data.name || '',
                    phone: data.phone || '',
                    address: {
                        publicPlace: data.address?.publicPlace || '',
                        number: data.address?.number || '',
                        complement: data.address?.complement || '',
                        neighborhood: data.address?.neighborhood || '',
                        city: data.address?.city || '',
                        uf: data.address?.uf || '',
                        zipCode: data.address?.zipCode || ''
                    }
                });
            } catch (error) {
                console.error("Erro ao buscar dados do médico", error);
                handleApiError(error, showToast);
            } finally {
                setLoading(false);
            }
        };

        fetchDoctorData();
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    const handleUpdate = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const response = await api.put(`/medico/${doctor.id}`, formData);
            const successMessage = response.data.message || "Dados atualizados com sucesso!";
            showToast(successMessage, "success");
            
            // Atualizar os dados localmente
            setDoctor({ 
                ...doctor, 
                name: formData.name,
                phone: formData.phone,
                address: formData.address
            });
            setIsEditing(false);
        } catch (error) {
            handleApiError(error, showToast);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async () => {
        if (!window.confirm('Tem certeza que deseja excluir seu cadastro de médico? Esta ação não pode ser desfeita.')) {
            return;
        }

        setLoading(true);
        try {
            const response = await api.delete(`/medico/${doctor.id}`);
            const successMessage = response.data.message || "Cadastro de médico excluído com sucesso!";
            showToast(successMessage, "success");
            
            // Fazer logout completo - remove todos os dados
            setTimeout(() => {
                localStorage.removeItem('token');
                localStorage.removeItem('patientId');
                localStorage.removeItem('doctorId');
                localStorage.removeItem('userRoles');
                localStorage.removeItem('userId');
                localStorage.removeItem('userName');
                navigate('/');
            }, 2000);
        } catch (error) {
            handleApiError(error, showToast);
            setLoading(false);
        }
    };

    const handleCancel = () => {
        setFormData({
            name: doctor.name || '',
            phone: doctor.phone || '',
            address: {
                publicPlace: doctor.address?.publicPlace || '',
                number: doctor.address?.number || '',
                complement: doctor.address?.complement || '',
                neighborhood: doctor.address?.neighborhood || '',
                city: doctor.address?.city || '',
                uf: doctor.address?.uf || '',
                zipCode: doctor.address?.zipCode || ''
            }
        });
        setIsEditing(false);
    };

    if (loading) return <div>Carregando perfil...</div>;
    if (!doctor) return <div>Erro ao carregar perfil.</div>;

    const address = doctor.address || {};

    return (
        <div style={{ maxWidth: '800px', margin: '0 auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
                <h2 style={{ margin: 0, color: 'var(--primary-color)' }}>Dados do Médico</h2>
                <div style={{ display: 'flex', gap: '0.5rem' }}>
                    {!isEditing ? (
                        <>
                            <button 
                                className="btn btn-primary" 
                                onClick={() => setIsEditing(true)}
                                style={{ padding: '0.5rem 1rem' }}
                            >
                                ✏️ Editar
                            </button>
                            <button 
                                className="btn" 
                                onClick={handleDelete}
                                style={{ padding: '0.5rem 1rem', backgroundColor: 'var(--error-color)', color: 'white' }}
                            >
                                🗑️ Excluir Cadastro
                            </button>
                        </>
                    ) : (
                        <>
                            <button 
                                className="btn btn-primary" 
                                onClick={handleUpdate}
                                disabled={loading}
                                style={{ padding: '0.5rem 1rem' }}
                            >
                                {loading ? 'Salvando...' : '💾 Salvar'}
                            </button>
                            <button 
                                className="btn btn-secondary" 
                                onClick={handleCancel}
                                disabled={loading}
                                style={{ padding: '0.5rem 1rem' }}
                            >
                                ❌ Cancelar
                            </button>
                        </>
                    )}
                </div>
            </div>

            <div className="card">
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '2rem', paddingBottom: '1rem', borderBottom: '2px solid #eee' }}>
                    <div style={{ fontSize: '4rem' }}>👨‍⚕️</div>
                    <div style={{ flex: 1 }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                            <h3 style={{ margin: '0 0 0.5rem 0', color: 'var(--secondary-color)' }}>{doctor.name || 'Nome não informado'}</h3>
                            <span style={{ 
                                backgroundColor: doctor.active ? 'var(--success-color)' : 'var(--error-color)', 
                                color: 'white', 
                                padding: '0.25rem 0.75rem', 
                                borderRadius: '12px', 
                                fontSize: '0.75rem',
                                fontWeight: 'bold'
                            }}>
                                {doctor.active ? 'ATIVO' : 'INATIVO'}
                            </span>
                        </div>
                        <p style={{ margin: 0, color: 'var(--text-secondary)' }}>{doctor.email || 'Email não informado'}</p>
                    </div>
                </div>

                {isEditing ? (
                    <form onSubmit={handleUpdate} style={{ display: 'grid', gap: '1rem' }}>
                        <div className="form-group">
                            <label className="form-label">Nome Completo</label>
                            <input 
                                className="form-control"
                                value={formData.name}
                                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                required
                                placeholder="Seu nome completo"
                            />
                        </div>
                        <div className="form-group">
                            <label className="form-label">Telefone</label>
                            <input 
                                className="form-control"
                                value={formData.phone}
                                onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                                required
                                placeholder="(XX) XXXXX-XXXX"
                            />
                        </div>

                        <h4 style={{ margin: '1.5rem 0 1rem', color: 'var(--secondary-color)', borderBottom: '1px solid #eee', paddingBottom: '0.5rem' }}>
                            Endereço
                        </h4>

                        <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '1rem' }}>
                            <div className="form-group">
                                <label className="form-label">Logradouro</label>
                                <input 
                                    className="form-control"
                                    value={formData.address.publicPlace}
                                    onChange={(e) => setFormData({ 
                                        ...formData, 
                                        address: { ...formData.address, publicPlace: e.target.value }
                                    })}
                                    required
                                    placeholder="Rua, Avenida..."
                                />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Número</label>
                                <input 
                                    className="form-control"
                                    type="number"
                                    value={formData.address.number}
                                    onChange={(e) => setFormData({ 
                                        ...formData, 
                                        address: { ...formData.address, number: e.target.value }
                                    })}
                                    required
                                    placeholder="123"
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="form-label">Complemento (Opcional)</label>
                            <input 
                                className="form-control"
                                value={formData.address.complement}
                                onChange={(e) => setFormData({ 
                                    ...formData, 
                                    address: { ...formData.address, complement: e.target.value }
                                })}
                                placeholder="Apto, Bloco..."
                            />
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                            <div className="form-group">
                                <label className="form-label">Bairro</label>
                                <input 
                                    className="form-control"
                                    value={formData.address.neighborhood}
                                    onChange={(e) => setFormData({ 
                                        ...formData, 
                                        address: { ...formData.address, neighborhood: e.target.value }
                                    })}
                                    required
                                    placeholder="Bairro"
                                />
                            </div>
                            <div className="form-group">
                                <label className="form-label">Cidade</label>
                                <input 
                                    className="form-control"
                                    value={formData.address.city}
                                    onChange={(e) => setFormData({ 
                                        ...formData, 
                                        address: { ...formData.address, city: e.target.value }
                                    })}
                                    required
                                    placeholder="Cidade"
                                />
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '1rem' }}>
                            <div className="form-group">
                                <label className="form-label">UF</label>
                                <input 
                                    className="form-control"
                                    value={formData.address.uf}
                                    onChange={(e) => setFormData({ 
                                        ...formData, 
                                        address: { ...formData.address, uf: e.target.value.toUpperCase() }
                                    })}
                                    required
                                    maxLength="2"
                                    placeholder="SP"
                                />
                            </div>
                            <div className="form-group">
                                <label className="form-label">CEP</label>
                                <input 
                                    className="form-control"
                                    value={formData.address.zipCode}
                                    onChange={(e) => setFormData({ 
                                        ...formData, 
                                        address: { ...formData.address, zipCode: e.target.value }
                                    })}
                                    required
                                    placeholder="00000-000"
                                />
                            </div>
                        </div>
                    </form>
                ) : (
                    <div style={{ display: 'grid', gap: '1rem' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                            <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>CRM:</label>
                            <span>{doctor.crm}</span>
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                            <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>Especialidade:</label>
                            <span>{doctor.speciality}</span>
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                            <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>Telefone:</label>
                            <span>{doctor.phone || 'Não informado'}</span>
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                            <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>Endereço:</label>
                            <span>
                                {address.publicPlace && address.number 
                                    ? `${address.publicPlace}, ${address.number} - ${address.neighborhood || ''}, ${address.city || ''} - ${address.uf || ''}` 
                                    : 'Não informado'}
                            </span>
                        </div>
                        {address.zipCode && (
                            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '0.75rem', backgroundColor: '#f9f9f9', borderRadius: '4px' }}>
                                <label style={{ fontWeight: 'bold', color: 'var(--secondary-color)' }}>CEP:</label>
                                <span>{address.zipCode}</span>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default DoctorProfile;