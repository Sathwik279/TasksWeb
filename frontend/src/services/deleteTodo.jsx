
export default async function deleteTodoApi(token,id){
        const baseURL = import.meta.env.VITE_API_URL;

    const response = await fetch(`${baseURL}/crud/todo/${id}`,{
        method:"DELETE",
        headers:{
            "Content-Type":"application/json",
            "Authorization":`Bearer ${token}`
        },
    });
        console.log(`response object is ${response}`)


    if(!response.ok){
        throw new Error("Deletion Failed")
    }

    return response.json();
}
