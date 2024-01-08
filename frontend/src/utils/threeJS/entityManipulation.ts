import type { IVector3 } from '@/types/global'
import * as THREE from 'three'

export const highlightObjectWithColor = (object: THREE.Group, color: boolean) => {
  if (!color)
      object.children.forEach((element: any) => {
          switch (element.type) {
              case 'Mesh':
                  element.material.emissive.set(0x000000)
                  break
              case 'Group':
                  element.children.forEach((ele: any) => ele.material.emissive.set(0x000000))
                  break
              default:
                  break
          }
      })
  else
      object.children.forEach((element: any) => {
          switch (element.type) {
              case 'Mesh':
                  element.material.emissive.setRGB(0, 0.1, 0)
                  break
              case 'Group':
                  element.children.forEach((ele: any) => ele.material.emissive.setRGB(0, 0.1, 0))
                  break
              default:
                  break
          }
      })
}

export const replaceEntity = (
  pos: IVector3,
  currentObjectSelected: THREE.Group,
  lastObjectSelected: THREE.Group
) => {
  currentObjectSelected.position.set(pos.x, pos.y, pos.z)
  lastObjectSelected = currentObjectSelected
  highlightObjectWithColor(currentObjectSelected, false)
}

export const placeEntity = (loader: any, scene: THREE.Scene, pos: IVector3, path: string): Promise<string> => {
    return new Promise((resolve, reject) => {
        var object: any;
        loader.load(
            path,
            function (gltf: any) {
                object = gltf.scene;
                object.position.set(pos.x, pos.y, pos.z);
                object.rotation.set(0, 0, 0);
                object.name = 'entity';
                scene.add(gltf.scene);

                // Resolve the promise with the UUID
                resolve(object.uuid);
            },
            undefined,
            function (error: any) {
                // Reject the promise with the error
                reject(error);
            }
        );
    });
};
