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


export const sendScriptingToBackend = async (modelId: number, scriptContent: string) => {
    try {
        const requestBody = JSON.stringify({
            modelId: modelId,
            scriptContent: scriptContent
        })

        console.log("sendScriptingToBackend() requestBody: ", requestBody);

        const response = await fetch(backendUrl + '/api/entity/postScript/' + modelId, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: requestBody,
            credentials: 'include'
        })

        console.log("sendScriptingToBackend() response: ", response);

        if (!response.ok) throw new Error(response.statusText)

        // return await response.json() // diese klappt nicht
        const res = await response.text()
        // console.log(res)
        return res

    } catch (err) {
        console.error(err)
        return false
    }
}


export const enterFactory = async (factoryID: number, userName: string) => {
    
    try {
        console.log(`Entering factory with ID: ${factoryID} and username: ${userName}`);
        const url = backendUrl + '/api/factory/enter'
        const requestBody = JSON.stringify({
            factoryID: factoryID,
            userName: userName
        });
        console.log(requestBody); // Log the payload
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: requestBody,
        });

        console.log('Response status:', response.status);
        const responseBody = await response.text();
        console.log('Response body:', responseBody);

        if (!response.ok) {
            return "Entering unsuccessful";
        } else {
            console.log(response);
        }
            
    } catch (error) {
        console.error('Error while entering factory: ', error);
        return "Error entering factory";
    }
}

export const leaveFactory = async (factoryID: number, userName: string) => {
    
    try {
        console.log(`Leaving factory with ID: ${factoryID} and username: ${userName}`);
        const url = backendUrl + '/api/factory/leave'
        const requestBody = JSON.stringify({
            factoryID: factoryID,
            userName: userName
        });
        console.log(requestBody); // Log the payload
        const response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: requestBody,
        });

        console.log('Response status:', response.status);
        const responseBody = await response.text();
        console.log('Response body:', responseBody);

        if (!response.ok) {
            return "Leaving unsuccessful";
        } else {
            console.log(response);
        }
            
    } catch (error) {
        console.error('Error while leaving factory: ', error);
        return "Error leaving factory";
    }
}
