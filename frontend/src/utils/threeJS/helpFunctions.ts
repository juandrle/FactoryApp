import type {IVector3} from "@/types/global"
import * as THREE from 'three'
import type {Intersection, Object3DEventMap} from "three";
import {Object3D} from "three";

import {highlightObjectWithColor} from '@/utils/threeJS/entityManipulation'

let existingGrid: THREE.Mesh[] = []
export const getGrid = (gridID: number, scene: THREE.Scene) => {
    return scene.children.find((object: any) => object.name === `layer ${gridID}`)
}
export const getGridZ = (gridID: number, scene: THREE.Scene) => {
    //return getGrid(gridID, scene).position.z
}
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
export const createPlaneWithTextures = (
    texturePath: string,
    scene: THREE.Scene,
    width: number,
    depth: number,
    height: number,
    createRoof: boolean
) => {
    const textureLoader = new THREE.TextureLoader();
    const texture = textureLoader.load(texturePath);

    texture.minFilter = THREE.LinearFilter;
    texture.magFilter = THREE.LinearFilter;

    const material = new THREE.MeshStandardMaterial({map: texture});
    const planeGeometry = new THREE.PlaneGeometry(width, depth);
    const planeMesh = new THREE.Mesh(planeGeometry, material);

    if (createRoof) {
        planeMesh.position.set(0, 0, height);
        planeMesh.rotateX(Math.PI);
    }

    planeMesh.name = "buildingMesh";
    scene.add(planeMesh);
};
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
export const moveHighlight = (highlight: any, activeLayer: number, intersections: Intersection<Object3D<Object3DEventMap>>[]) => {
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
        console.log(filteredSceneObjects)
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
export const updateHighlightModel: any = async (
    prevHighlight: any,
    url: string,
    scene: THREE.Scene,
    loader: any
) => {
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


export const selectionObject = (currentObjectSelected: THREE.Group, lastObjectSelected: THREE.Group, intersections: any) => {
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

export const createRoom = (x: number, y: number, z: number, scene: THREE.Scene) => {
    // Add Grid
    createGrids(x, y, z, scene)
    // creating roomtextures
    createPlaneWithTextures('factoryGround.jpeg', scene, x, y, z, false)
    createPlaneWithTextures('factoryRoof.jpeg', scene, x, y, z, true)
    createWallsWithTexture('factoryWall.jpg', scene, x, y, z)
}