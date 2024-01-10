import * as THREE from 'three'
import { PointerLockControls } from './CustomPointerLock'


class CustomFlyControls {
  movementSpeed = 0.5
  movement = null
  camera = null
  currentMouse = null
  prevMouse = null
  rotation = new THREE.Quaternion()
  translation = new THREE.Vector3()
  phi = 0
  theta = 0
  thetaSpeed = 1
  phiSpeed = 1

  controls = null;

  constructor(camera, domElement) {
    this.camera = camera
    this.movement = { x: 0, y: 0, z: 0 }
    this.thetaSpeed = 1
    this.phiSpeed = 1
    this.moved = false;
    this.currentMouse = {
      x: 0,
      y: 0,
      xDelta: 0,
      yDelta: 0
    }

    this.controls = new PointerLockControls(camera, domElement)
    this.controls.lock()

    this.onKeyDown = (event) => {
      switch (event.code) {
        case 'KeyW':
          this.movement.z = -this.movementSpeed
          break
        case 'KeyA':
          this.movement.x = -this.movementSpeed
          break
        case 'KeyS':
          this.movement.z = this.movementSpeed
          break
        case 'KeyD':
          this.movement.x = this.movementSpeed
          break
        case 'ShiftLeft':
          this.movement.y = -this.movementSpeed
          break
        case 'Space':
          this.movement.y = this.movementSpeed
          break
      }
    }

    this.onKeyUp = (event) => {
      switch (event.code) {
        case 'KeyW':
          this.movement.z = 0
          break
        case 'KeyA':
          this.movement.x = 0
          break
        case 'KeyS':
          this.movement.z = 0
          break
        case 'KeyD':
          this.movement.x = 0
          break
        case 'ShiftLeft':
          this.movement.y = 0
          break
        case 'Space':
          this.movement.y = 0
          break
      }
    }

    window.addEventListener('keydown', this.onKeyDown)
    window.addEventListener('keyup', this.onKeyUp)
  }

  dispose() {
    window.removeEventListener('keydown', this.onKeyDown)
    window.removeEventListener('keyup', this.onKeyUp)

    this.controls.unlock()
    this.controls.dispose()
  }

  update() {
    this.camera.translateX(this.movement.x)
    this.camera.translateY(this.movement.y)
    this.camera.translateZ(this.movement.z)
  }
}

export { CustomFlyControls }
