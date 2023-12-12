import { CameraMode } from '@/enum/CameraMode'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { CustomFlyControls } from './CustomFlyControlls.js'
import type { ICameraInfos } from '@/types/global.js'
import { ExtractCameraInfo, SetCameraInfo } from '@/utils/threeJS/camera.js'

class CameraControlsManager {
  public currentMode: CameraMode | null = null
  public controlls: CustomFlyControls | OrbitControls | null = null
  public camera: any = null
  public orbitCameraInfos: ICameraInfos | null = null
  private domElement: any = null

  constructor(camera: any, domElement: any, mode: CameraMode) {
    this.domElement = domElement
    this.camera = camera
    this.switchTo(mode)
  }

  toggleMode() {
    if (this.currentMode == CameraMode.FREE) this.switchTo(CameraMode.ORBIT)
    else this.switchTo(CameraMode.FREE)
  }

  switchTo(newMode: CameraMode) {
    if (this.currentMode === CameraMode.ORBIT) {
      console.log(this.camera.position, '1')
      this.orbitCameraInfos = ExtractCameraInfo(this.camera)
    }

    if (this.controlls) {
      this.controlls.dispose()
    }

    this.currentMode = newMode

    switch (newMode) {
      case CameraMode.FREE: {
        SetCameraInfo(this.camera, {
          position: { x: 0, y: 0, z: 1 },
          up: { x: 0, y: 0, z: 1 },
          lookAt: { x: 0, y: 1, z: 1 }
        })

        this.controlls = new CustomFlyControls(this.camera, this.domElement)
        break
      }

      case CameraMode.ORBIT: {
        if (this.orbitCameraInfos) SetCameraInfo(this.camera, this.orbitCameraInfos)

        this.controlls = new OrbitControls(this.camera, this.domElement)
        break
      }
    }
  }

  update() {
    if (this.currentMode === CameraMode.FREE) {
      this.controlls.update()
    }
  }
}

export { CameraControlsManager }
