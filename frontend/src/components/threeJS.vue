<script setup>
import { ref, onMounted } from 'vue';
import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';


/*******************************/
/*********** COFIG *************/
/*******************************/

const BLOCK_SIZE = 1;
const ACTIVE_LAYER = 2; // possible between 0 and (GRID.z -1)
const GRID = {
  x: 20,
  y: 20,
  z: 3
};

/*******************************/
/****** FUNCTIONAL UTILS *******/
/*******************************/

const createGrids = (x, y, z, scene) => {
  let zStart = ((BLOCK_SIZE / 2 * z) - BLOCK_SIZE / 2) * -1
  for (let i = 0; i < z; i++) {
    // Create Grid
    const grid = new THREE.GridHelper(x, y);
    grid.rotateX(Math.PI / 2);

    // Position Grid
    grid.position.z = zStart;

    // Set name
    grid.name = `layer ${i}`

    // Add to scene
    scene.add(grid)

    // Calc new position for next grid
    zStart += BLOCK_SIZE
  }
}

const getIntersectionsMouse = (mouseMoveEvent) => {
  const raycaster = new THREE.Raycaster();
  raycaster.setFromCamera(new THREE.Vector2(
    (event.clientX / window.innerWidth) * 2 - 1,
    - (event.clientY / window.innerHeight) * 2 + 1
  ), camera);
  return raycaster.intersectObjects(scene.children);
}

const getGrid = (gridID, scene) => scene.children.find((object) => object.name === `layer ${gridID}`)

const getGridZ = (gridID, scene) => getGrid(gridID, scene).position.z

const getIntersectionWithGrid = (gridID, intersections) => {
  return intersections.find((intersection) => intersection.object.name === `layer ${gridID}`) || false
}


/****************************/
/******* SETUP - START ******/
/****************************/
const target = ref();

// Get Screen size
let sizes = {
  width: window.innerWidth,
  height: window.innerHeight,
  ratio: window.innerWidth / window.innerHeight
}

// Add Scene & Renderer
const scene = new THREE.Scene();
const renderer = new THREE.WebGLRenderer();
renderer.setSize(sizes.width, sizes.height);

// Camera
const camera = new THREE.PerspectiveCamera(50, sizes.ratio);
camera.position.set(40, -15, 15);
camera.up.set(0, 0, 1);
camera.lookAt(0, 0, 0);

// OrbitControlls 
const controls = new OrbitControls(camera, renderer.domElement);

/********************/
/******* INIT *******/
/********************/

// Add Grid
createGrids(GRID.x, GRID.y, GRID.z, scene)

// Add axis helper
const axesHelper = new THREE.AxesHelper(10);
scene.add(axesHelper);

const geometry = new THREE.BoxGeometry(1, 1, 1);
const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
const cube = new THREE.Mesh(geometry, material);
cube.name = "highlight";
scene.add(cube);


/********************/
/**** CALLBACKS *****/
/********************/

// onMouseMove
addEventListener("mousemove", (event) => {

  const intersection = getIntersectionWithGrid(ACTIVE_LAYER, getIntersectionsMouse(event))
  if (intersection) {
    const pos = new THREE.Vector3().copy(intersection.point).floor().addScalar(0.5)
    cube.position.x = pos.x
    cube.position.y = pos.y
    cube.position.z = getGridZ(ACTIVE_LAYER, scene) + BLOCK_SIZE / 2 // Sollte nicht nötig sein
  }

});

// onRezise
window.addEventListener("resize", () => {

  // Variables
  sizes = {
    width: window.innerWidth,
    height: window.innerHeight,
    ratio: window.innerWidth / window.innerHeight
  }

  // Camera ratio
  camera.aspect = sizes.ratio;
  camera.updateProjectionMatrix();

  // Resize Renderer
  renderer.setSize(sizes.width, sizes.height);
})

// Loop
function animate() {
  requestAnimationFrame(animate);

  // Render new frame
  renderer.render(scene, camera);
}

onMounted(() => {
  // Renderer gets appended to target
  target.value.appendChild(renderer.domElement);

  // Start Animation
  animate();
});
</script>

<template>
  <div ref="target"></div>
</template>