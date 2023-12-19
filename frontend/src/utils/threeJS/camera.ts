import type { ICameraInfo, IVector3 } from '@/types/global'

export const ExtractCameraInfo = (camera: any): ICameraInfo => {
  return {
    position: {...camera.position},
    lookAt: {
      x: camera.matrix.elements[8],
      y: camera.matrix.elements[9],
      z: camera.matrix.elements[10]
    } as IVector3,
    up: {...camera.up}
  }
}

export const SetCameraInfo = (camera: any, info: ICameraInfo): void => {
  camera.position.set(info.position.x, info.position.y, info.position.z)
  camera.up.set(info.up.x, info.up.y, info.up.z)
  camera.lookAt(info.lookAt.x, info.lookAt.y, info.lookAt.z)
}
