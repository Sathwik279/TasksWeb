
export default async function loginUser(data){
        const baseURL = import.meta.env.VITE_API_URL;

    const response = await fetch(`${baseURL}/auth/login`,{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body: JSON.stringify(data)
    });

    console.log(`response object is ${response}`)
    if(!response.ok){
        throw new Error("Registration Failed")
    }

    return response.json();
}