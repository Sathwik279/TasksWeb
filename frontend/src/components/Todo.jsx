import { useTodos } from '../context/TodoContext';
import { useState, useEffect } from 'react';

export default function Todo({ todo }) {
    const { updateTodo, deleteTodoLocal } = useTodos();

    const [title, setTitle] = useState(todo.title || '');
    const [description, setDescription] = useState(todo.description || '');
    const [aiEnabled, setAiEnabled] = useState(Boolean(todo.aiEnabled));
    
    const [isAiExpanded, setIsAiExpanded] = useState(false);

    useEffect(() => {
        setTitle(todo.title || "");
        setDescription(todo.description || "");
        setAiEnabled(Boolean(todo.aiEnabled));
    }, [todo]);

    useEffect(() => {
        const hasChanged =
            title !== (todo.title || "") ||
            description !== (todo.description || "") ||
            aiEnabled !== Boolean(todo.aiEnabled);

        if (!hasChanged || !todo.id) {
            return;
        }

        const timeoutId = setTimeout(() => {
            const updatedData = { ...todo, title, description, aiEnabled };
            updateTodo(todo.id, updatedData);
        }, 1000);

        return () => clearTimeout(timeoutId);
    }, [title, description, aiEnabled, todo]);

    return (
        <div className="keep-card">
            <input
                className="keep-title-input"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Title"
            />
            
            <textarea
                className="keep-desc-input"
                rows="4"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Take a note..."
            />

            {todo.aiContent && (
                <div className="ai-summary-box">
                    <span className="ai-label">AI</span>
                    
                    <div 
                        className={`ai-text ${isAiExpanded ? 'expanded' : 'clamped'}`}
                        onClick={() => setIsAiExpanded(!isAiExpanded)}
                        onKeyDown={(e) => {
                            if (e.key === 'Enter' || e.key === ' ') {
                                e.preventDefault();
                                setIsAiExpanded(!isAiExpanded);
                            }
                        }}
                        tabIndex={0}
                        role="button"
                        aria-expanded={isAiExpanded}
                    >
                        {todo.aiContent}
                    </div>
                    
                    <div 
                        className="ai-toggle-hint" 
                        onClick={() => setIsAiExpanded(!isAiExpanded)}
                    >
                        {isAiExpanded ? 'Show less' : 'Read more...'}
                    </div>
                </div>
            )}

            <div className="card-actions">
                <div className='aiButton'>
                    <label className="switch">
                        <input
                            type="checkbox"
                            checked={aiEnabled}
                            onChange={(e) => setAiEnabled(e.target.checked)}
                        />
                        <span className="slider"></span>
                    </label>
                    <span>Ai</span>
                </div>
                <button onClick={() => deleteTodoLocal(todo.id)}>Del</button>
            </div>
        </div>
    );
}
