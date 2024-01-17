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

// Scripting Request

export const getAllSystemProperties = async () => {
  return fetch(backendUrl + '/api/systemProperties/getAll').then((res) => res.json())
}

export const getAllUserProperties = async () => {
  return fetch(backendUrl + '/api/userProperties/getAll').then((res) => res.json())
}

// get scripting file to display in FE View "ScriptContainer"
export const getScriptingContent: (modelId: number) => Promise<String> = async (modelId: number) => {
  return fetch(backendUrl + '/api/entity/getScript/' + modelId).then((res) => res.text())
}