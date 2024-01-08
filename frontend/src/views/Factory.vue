<script setup lang="ts">
import type {Ref} from 'vue'
import {inject, onMounted, onUnmounted, provide, ref, watch} from 'vue'
import type {IEntity, IVector3} from '@/types/global'
import type {IBackendEntity, IBackendEntityPreview} from '@/types/backendTypes'
import * as THREE from 'three'
import {CameraControlsManager} from '@/classes/CameraControlsManager'
import {getIntersectionsMouse} from '@/utils/threeJS/3d'
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js'
import EntityBar from '@/components/temp/EntityBar.vue'
import Button from '@/components/temp/Button.vue'
import CircularMenu from '@/components/ui/CircularMenu.vue'
import {factoryImageUpdate, moveRequest, placeRequest, rotationRequest} from '@/utils/backendComms/postRequests'
import {entityDeleteRequest} from '@/utils/backendComms/deleteRequest';
import {getAllEntities, getAllEntitiesInFactory} from '@/utils/backendComms/getRequests'
import {backendUrl} from '@/utils/config/config'
import {CameraMode} from '@/enum/CameraMode'
import {ManipulationMode} from '@/enum/ManipulationMode'

import {createRoom, moveHighlight, selectionObject, updateHighlightModel} from '@/utils/threeJS/helpFunctions'

import {highlightObjectWithColor, placeEntity, replaceEntity} from '@/utils/threeJS/entityManipulation'
import {rotateModel, rotateModelfromXtoY, turnLeft, turnRight} from "@/utils/rotation/rotate";

/**
 * Config
 **/

const ACTIVE_LAYER: number = 0

/**
 * Variables -> ref
 **/

const target = ref()
const manipulationMode: Ref<ManipulationMode> = ref<ManipulationMode>(ManipulationMode.IDLE)
const allEntitys: Ref<IBackendEntityPreview[] | undefined> = ref()
const activeEntity: Ref<IBackendEntityPreview | undefined> = ref()
// quick fix to any
let currentObjectSelected: any
let lastObjectSelected: any
let currObjSelectedOriginPos: IVector3 = { x: 0, y: 0, z: 0 }
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
let ccm: CameraControlsManager
let previousTime: number = 0
let currentMode: CameraMode | null
let pivot: THREE.Object3D
let allPlacedEntities: { [uuid: string]: IEntity; } = {}
let originalOrientation = "";

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
    factorySize: Ref<IVector3>
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
  ccm = new CameraControlsManager(camera, renderer.domElement, CameraMode.ORBIT)
  currentMode = ccm.currentMode
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
const captureScreenshot = () => {
  renderer.clear();
  renderer.render(scene, camera);
  setupCamera()
  console.log(camera);
  const canvas = renderer.domElement
  return canvas.toDataURL("image/png")
}
/**
 * Buttons
 */

const onLoadFactoryButton = () => {
  if (factoryID === undefined) return

}

const onToggleMenuVisibility = () => {
  showCircMenu.value = !showCircMenu.value
}

/**
 * Circle / Manipulation
 */

const onChangeEntityClicked = (situation: string) => {
  // When one circle option was clicked
  switch (situation) {
    case 'delete':
      // Delete Request
      entityDeleteRequest({
        factoryId: factoryID.value,
        id: allPlacedEntities[currentObjectSelected.uuid].id
      }).then((success) => {
        console.log(success);
        if (success) {
          delete allPlacedEntities[currentObjectSelected.uuid];

          // Remove from scene
          scene.remove(currentObjectSelected)

          //if (currentObjectSelected.parent.type !== 'Scene')
            //scene.remove(currentObjectSelected.parent)
        }
      })

      break

    case 'rotate':
      // Set mode
      manipulationMode.value = ManipulationMode.ROTATE

      // set pivot point for future rotation
      if (!pivot || currentObjectSelected !== pivot.children[0]) {
        if (currentObjectSelected.parent.type === 'Object3D') {
          pivot = currentObjectSelected.parent
          return
        }
        let box = new THREE.Box3().setFromObject(currentObjectSelected)
        let center = new THREE.Vector3()
        box.getCenter(center)
        currentObjectSelected.position.sub(new THREE.Vector3(center.x, center.y, 0))
        pivot = new THREE.Object3D()
        pivot.position.set(center.x, center.y, currentObjectSelected.position.z)
        scene.add(pivot)
        pivot.add(currentObjectSelected)
      }
      break

    case 'move':
      // Set move mode
      manipulationMode.value = ManipulationMode.MOVE

      // Set object position
      currObjSelectedOriginPos = currentObjectSelected.position.clone()

      break

    case 'script':
      console.log('scripting Entity')
      break

    case 'clone':
      if(allEntitys.value) {
        // Find highlight by name
        activeEntity.value = allEntitys.value.find((obj) => obj.name === allPlacedEntities[currentObjectSelected.uuid].modelId)

        // If it was the same, update manually
        if(activeEntity.value)
          updateHighlightModel(highlight, backendUrl + activeEntity.value.modelFile, scene, loader).then(
              (newHighlight: THREE.Group) => {
                highlight = newHighlight
              }
          )

        // Normal set mode
        manipulationMode.value = ManipulationMode.SET
      }

      console.log('cloning Entity')
      break
  }
}

const clickActionBasedOnMode = () => {
  switch (manipulationMode.value) {
    case ManipulationMode.SET:
      if(activeEntity.value){
        placeRequest({
          x: highlight.position.x,
          y: highlight.position.y,
          z: highlight.position.z,
          modelId: activeEntity.value.name,
          factoryID: factoryID.value
        }).then(response => response.json())
            .then(id => {
              if(id === -1) return
              if(activeEntity.value) {
                placeEntity(loader, scene, highlight.position, backendUrl + activeEntity.value.modelFile)
                    .then(uuid => {
                      if(activeEntity.value) // ...bruh
                        allPlacedEntities[uuid] = {id: id, orientation: "North", modelId:activeEntity.value.name}
                      console.log(allPlacedEntities)
                    })
              }
            })
            .catch(error => console.error("Es gab einen Fehler:", error));
      }
      break
    case ManipulationMode.MOVE:
      moveRequest(
        {
          x: highlight.position.x,
          y: highlight.position.y,
          z: highlight.position.z,
          id: allPlacedEntities[currentObjectSelected.uuid].id,
          factoryId: factoryID.value
         }
      ).then(response => response.json())
          .then((success: boolean) => {
        if (success) {
          console.log("moved")
          replaceEntity(currentObjectSelected.position, currentObjectSelected, lastObjectSelected)
          manipulationMode.value = ManipulationMode.IDLE
        } else {
          console.log("not moved")
        }
      })
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
  switch (event.key.toUpperCase()) {
    case 'V':

      if(manipulationMode.value === ManipulationMode.ROTATE) {
        rotationRequest({
          id: allPlacedEntities[currentObjectSelected.uuid].id,
          orientation: allPlacedEntities[currentObjectSelected.uuid].orientation,
          factoryId: factoryID.value
        }).then(res => res.json()).then(success => {

          if(!success){
            rotateModelfromXtoY(originalOrientation, allPlacedEntities[currentObjectSelected.uuid].orientation, pivot)
          }
          console.log(success)
        })
      }


      manipulationMode.value = ManipulationMode.IDLE
      showCircMenu.value = false
      if (currentObjectSelected) highlightObjectWithColor(currentObjectSelected, false)
      scene.remove(highlight)
      break

    case 'ESCAPE':
      switch (manipulationMode.value) {
        case ManipulationMode.MOVE:
          // Zurücksetzen
          replaceEntity(currObjSelectedOriginPos, currentObjectSelected, currentObjectSelected)
          manipulationMode.value = ManipulationMode.IDLE
      }
      break

    case 'ARROWLEFT':
      switch (manipulationMode.value) {
        case ManipulationMode.ROTATE:
          rotateModel("left", pivot);
          allPlacedEntities[currentObjectSelected.uuid].orientation = turnLeft(allPlacedEntities[currentObjectSelected.uuid].orientation)
      }
      break

    case 'ARROWRIGHT':
      switch (manipulationMode.value) {
        case ManipulationMode.ROTATE:
          rotateModel("right", pivot);
          allPlacedEntities[currentObjectSelected.uuid].orientation = turnRight(allPlacedEntities[currentObjectSelected.uuid].orientation)
      }
      break

    case 'Q':
      ccm.toggleMode()
      break

    default:
      break
  }

  console.log(allPlacedEntities)
}

const handleMouseMove = (event: MouseEvent) => {
  // Get all intersections with mouse and world
  const intersections = getIntersectionsMouse(event, camera, scene)

  // Update the highlighter
  if (highlight && manipulationMode.value === ManipulationMode.SET) {
    // Object model wird asynchron geladen
    moveHighlight(highlight, ACTIVE_LAYER, intersections)
  } else if (currentObjectSelected && manipulationMode.value === ManipulationMode.MOVE) {
    moveHighlight(currentObjectSelected, ACTIVE_LAYER, intersections)
  } else if (currentObjectSelected && manipulationMode.value === ManipulationMode.CLONE) {
    moveHighlight(currentObjectSelected, ACTIVE_LAYER, intersections)
  }
}

const handleClick = (event: any) => {
  // close circle if is open
  if (showCircMenu.value) {
    showCircMenu.value = false
    if (manipulationMode.value === ManipulationMode.IDLE)
      highlightObjectWithColor(currentObjectSelected, false)
    return
  }

  // Ignore sidebar
  if (event.target.id == 'ignore') return

  // Cirlce events
  clickActionBasedOnMode()
}

const handleContextMenu = (event: MouseEvent) => {
  // Context öffnet sich
  event.preventDefault()
  if (manipulationMode.value !== ManipulationMode.IDLE) return
  const intersections = getIntersectionsMouse(event, camera, scene)
  const result = selectionObject(currentObjectSelected, lastObjectSelected, intersections)
  if (result && typeof result === 'object') {
    const { worked, currObj, lastObj } = result
    if (worked) {
      currentObjectSelected = currObj
      originalOrientation = allPlacedEntities[currentObjectSelected.uuid].orientation;
      console.log(currentObjectSelected)
      console.log(originalOrientation)

      lastObjectSelected = lastObj
      if (dynamicDiv) {
        dynamicDiv.style.left = event.clientX - 50 + 'px'
        dynamicDiv.style.top = event.clientY + 20 + 'px'
        dynamicDiv.style.display = 'block'
      }
      showCircMenu.value = true
    }
  }
  if (currentObjectSelected) currObjSelectedOriginPos = currentObjectSelected.position
}

/**
 * Watcher
 * **/

watch(activeEntity, () => {
  manipulationMode.value = ManipulationMode.SET

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
    console.log(json)
    // Alle entittys sind nun zugänglich für uns
    allEntitys.value = json
    // Active entity ändern
    activeEntity.value = allEntitys.value[0]
  })

  // Load all
  getAllEntitiesInFactory(factoryID.value).then((backendEntitys: IBackendEntity[]) => {
    console.log("load all factorys ", factoryID.value);
    backendEntitys.forEach((backendEntity) => {
      placeEntity(
          loader,
          scene,
          { x: backendEntity.x, y: backendEntity.y, z: backendEntity.z },
          backendUrl + backendEntity.path
      ).then(uuid => {
        allPlacedEntities[uuid] = {id: backendEntity.id, orientation: backendEntity.orientation, modelId: backendEntity.modelId}
        console.log(allPlacedEntities)
      })
    })
  })

  // initial function calls
  animate(0)
})

onUnmounted(() => {
  // remove eventListeners
  factoryImageUpdate(factoryID.value, captureScreenshot()).then((success: boolean) => {
    if (success) console.log("successfully saved image")
    else console.log("didn't save image")
  })

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

  ccm.update(deltaTime)
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
    <div class="debug-bar"></div>
    <EntityBar
      id="ignore"
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
