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

export const getFactoryImage: (factoryId: number) => Promise<String> = async (factoryId: number) => {
  return fetch(backendUrl + '/api/factory/getImage/').then((res) => res.json())
}
