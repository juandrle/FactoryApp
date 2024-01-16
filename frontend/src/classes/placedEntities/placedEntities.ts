import * as THREE from "three"
/**
 * Represents the collection of all current entities in the factory
 */
export class PlacedEntities {
  private allEntities: IEntity[] = []

  public add = (entitie: IEntity) => this.allEntities.push(entitie)

  public getByUUID = (uuid: string): IEntity => {
    const entity = this.allEntities.find((e) => e.uuid === uuid);
    return entity !== undefined ? entity : {
        id: 0,
        modelId: "0",
        uuid: "0",
        orientation: "0",
        threejsObject: new THREE.Object3D()
    };
  }

  public rotateEntityByUUID = (uuid: string, dir: string) => {
    let entitie: IEntity = this.getByUUID(uuid);
    
    if(dir === "left") {
        entitie.orientation = turnLeft(entitie.orientation)
    } else {
        entitie.orientation = turnRight(entitie.orientation);
    }
  }

  public deleteByUUID = (uuid: string): void => {
    this.allEntities = this.allEntities.filter((entity) => entity.uuid !== uuid)
  }

  public getAllEntites = (): any => this.allEntities

  public getAllStraightPipes = () => this.allEntities.filter(entitie => entitie.modelId === "pipe_straight")
}

export type IEntity = {
  id: number // Wie im backend
  modelId: string // Modelname
  uuid: string // UUID vom threejs object
  orientation: string
  threejsObject: THREE.Object3D
}

const turnLeft = (orientation: string): string => {
    switch (orientation) {
        case "North":
            return "West";
        case "West":
            return "South";
        case "South":
            return "East";
        case "East":
            return "North";
        default:
            return orientation;
    }
};

const turnRight = (orientation: string): string => {
    switch (orientation) {
        case "North":
            return "East";
        case "East":
            return "South";
        case "South":
            return "West";
        case "West":
            return "North";
        default:
            return orientation;
    }
};
