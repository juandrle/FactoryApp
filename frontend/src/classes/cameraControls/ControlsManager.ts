import { ControlMode } from '@/enum/ControlMode'
import { CameraControlsManager } from './CameraControlsManager'
import { CameraMode } from '@/enum/CameraMode'

class ControlsManager {
  public currentMode: ControlMode | null = null
  public ccm: CameraControlsManager;

  constructor(camera: any, domElement: any) {
    this.ccm = new CameraControlsManager(camera, domElement, CameraMode.ORBIT)
  }

  toggleMode() {
    if (this.currentMode == ControlMode.ANIMATION) this.switchTo(ControlMode.EDIT)
    else this.switchTo(ControlMode.ANIMATION)
  }

  switchTo(newMode: ControlMode) {

    this.currentMode = newMode;

    switch (this.currentMode) {
      case ControlMode.ANIMATION:
        this.ccm.switchTo(CameraMode.FREE)
        break
      case ControlMode.EDIT:
        this.ccm.switchTo(CameraMode.ORBIT)
        break
    }
  }

  update(deltaTime: number){
    this.ccm.update(deltaTime)
  }
}

export { ControlsManager }

