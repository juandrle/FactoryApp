export interface IVector3 {
    x: number,
    y: number,
    z: number
}

export type ISizes = {
    width: number,
    height: number,
    ratio: number,
}

export type ICameraInfo = {
    position: IVector3,
    lookAt: IVector3,
    up: IVector3
}
