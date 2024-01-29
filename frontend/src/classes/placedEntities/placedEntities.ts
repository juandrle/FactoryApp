import { getCenterPoint } from '@/utils/rotation/rotate'
import { drawBox, drawLine } from '@/utils/threeJS/helpFunctions';
import * as THREE from 'three'

/**
 * Represents the collection of all current entities in the factory
 */
export class PlacedEntities {
  private allEntities: IEntity[] = []
  private sceneRef: THREE.Scene;

  constructor(sceneRef: THREE.Scene) {
    this.sceneRef = sceneRef;
  }

  /**
   * Single Entity Operations
   */
  public add = (entity: IEntity) => this.allEntities.push(entity)

  public updateLastId = (id: number) => {
    const lastIndex = this.allEntities.length - 1
    if (lastIndex >= 0) {
      this.allEntities[lastIndex].id = id
    }
  }
  public pop = () => {
    return this.allEntities.pop()
  }

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
  public getAllStraightSinglePipes = (): IEntity[] => {
    return this.allEntities.filter((entity) => entity.modelId === 'pipe_straight')
  }

  public getAllCurvedPipes = (): {
    startPoint: THREE.Vector3
    endPoint: THREE.Vector3
  }[] => {
    const allCurvedPipes = this.allEntities.filter((entity) => entity.modelId === 'pipe_curved')
    const out : {
      startPoint: THREE.Vector3
      endPoint: THREE.Vector3
    }[] = []

    allCurvedPipes.forEach((mesh) => {
      out.push({
        startPoint: this.getPointsFromStraightSinglePipe(mesh).startPoint.clone(),
        endPoint: this.getPointsFromStraightSinglePipe(mesh).endPoint.clone()
      })
    })

    return out;
  }

  public getPointsFromStraightSinglePipe = (
    pipe: IEntity
  ): { startPoint: THREE.Vector3; endPoint: THREE.Vector3 } => {
    const pipeEntrance = pipe.threejsObject.children.find(
      (mesh) => mesh.name === 'pipe_entrance' || mesh.name === 'pipe_entrence'
    )
    const pipeExit = pipe.threejsObject.children.find((mesh) => mesh.name === 'pipe_exit')

    if (!pipeEntrance || !pipeExit) {
      console.log('didnt find pipe exit or entrance')
      return {
        startPoint: new THREE.Vector3(0, 0, 0),
        endPoint: new THREE.Vector3(0, 0, 0)
      }
    }

    return {
      startPoint: getCenterPoint(pipeEntrance).clone(),
      endPoint: getCenterPoint(pipeExit).clone()
    }
  }

  /**
   * Pipe System
   *
   * TODO: Auch rotierte Pipes also | start - end end - start|
   */

  public getAllPipes = (): {
    startPoint: THREE.Vector3
    endPoint: THREE.Vector3
    pipeCount: number
    type: string
  }[] => {
    return []
  }

  public getAllStraightPipes = (): {
    startPoint: THREE.Vector3
    endPoint: THREE.Vector3
    pipeCount: number
  }[] => {
    let out: { startPoint: THREE.Vector3; endPoint: THREE.Vector3; pipeCount: number }[] = []

    this.getAllStraightSinglePipes().forEach((currentPipe) => {
      let currentStartPoint = this.getPointsFromStraightSinglePipe(currentPipe).startPoint.clone()
      let currentEndPoint = this.getPointsFromStraightSinglePipe(currentPipe).endPoint.clone()
      let isPartOfBiggerPipe = false

      
      // console.log(currentEndPoint, "rounded to", roundVector(currentEndPoint))
      // console.log(currentStartPoint, "rounded to",roundVector(currentEndPoint))
      out.forEach((wholePipe) => {

        if (currentStartPoint.clone().round().equals(wholePipe.endPoint.clone().round())) {
          // Nachbar links gefunden
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
          // Nachbar rechts gefunden
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
