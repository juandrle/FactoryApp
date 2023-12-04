import { CameraMode } from '@/enum/CameraMode'
import { FlyControls } from 'three/addons/controls/FlyControls.js'

class FreeMoveControls extends FlyControls {
  private movementSpeed: number = 50
  private dragToLook: boolean = false
  private rollSpeed: number = 0.3

  constructor(camera: any, domElement: any, mode: CameraMode) {
    super(camera, domElement)
    this.switchTo(mode) 
  }

  switchTo(mode: CameraMode) {
    switch (mode) {
      case CameraMode.FREE: {
        break
      }

      case CameraMode.ORBIT: {
        break
      }
    }

    console.log(mode)
  }
}

export { FreeMoveControls }
