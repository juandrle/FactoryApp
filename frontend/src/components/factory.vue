<script setup lang="ts">
import { ref, onMounted } from 'vue';
import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { createGrids, updateHighlight, placeEntity, setGroundTexture } from "../utils/factory.js"
import { getIntersectionsMouse } from "../utils/3d.js";
import { type IGrid, type ISizes } from "../types/global"
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';

const {
  grid_width,
  grid_lenght,
  grid_height
} = defineProps(['grid_width', "grid_lenght", "grid_height"])

/*******************************/
/*********** COFIG *************/
/*******************************/

const ACTIVE_LAYER: number = 0;
const GRID: IGrid = {
  x: grid_width,
  y: grid_lenght,
  z: grid_height
};

/********************/
/******* SETUP ******/
/********************/
const target = ref()


// Get Screen size
let sizes: ISizes = {
  width: window.innerWidth,
  height: window.innerHeight,
  ratio: window.innerWidth / window.innerHeight
}

// Add Scene & Renderer
const scene: any = new THREE.Scene();
const renderer: any = new THREE.WebGLRenderer();
renderer.setSize(sizes.width, sizes.height);
scene.background = new THREE.Color("white");
const ambientLight = new THREE.AmbientLight(0xffffff, 0.5); // Add ambient light
scene.add(ambientLight);

const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5); // Add directional light
directionalLight.position.set(1, 1, 1).normalize();
scene.add(directionalLight);


// Create camera an position it
const camera: any = new THREE.PerspectiveCamera(50, sizes.ratio);
camera.position.set(40, -15, 15);
camera.up.set(0, 0, 1);
camera.lookAt(0, 0, 0);

// OrbitControlls 
new OrbitControls(camera, renderer.domElement);

// Model loader
const loader: any = new GLTFLoader();

/*****************************/
/******* START OBJECTS *******/
/*****************************/

// Add Grid
createGrids(GRID.x, GRID.y, GRID.z, scene)

// set Texture

setGroundTexture('old-cement-wall-texture.jpg', scene)

// Add axis helper
const axesHelper: any = new THREE.AxesHelper(20);
scene.add(axesHelper);

// Add Highlight cube
const geometry: any = new THREE.BoxGeometry(25, 25, 25);

const material: any = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
const highlightCube: any = new THREE.Mesh(geometry, material);
highlightCube.name = "highlight";
scene.add(highlightCube);


/********************/
/**** CALLBACKS *****/
/********************/

// onMouseMove
addEventListener("mousemove", (event: MouseEvent) => {
  // Get all intersections with mouse and world
  const intersections = getIntersectionsMouse(event, camera, scene)

  // Update the highlighter
  updateHighlight(highlightCube, ACTIVE_LAYER, intersections)
});

//onClick
addEventListener("click", () => {  
  // Place cube
  placeEntity(loader, scene, highlightCube.position, "cube.gltf")
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