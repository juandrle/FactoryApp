import type {IBackendEntity, IBackendEntityPreview} from '@/types/backendEntity'
import type {IVector3} from '@/types/global'
import type {IPlaceRequest} from '@/types/placeRequest'
import * as THREE from 'three'
import type {GLTFLoader} from "three/addons/loaders/GLTFLoader.js";

export const getGrid = (gridID: number, scene: THREE.Scene) => {
    return scene.children.find((object: any) => object.name === `layer ${gridID}`)
}

export const getGridZ = (gridID: number, scene: THREE.Scene) => {
    return getGrid(gridID, scene).position.z
}

export const createGrids = (x: number, y: number, z: number, scene: THREE.Scene) => {
    // The Color settings of the First layer
    const colorFirst: THREE.Color = new THREE.Color('white')

    // The Color settings of all layers on top
    const colorRest: THREE.Color = new THREE.Color('#5d81cf')

    let zStart: number = 0
    for (let i: number = 0; i < z; i++) {
        // Create Grid
        const grid: THREE.GridHelper = new THREE.GridHelper(x, y)
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
        //scene.add(grid)

        //Create Layer
        const layer: THREE.Mesh = new THREE.Mesh(
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
    const textureLoader: THREE.TextureLoader = new THREE.TextureLoader()
    const texture = textureLoader.load(path)

    texture.minFilter = THREE.LinearFilter
    texture.magFilter = THREE.LinearFilter

    const material: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial({map: texture})

    const groundPane: THREE.PlaneGeometry = new THREE.PlaneGeometry(width, depth)

    const groundMesh: THREE.Mesh = new THREE.Mesh(groundPane, material)
    groundMesh.name = "buildingMesh"

    scene.add(groundMesh)
}

export const createRoofWithTextures = (
    path: string,
    scene: any,
    width: number,
    depth: number,
    height: number
) => {
    const textureLoader: THREE.TextureLoader = new THREE.TextureLoader()
    const texture = textureLoader.load(path)

    texture.minFilter = THREE.LinearFilter
    texture.magFilter = THREE.LinearFilter

    const material: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial({map: texture})

    const roofPane: THREE.PlaneGeometry = new THREE.PlaneGeometry(width, depth)

    const roofMesh: THREE.Mesh = new THREE.Mesh(roofPane, material)
    roofMesh.position.set(0, 0, height)
    roofMesh.rotateX(Math.PI)
    roofMesh.name = "buildingMesh"
    scene.add(roofMesh)
}

export const createWallsWithTexture = (
    path: string,
    scene: any,
    width: number,
    depth: number,
    height: number
) => {
    const textureLoader: THREE.TextureLoader = new THREE.TextureLoader()
    const texture = textureLoader.load(path)
    const material: THREE.StandardMaterial = new THREE.MeshStandardMaterial({map: texture})

    texture.minFilter = THREE.LinearFilter
    texture.magFilter = THREE.LinearFilter

    const widthWallPane: THREE.PlaneGeometry = new THREE.PlaneGeometry(width, height)
    widthWallPane.rotateX(Math.PI / 2)
    const depthWallPane: THREE.PlaneGeometry = new THREE.PlaneGeometry(depth, height)
    depthWallPane.rotateX(Math.PI / 2)
    depthWallPane.rotateZ(Math.PI / 2)

    const widthWallMesh1: THREE.Mesh = new THREE.Mesh(widthWallPane, material)
    widthWallMesh1.position.set(0, depth / 2, height / 2)
    const widthWallMesh2: THREE.Mesh = new THREE.Mesh(widthWallPane, material)
    widthWallMesh2.position.set(0, -depth / 2, height / 2)
    widthWallMesh2.rotateZ(Math.PI)
    const depthWallMesh1: THREE.Mesh = new THREE.Mesh(depthWallPane, material)
    depthWallMesh1.position.set(-width / 2, 0, height / 2)
    const depthWallMesh2: THREE.Mesh = new THREE.Mesh(depthWallPane, material)
    depthWallMesh2.position.set(width / 2, 0, height / 2)
    depthWallMesh2.rotateZ(Math.PI)
    widthWallMesh2.name = "buildingMesh"
    widthWallMesh1.name = "buildingMesh"
    depthWallMesh1.name = "buildingMesh"
    depthWallMesh2.name = "buildingMesh"
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

export const updateHighlight = (highlight: THREE.Group, activeLayer: number, intersections: any) => {
    // "Trim" intersctions to only geht intersection with the grid
    const intersection: any = getIntersectionWithGrid(activeLayer, intersections)

    // Now we got the intersection with the activ grid, and can set the highlight
    if (intersection) {
        // Get the exact position of the Intersection and Make it snapping with the grid (floor, addScalar)
        const pos: any = new THREE.Vector3().copy(intersection.point).floor()

        // Set the highlight
        highlight.position.set(pos.x, pos.y, intersection.object.position.z)
    }
}

export const updateHighlightModel: any = async (
    prevHighlight: any,
    url: string,
    scene: any,
    loader: any
) => {
    return await loader.loadAsync(url).then((model: any) => {

        // Neuen highlighter vorbereiten
        let newHighlight: any = model.scene;
        //TODO: TEMPORARY
        newHighlight.rotation.set(Math.PI / 2, 0, 0)
        newHighlight.position.set(prevHighlight.position)
        console.log(newHighlight.position)
        newHighlight.name = prevHighlight.name

        // Delete old highlight
        scene.children = scene.children.filter((object: any) => object.name !== prevHighlight.name)
        prevHighlight = null;

        // add new to scene
        scene.add(newHighlight)

        return newHighlight
    })
}

export const placeEntity = (loader: GLTFLoader, scene: THREE.Scene, pos: IVector3, path: string) => {
    var object: any
    loader.load(
        path,
        function (gltf: any) {
            object = gltf.scene
            object.position.set(pos.x, pos.y, pos.z)
            // TODO: TEMPORARY
            object.rotation.set(Math.PI / 2, 0, 0)
            object.name = 'entity'
            scene.add(gltf.scene)
        },
        undefined,
        function (error: any) {
            console.error(error)
        }
    )
}
export const replaceEntity = (pos: IVector3, currentObjectSelected: THREE.Group, lastObjectSelected: THREE.Group) => {
    currentObjectSelected.value.position.set(pos.x, pos.y, pos.z)
    lastObjectSelected.value = currentObjectSelected.value
}
export const selectionObject = (currentObjectSelected: THREE.Group, lastObjectSelected: THREE.Group, intersections: any) => {
    if (intersections.length > 0) {
        let filteredIntersections = intersections.filter((item: THREE.Mesh) => (!item.object.name.includes('layer') &&
            !item.object.name.includes('building') && !item.object.type.includes('Axes') && !item.object.type.includes('Scene')))

        if (filteredIntersections.length < 1) return
        const closestIntersection = filteredIntersections.reduce((min: any, obj: any) => {
            return obj.distance < min.distance ? obj : min;
        }, {distance: Infinity})
        currentObjectSelected.value = closestIntersection.object.parent
        currentObjectSelected.value.children.forEach((element: any) => {
            switch (element.type) {
                case 'Mesh':
                    element.material.emissive.setRGB(0,0.1,0)
                    break
                case 'Group':
                    element.children.forEach((ele: any) => ele.material.emissive.setRGB(0,0.1,0))
                    break
                default:
                    break
            }
        })

        if (lastObjectSelected.value && lastObjectSelected.value != currentObjectSelected.value)
            lastObjectSelected.value.children.forEach((element: THREE.Mesh) => {
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
        lastObjectSelected.value = currentObjectSelected.value
    }
}
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

export const placeRequest = (placeRequest: IPlaceRequest) => {
    // console.log(placeRequest, "... würde jetzt ans backend gesendet werden")
    return true
}


