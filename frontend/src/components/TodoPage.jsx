import Input from './Input'
import { useEffect } from 'react';
import Todo from './Todo'
import {useTodos} from '../context/TodoContext'

export default function TodoPage(){
    let { todos,fetchTodos } = useTodos();
    
    useEffect(() => {
        fetchTodos();
    }, [fetchTodos]);

    return (
      <div className="vertical">
        <Input/>

        <div className='masonry-grid'>
            {todos.map((todo)=>(
                <Todo key={todo.id} todo={todo}/>
            ))}
        </div>
      </div>
    )
}
