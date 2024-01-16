import type { IEntity } from '@/classes/placedEntities/placedEntities'
import type { IVector3 } from '@/types/global'
import * as THREE from 'three'

const interpolateObject = (
  from: IVector3,
  to: IVector3,
  object: THREE.Object3D,
  factor: number
) => {
  const interpolatedX = from.x + (to.x - from.x) * factor
  const interpolatedY = from.y + (to.y - from.y) * factor
  const interpolatedZ = from.z + (to.z - from.z) * factor

  object.position.set(interpolatedX, interpolatedY, interpolatedZ)
}

export const animateObject = (
  from: IVector3,
  to: IVector3,
  object: THREE.Object3D,
  duration: number,
  onEnd?: () => void
) => {
  let startTime: number
  const animate = (timestamp: number) => {
    if (!startTime) startTime = timestamp

    const elapsed = timestamp - startTime
    const progress = Math.min(elapsed / duration, 1)

    interpolateObject(from, to, object, progress)

    if (progress < 1) {
      requestAnimationFrame(animate)
    } else if (onEnd) {
      onEnd()
    }
  }

  requestAnimationFrame(animate)
}

export const getStartAndEndPointFromPipe = (
  pipe: IEntity
): { startPoint: THREE.Vector3; endPoint: THREE.Vector3 } => {
  const startPointLocal: THREE.Vector3 = new THREE.Vector3().copy(
    pipe.threejsObject.children.find((mesh) => mesh.name === 'pipe_entrance')?.geometry.boundingSphere.center
  )
  const endPointLocal: THREE.Vector3 = new THREE.Vector3().copy(
    pipe.threejsObject.children.find((mesh) => mesh.name === 'pipe_exit')?.geometry.boundingSphere.center
  )
  const pipePositionWorld: THREE.Vector3 = pipe.threejsObject.position
  const startPoint = new THREE.Vector3().copy(pipePositionWorld).add(startPointLocal)
  const endPoint = new THREE.Vector3().copy(pipePositionWorld).add(endPointLocal)

  return {
    startPoint: startPoint,
    endPoint: endPoint
  }
}
