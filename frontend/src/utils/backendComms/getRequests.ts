import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'
import { IBackendEntity, IBackendEntityPreview } from '@/types/backendEntity'
import * as THREE from 'three'
import { placeEntity } from '@/utils/threeJS/helpFunctions'
import { backendUrl } from '@/utils/config/config.js'

export const getAllEntitys: () => Promise<IBackendEntityPreview[]> = async () => {
  return fetch(backendUrl + '/api/entity/getAll').then((res) => res.json())
}

export const getAllEntitysInFactory: (factoryId: number) => Promise<IBackendEntity[]> = async (factoryId: number) => {
  return fetch(backendUrl + '/api/entity/getAll/' + factoryId).then((res) => res.json())
}
export const getAllFactories = async () => {
  return fetch(backendUrl + '/api/factory/getAll').then((res) => res.json())
}

export const fetchCurrentUser = async () => {
  try {
      const response = await fetch(backendUrl + '/api/user/current', {
          method: 'GET',
          headers: {
              'Content-Type': 'application/json',
              // Include any necessary authentication headers (e.g., tokens)
          },
      });

      return response;
  } catch (error) {
      // Handle other errors, such as network issues
      console.error('Error during fetchCurrentUser:', error);
      throw error;
  }
}