<script setup lang="ts">
import {ref, onMounted, watch, provide, inject, onUnmounted} from 'vue'
import type {Ref} from 'vue'
import type {IVector3} from '@/types/global'
import type {IBackendEntityPreview, IBackendEntity} from '@/types/backendEntity'
import * as THREE from 'three'
import {ControlsManager} from '@/classes/ControlsManager'
import {getIntersectionsMouse} from '@/utils/threeJS/3d'
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js'
import EntityBar from '@/components/temp/EntityBar.vue'
import Button from '@/components/temp/Button.vue'
import CircularMenu from '@/components/ui/CircularMenu.vue'
import {placeRequest} from '@/utils/backendComms/postRequests'
import {getAllEntities, getAllEntitiesInFactory} from '@/utils/backendComms/getRequests'
import {backendUrl} from '@/utils/config/config'
import {ControlMode} from '@/enum/ControlMode'

import {
  highlightObjectWithColor,
  moveHighlight,
  placeEntity,
  replaceEntity,
  selectionObject,
  updateHighlightModel,
  createRoom, deepCloneObject
} from '@/utils/threeJS/helpFunctions'
import {MOUSE} from "three";

/**
 * Config
 **/

const ACTIVE_LAYER: number = 0

/**
 * Variables -> ref
 **/

const target = ref()
const manipulationMode: Ref<String> = ref<'move' | 'rotate' | '' | 'set' | 'clone'>('')
const allEntitys: Ref<IBackendEntityPreview[] | undefined> = ref()
const activeEntity: Ref<IBackendEntityPreview | undefined> = ref()
// quick fix to any
let currentObjectSelected: any
let lastObjectSelected: any
let currObjSelectedOriginPos: IVector3 = {x: 0, y: 0, z: 0}
const showCircMenu: Ref<Boolean> = ref(false)

/**
 * Variables
 **/

let factorySize: Ref<IVector3>
let factoryID: Ref<number>
let dynamicDiv: HTMLElement | null
let sizes: {
  width: number
  height: number
  ratio: number
}
let scene: THREE.Scene
let renderer: THREE.WebGLRenderer
let camera: THREE.PerspectiveCamera
let loader: any
let highlight: THREE.Group
let cm: ControlsManager;
let previousTime: number = 0
let currentMode: ControlMode | null;


/**
 * Setup
 **/

const setupScene = () => {
  scene = new THREE.Scene()
  scene.background = new THREE.Color('#12111A')
}
const setupInjections = () => {
  const resultID = inject<{
    factoryID: Ref<number>
    updateFactoryID: (newID: number) => void
  }>('factoryID')
  if (resultID && typeof resultID === 'object') factoryID = resultID.factoryID
  const resultSize = inject<{
    factorySize: Ref<IVector3>,
    updateFactorySize: (newSize: IVector3) => void
  }>('factorySize')
  if (resultSize && typeof resultSize === 'object') factorySize = resultSize.factorySize
}

const setupRenderer = () => {
  renderer = new THREE.WebGLRenderer()
  renderer.setSize(sizes.width, sizes.height)
}

const setupCamera = () => {
  camera = new THREE.PerspectiveCamera(50, sizes.ratio)
  camera.position.set(40, -15, 15)
  camera.up.set(0, 0, 1)
  camera.lookAt(0, 0, 0)
}

const setupLights = () => {
  const ambientLight = new THREE.AmbientLight(0xffffff, 0.5)
  scene.add(ambientLight)

  const directionalLight = new THREE.DirectionalLight(0xffffff, 0.5)
  directionalLight.position.set(1, 1, 1).normalize()
  scene.add(directionalLight)
}

const setupControls = () => {
  cm = new ControlsManager(camera, renderer.domElement)
  currentMode = cm.currentMode;
}

const setupLoader = () => {
  loader = new GLTFLoader()
}

const initalLoadHighlightModel = (modelUrl: string) => {
  loader.load(
      modelUrl,
      (gltf: any) => {
        highlight = gltf.scene
        highlight.position.set(0, 0, 0)
        scene.add(highlight)
        highlight.name = 'highlight'
      },
      undefined,
      (error: Error) => {
        console.error(error)
      }
  )
}

/*
 * Buttons
 */

const onLoadFactoryButton = () => {
  if (factoryID === undefined) return
  getAllEntitiesInFactory(factoryID.value).then((backendEntitys: IBackendEntity[]) => {
    backendEntitys.forEach((backendEntity) => {
      placeEntity(
          loader,
          scene,
          {x: backendEntity.x, y: backendEntity.y, z: backendEntity.z},
          backendUrl + backendEntity.path
      )
    })
  })
}

const onToggleMenuVisibility = () => {
  showCircMenu.value = !showCircMenu.value
}

const onChangeEntityClicked = (situation: string) => {
  switch (situation) {
    case 'delete':
      scene.remove(currentObjectSelected)
      console.log('deleting Entity')
      break
    case 'rotate':
      manipulationMode.value = 'rotate'
      console.log('rotating Entity')
      break
    case 'move':
      manipulationMode.value = 'move'
      currObjSelectedOriginPos = currentObjectSelected.position.clone()
      console.log('moving Entity')
      break
    case 'script':
      console.log('scripting Entity')
      break
    case 'clone':
      highlightObjectWithColor(currentObjectSelected, false)
      lastObjectSelected = currentObjectSelected
      currentObjectSelected = deepCloneObject(currentObjectSelected)
      scene.add(currentObjectSelected)
      highlightObjectWithColor(currentObjectSelected, true)
      manipulationMode.value = 'clone'
      console.log('cloning Entity')
      break
  }
}


/**
 * Handlers
 **/

const handleResize = () => {
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
}

const handleKeyDown = (event: KeyboardEvent) => {
  if (event.key === 'v' || event.key === 'V') {
    manipulationMode.value = ''
    showCircMenu.value = false
    if (currentObjectSelected) highlightObjectWithColor(currentObjectSelected, false)
    scene.remove(highlight)
  }
  if (event.key === 'Escape') {
    if (manipulationMode.value === 'move') {
      replaceEntity(currObjSelectedOriginPos, currentObjectSelected, currentObjectSelected)
      manipulationMode.value = ''
    }
  }
  if (manipulationMode.value === 'rotate') {
    const currObjSelRot = currentObjectSelected.rotation
    if (event.key === 'ArrowLeft') {
      currObjSelRot.set(Math.PI / 2, currObjSelRot.y + Math.PI / 2, 0)
    }
    if (event.key === 'ArrowRight') {
      currObjSelRot.set(Math.PI / 2, currObjSelRot.y - Math.PI / 2, 0)
    }
  }
  if (event.key === 'Q' || event.key === 'q') cm.toggleMode()
}

const handleMouseMove = (event: MouseEvent) => {
  // Get all intersections with mouse and world
  const intersections = getIntersectionsMouse(event, camera, scene)

  // Update the highlighter
  if (highlight && manipulationMode.value === 'set') {
    // Object model wird asynchron geladen
    moveHighlight(highlight, ACTIVE_LAYER, intersections)
  } else if (currentObjectSelected && manipulationMode.value === 'move') {
    moveHighlight(currentObjectSelected, ACTIVE_LAYER, intersections)
  } else if (currentObjectSelected && manipulationMode.value === 'clone') {
    moveHighlight(currentObjectSelected, ACTIVE_LAYER, intersections)
  }
}

const handleClick = (event: any) => {
  // Place cube
  if (showCircMenu.value) {
    showCircMenu.value = false
    if (manipulationMode.value === '') highlightObjectWithColor(currentObjectSelected, false)
    return
  }
  if (event.target.id == 'ignore') return;
  switch (manipulationMode.value) {
    case 'set':
      placeRequest({
        x: highlight.position.x,
        y: highlight.position.y,
        z: highlight.position.z,
        orientation: 'N',
        entityID: 'cube',
        factoryID: 1
      }, '/place').then((success: boolean) => {
        console.log('placing entity: ' + success)
        if (success) {
          if (activeEntity.value)
            placeEntity(loader, scene, highlight.position, backendUrl + activeEntity.value.modelFile)
        }
      })
      break
    case 'move':
      placeRequest({
        x: highlight.position.x,
        y: highlight.position.y,
        z: highlight.position.z,
        orientation: 'N',
        entityID: 'cube',
        factoryID: 1
      }, '/move').then((success: boolean) => {
        console.log('placing entity: ' + success)
        if (success) {
          replaceEntity(currentObjectSelected.position, currentObjectSelected, lastObjectSelected)
          manipulationMode.value = ''
        }
      })
      break
    case 'clone':
      placeRequest({
        x: currentObjectSelected.position.x,
        y: currentObjectSelected.position.y,
        z: currentObjectSelected.position.z,
        orientation: 'N',
        entityID: 'cube',
        factoryID: 1
      }, '/clone').then((success: boolean) => {
        console.log('placing entity: ' + success)
        if (success) {
          replaceEntity(currentObjectSelected.position, currentObjectSelected, lastObjectSelected)
          manipulationMode.value = ''
        }
      })
      break
  }


}

const handleContextMenu = (event: MouseEvent) => {
  event.preventDefault()
  if (manipulationMode.value !== '') return
  const intersections = getIntersectionsMouse(event, camera, scene)
  const result = selectionObject(currentObjectSelected, lastObjectSelected, intersections)
  if (result && typeof result === 'object') {
    const {worked, currObj, lastObj} = result;
    if (worked) {
      currentObjectSelected = currObj;
      lastObjectSelected = lastObj;
      if (dynamicDiv) {
        dynamicDiv.style.left = event.clientX - 50 + 'px'
        dynamicDiv.style.top = event.clientY + 20 + 'px'
        dynamicDiv.style.display = 'block'
      }
      showCircMenu.value = true
    }
  }
  if (currentObjectSelected)
    currObjSelectedOriginPos = currentObjectSelected.position
}

/**
 * Watcher
 * **/

watch(activeEntity, () => {
  manipulationMode.value = 'set'

  if (activeEntity.value) {
    updateHighlightModel(highlight, backendUrl + activeEntity.value.modelFile, scene, loader).then(
        (newHighlight: THREE.Group) => {
          highlight = newHighlight
        }
    )
  } else initalLoadHighlightModel('mock/.gltf/cube.gltf')

})

/**
 * Gamecycle
 **/

onMounted(() => {
  // add eventListeners
  window.addEventListener('resize', handleResize)
  window.addEventListener('keydown', handleKeyDown)
  window.addEventListener('mousemove', handleMouseMove)
  window.addEventListener('click', handleClick)
  window.addEventListener('contextmenu', handleContextMenu)
  target.value.appendChild(renderer.domElement)
  dynamicDiv = document.getElementById('dynamicDiv')

  // Renderer gets appended to target
  getAllEntities().then((json) => {
    // Alle entittys sind nun zugänglich für uns
    allEntitys.value = json
    // Active entity ändern
    activeEntity.value = allEntitys.value[0]
  })
  // initial function calls
  animate(0)
})

onUnmounted(() => {
  // remove eventListeners
  window.removeEventListener('resize', handleResize)
  window.removeEventListener('keydown', handleKeyDown)
  window.removeEventListener('mousemove', handleMouseMove)
  window.removeEventListener('click', handleClick)
  window.removeEventListener('contextmenu', handleContextMenu)
})

function init() {
  // provides & injections
  provide('showCircleMenu', showCircMenu)
  sizes = {
    width: window.innerWidth,
    height: window.innerHeight,
    ratio: window.innerWidth / window.innerHeight
  }
  setupScene()
  setupInjections()
  setupRenderer()
  setupCamera()
  setupLights()
  setupControls()
  setupLoader()
  createRoom(factorySize.value.x, factorySize.value.y, factorySize.value.z, scene)
}

const animate = (timestamp: any) => {
  const currentTime = timestamp || 0
  const deltaTime = (currentTime - previousTime) / 1000

  previousTime = currentTime

  requestAnimationFrame(animate)

  cm.update(deltaTime)
  renderer.render(scene, camera)
}

/**
 * Start Game
 **/

init()
</script>

<template>
  <div class="target" ref="target">
    <div id="dynamicDiv" style="background-color: #2c3e50; position: absolute">
      <CircularMenu
          :toggleMenuVisibility="onToggleMenuVisibility"
          @changeEntity="onChangeEntityClicked"
      ></CircularMenu>
    </div>
    <div class="button-bar">
      <button @click="onLoadFactoryButton" link="">Test Load Factory</button>
    </div>
    <div class="debug-bar">
    </div>
    <EntityBar id="ignore"
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
