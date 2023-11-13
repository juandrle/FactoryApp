import * as THREE from 'three'

export const getGrid = (gridID, scene) => {
  return scene.children.find((object) => object.name === `layer ${gridID}`)
}

export const getGridZ = (gridID, scene) => {
  return getGrid(gridID, scene).position.z
}

export const createGrids = (x, y, z, scene) => {
  let zStart = (0.5 * z - 0.5) * -1
  for (let i = 0; i < z; i++) {
    // Create Grid
    const grid = new THREE.GridHelper(x, y)
    grid.rotateX(Math.PI / 2)

    // Position Grid
    grid.position.z = zStart

    // Set name
    grid.name = `grid ${i}`

    // Add to scene
    scene.add(grid)

    //Create Layer
    const layer = new THREE.Mesh(
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

export const getIntersectionWithGrid = (gridID, intersections) => {
  return (
    intersections.find((intersection) => intersection.object.name === `layer ${gridID}`) || false
  )
}

export const updateHighlight = (highlight, activeLayer, intersections) => {

  // "Trim" intersctions to only geht intersection with the grid 
  const intersection = getIntersectionWithGrid(activeLayer, intersections)
  
  // Now we got the intersection with the activ grid, and can set the highlight
  if (intersection) {

    // Get the exact position of the Intersection and Make it snapping with the grid (floor, addScalar)
    const pos = new THREE.Vector3().copy(intersection.point).floor().addScalar(0.5)

    // Set the highlight
    highlight.position.set(pos.x, pos.y, intersection.object.position.z + 0.5)
  }
}
