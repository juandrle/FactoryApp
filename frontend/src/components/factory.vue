<script setup lang="ts">
import {ref, onMounted} from 'vue';
import type {IVector3} from '@/types/global'
import * as THREE from 'three';
import {OrbitControls} from 'three/addons/controls/OrbitControls.js';
import {
  createGrids,
  updateHighlight,
  placeEntity,
  createWallsWithTexture,
  createGroundWithTextures,
  createRoofWithTextures,
  loadFactory,
  placeRequest
} from "../utils/factory.js"
import {getIntersectionsMouse} from "../utils/3d.js";
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js';

const props = withDefaults(defineProps<{ grid_width: number, grid_length: number, grid_height: number }>(), {
  grid_width: 30, grid_length: 50, grid_height: 8})

/*******************************/
/*********** COFIG *************/
/*******************************/

const ACTIVE_LAYER: number = 0;
const GRID: IVector3 = {
  x: props.grid_width,
  y: props.grid_length,
  z: props.grid_height
};

/********************/
/******* SETUP ******/
/********************/
const target = ref()


// Get Screen size
let sizes: {
  width: number
  height: number
  ratio: number
} = {
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

// creating roomtextures
createGroundWithTextures('factoryGround.jpeg', scene, GRID.x, GRID.y);
createRoofWithTextures('factoryRoof.jpeg', scene, GRID.x, GRID.y, GRID.z);
createWallsWithTexture('factoryWall.jpg', scene, GRID.x, GRID.y, GRID.z);

// Add axis helper
const axesHelper: any = new THREE.AxesHelper(20);
scene.add(axesHelper);

// Add Highlight cube
var highlight: any;
loader.load(
    "/mock/.gltf/cube.gltf",
    function (gltf: any) {
      highlight = gltf.scene
      highlight.position.set(0, 0, 0)
      highlight.name = 'highlight'
      scene.add(gltf.scene)
    },
    undefined,
    function (error: any) {
      console.error(error)
    }
)


/********************/
/**** CALLBACKS *****/
/********************/

// onMouseMove
addEventListener("mousemove", (event: MouseEvent) => {
  // Get all intersections with mouse and world
  const intersections = getIntersectionsMouse(event, camera, scene)

  // Update the highlighter
  if (highlight) // Object model wird asynchron geladen
    updateHighlight(highlight, ACTIVE_LAYER, intersections)
});

//onClick
addEventListener("click", () => {

  // Place cube
  if (placeRequest({
    x: highlight.position.x,
    y: highlight.position.y,
    z: highlight.position.z,
    orientation: "N",
    entityID: "cube"
  })) {
    placeEntity(loader, scene, highlight.position, "/mock/.gltf/cube.gltf")
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

// Entry Point
onMounted(() => {
  // Renderer gets appended to target
  target.value.appendChild(renderer.domElement);

  // Start Animation
  animate();
});

const onButtonClicked = () => {
  loadFactory(scene, loader, "factory_id_sample")
}

</script>

<template>
  <button @click="onButtonClicked" style="
    font-size:20px;
    bottom: 150px;
    left: 300px;
    cursor: pointer;
    position: absolute;
    padding: 8px 12px;
    background-color: 	#282b30;
    border: 2px 	#7289da solid;
    font-weight: 600;
    border-radius: 10px;
    color: 	#7289da">Test Load Factory
  </button>
  <div ref="target"></div>
</template>