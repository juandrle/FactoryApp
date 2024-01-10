import type {IEntityDelete, IFactoryDelete} from "@/types/backendTypes";
import {backendUrl} from "@/utils/config/config";

export const entityDeleteRequest = async (entity: IEntityDelete): Promise<boolean> => {
    try {
        const url = backendUrl + '/api/entity/delete'
        const requestBody = JSON.stringify(entity)
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
export const factoryDeleteRequest = async (factory?: IFactoryDelete) => {
    if (!factory) return
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
            factory.element.remove()
        console.log(factory.element)
    } catch (err) {
        return false
    }
}