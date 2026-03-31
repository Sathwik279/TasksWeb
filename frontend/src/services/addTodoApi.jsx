// No import needed for meta.env!

export default async function addTodoApi(data, token) {
    // Access the variable directly from import.meta.env
    // Ensure your .env file uses: VITE_API_URL=https://...
    const baseURL = import.meta.env.VITE_API_URL;

    const response = await fetch(`${baseURL}/crud/todo`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error("Adding todo Failed");
    }

    return response.json();
}