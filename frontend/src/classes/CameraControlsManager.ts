import { CameraMode } from '@/enum/CameraMode'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { CustomFlyControls } from './CustomFlyControlls.js'

class CameraControlsManager {
  public mode: CameraMode | null = null
  public controlls: CustomFlyControls | OrbitControls | null = null
  public camera: any = null

  private domElement: any = null

  constructor(camera: any, domElement: any, mode: CameraMode) {
    this.domElement = domElement
    this.camera = camera

    this.switchTo(mode)
  }

  toggleMode() {
    console.log(this.mode)
    if (this.mode == CameraMode.FREE) this.switchTo(CameraMode.ORBIT)
    else this.switchTo(CameraMode.FREE)
  }

  switchTo(mode: CameraMode) {
    // für toggle
    this.mode = mode

    if (this.controlls) this.controlls.dispose()

    switch (mode) {
      case CameraMode.FREE: {
        this.controlls = new CustomFlyControls(this.camera, this.domElement)
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
      this.controlls.update()
    }
  }
}

export { CameraControlsManager }
