# Sistema de Tratamento de Erros - Medical Clinic Frontend

## 📋 Visão Geral

Este sistema oferece tratamento centralizado e padronizado de erros da API, com exibição elegante através de notificações Toast.

## 🎯 Componentes Principais

### 1. **errorHandler.js** - Utilitário de Tratamento de Erros
Localização: `src/utils/errorHandler.js`

#### Funções Disponíveis:

**`extractErrorMessage(error)`**
- Extrai mensagens de erro em diferentes formatos do backend
- Trata arrays de validação, objetos de erro e strings
- Retorna mensagem formatada e legível

**`handleApiError(error, showToast)`**
- Wrapper conveniente que extrai e exibe o erro
- Usa toast se disponível, senão usa alert
- Retorna a mensagem extraída

#### Formatos de Erro Suportados:

```javascript
// 1. Array de erros de validação
[
  { field: "userData.name", message: "Name is required" },
  { field: "userData.phone", message: "Phone is required" }
]

// 2. Objeto de erro de negócio
{
  message: "Erro no processamento",
  details: "CPF já cadastrado no sistema",
  statusCode: 409
}

// 3. String simples
"Erro ao processar requisição"
```

### 2. **Toast.jsx** - Componente de Notificações
Localização: `src/components/Toast.jsx`

Sistema de notificações não-intrusivas que aparecem no canto superior direito.

#### Tipos de Toast:
- ✅ **success** - Verde (operações bem-sucedidas)
- ❌ **error** - Vermelho (erros)
- ⚠️ **warning** - Amarelo (avisos)
- ℹ️ **info** - Azul (informações)

#### Características:
- Auto-fechamento após 5 segundos
- Clique para fechar manualmente
- Suporta múltiplas notificações simultâneas
- Animação suave de entrada
- Responsivo

## 🚀 Como Usar

### Setup Inicial

1. **Adicionar Toast no componente raiz:**

```jsx
import Toast from '../components/Toast';

function Home() {
    return (
        <div>
            {/* Seu conteúdo */}
            <Toast />
        </div>
    );
}
```

2. **Usar o hook useToast em qualquer componente:**

```jsx
import { useToast } from '../components/Toast';
import { handleApiError } from '../utils/errorHandler';

function MeuComponente() {
    const { showToast } = useToast();

    const salvarDados = async () => {
        try {
            await api.post('/endpoint', dados);
            showToast('Dados salvos com sucesso!', 'success');
        } catch (err) {
            handleApiError(err, showToast);
        }
    };
}
```

### Exemplos Práticos

#### Exemplo 1: Cadastro de Paciente

```jsx
const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (cpf.length !== 11) {
        showToast("CPF deve conter 11 dígitos", "warning");
        return;
    }

    try {
        const response = await api.post("/paciente/perfil", { cpf });
        showToast("Cadastro realizado com sucesso!", "success");
        onSuccess(response.data);
    } catch (err) {
        handleApiError(err, showToast);
    }
};
```

#### Exemplo 2: Agendar Consulta

```jsx
const agendarConsulta = async () => {
    try {
        await api.post('/consulta', { patientId, doctorId, dateTime });
        showToast('Consulta agendada com sucesso!', 'success');
        fetchData();
    } catch (err) {
        // Exibe automaticamente:
        // - Erros de validação formatados
        // - Conflitos de horário
        // - Erros de permissão
        handleApiError(err, showToast);
    }
};
```

#### Exemplo 3: Login

```jsx
const handleLogin = async (e) => {
    e.preventDefault();
    
    try {
        const response = await api.post("/auth/login", { email, password });
        localStorage.setItem("token", response.data.token);
        navigate("/home");
    } catch (err) {
        // Extrai a mensagem e salva no estado para exibir no form
        const errorMsg = extractErrorMessage(err);
        setError(errorMsg);
    }
};
```

#### Exemplo 4: Notificações Manuais

```jsx
// Sucesso
showToast('Operação concluída!', 'success');

// Erro
showToast('Algo deu errado!', 'error');

// Aviso
showToast('Atenção: campos obrigatórios', 'warning');

// Informação
showToast('Processando sua solicitação...', 'info');
```

## 🎨 Personalização

### Modificar Tempo de Auto-Fechamento

Em `Toast.jsx`, linha 25:

```javascript
setTimeout(() => {
    removeToast(id);
}, 5000); // Altere para o tempo desejado em ms
```

### Adicionar Novos Mapeamentos de Campos

Em `errorHandler.js`, função `formatFieldName`:

```javascript
const fieldMap = {
    'userData.name': 'Nome',
    'userData.email': 'E-mail',
    'meuNovoCampo': 'Minha Label',
    // ... adicione mais aqui
};
```

### Estilizar Toast

Edite `Toast.css` para customizar cores, tamanhos e animações.

## 📱 Tratamento de Erros do Backend

### RestControllerAdvice Compatível

O sistema é compatível com este formato de RestControllerAdvice:

```java
@RestControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorData>> handleValidationErrors() {
        // Retorna: [{ field: "...", message: "..." }]
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseData> handleBusinessLogicError() {
        // Retorna: { message: "...", details: "...", statusCode: 400 }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound() {
        // Retorna: 404
    }
}
```

## ✅ Benefícios

1. ✨ **UX Melhorada**: Notificações elegantes ao invés de alerts
2. 🎯 **Consistência**: Tratamento padronizado em toda aplicação
3. 🔍 **Debug Facilitado**: Mensagens claras e específicas
4. 🌐 **Internacionalização Ready**: Fácil adicionar traduções
5. 📱 **Responsivo**: Funciona em mobile e desktop
6. ♿ **Acessível**: Pode ser estendido com ARIA labels

## 🔧 Troubleshooting

### Toast não aparece?
- Verifique se `<Toast />` está renderizado no componente pai
- Certifique-se de estar usando `useToast()` dentro de um componente React

### Mensagens em inglês?
- Adicione mapeamentos em `formatFieldName` no `errorHandler.js`

### Múltiplos toasts empilhados?
- Comportamento esperado para múltiplos erros
- Para limitar, adicione lógica de fila em `Toast.jsx`

## 📚 Próximos Passos

- [ ] Adicionar posição configurável dos toasts
- [ ] Implementar fila de prioridade
- [ ] Adicionar sons de notificação (opcional)
- [ ] Integrar com sistema de logging
- [ ] Adicionar testes unitários

---

**Desenvolvido para Medical Clinic System** 🏥
