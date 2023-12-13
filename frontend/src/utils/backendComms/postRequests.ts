import {IPlaceRequest} from "@/types/placeRequest";
import type {IFactoryCreate, IFactoryDelete} from "@/types/backendEntity";

export const placeRequest = (placeRequest: IPlaceRequest) => {
    // console.log(placeRequest, "... würde jetzt ans backend gesendet werden")
    return true
}

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