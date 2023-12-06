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