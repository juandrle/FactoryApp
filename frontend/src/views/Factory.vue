<script setup lang="ts">
import {ref, onMounted, watch, provide, inject} from 'vue'
import type {Ref} from 'vue'
import type {IVector3} from '@/types/global'
import type {IBackendEntityPreview} from '@/types/backendEntity'
import * as THREE from 'three'
import {OrbitControls} from 'three/addons/controls/OrbitControls.js'
import {FlyControls} from 'three/addons/controls/FlyControls.js'

import {getIntersectionsMouse} from '../utils/threeJS/3d'
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js'
import EntityBar from '@/components/temp/EntityBar.vue'
import Button from "@/components/temp/Button.vue";
import CircularMenu from "@/components/ui/CircularMenu.vue";
import {placeRequest} from "@/utils/backendComms/postRequests";
import {getAllEntitys, loadFactory} from "@/utils/backendComms/getRequests";
import {
  createGrids,
  createGroundWithTextures,
  createRoofWithTextures,
  createWallsWithTexture,
  highlightObjectWithColor, moveHighlight,
  placeEntity,
  replaceEntity,
  selectionObject,
  updateHighlightModel
} from "@/utils/threeJS/helpFunctions";

/*******************************/
/*********** COFIG *************/
/*******************************/

const ACTIVE_LAYER: number = 0
/********************/
/*** REF VARIABLES **/
/********************/
const target = ref()
const moveMode: Ref<String> = ref<'orbit' | 'fly'>('orbit')
const moveOrSelectionMode: Ref<String> = ref<'set' | ''>('')
const manipulationMode: Ref<String> = ref<'move' | 'rotate' | 'clone' | ''>('')
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

/********************/
/*** VARIABLES ******/
/********************/
const {factorySize} = inject<{
  factorySize: Ref<IVector3>,
  updateFactorySize: (newSize: IVector3) => void
}>('factorySize')
let dynamicDiv: HTMLElement | null
let sizes: {
  width: number
  height: number
  ratio: number
}
let scene: THREE.Scene
let renderer: THREE.WebGLRenderer
let camera: THREE.PerspectiveCamera
let cameraControls: THREE.OrbitControls
let loader: THREE.GLTFLoader
let highlight: THREE.Group


function init() {
  // provides & injections
  provide('showCircleMenu', showCircMenu)
  sizes = {
    width: window.innerWidth,
    height: window.innerHeight,
    ratio: window.innerWidth / window.innerHeight
  }
  scene = new THREE.Scene()

  scene.background = new THREE.Color('#12111A')
  renderer = new THREE.WebGLRenderer()

  renderer.setSize(sizes.width, sizes.height)
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.5)

  scene.add(ambientLight)
  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5)
  directionalLight.position.set(1, 1, 1).normalize()

  scene.add(directionalLight)
  camera = new THREE.PerspectiveCamera(50, sizes.ratio)
  camera.position.set(40, -15, 15)
  camera.up.set(0, 0, 1)
  camera.lookAt(0, 0, 0)

  cameraControls = new OrbitControls(camera, renderer.domElement)

  loader = new GLTFLoader()

  // Add axis helper
  // const axesHelper: any = new THREE.AxesHelper(20)
  // scene.add(axesHelper)

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
  createRoom(factorySize.value.x,factorySize.value.y,factorySize.value.z)
}

const createRoom = (x: number, y: number, z: number) => {
  // Add Grid
  createGrids(x, y, z, scene)
  // creating roomtextures
  createGroundWithTextures('factoryGround.jpeg', scene, x, y)
  createRoofWithTextures('factoryRoof.jpeg', scene, x, y, z)
  createWallsWithTexture('factoryWall.jpg', scene, x, y, z)
}


// Loop
const animate = () => {

  requestAnimationFrame(animate)
  if (moveMode.value === 'fly') {

    cameraControls.update(0.05)
  }
  // Render new frame

  renderer.render(scene, camera)
}

// Entry Point

// Button onLoadFactory
const onLoadFactoryButton = () => {
  loadFactory(scene, loader, 'factory_id_sample')

}
// Button onToggleMoveMode
const toggleMenuVisibility = () => {
  showCircMenu.value = !showCircMenu.value;

};
const onChangeEntityClicked = (situation: string) => {
  switch (situation) {
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
      const newObj = currentObjectSelected.value.clone()
      scene.add(newObj)
      highlightObjectWithColor(currentObjectSelected, false)
      console.log(currentObjectSelected.value)
      currentObjectSelected.value = newObj
      console.log(currentObjectSelected.value)
      manipulationMode.value = 'clone'
      console.log('cloning Entity')
      break
  }
}
const onToggleMoveModeButton = () => {
  moveMode.value = moveMode.value === 'orbit' ? 'fly' : 'orbit'

}
/**
 * LISTENERS
 **/
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
addEventListener('keydown', (event) => {
  if (event.key === 'v' || event.key === 'V') {
    moveOrSelectionMode.value = ''
    manipulationMode.value = ''
    showCircMenu.value = false
    if (currentObjectSelected.value) highlightObjectWithColor(currentObjectSelected, false)
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
addEventListener('mousemove', (event: MouseEvent) => {
  // Get all intersections with mouse and world
  const intersections = getIntersectionsMouse(event, camera, scene)

  // Update the highlighter
  if (highlight && moveOrSelectionMode.value === 'set')
      // Object model wird asynchron geladen
  {
    moveHighlight(highlight, ACTIVE_LAYER, intersections)
  } else if (currentObjectSelected.value && (manipulationMode.value === 'move' || manipulationMode.value === 'clone')) {
    moveHighlight(currentObjectSelected.value, ACTIVE_LAYER, intersections)
  }

})
addEventListener('click', (event) => {
  const intersections = getIntersectionsMouse(event, camera, scene)
  // Place cube
  if (showCircMenu.value) {
    showCircMenu.value = false
    if (manipulationMode.value === 'clone') {
      selectionObject(currentObjectSelected,lastObjectSelected,intersections)
      }
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
  } else if ((manipulationMode.value === 'move' || manipulationMode.value == 'clone') &&
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
/**
 * WATCHERS
 * **/
watch(activeEntity, () => {
  moveOrSelectionMode.value = 'set'
  scene.add(highlight)
  updateHighlightModel(highlight, activeEntity.value.path, scene, loader).then(
      (newHighlight: THREE.Group) => {
        highlight = newHighlight
      }
  )
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
init()
onMounted(() => {
  target.value.appendChild(renderer.domElement)
  dynamicDiv = document.getElementById('dynamicDiv')
  // Renderer gets appended to target
  getAllEntitys().then((json) => {
    // Alle entittys sind nun zugänglich für uns
    allEntitys.value = json
    // Active entity ändern
    activeEntity.value = allEntitys.value[0]
  })
  // initial function calls
  animate()
})
</script>

<template>
  <div className="target" ref="target">
    <div id="dynamicDiv" style="background-color: #2c3e50; position: absolute">
      <CircularMenu :toggleMenuVisibility="toggleMenuVisibility"
                    @changeEntity="onChangeEntityClicked($event)"
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
