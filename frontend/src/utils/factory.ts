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
        zStart += 25
    }
}
export const setGroundTexture = (path: string, scene: any) => {
  const textureLoader = new THREE.TextureLoader();
  const texture = textureLoader.load(path);
  const material = new THREE.MeshStandardMaterial({map: texture});
  const geometry = new THREE.PlaneGeometry(1000,1000);
  const mesh = new THREE.Mesh(geometry, material);

  scene.add(mesh);
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
        const pos: any = new THREE.Vector3().copy(intersection.point).floor().addScalar(0.5)

        // Set the highlight
        object.position.set(pos.x, pos.y, intersection.object.position.z + object.geometry.parameters.height / 2)

    }
}

export const placeEntity = (loader: any, scene: any, pos: any, path: string) => {
    var object: any
    loader.load(
        path,
        function (gltf: any) {
            object = gltf.scene
            object.position.set(pos.x, pos.y, pos.z)
            object.scale.set(25, 25, 25)

            scene.add(gltf.scene)
        },
        undefined,
        function (error: any) {
            console.error(error)
        }
    )
}
