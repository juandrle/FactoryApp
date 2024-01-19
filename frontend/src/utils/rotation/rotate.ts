export const turnLeft = (orientation: string): string => {
    switch (orientation) {
        case "North":
            return "West";
        case "West":
            return "South";
        case "South":
            return "East";
        case "East":
            return "North";
        default:
            return orientation;
    }
};

export const turnRight = (orientation: string): string => {
    switch (orientation) {
        case "North":
            return "East";
        case "East":
            return "South";
        case "South":
            return "West";
        case "West":
            return "North";
        default:
            return orientation;
    }
};

export const rotateModel = (dir: string, object: any)=> {
    // adjusted rotation for usability (right key to rotate clockwise)
    if(dir === "left") {
        object.rotation.z += Math.PI / 2
    } else {
        object.rotation.z -= Math.PI / 2
    } //lul
}

export const rotateModelfromXtoY = (from: string, to: string, object: any) => {
    let directions = ["North", "East", "South", "West"];
    let toIndex = directions.indexOf(to);
    let fromIndex = directions.indexOf(from);
    let rotations = (toIndex - fromIndex + 4) % 4;

    for(let i = 0; i< rotations; i++) {
        console.log("turning");
        rotateModel("left", object)
    }

}
