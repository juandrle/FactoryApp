import * as THREE from 'three';

export const getIntersectionsMouse = (mouseMoveEvent: MouseEvent, camera: any, scene: any) => {
  const raycaster = new THREE.Raycaster()
  raycaster.setFromCamera(
    new THREE.Vector2(
      (mouseMoveEvent.clientX / window.innerWidth) * 2 - 1,
      -(mouseMoveEvent.clientY / window.innerHeight) * 2 + 1
    ),
    camera
  )
  return raycaster.intersectObjects(scene.children)
}
