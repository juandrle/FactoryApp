import { CameraMode } from '@/enum/CameraMode'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { CustomFlyControls } from './CustomFlyControls.js'
import type { ICameraInfo } from '@/types/global.js'
import { ExtractCameraInfo, SetCameraInfo } from '@/utils/threeJS/camera.js'

class CameraControlsManager {
  public currentMode: CameraMode | null = null
  public controls: CustomFlyControls | OrbitControls | null = null
  public camera: any = null
  public orbitCameraInfos: ICameraInfo | null = null
  public freeCameraInfos: ICameraInfo | null = null
  private readonly domElement: any = null

  constructor(camera: any, domElement: any, mode: CameraMode) {
    this.domElement = domElement
    this.camera = camera
    this.switchTo(mode)
  }

  switchTo(newMode: CameraMode, ref?: any) {
    switch (this.currentMode) {
      case CameraMode.ORBIT:
        this.orbitCameraInfos = ExtractCameraInfo(this.camera)
        break

      case CameraMode.FREE:
        this.freeCameraInfos = ExtractCameraInfo(this.camera)
        break
    }

    if (this.controls) {
      this.controls.dispose()
    }

    this.currentMode = newMode

    switch (this.currentMode) {
      case CameraMode.FREE: {
        if (this.freeCameraInfos) {
          SetCameraInfo(this.camera, this.freeCameraInfos)
        } else {
          SetCameraInfo(this.camera, {
            position: { x: 0, y: -10, z: 1 },
            up: { x: 0, y: 0, z: 1 },
            lookAt: { x: 0, y: 1, z: 1 }
          })
        }

        this.controls = new CustomFlyControls(this.camera, this.domElement)
        break
      }

      case CameraMode.ORBIT: {
        if (this.orbitCameraInfos) SetCameraInfo(this.camera, this.orbitCameraInfos)

        this.controls = new OrbitControls(this.camera, this.domElement)
        break
      }
    }

    if(ref)
      ref.value = this.currentMode;
  }

  update(deltaTime: number) {
    if (this.currentMode === CameraMode.FREE) {
      this.controls.update(deltaTime)
    }
  }

  toggleMode(ref?: any) {
    if(this.currentMode == CameraMode.FREE)
      this.switchTo(CameraMode.ORBIT, ref)
    else
      this.switchTo(CameraMode.FREE, ref)
  }
}

export { CameraControlsManager }
