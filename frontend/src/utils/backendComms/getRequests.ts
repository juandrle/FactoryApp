import type { IBackendEntity, IBackendEntityPreview } from '@/types/backendTypes'
import { backendUrl } from '@/utils/config/config'

export const getAllEntities: () => Promise<IBackendEntityPreview[]> = async () => {
  return fetch(backendUrl + '/api/entity/getAll').then((res) => res.json())
}

export const getAllEntitiesInFactory: (factoryId: number) => Promise<IBackendEntity[]> = async (factoryId: number) => {
  return fetch(backendUrl + '/api/factory/getAll/' + factoryId).then((res) => res.json())
}
