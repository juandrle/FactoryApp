/**
 * Represents the collection of all current entities in the factory
 */
export class PlacedEntities {
  private allEntities: IEntity[] = []

  public add = (entitie: IEntity) => this.allEntities.push(entitie)

  public getByUUID = (uuid: string): IEntity | undefined =>
    this.allEntities.find((entity) => entity.uuid === uuid)

  public deleteByUUID = (uuid: string): void => {
    this.allEntities = this.allEntities.filter((entity) => entity.uuid !== uuid)
  }

  public getAllEntites = (): any => this.allEntities
}

export type IEntity = {
  id: number // Wie im backend
  modelId: string // Modelname
  uuid: string // UUID vom threejs object
  orientation: string
}
