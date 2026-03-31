
export default async function updateTodoApi(token,id,todo){
        const baseURL = import.meta.env.VITE_API_URL;

    const response = await fetch(`${baseURL}/crud/todo/${id}`,{
        method:"PUT",
        headers:{
            "Content-Type":"application/json",
            "Authorization":`Bearer ${token}`
        },
        body: JSON.stringify(todo)
    });
        console.log(`response object is ${response}`)


    if(!response.ok){
        throw new Error("Updation Failed")
    }

    return response.json();
}
