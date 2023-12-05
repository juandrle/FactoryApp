import { CameraMode } from '@/enum/CameraMode'
import { FlyControls } from 'three/addons/controls/FlyControls.js'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'

class CameraControlsManager {

  public mode: CameraMode | null = null
  public controlls: FlyControls | OrbitControls | null = null
  private domElement: any = null
  public camera: any = null

  constructor(camera: any, domElement: any, mode: CameraMode) {
    this.domElement = domElement
    this.camera = camera

    this.switchTo(mode)
  }

  toggleMode() {
    if (this.mode == CameraMode.FREE) this.switchTo(CameraMode.ORBIT)
    else this.switchTo(CameraMode.FREE)
  }

  switchTo(mode: CameraMode) {
    this.mode = mode // für toggle
    switch (mode) {
      case CameraMode.FREE: {
        // Bro keine ahnung fixed den bug wenn das es nach dem toggeln immer schneller wird 
        // vorher wurde einfach die camera ausgewechselt (siehe alte commits)
        // Dispose removed alle event listener
        this.controlls.dispose()

        // change cam
        this.controlls = new FlyControls(this.camera, this.domElement)

        // Config
        this.controlls.movementSpeed = 50
        this.controlls.dragToLook = false
        this.controlls.rollSpeed = 0.3
        break
      }

      case CameraMode.ORBIT: {
        this.controlls = new OrbitControls(this.camera, this.domElement)
        break
      }
    }
  }

  update() {
    if (this.mode === CameraMode.FREE) {
      this.controlls.update(0.05)
    }
  }
}

export { CameraControlsManager }
