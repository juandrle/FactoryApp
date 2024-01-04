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
