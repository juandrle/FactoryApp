import { interpolateVector } from '@/utils/animation/animation'
import { loadEntitie } from '@/utils/threeJS/entityManipulation'
import * as THREE from 'three'
import type { PlacedEntities } from '../placedEntities/placedEntities'

export class AnimationManager {
  private placedEntitesRef: PlacedEntities
  private sceneRef: THREE.Scene
  private loaderRef: THREE.Loader
  private mockModelUrl: string = "http://localhost:8080/models/mock/items/processed/kupfer_barren.gltf"

  constructor(placedEntitiesRef: PlacedEntities, sceneRef: THREE.Scene, loaderRef: THREE.Loader) {
    this.placedEntitesRef = placedEntitiesRef
    this.sceneRef = sceneRef
    this.loaderRef = loaderRef
  }

  startAnimation() {
    this.placedEntitesRef.getAllStraihtPipes().forEach(({ startPoint, endPoint, pipeCount }) => {
      this.animateObject(
        startPoint,
        endPoint,
        this.mockModelUrl,
        500 * pipeCount,
        () => {}
      )
    })
  }

  stoppAnimation() {}

  animateObject = (
    from: THREE.Vector3,
    to: THREE.Vector3,
    path: string,
    duration: number,
    onEnd?: () => void
  ) => {
    let startTime: number

    loadEntitie(this.loaderRef, path).then((object) => {
      // Berechne die Größe der BoundingBox und verschiebe in die mitte
      let boundingBox = new THREE.Box3().setFromObject(object)
      let centerOfBoundingBox = new THREE.Vector3()
      boundingBox.getCenter(centerOfBoundingBox)
      from.sub(centerOfBoundingBox.clone())
      to.sub(centerOfBoundingBox.clone())

      // Setting start position
      object.position.set(from.x, from.y, from.z)

      // Add to scene
      this.sceneRef.add(object)

      const animate = (timestamp: number) => {
        if (!startTime) startTime = timestamp

        const elapsed = timestamp - startTime
        const progress = Math.min(elapsed / duration, 1)

        interpolateVector(from, to, object, progress)

        if (progress < 1) {
          requestAnimationFrame(animate)
        } else if (onEnd) {
          this.sceneRef.remove(object)
          onEnd()
        }
      }

      requestAnimationFrame(animate)
    })
  }
}
