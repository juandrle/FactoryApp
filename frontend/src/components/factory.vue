<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import type { Ref } from 'vue'
import type { IVector3, ISizes } from '@/types/global'
import type { IBackendEntityPreview } from '@/types/backendEntity'
import * as THREE from 'three'
import {
  createGrids,
  updateHighlight,
  placeEntity,
  createWallsWithTexture,
  createGroundWithTextures,
  createRoofWithTextures,
  loadFactory,
  placeRequest,
  getAllEntitys,
  updateHighlightModel
} from '../utils/factory.js'
import { getIntersectionsMouse } from '../utils/3d.js'
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js'
import EntityBar from './EntityBar.vue'
import { CameraControlsManager } from '../classes/CameraControlsManager.js'
import { CameraMode } from '@/enum/CameraMode'

/*******************************/
/*********** COFIG *************/
/*******************************/

const ACTIVE_LAYER: number = 0
const GRID: IVector3 = {
  x: 30,
  y: 50,
  z: 8
}
/********************/
/******* SETUP ******/
/********************/
const target = ref()
const initalMoveMode: CameraMode = CameraMode.ORBIT
const allEntitys: Ref<IBackendEntityPreview[]> = ref([])
const activeEntity: Ref<IBackendEntityPreview> = ref({
  path: '/fallback/.gltf/cube.gltf',
  icon: '',
  entityID: 'loadingCube'
})

// Get Screen size
let sizes: ISizes = {
  width: window.innerWidth,
  height: window.innerHeight,
  ratio: window.innerWidth / window.innerHeight
}

// Add Scene & Renderer
const scene: any = new THREE.Scene()
const renderer: any = new THREE.WebGLRenderer()
renderer.setSize(sizes.width, sizes.height)
scene.background = new THREE.Color('white')

// Add ambient light
const ambientLight = new THREE.AmbientLight(0xffffff, 0.5)
scene.add(ambientLight)

// Add directional light
const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5)
directionalLight.position.set(1, 1, 1).normalize()
scene.add(directionalLight)

// Create camera an position it
let camera: any = new THREE.PerspectiveCamera(50, sizes.ratio)
camera.position.set(40, -15, 15)
camera.up.set(0, 0, 1)
camera.lookAt(0, 0, 0)

// Set Camera controll option (Orbit as Defaukt)
let ccm: CameraControlsManager = new CameraControlsManager(
  camera,
  renderer.domElement,
  initalMoveMode
)

// Model loader
const loader: any = new GLTFLoader()

/*****************************/
/******* START OBJECTS *******/
/*****************************/

// Add Grid
createGrids(GRID.x, GRID.y, GRID.z, scene)

// creating roomtextures
createGroundWithTextures('factoryGround.jpeg', scene, GRID.x, GRID.y)
createRoofWithTextures('factoryRoof.jpeg', scene, GRID.x, GRID.y, GRID.z)
createWallsWithTexture('factoryWall.jpg', scene, GRID.x, GRID.y, GRID.z)

// Add axis helper
const axesHelper: any = new THREE.AxesHelper(20)
scene.add(axesHelper)

// Add Highlight cube
var highlight: any
loader.load(
  '/fallback/.gltf/cube.gltf',
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
addEventListener('mousemove', (event: MouseEvent) => {
  // Get all intersections with mouse and world
  const intersections = getIntersectionsMouse(event, camera, scene)

  // Update the highlighter
  if (highlight)
    // Object model wird asynchron geladen
    updateHighlight(highlight, ACTIVE_LAYER, intersections)
})

//onClick
addEventListener('click', () => {
  // Place cube
  if (
    placeRequest({
      x: highlight.position.x,
      y: highlight.position.y,
      z: highlight.position.z,
      orientation: 'N',
      entityID: 'cube'
    })
  ) {
    placeEntity(loader, scene, highlight.position, activeEntity.value.path)
  }
})

// onRezise
window.addEventListener('resize', () => {
  // Variables
  sizes = {
    width: window.innerWidth,
    height: window.innerHeight,
    ratio: window.innerWidth / window.innerHeight
  }

  // Camera ratio
  camera.aspect = sizes.ratio
  camera.updateProjectionMatrix()

  // Resize Renderer
  renderer.setSize(sizes.width, sizes.height)
})

// Loop
function animate() {
  requestAnimationFrame(animate)

  ccm.update()

  // Render new frame
  renderer.render(scene, camera)
}

// Entry Point
onMounted(() => {
  // Renderer gets appended to target
  target.value.appendChild(renderer.domElement)

  getAllEntitys().then((json) => {
    // Alle entittys sind nun zugänglich für uns
    allEntitys.value = json

    // Active entity ändern
    activeEntity.value = allEntitys.value[0]
  })

  // Start Animation
  animate()
})

// Button onLoadFactory
const onLoadFactoryButton = () => {
  loadFactory(scene, loader, 'factory_id_sample')
}

// Button onToggleMoveMode
const onToggleMoveModeButton = () => {
  ccm.toggleMode()
}

// watch active Entity to change current model
watch(activeEntity, () => {
  updateHighlightModel(highlight, activeEntity.value.path, scene, loader).then(
    (newHighlight: any) => {
      highlight = newHighlight
    }
  )
})
</script>

<template>
  <div className="target" ref="target">
    <div className="button-bar">
      <button @click="onLoadFactoryButton">Test Load Factory</button>
      <button @click="onToggleMoveModeButton">Toggle Camera Mode</button>
    </div>
    <div className="debug-bar">
      <div>Active Entity: {{ activeEntity.entityID }}</div>
    </div>
    <EntityBar
      :entities="allEntitys"
      :active-entity="activeEntity"
      @update-active-entity="(id) => (activeEntity = allEntitys[id])"
    />
  </div>
</template>

<style>
.button-bar {
  display: flex;
  position: absolute;
  bottom: 60px;
  left: 60px;
  gap: 20px;
}

.button-bar button {
  font-size: 20px;
  cursor: pointer;
  padding: 8px 12px;
  background-color: #282b30;
  border: 2px #7289da solid;
  font-weight: 600;
  border-radius: 10px;
  color: #7289da;
}

.debug-bar {
  position: absolute;
  top: 60px;
  left: 60px;
  color: #282b30;
  font-size: 20px;
}
</style>
