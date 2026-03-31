import { useTodos } from '../context/TodoContext';

export default function Input() {
    const { addTodoLocal } = useTodos();

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const data = {
            title: e.target.title.value,
            description: e.target.description.value,
        };

        console.log(`at input`,data)

        await addTodoLocal(data);
        e.target.reset();
    };

    return (
        <form onSubmit={handleSubmit} className="keep-input-form">
            <div className='horizontal inputContainer'>
                
               <div className='nearAdjust'>
                    <input 
                        id='title' 
                        name='title' 
                        placeholder="Title" 
                        className="keep-title-input"
                    />
                    <textarea 
                        id='description' 
                        name='description' 
                        placeholder="Description"
                        className="keep-desc-input"
                        rows="3"
                    />
                </div>
            
                <button type="submit">Add Note</button>
            </div>
        </form>
    );
}
