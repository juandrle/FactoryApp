import type { IVector3 } from '@/types/global'
import * as THREE from 'three'

export const interpolateVector = (
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


