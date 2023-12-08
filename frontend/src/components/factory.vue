<script setup lang="ts">
import {ref, onMounted, watch, provide} from 'vue'
import type {Ref} from 'vue'
import type {IVector3} from '@/types/global'
import type {IBackendEntityPreview} from '@/types/backendEntity'
import * as THREE from 'three'
import {OrbitControls} from 'three/addons/controls/OrbitControls.js'
import {FlyControls} from 'three/addons/controls/FlyControls.js'

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
  updateHighlightModel, selectionObject, replaceEntity, highlightObjectWithColor
} from '../utils/factory.js'
import {getIntersectionsMouse} from '../utils/3d.js'
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js'
import EntityBar from './EntityBar.vue'
import Button from "@/components/Button.vue";
import CircularMenu from "@/components/CircularMenu.vue";

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
const moveMode: Ref<String> = ref<'orbit' | 'fly'>('orbit')
const moveOrSelectionMode: Ref<String> = ref<'set' | ''>('')
const manipulationMode: Ref<String> = ref<'move' | 'rotate' | ''>('')
const allEntitys: Ref<IBackendEntityPreview[]> = ref([])
const activeEntity: Ref<IBackendEntityPreview> = ref({
  path: '/fallback/.gltf/cube.gltf',
  icon: '',
  entityID: 'loadingCube'
})
const currentObjectSelected: Ref<THREE.Group> = ref()
const lastObjectSelected: Ref<THREE.Group> = ref()
const currObjSelectedOriginPos: Ref<IVector3> = ref({x: 0, y: 0, z: 0})
const showCircMenu: Ref<Boolean> = ref(false)
provide('showCircleMenu', showCircMenu)
let dynamicDiv: HTMLElement | null

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
let cameraControls: any = new OrbitControls(camera, renderer.domElement)

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
      scene.add(highlight)
      highlight.name = 'highlight'
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
  if (highlight && moveOrSelectionMode.value === 'set')
      // Object model wird asynchron geladen
  {
    updateHighlight(highlight, ACTIVE_LAYER, intersections)
  } else if (currentObjectSelected.value && manipulationMode.value === 'move') {
    updateHighlight(currentObjectSelected.value, ACTIVE_LAYER, intersections)
  }

})

//onClick
addEventListener('contextmenu', (event: MouseEvent) => {
  event.preventDefault()
  if (moveOrSelectionMode.value !== '' || manipulationMode.value !== '') return
  const intersections = getIntersectionsMouse(event, camera, scene)
  if (selectionObject(currentObjectSelected, lastObjectSelected, intersections)) {
    if (dynamicDiv) {
      dynamicDiv.style.left = event.clientX - 50 + 'px'
      dynamicDiv.style.top = event.clientY + 20 + 'px'
      dynamicDiv.style.display = 'block'
    }
    showCircMenu.value = true
  }
  if (currentObjectSelected.value)
    currObjSelectedOriginPos.value = currentObjectSelected.value.position
})
addEventListener('click', () => {
  // Place cube
  if (showCircMenu.value) {
    showCircMenu.value = false
    if (manipulationMode.value === '')
      highlightObjectWithColor(currentObjectSelected, false)
    return
  }
  if (moveOrSelectionMode.value === 'set' &&
      placeRequest({
        x: highlight.position.x,
        y: highlight.position.y,
        z: highlight.position.z,
        orientation: 'N',
        entityID: 'cube'
      })
  ) {
    placeEntity(loader, scene, highlight.position, activeEntity.value.path)
  } else if (manipulationMode.value === 'move' &&
      placeRequest({
        x: currentObjectSelected.value.position.x,
        y: currentObjectSelected.value.position.y,
        z: currentObjectSelected.value.position.z,
        orientation: 'N',
        entityID: 'cube'
      })) {
    replaceEntity(currentObjectSelected.value.position, currentObjectSelected, lastObjectSelected)
    manipulationMode.value = ''
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

  if (moveMode.value === 'fly') {
    cameraControls.update(0.05)
  }

  // Render new frame
  renderer.render(scene, camera)
}

// Entry Point
onMounted(() => {
  dynamicDiv = document.getElementById('dynamicDiv')
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
  moveMode.value = moveMode.value === 'orbit' ? 'fly' : 'orbit'
}

// watch(manipulationMode, () => {
//   if (manipulationMode.value !== 'move' && currentObjectSelected.value.position != currObjSelectedOriginPos.value) {
//     replaceEntity(currObjSelectedOriginPos.value, currentObjectSelected, lastObjectSelected)
//     console.log(currObjSelectedOriginPos.value)
//   }
// })
// watch active Entity to change current model
watch(activeEntity, () => {
  moveOrSelectionMode.value = 'set'
  scene.add(highlight)
  updateHighlightModel(highlight, activeEntity.value.path, scene, loader).then(
      (newHighlight: THREE.Group) => {
        highlight = newHighlight
      }
  )
})
addEventListener('keydown', (event) => {
  if (event.key === 'v' || event.key === 'V') {
    moveOrSelectionMode.value = ''
    manipulationMode.value = ''
    highlightObjectWithColor(currentObjectSelected, false)
    scene.remove(highlight)
  }
  if (manipulationMode.value === 'rotate') {
    const currObjSelRot = currentObjectSelected.value.rotation
    if (event.key === 'ArrowLeft') {
      currObjSelRot.set(0, 0, currObjSelRot.z + (Math.PI / 2))
    }
    if (event.key === 'ArrowRight') {
      currObjSelRot.set(0, 0, currObjSelRot.z - (Math.PI / 2))
    }
  }
})
// Watch the moveMode to change camera option
watch(moveMode, () => {
  var prevCamera = camera
  camera = new THREE.PerspectiveCamera(50, sizes.ratio)
  camera.position.copy(prevCamera.position)
  camera.rotation.copy(prevCamera.rotation)
  camera.up.copy(prevCamera.up)

  const controls =
      moveMode.value === 'fly'
          ? new FlyControls(camera, renderer.domElement)
          : new OrbitControls(camera, renderer.domElement)

  if (moveMode.value === 'fly') {
    controls.movementSpeed = 10
    controls.dragToLook = true
    controls.rollSpeed = 0.3
  }

  cameraControls = controls
})
const toggleMenuVisibility = () => {
  showCircMenu.value = !showCircMenu.value;
};
const onChangeEntityClicked = (situation: string) => {
  switch (situation){
    case 'delete':
      scene.remove(currentObjectSelected.value)
      console.log('deleting Entity')
      break
    case 'rotate':
      manipulationMode.value = 'rotate'
      console.log('rotating Entity')
      break
    case 'move':
      manipulationMode.value = 'move'
      console.log('moving Entity')
      break
    case 'script':
      console.log('scripting Entity')
      break
    case 'clone':
      console.log('cloning Entity')
      break
  }

}
</script>

<template>
  <div className="target" ref="target">
    <div id="dynamicDiv" style="background-color: #2c3e50; position: absolute">
      <CircularMenu :toggleMenuVisibility="toggleMenuVisibility"
                    @changeEntity="onChangeEntityClicked"
      ></CircularMenu>
    </div>
    <div className="button-bar">
      <button @click="onLoadFactoryButton">Test Load Factory</button>
      <button @click="onToggleMoveModeButton">Toggle Camera Mode</button>
    </div>
    <div className="debug-bar">
      <div>Current Camera Mode: {{ moveMode }}</div>
      <div>Active Entity: {{ activeEntity.entityID }}</div>
    </div>
    <EntityBar :entities="allEntitys" :active-entity="activeEntity"
               @update-active-entity="id => activeEntity = allEntitys[id]"/>
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
