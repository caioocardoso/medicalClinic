/**
 * Extrai mensagens de erro do backend em diferentes formatos
 * @param {Error} error - Erro do axios
 * @returns {string} - Mensagem de erro formatada
 */
export const extractErrorMessage = (error) => {
    if (!error.response) {
        // Erro de rede ou servidor não responde
        return 'Erro de conexão. Verifique sua internet ou tente novamente.';
    }

    const { data, status } = error.response;

    // Caso 1: Array de erros de validação
    // Formato: [{ field: "userData.name", message: "Name is required" }]
    if (Array.isArray(data)) {
        const messages = data.map(err => {
            const fieldName = formatFieldName(err.field);
            return `${fieldName}: ${err.message}`;
        });
        return messages.join('\n');
    }

    // Caso 2: Erro de negócio (ResponseStatusException)
    // Formato: { message: "Erro...", details: "...", statusCode: 400 }
    if (data && typeof data === 'object') {
        if (data.details) {
            return data.details;
        }
        if (data.message) {
            return data.message;
        }
    }

    // Caso 3: String simples
    if (typeof data === 'string') {
        return data;
    }

    // Caso 4: Erros HTTP padrão
    const statusMessages = {
        400: 'Dados inválidos. Verifique as informações e tente novamente.',
        401: 'Não autorizado. Faça login novamente.',
        403: 'Você não tem permissão para realizar esta ação.',
        404: 'Recurso não encontrado.',
        409: 'Conflito. O recurso já existe ou está em uso.',
        500: 'Erro interno do servidor. Tente novamente mais tarde.',
        503: 'Serviço temporariamente indisponível.'
    };

    return statusMessages[status] || `Erro ${status}: Ocorreu um erro inesperado.`;
};

/**
 * Formata nomes de campos para exibição
 * @param {string} field - Nome do campo (ex: "userData.name")
 * @returns {string} - Nome formatado (ex: "Nome")
 */
const formatFieldName = (field) => {
    const fieldMap = {
        'userData.name': 'Nome',
        'userData.email': 'E-mail',
        'userData.phone': 'Telefone',
        'userData.password': 'Senha',
        'cpf': 'CPF',
        'crm': 'CRM',
        'speciality': 'Especialidade',
        'address.street': 'Rua',
        'address.number': 'Número',
        'address.city': 'Cidade',
        'address.state': 'Estado',
        'address.zipCode': 'CEP',
        'dateTime': 'Data e Hora',
        'patientId': 'Paciente',
        'doctorId': 'Médico'
    };

    return fieldMap[field] || field.split('.').pop();
};

/**
 * Exibe mensagem de erro de forma amigável
 * @param {Error} error - Erro do axios
 * @param {Function} showToast - Função opcional para exibir toast
 * @returns {string} - Mensagem extraída
 */
export const handleApiError = (error, showToast = null) => {
    const message = extractErrorMessage(error);
    
    if (showToast) {
        showToast(message, 'error');
    } else {
        alert(message);
    }
    
    return message;
};
