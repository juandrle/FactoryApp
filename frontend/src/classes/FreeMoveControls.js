import { FlyControls } from 'three/addons/controls/FlyControls.js'

class FreeMoveControls extends FlyControls {
  constructor(camera, domElement) {
    super(camera, domElement);
    this.movementSpeed = 10
    this.dragToLook = true
    this.rollSpeed = 0.3
    // controlls.movementSpeed = 10
    // controlls.dragToLook = true
    // controlls.rollSpeed = 0.3
  }
}

export { FreeMoveControls };
