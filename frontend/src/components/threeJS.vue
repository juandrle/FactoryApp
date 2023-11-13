<script setup>
import { ref, onMounted } from 'vue';
import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { createGrids, updateHighlight, placeEntity } from "../utils/factory.js"
import { getIntersectionsMouse } from "../utils/3d.js"

const {
  grid_width,
  grid_lenght,
  grid_height
} = defineProps(['grid_width', "grid_lenght", "grid_height"])

/*******************************/
/*********** COFIG *************/
/*******************************/

const ACTIVE_LAYER = 0;
const GRID = {
  x: grid_width,
  y: grid_lenght,
  z: grid_height
};

/********************/
/******* SETUP ******/
/********************/
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
scene.background = new THREE.Color("black")

// Create camera an position it
const camera = new THREE.PerspectiveCamera(50, sizes.ratio);
camera.position.set(40, -15, 15);
camera.up.set(0, 0, 1);
camera.lookAt(0, 0, 0);

// OrbitControlls 
const controls = new OrbitControls(camera, renderer.domElement);

// Start Items
const items = []

/*****************************/
/******* START OBJECTS *******/
/*****************************/

// Add Grid
createGrids(GRID.x, GRID.y, GRID.z, scene)

// Add axis helper
// const axesHelper = new THREE.AxesHelper(10);
// scene.add(axesHelper);

// Add Highlight cube
const geometry = new THREE.BoxGeometry(1, 1, 1);
const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
const highlightCube = new THREE.Mesh(geometry, material);
highlightCube.position.set(0.5, 0.5, 0.5)
highlightCube.name = "highlight";
scene.add(highlightCube);


/********************/
/**** CALLBACKS *****/
/********************/

// onMouseMove
addEventListener("mousemove", (event) => {
  // Get all intersections with mouse and world
  const intersections = getIntersectionsMouse(event, camera, scene)

  // Update the highlighter
  updateHighlight(highlightCube, ACTIVE_LAYER, intersections)
});

addEventListener("click", (event) => {

  /**
   * Hardcoding an Entity 
   * This will be the "Selected Entity" in the future
   * For now its just a cube.
   */
  const my_current_entity = new THREE.Mesh(
    new THREE.BoxGeometry(1, 1, 1), 
    new THREE.MeshBasicMaterial({ color: new THREE.Color("red") })
  );
  highlightCube.name = "entity_cube";

  // Place cube
  placeEntity(scene, highlightCube.position, my_current_entity)
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

// Entry Point
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