import {GLTFLoader} from "three/examples/jsm/loaders/GLTFLoader";
import {IBackendEntity, IBackendEntityPreview} from "@/types/backendEntity";
import * as THREE from 'three';
import {placeEntity} from "@/utils/threeJS/helpFunctions";

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
    // Simuliere einen Verzögerung von 2 Sekunden (2000 Millisekunden)
    await new Promise((resolve) => setTimeout(resolve, 500))

    // Führe die tatsächliche Anfrage aus und gib die Daten zurück
    return fetch('/mock/backend/mockBackendGetAllEntitys.json').then((res) => res.json())
}