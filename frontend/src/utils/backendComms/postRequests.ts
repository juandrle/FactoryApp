import type {
    IEntityDelete,
    IEntityMove,
    IEntityRotate,
    IFactoryCreate,
    IFactoryDelete,
    IManipulationRequest,
    IPlaceRequest,
    IUserForm,
    ILoginForm
} from "@/types/backendTypes";
import {backendUrl} from "@/utils/config/config"


export const placeRequest = async (placeRequest: IPlaceRequest) => {
    return await fetch(backendUrl + '/api/entity/place', {
        method: "POST",
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(placeRequest),
    });
}

export const rotationRequest = async (rotateRequest: IEntityRotate) => {
    return await fetch(backendUrl + '/api/entity/rotate', {
        method: "POST",
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(rotateRequest),
    });
}

export const moveRequest = async (moveRequest: IEntityMove) => {
    return await fetch(backendUrl + '/api/entity/move', {
        method: "POST",
        headers: {'Content-type': 'application/json'},
        body: JSON.stringify(moveRequest),
    });
}

export const manipulationRequest = async (manipulationRequest: IManipulationRequest, suffix: string): Promise<boolean> => {
    try {
        const response = await fetch(backendUrl + '/api/entity/' + suffix, {
            method: "POST",
            headers: {'Content-type': 'application/json'},
            body: JSON.stringify(manipulationRequest),
        });
        return await response.json();
    } catch (error) {
        console.error("Error placing entity:", error)
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

export const factoryImageUpdate = async (factoryID: number, screenshot: string) : Promise<boolean> => {

    try {
        const url = backendUrl + '/api/factory/updateImage'
        const requestBody = JSON.stringify({
            factoryID: factoryID,
            screenshot: screenshot
        })
        console.log(requestBody)
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: requestBody,
        })
        if (!response.ok) throw new Error(response.statusText)
        return await response.json()
    } catch (err) {
        console.error(err)
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


export const sendScriptingToBackend = async (modelID: Number, scriptContent: string) : Promise<boolean> => {

    try {
        const url = backendUrl + '/api/entity/postScript'
        const requestBody = JSON.stringify({
            modelID: Number,
            scriptContent: String
        })
        console.log(requestBody)
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: requestBody,
        })
        if (!response.ok) throw new Error(response.statusText)
        return await response.json()
    } catch (err) {
        console.error(err)
        return false
    }
}

// oder so? :
//   export const sendScriptingToBackend: any = async ( // siehe factory.vue z. 241f. 
//     url: string,
//     scene: THREE.Scene,
//     loader: any,
//     code: string
// ) => {}

