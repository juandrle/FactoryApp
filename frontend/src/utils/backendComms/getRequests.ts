import {GLTFLoader} from "three/examples/jsm/loaders/GLTFLoader";
import {IBackendEntity, IBackendEntityPreview} from "@/types/backendEntity";
import * as THREE from 'three';
import {placeEntity} from "@/utils/threeJS/helpFunctions";
import {backendUrl} from "@/utils/config/config.js"

export const loadFactory = (scene: THREE.Scene, loader: GLTFLoader, factory_id: string) => {
    fetch('/mock/backend/mockBackendLoadFactoryResponse.json').then((res) =>
        res.json().then((backendEntitys: IBackendEntity[]) => {
            backendEntitys.forEach((backendEntity) => {
                placeEntity(
                    loader,
                    scene,
                    {
                        x: backendEntity.x,
                        y: backendEntity.y,
                        z: backendEntity.z
                    },
                    backendEntity.path
                )
            })
        })
    )
}
export const getAllEntitys: () => Promise<IBackendEntityPreview[]> = async () => {
    return fetch(backendUrl + "/api/entity/getAll").then((res) => res.json())
}

export const getAllEntitysInFactory: () => Promise<IBackendEntity[]> = async () => {
    return fetch(backendUrl + "/api/entity/getAll").then((res) => res.json())
}