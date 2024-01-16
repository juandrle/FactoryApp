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
  from: THREE.Vector3,
  to: THREE.Vector3,
  object: THREE.Object3D,
  duration: number,
  kill: boolean,
  scene: THREE.Scene,
  onEnd?: () => void
) => {
  let startTime: number

  // Berechne die Größe der BoundingBox und verschiebe
  let boundingBox = new THREE.Box3().setFromObject(object)
  let centerOfBoundingBox = new THREE.Vector3()
  boundingBox.getCenter(centerOfBoundingBox)
  from.sub(centerOfBoundingBox.clone())
  to.sub(centerOfBoundingBox.clone())

  // Setting start position
  object.position.set(from.x, from.y, from.z)

  // Add to scene
  scene.add(object)

  const animate = (timestamp: number) => {
    if (!startTime) startTime = timestamp

    const elapsed = timestamp - startTime
    const progress = Math.min(elapsed / duration, 1)

    interpolateObject(from, to, object, progress)

    if (progress < 1) {
      requestAnimationFrame(animate)
    } else if (onEnd) {
      if (kill) scene.remove(object)
      onEnd()
    }
  }

  requestAnimationFrame(animate)
}
