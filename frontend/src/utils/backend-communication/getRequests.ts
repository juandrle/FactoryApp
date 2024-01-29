import type { IBackendEntity, IBackendEntityPreview, ISystemProperty, IUserProperty } from '@/types/backendTypes'
import { backendUrl } from '@/utils/config/config'

export const getAllEntities: () => Promise<IBackendEntityPreview[]> = async () => {
  return fetch(backendUrl + '/api/entity/getAll').then((res) => res.json())
}

export const getAllEntitiesInFactory: (factoryId: number) => Promise<IBackendEntity[]> = async (factoryId: number) => {
  return fetch(backendUrl + '/api/factory/getAll/' + factoryId).then((res) => res.json())
}
export const getAllFactories = async () => {
  return fetch(backendUrl + '/api/factory/getAll').then((res) => res.json())
}
export const getFactoryImage: (factoryId: number) => Promise<String> = async (factoryId: number) => {
  return fetch(backendUrl + '/api/factory/getImage/' + factoryId).then((res) => res.text())
}
export const getAllUsers: () => Promise<{ username: string }[]> = async () => {
  return fetch(backendUrl + '/api/users/getAll').then((res) => res.json())
}
// Scripting Requests

// get scripting file to display in FE View "ScriptContainer"
export const getScriptingContent = async (modelId: number): Promise<String> => {
  console.log("AYWAAAAAAA!!!!!! im FE Anfrage Scripting aufgerufen")
  return fetch(backendUrl + '/api/entity/getScriptContent/' + modelId).then((res) => res.text())
}

// get systemProperties file to display on the right side in FE View "ScriptContainer"
export const getAllSystemProperties : (modelId: number) => Promise<ISystemProperty[]>= async (modelId: number) => {
  console.log("AYWAAAAAAA!!!!!! im FE Anfrage getAllSystemProperties aufgerufen")
  return fetch(backendUrl + '/api/entity/systemProperties/getAll/' + modelId, { credentials: 'include'}).then((res) => res.json())
}

// get userProperties file to display on the right side in FE View "ScriptContainer"
export const getAllUserProperties : (modelId: number) => Promise<IUserProperty[]>= async (modelId: number) => {
  console.log("AYWAAAAAAA!!!!!! im FE Anfrage getAllUserProperties aufgerufen")
  return fetch(backendUrl + '/api/entity/userProperties/getAll/'+ modelId, { credentials: 'include'}).then((res) => res.json())
}

export const getAllUsersInFactory = async (factoryId: number): Promise<{ username: string }[]> => {
  try {
    const response = await fetch(`${backendUrl}/api/factory/${factoryId}/users`, {
      credentials: 'include',
    });

    if (!response.ok) {
      console.error('HTTP error!', response.status, response.statusText);
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    // Assuming the response is plain text
    const data = await response.json();
    console.log(data)
    return data;
  } catch (error) {
    console.error('Error fetching users:', error);
    throw error;
  }
};



