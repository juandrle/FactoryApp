import type { IVector3 } from '@/types/global'
import * as THREE from 'three'

/**
 * Highlights an object with a specified color or removes the highlight.
 * If isColor is false, the object and its children will be unhighlighted.
 * If isColor is true and a color is provided, the object and its children will be highlighted with the specified color.
 * If isColor is true and no color is provided, the object and its children will be highlighted with a default color (green).
 *
 * @param {THREE.Object3D} object - The object to highlight.
 * @param {boolean} isColor - Determines whether to highlight the object or remove the highlight.
 * @param {string} [color] - The color to use for highlighting the object. Optional, defaults to green if not provided.
 *
 * @returns {void}
 */
export const highlightObjectWithColor = (
  object: THREE.Object3D,
  isColor: boolean,
  color?: string
): void => {
  let currColor: { r: number; g: number; b: number }
  switch (color) {
    case 'green':
      currColor = { r: 0, g: 0.1, b: 0 }
      break
    case 'red':
      currColor = { r: 0.1, g: 0, b: 0 }
      break
    default:
      currColor = { r: 0, g: 0.1, b: 0 }
      break
  }

  if (!isColor)
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
          element.material.emissive.setRGB(currColor.r, currColor.g, currColor.b)
          break
        case 'Group':
          element.children.forEach((ele: any) =>
            ele.material.emissive.setRGB(currColor.r, currColor.g, currColor.b)
          )
          break
        default:
          break
      }
    })
}

export const replaceEntity = (
  pos: IVector3,
  currentObjectSelected: THREE.Object3D,
  lastObjectSelected: THREE.Object3D
) => {
  currentObjectSelected.position.set(pos.x, pos.y, pos.z)
  lastObjectSelected = currentObjectSelected
  highlightObjectWithColor(currentObjectSelected, false)
}

/**
 * Loads and places an entity in a scene.
 *
 * @param {any} loader - The loader used to load the entity.
 * @param {THREE.Scene} scene - The scene where the entity will be added.
 * @param {IVector3} pos - The position of the entity in the scene.
 * @param {string} path - The path to the entity's file.
 * @returns {Promise<string>} A promise that resolves with the UUID of the placed entity, or rejects with an error.
 */
export const placeEntity = (
  loader: any,
  scene: THREE.Scene,
  pos: IVector3,
  path: string
): Promise<THREE.Object3D> => {
  return new Promise((resolve, reject) => {
    let object: THREE.Object3D
    loader.load(
      path,
      function (gltf: any) {
        object = gltf.scene
        object.position.set(pos.x, pos.y, pos.z)
        object.rotation.set(0, 0, 0)
        object.name = 'entity'
        scene.add(gltf.scene)

        // Resolve the promise with the UUID
        resolve(object)
      },
      undefined,
      function (error: undefined) {
        // Reject the promise with the error
        reject(error)
      }
    )
  })
}

export const loadEntitie = (loader: any, path: string): Promise<THREE.Object3D> => {
  return new Promise((resolve, reject) => {
    loader.load(
      path,
      function (gltf: any) {
        resolve(gltf.scene)
      },
      undefined,
      function (error: undefined) {
        // Reject the promise with the error
        reject(error)
      }
    )
  })
}

/**
 * Sets the transparency of an object and its child elements.
 * @param {boolean} makeTransparent - Indicates whether to make the object transparent or not.
 * @param {THREE.Object3D} object - The object to apply transparency to.
 */
export const makeObjectTransparent = (makeTransparent: boolean, object: THREE.Object3D) => {
  if (!makeTransparent)
    object.children.forEach((element: any) => {
      switch (element.type) {
        case 'Mesh':
          element.visible = true
          break
        case 'Group':
          element.children.forEach((ele: any) => (ele.visible = true))
          break
        default:
          break
      }
    })
  else
    object.children.forEach((element: any) => {
      switch (element.type) {
        case 'Mesh':
          element.visible = false
          break
        case 'Group':
          element.children.forEach((ele: any) => (ele.visible = false))

          break
        default:
          break
      }
    })
}
