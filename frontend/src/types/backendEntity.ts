export interface IBackendEntity {
    x: number;
    y: number;
    z: number;
    path: string; 
    orientation: string;
}

export interface IBackendEntityPreview {
    path: string;
    icon: string;
    entityID: string;
}
export interface IFactoryCreate {
    name: string;
    password: string;
    width: number;
    depth: number;
    height: number
}