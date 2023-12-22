import type { IBackendEntity, IBackendEntityPreview } from '@/types/backendEntity'
import { backendUrl } from '@/utils/config/config'

export const getAllEntitys: () => Promise<IBackendEntityPreview[]> = async () => {
  return fetch(backendUrl + '/api/entity/getAll').then((res) => res.json())
}

export const getAllEntitysInFactory: (factoryId: number) => Promise<IBackendEntity[]> = async (factoryId: number) => {
  return fetch(backendUrl + '/api/entity/getAll/' + factoryId).then((res) => res.json())
}
