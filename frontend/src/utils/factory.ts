import type { IBackendEntity } from '@/types/backendEntity'
import type { IVector3 } from '@/types/global'
import type { IPlaceRequest } from '@/types/placeRequest'
import * as THREE from 'three'

export const getGrid = (gridID: number, scene: any) => {
  return scene.children.find((object: any) => object.name === `layer ${gridID}`)
}

export const getGridZ = (gridID: number, scene: any) => {
  return getGrid(gridID, scene).position.z
}

export const createGrids = (x: number, y: number, z: number, scene: any) => {
  // The Color settings of the First layer
  const colorFirst: any = new THREE.Color('white')

  // The Color settings of all layers on top
  const colorRest: any = new THREE.Color('#5d81cf')

  let zStart: number = 0
  for (let i: number = 0; i < z; i++) {
    // Create Grid
    const grid: any = new THREE.GridHelper(x, y)
    grid.rotateX(Math.PI / 2)

    // Set color based on the layer
    if (i === 0) {
      grid.material.color.set(colorFirst)
    } else {
      grid.material.color.set(colorRest)
    }
    grid.visible = false

    // Position Grid
    grid.position.z = zStart

    // Set name
    grid.name = `grid ${i}`

    // Add to scene
    scene.add(grid)

    //Create Layer
    const layer: any = new THREE.Mesh(
      new THREE.PlaneGeometry(x, y),
      new THREE.MeshBasicMaterial({
        side: THREE.DoubleSide,
        visible: false
      })
    )

    // Position Grid
    layer.position.z = zStart

    //Set name
    layer.name = `layer ${i}`

    // Add to scene
    scene.add(layer)

    // Calc new position for next grid
    zStart += 1
  }
}

export const createGroundWithTextures = (
  path: string,
  scene: any,
  width: number,
  depth: number
) => {
  const textureLoader = new THREE.TextureLoader()
  const texture = textureLoader.load(path)

  texture.minFilter = THREE.LinearFilter
  texture.magFilter = THREE.LinearFilter

  const material = new THREE.MeshStandardMaterial({ map: texture })

  const groundPane = new THREE.PlaneGeometry(width, depth)

  const groundMesh = new THREE.Mesh(groundPane, material)

  scene.add(groundMesh)
}

export const createRoofWithTextures = (
  path: string,
  scene: any,
  width: number,
  depth: number,
  height: number
) => {
  const textureLoader = new THREE.TextureLoader()
  const texture = textureLoader.load(path)

  texture.minFilter = THREE.LinearFilter
  texture.magFilter = THREE.LinearFilter

  const material = new THREE.MeshStandardMaterial({ map: texture })

  const roofPane = new THREE.PlaneGeometry(width, depth)

  const roofMesh = new THREE.Mesh(roofPane, material)
  roofMesh.position.set(0, 0, height)
  roofMesh.rotateX(Math.PI)

  scene.add(roofMesh)
}

export const createWallsWithTexture = (
  path: string,
  scene: any,
  width: number,
  depth: number,
  height: number
) => {
  const textureLoader = new THREE.TextureLoader()
  const texture = textureLoader.load(path)
  const material = new THREE.MeshStandardMaterial({ map: texture })

  texture.minFilter = THREE.LinearFilter
  texture.magFilter = THREE.LinearFilter

  const widthWallPane = new THREE.PlaneGeometry(width, height)
  widthWallPane.rotateX(Math.PI / 2)
  const depthWallPane = new THREE.PlaneGeometry(depth, height)
  depthWallPane.rotateX(Math.PI / 2)
  depthWallPane.rotateZ(Math.PI / 2)

  const widthWallMesh1 = new THREE.Mesh(widthWallPane, material)
  widthWallMesh1.position.set(0, depth / 2, height / 2)
  const widthWallMesh2 = new THREE.Mesh(widthWallPane, material)
  widthWallMesh2.position.set(0, -depth / 2, height / 2)
  widthWallMesh2.rotateZ(Math.PI)
  const depthWallMesh1 = new THREE.Mesh(depthWallPane, material)
  depthWallMesh1.position.set(-width / 2, 0, height / 2)
  const depthWallMesh2 = new THREE.Mesh(depthWallPane, material)
  depthWallMesh2.position.set(width / 2, 0, height / 2)
  depthWallMesh2.rotateZ(Math.PI)
  scene.add(widthWallMesh2)
  scene.add(widthWallMesh1)
  scene.add(depthWallMesh1)
  scene.add(depthWallMesh2)
}

export const getIntersectionWithGrid = (gridID: number, intersections: any) => {
  return (
    intersections.find((intersection: any) => intersection.object.name === `layer ${gridID}`) ||
    false
  )
}

export const updateHighlight = (object: any, activeLayer: number, intersections: any) => {
  // "Trim" intersctions to only geht intersection with the grid
  const intersection: any = getIntersectionWithGrid(activeLayer, intersections)

  // Now we got the intersection with the activ grid, and can set the highlight
  if (intersection) {
    // Get the exact position of the Intersection and Make it snapping with the grid (floor, addScalar)
    const pos: any = new THREE.Vector3().copy(intersection.point).floor()

    // Set the highlight
    object.position.set(
      pos.x,
      pos.y,
      intersection.object.position.z
    )
  }
}

export const placeEntity = (loader: any, scene: any, pos: IVector3, path: string) => {
  var object: any
  loader.load(
    path,
    function (gltf: any) {
      object = gltf.scene
      object.position.set(pos.x, pos.y, pos.z)
      object.name = 'entity'
      scene.add(gltf.scene)
    },
    undefined,
    function (error: any) {
      console.error(error)
    }
  )
}

export const loadFactory = (scene: any, loader: any, factory_id: string) => {
  fetch('/mock/backend/mockBackendResponse.json').then((res) =>
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

export const placeRequest = (placeRequest: IPlaceRequest) => {}
