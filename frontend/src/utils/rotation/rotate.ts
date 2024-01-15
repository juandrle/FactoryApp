export const rotateModel = (dir: string, object: THREE.Object3D)=> {
    if(dir === "left") {
        object.rotation.z -= Math.PI / 2
    } else {
        object.rotation.z += Math.PI / 2
    } //lul
}

export const rotateModelFromXtoY = (from: string, to: string, object: THREE.Object3D) => {
    let directions = ["North", "East", "South", "West"];
    let toIndex = directions.indexOf(to);
    let fromIndex = directions.indexOf(from);
    let rotations = (toIndex - fromIndex + 4) % 4;

    for(let i = 0; i< rotations; i++) {
        console.log("turning");
        rotateModel("left", object)
    }

}
