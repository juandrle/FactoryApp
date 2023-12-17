import {IPlaceRequest} from "@/types/placeRequest";
import type {IFactoryCreate, IFactoryDelete} from "@/types/backendEntity";

export const placeRequest = async (placeRequest: IPlaceRequest): Promise<boolean> => {
    try {
        const response = await fetch("http://localhost:8080/api/entity/place", {
            method: "POST",
            body: JSON.stringify(placeRequest),
            headers: {"Content-type": "application/json"},
        });

        const json = await response.json();
        return json;
    } catch (error) {
        console.error("Error placing request:", error);
        return false;
    }
};


export const factoryCreateRequest = async (factory: IFactoryCreate) => {
    try {
        const url = 'api/factory/create'
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