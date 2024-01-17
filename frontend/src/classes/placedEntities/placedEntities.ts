import * as THREE from 'three'

/**
 * Represents the collection of all current entities in the factory
 */
export class PlacedEntities {
  private allEntities: IEntity[] = []

  /**
   * Single Entity Operations
   */
  public add = (entity: IEntity) => this.allEntities.push(entity)

  public getByUUID = (uuid: string): IEntity => {
    const entity = this.allEntities.find((e) => e.uuid === uuid)
    if (!entity) throw new Error('Entity not found')
    return entity
  }

  public rotateEntityByUUID = (uuid: string, dir: 'left' | 'right') => {
    let entity: IEntity = this.getByUUID(uuid)

    if (dir === 'left') {
      entity.orientation = turnLeft(entity.orientation)
    } else {
      entity.orientation = turnRight(entity.orientation)
    }
  }

  public deleteByUUID = (uuid: string): void => {
    this.allEntities = this.allEntities.filter((entity) => entity.uuid !== uuid)
  }

  public getAllEntities = (): IEntity[] => this.allEntities

  /**
   * Single Pipe Actions
   */
  public getAllStraightSinglePipes = () =>
    this.allEntities.filter((entity) => entity.modelId === 'pipe_straight')

  public getPointsFromStraightSinglePipe = (
    pipe: IEntity
  ): { startPoint: THREE.Vector3; endPoint: THREE.Vector3 } => {
    const startPointLocal: THREE.Vector3 = new THREE.Vector3().copy(
      pipe.threejsObject.children.find((mesh) => mesh.name === 'pipe_entrance')?.geometry
        ?.boundingSphere?.center
    )
    const endPointLocal: THREE.Vector3 = new THREE.Vector3().copy(
      pipe.threejsObject.children.find((mesh) => mesh.name === 'pipe_exit')?.geometry
        ?.boundingSphere?.center
    )
    const pipePositionWorld: THREE.Vector3 = pipe.threejsObject.position
    const startPoint = new THREE.Vector3().copy(pipePositionWorld).add(startPointLocal)
    const endPoint = new THREE.Vector3().copy(pipePositionWorld).add(endPointLocal)

    return {
      startPoint: startPoint,
      endPoint: endPoint
    }
  }

  /**
   * Pipe System
   */

  public getAllStraihtPipes = (): {
    startPoint: THREE.Vector3
    endPoint: THREE.Vector3
    pipeCount: number
  }[] => {
    let out: { startPoint: THREE.Vector3; endPoint: THREE.Vector3; pipeCount: number }[] = []

    this.getAllStraightSinglePipes().forEach((currentPipe) => {
      let currentStartPoint = this.getPointsFromStraightSinglePipe(currentPipe).startPoint.clone()
      let currentEndPoint = this.getPointsFromStraightSinglePipe(currentPipe).endPoint.clone()
      let isPartOfBiggerPipe = false

      out.forEach((wholePipe) => {
        if (currentStartPoint.clone().round().equals(wholePipe.endPoint.clone().round())) {
          // Nachbar rechts gefunden
          wholePipe.endPoint = currentEndPoint
          wholePipe.pipeCount++
          isPartOfBiggerPipe = true

          let potentialOtherRight = out.find(({ startPoint }) =>
            currentEndPoint.clone().round().equals(startPoint.clone().round())
          )

          // Potentieller nachbar für rechts suchen
          if (potentialOtherRight) {
            // Deleting other
            out = out.filter((pipe) => pipe != potentialOtherRight)

            // Extending
            wholePipe.endPoint = potentialOtherRight.endPoint
            wholePipe.pipeCount += potentialOtherRight.pipeCount

          }
        } else if (currentEndPoint.clone().round().equals(wholePipe.startPoint.clone().round())) {
          // Nachbar links gefunden
          wholePipe.startPoint = currentStartPoint
          wholePipe.pipeCount++
          isPartOfBiggerPipe = true

          let potentialOtherLeft = out.find(({ endPoint }) =>
            currentStartPoint.clone().round().equals(endPoint.clone().round())
          )

          if (potentialOtherLeft) {
            // Deleting other
            out = out.filter((pipe) => pipe != potentialOtherLeft)

            // Extending
            wholePipe.startPoint = potentialOtherLeft.startPoint
            wholePipe.pipeCount += potentialOtherLeft.pipeCount
          }
        }
      })

      if (!isPartOfBiggerPipe) {
        out.push({ startPoint: currentStartPoint, endPoint: currentEndPoint, pipeCount: 1 })
      }
    })

    return out
  }
}

export type IEntity = {
  id: number // Wie im backend
  modelId: string // Modelname
  uuid: string // UUID vom threejs object
  orientation: string
  threejsObject: THREE.Object3D
}

/**
 * Helper
 */
const turnLeft = (orientation: string): string => {
  switch (orientation) {
    case 'North':
      return 'West'
    case 'West':
      return 'South'
    case 'South':
      return 'East'
    case 'East':
      return 'North'
    default:
      return orientation
  }
}

const turnRight = (orientation: string): string => {
  switch (orientation) {
    case 'North':
      return 'East'
    case 'East':
      return 'South'
    case 'South':
      return 'West'
    case 'West':
      return 'North'
    default:
      return orientation
  }
}
