import React, { useState, useEffect } from 'react';
import './Toast.css';

let globalShowToast = null;

export const useToast = () => {
    return {
        showToast: (message, type = 'info') => {
            if (globalShowToast) {
                globalShowToast(message, type);
            }
        }
    };
};

const Toast = () => {
    const [toasts, setToasts] = useState([]);

    useEffect(() => {
        globalShowToast = (message, type = 'info') => {
            const id = Date.now();
            const newToast = { id, message, type };
            
            setToasts(prev => [...prev, newToast]);

            setTimeout(() => {
                removeToast(id);
            }, 5000);
        };

        return () => {
            globalShowToast = null;
        };
    }, []);

    const removeToast = (id) => {
        setToasts(prev => prev.filter(toast => toast.id !== id));
    };

    if (toasts.length === 0) return null;

    return (
        <div className="toast-container">
            {toasts.map(toast => (
                <div 
                    key={toast.id} 
                    className={`toast toast-${toast.type}`}
                    onClick={() => removeToast(toast.id)}
                >
                    <div className="toast-icon">
                        {toast.type === 'success' && '✓'}
                        {toast.type === 'error' && '✕'}
                        {toast.type === 'warning' && '⚠'}
                        {toast.type === 'info' && 'ℹ'}
                    </div>
                    <div className="toast-content">
                        <div className="toast-message">{toast.message}</div>
                    </div>
                    <button 
                        className="toast-close"
                        onClick={(e) => {
                            e.stopPropagation();
                            removeToast(toast.id);
                        }}
                    >
                        ×
                    </button>
                </div>
            ))}
        </div>
    );
};

export default Toast;
