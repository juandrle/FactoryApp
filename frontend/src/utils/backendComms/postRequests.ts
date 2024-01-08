import type {IPlaceRequest} from "@/types/placeRequest";
import type {IEntityDelete, IFactoryCreate, IFactoryDelete, ILoginForm} from "@/types/backendEntity";
import {type IUserForm} from "@/types/backendEntity";
import {backendUrl} from "@/utils/config/config.js"


export const placeRequest = async (placeRequest: IPlaceRequest, suffix: string): Promise<boolean> => {
    try {
        const url = backendUrl + '/api/entity' + suffix
        const requestBody = JSON.stringify(placeRequest)
        const response = await fetch(url, {
            method: "POST",
            headers: {'Content-type': 'application/json'},
            body: requestBody,
        });
        const json = await response.json();
        return json
    } catch (error) {
        console.error("Error placing entity:", error)
        return false
    }
}

export const entityDeleteRequest = async (entity: IEntityDelete): Promise<boolean> => {
    try {
        const url = backendUrl + '/api/entity/delete'
        const requestBody = JSON.stringify(entity.id)
        const response = await fetch(url, {
            method: "POST",
            headers: {'Content-type': 'application/json'},
            body: requestBody,
        });
        const json = await response.json();
        return json
    } catch (error) {
        console.error("Error deleting entity:", error)
        return false
    }
}

export const factoryCreateRequest = async (factory: IFactoryCreate) => {
    try {
        const url = backendUrl + "/api/factory/create"
        const requestBody = JSON.stringify(factory)
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: requestBody,
        })
        if (!response.ok) throw new Error(response.statusText)
        const jsonData: number = await response.json()
        return jsonData
    } catch (err) {
        console.error(err)
        return 0
    }
}

export const factoryDeleteRequest = async (factory: IFactoryDelete) => {
    try {
        const url = 'api/factory/delete'
        const requestBody = JSON.stringify(factory.id)
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: requestBody,
        })
        if (!response.ok) throw new Error(response.statusText)
        const jsonData: boolean = await response.json()
        if (jsonData)
        factory.element.parent.remove(factory.element)

    } catch (err) {
        return false
    }
}
export const signupUser = async (userForm: IUserForm) => {
    try {
        const response = await fetch(backendUrl + '/api/users/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userForm),
        });
        console.log("das is von backend", response)
        if (response.status === 400) {
            return await response.text()
        }
        if (!response.ok) {
            // Handle non-successful responses
            return "communication error"
        }
        if (response.ok) {
            const res = await response.text()
            console.log(res)
            return res
            // Optionally, you can redirect the user to the home page or perform other actions
        }
    } catch (error) {
        console.error('Error during signup:', error)
        return error
        // Handle other errors, such as network issues
    }
};

export const loginUser = async (loginForm : ILoginForm) => {
    try{
        const response = await fetch(backendUrl + '/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',
            },
            body : JSON.stringify(loginForm)
        });
        if(!response.ok){
            return "communcation error"
        }
        const res = await response.text()
        
        console.log(res)
        return res
        
    }catch (error) {
        console.error('Error during signup:', error)
        return error
        
    }
}

export const logoutUser = async () => {
    
    const response = await fetch(backendUrl + '/api/users/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
       
        return "logout unsuccessful"
        
    }
    
    if(response.ok){
        const res = await response.text()
        console.log(res)
        return res
    }
}

