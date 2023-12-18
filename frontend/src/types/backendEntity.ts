export interface IBackendEntity {
    x: number;
    y: number;
    z: number;
    path: string; 
    orientation: string;
    factoryid: string;
    id:number;
}

export interface IBackendEntityPreview {
    modelFile: string;
    icon: string;
    id: string;
}
export interface IFactoryCreate {
    name: string;
    password: string;
    width: number;
    depth: number;
    height: number
}
export interface IFactoryDelete {
    id: number,
    element: any
}