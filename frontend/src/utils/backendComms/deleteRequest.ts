import type {IEntityDelete} from "@/types/backendTypes";
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