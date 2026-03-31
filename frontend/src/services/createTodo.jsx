
export default async function registerUser(data,token){
    const baseURL = import.meta.env.VITE_API_URL;

    const response = await fetch(`${baseURL}/crud/todo`,{
        method:"POST",
        headers:{
            "Content-Type":"application/json",
            "Authorization":`Bearer ${token}`
        },
        body: JSON.stringify(data)
    });
        console.log(`response object is ${response}`)


    if(!response.ok){
        throw new Error("Registration Failed")
    }

    return response.json();
}