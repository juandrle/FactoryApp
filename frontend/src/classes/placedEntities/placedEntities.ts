import * as THREE from 'three'
/**
 * Represents the collection of all current entities in the factory
 */
export class PlacedEntities {
  private allEntities: IEntity[] = []

  public add = (entitie: IEntity) => this.allEntities.push(entitie)

  public getByUUID = (uuid: string): IEntity => {
    const entity = this.allEntities.find((e) => e.uuid === uuid)
    return entity !== undefined
      ? entity
      : {
          id: 0,
          modelId: '0',
          uuid: '0',
          orientation: '0',
          threejsObject: new THREE.Object3D()
        }
  }

  public rotateEntityByUUID = (uuid: string, dir: string) => {
    let entitie: IEntity = this.getByUUID(uuid)

    if (dir === 'left') {
      entitie.orientation = turnLeft(entitie.orientation)
    } else {
      entitie.orientation = turnRight(entitie.orientation)
    }
  }

  public deleteByUUID = (uuid: string): void => {
    this.allEntities = this.allEntities.filter((entity) => entity.uuid !== uuid)
  }

  public getAllEntites = (): any => this.allEntities

  public getAllStraightPipes = () =>
    this.allEntities.filter((entitie) => entitie.modelId === 'pipe_straight')

  public getPointsFromPipe = (
    pipe: IEntity
  ): { startPoint: THREE.Vector3; endPoint: THREE.Vector3 } => {
    const startPointLocal: THREE.Vector3 = new THREE.Vector3().copy(
      pipe.threejsObject.children.find((mesh) => mesh.name === 'pipe_entrance')?.geometry
        .boundingSphere.center
    )
    const endPointLocal: THREE.Vector3 = new THREE.Vector3().copy(
      pipe.threejsObject.children.find((mesh) => mesh.name === 'pipe_exit')?.geometry.boundingSphere
        .center
    )
    const pipePositionWorld: THREE.Vector3 = pipe.threejsObject.position
    const startPoint = new THREE.Vector3().copy(pipePositionWorld).add(startPointLocal)
    const endPoint = new THREE.Vector3().copy(pipePositionWorld).add(endPointLocal)

    return {
      startPoint: startPoint,
      endPoint: endPoint
    }
  }

  public getAllWholePipesPoints = (): {
    startPoint: THREE.Vector3
    endPoint: THREE.Vector3
    pipeCount: number
  }[] => {
    let out: { startPoint: THREE.Vector3; endPoint: THREE.Vector3; pipeCount: number }[] = []

    this.getAllStraightPipes().forEach((currentPipe) => {
      let currentStartPoint = this.getPointsFromPipe(currentPipe).startPoint.clone()
      let currentEndPoint = this.getPointsFromPipe(currentPipe).endPoint.clone()
      let isPartOfBiggerPipe = false

      out.forEach((toCheckPipe) => {
        // Endpoints werden geupdatet
        if (currentStartPoint.clone().round().equals(toCheckPipe.endPoint.clone().round())) {
          toCheckPipe.endPoint = currentEndPoint
          toCheckPipe.pipeCount++
          isPartOfBiggerPipe = true
        } else if (currentEndPoint.clone().round().equals(toCheckPipe.startPoint.clone().round())) {
          toCheckPipe.startPoint = currentStartPoint
          toCheckPipe.pipeCount++
          isPartOfBiggerPipe = true
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
