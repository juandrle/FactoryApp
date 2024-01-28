import type { IBackendEntity, IBackendEntityPreview } from '@/types/backendTypes'
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



