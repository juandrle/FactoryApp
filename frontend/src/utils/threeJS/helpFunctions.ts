import * as THREE from 'three'
import type {Intersection, Object3DEventMap} from "three";
import {Object3D} from "three";
import {highlightObjectWithColor} from '@/utils/threeJS/entityManipulation'

let existingGrid: THREE.Mesh[] = []

/**
 * Creates a grid of layers in a three.js scene.
 *
 * @param {number} x - The width of each layer.
 * @param {number} y - The height of each layer.
 * @param {number} z - The number of layers to create.
 * @param {THREE.Scene} scene - The three.js scene to add the layers to.
 */
export const createGrids = (x: number, y: number, z: number, scene: THREE.Scene) => {
    let zStart: number = 0
    if (existingGrid.length > 0) {
        existingGrid.forEach((ele: any) => {
            scene.remove(ele)
            existingGrid.splice(ele)
        })
    }
    for (let i: number = 0; i < z; i++) {
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
        existingGrid[i] = layer
        scene.add(layer)
        // Calc new position for next grid
        zStart++
    }
}

/**
 * Create a plane with textures and add it to the scene.
 *
 * @param {string} texturePath - The path to the texture image.
 * @param {THREE.Scene} scene - The three.js scene to add the plane to.
 * @param {number} width - The width of the plane.
 * @param {number} depth - The depth of the plane.
 * @param {number} height - The height of the plane (optional, only used if isRoof is true).
 * @param {boolean} isRoof - Whether the plane should be positioned as a roof (optional).
 */
export const createPlaneWithTextures = (
    texturePath: string,
    scene: THREE.Scene,
    width: number,
    depth: number,
    height: number,
    isRoof?: boolean
) => {
    const textureLoader = new THREE.TextureLoader();
    const texture = textureLoader.load(texturePath);

    texture.minFilter = THREE.LinearFilter;
    texture.magFilter = THREE.LinearFilter;

    const material = new THREE.MeshStandardMaterial({map: texture});
    const planeGeometry = new THREE.PlaneGeometry(width, depth);
    const planeMesh = new THREE.Mesh(planeGeometry, material);

    if (isRoof) {
        planeMesh.position.set(0, 0, height);
        planeMesh.rotateX(Math.PI);
    }

    planeMesh.name = "buildingMesh";
    scene.add(planeMesh);
};
/**
 * Creates walls with a specified texture and adds them to the scene.
 *
 * @param {string} texturePath - The path to the texture image.
 * @param {THREE.Scene} scene - The THREE.Scene object to add the walls to.
 * @param {number} width - The width of the walls.
 * @param {number} depth - The depth of the walls.
 * @param {number} height - The height of the walls.
 */
export const createWallsWithTexture = (
    texturePath: string,
    scene: THREE.Scene,
    width: number,
    depth: number,
    height: number
) => {
    const textureLoader: THREE.TextureLoader = new THREE.TextureLoader()
    const texture = textureLoader.load(texturePath)
    const material: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial({map: texture})

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
export const getIntersectionWithGrid = (gridID: number, intersections: Intersection<Object3D<Object3DEventMap>>[]) => {
    return (
        intersections.find((intersection: any) => intersection.object.name === `layer ${gridID}`) ||
        false
    )
}
/**
 * Moves the highlight to the intersection point with the grid on the active layer.
 *
 * @param {any} highlight - The object to highlight.
 * @param {number} activeLayer - The active layer.
 * @param {Intersection<Object3D<Object3DEventMap>>[]} intersections - The intersections with the grid.
 * @returns {boolean} - A boolean indicating whether the highlight was set on an object or not.
 */
export const moveHighlight = (highlight: any, activeLayer: number, intersections: Intersection<Object3D<Object3DEventMap>>[]): boolean => {
    // "Trim" intersections to only geht intersection with the grid
    const intersection = getIntersectionWithGrid(activeLayer, intersections)

    // Now we got the intersection with the active grid, and can set the highlight
    if (intersection) {
        // Get the exact position of the Intersection and Make it snapping with the grid (floor, addScalar)
        const pos: THREE.Vector3 = new THREE.Vector3().copy(intersection.point).floor()
        // Set the highlight
        highlight.position.set(pos.x, pos.y, intersection.object.position.z)

        const highlightBox = new THREE.Box3().setFromObject(highlight);
        const contractionAmount = 0.3; // Adjust this value as needed
        highlightBox.expandByScalar(-contractionAmount);
        const sceneObjects = highlight.parent.children
        let filteredSceneObjects = sceneObjects.filter((item: any) => (item.name.includes('entity')))
        for (const obj of filteredSceneObjects) {
            const objBox = new THREE.Box3().setFromObject(obj);
            if (highlightBox.intersectsBox(objBox)) {
                highlightObjectWithColor(highlight, true, 'red')
                return true
            } else {
                highlightObjectWithColor(highlight, false)
            }
        }
    }
    return false
}

/**
 * Updates the highlight model in the scene.
 *
 * @param {THREE.Object3D} prevHighlight - The previous highlight model to be replaced.
 * @param {string} url - The URL from which to load the new highlight model.
 * @param {THREE.Scene} scene - The scene where the highlight model should be updated.
 * @param {any} loader - The GLTFLoader used to load the highlight model.
 * @returns {Promise<THREE.Object3D>} - A promise that resolves with the updated highlight model.
 */
export const updateHighlightModel = async (
    prevHighlight: THREE.Object3D,
    url: string,
    scene: THREE.Scene,
    loader: any
): Promise<THREE.Object3D>  => {
    return await loader.loadAsync(url).then((model: any) => {

        // Neuen highlighter vorbereiten
        let newHighlight: any = model.scene;
        // temporary rotation fix
        // newHighlight.rotation.set(Math.PI / 2, 0, 0)
        newHighlight.rotation.set(0, 0, 0)
        if (prevHighlight) {
            newHighlight.position.set(prevHighlight.position)
            newHighlight.name = prevHighlight.name
        } else {
            newHighlight.position.set(0, 0, 0)
            newHighlight.name = "highlight"
        }

        // Delete old highlight
        scene.remove(prevHighlight);

        // add new to scene
        scene.add(newHighlight)

        return newHighlight
    })
}


/**
 * Updates the selection of objects based on the given intersections.
 *
 * @param {THREE.Object3D} currentObjectSelected - The currently selected object.
 * @param {THREE.Object3D} lastObjectSelected - The previously selected object.
 * @param {any[]} intersections - The array of intersections.
 * @returns {object|boolean} - An object containing information about the selection, or false if no valid intersections were found.
 */
export const selectionObject = (currentObjectSelected: THREE.Object3D, lastObjectSelected: THREE.Object3D, intersections: any) => {
    if (intersections.length > 0) {
        let filteredIntersections = intersections.filter((item: any) => (!item.object.name.includes('layer') &&
            !item.object.name.includes('building') && !item.object.type.includes('Axes') && !item.object.type.includes('Scene')))
        if (filteredIntersections.length < 1) return false
        const closestIntersection = filteredIntersections.reduce((min: any, obj: any) => {
            return obj.distance < min.distance ? obj : min;
        }, {distance: Infinity})
        currentObjectSelected = closestIntersection.object.parent
        highlightObjectWithColor(currentObjectSelected, true)

        if (lastObjectSelected && lastObjectSelected != currentObjectSelected)
            highlightObjectWithColor(lastObjectSelected, false)
        lastObjectSelected = currentObjectSelected
        return {
            worked: true as boolean,
            currObj: currentObjectSelected as THREE.Group,
            lastObj: lastObjectSelected as THREE.Group
        }
    }
}

export const deepCloneObject = (object: any) => {
    const clone = object.clone();

    clone.children = [];

    object.children.forEach((child: any) => {
        const childClone = deepCloneObject(child);
        clone.add(childClone);
    });

    if (clone.material) {
        clone.material = clone.material.clone();
    }
    if (clone.geometry) {
        clone.geometry = clone.geometry.clone();
    }
    return clone;
}

/**
 * Creates a room in a given scene with specified dimensions and textures.
 *
 * @param {number} x - The x dimension of the room.
 * @param {number} y - The y dimension of the room.
 * @param {number} z - The z dimension of the room.
 * @param {THREE.Scene} scene - The scene to which the room will be added.
 *
 * @returns {void}
 */
export const createRoom = (x: number, y: number, z: number, scene: THREE.Scene): void => {
    // Add Grid
    createGrids(x, y, z, scene)
    // creating roomtextures
    createPlaneWithTextures('factoryGround.jpeg', scene, x, y, z, false)
    createPlaneWithTextures('factoryRoof.jpeg', scene, x, y, z, true)
    createWallsWithTexture('factoryWall.jpg', scene, x, y, z)
}