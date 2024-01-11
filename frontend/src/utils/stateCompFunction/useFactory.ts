import {ref, readonly, type Ref, computed} from 'vue';
import type {IVector3} from "@/types/global";

const defaultID = 0;
const defaultName = '';
const defaultSize: IVector3 = { x: 80, y: 120, z: 8 };
function parseWithDefault(value: string | null, defaultValue: number): number {
    const parsed = parseInt(value || '');
    return isNaN(parsed) ? defaultValue : parsed;
}

function parseJSONWithDefault<T>(value: string | null, defaultValue: T): T {
    try {
        return value ? JSON.parse(value) : defaultValue;
    } catch {
        return defaultValue;
    }
}
const factoryID = ref(parseWithDefault(localStorage.getItem('factoryID'), defaultID));
const factoryName = ref(localStorage.getItem('factoryName') || defaultName);
const factorySize = ref(parseJSONWithDefault<IVector3>(localStorage.getItem('factorySize'), defaultSize));

export function useFactory() {
    const updateFactoryID = (newID: number) => {
        factoryID.value = newID
        localStorage.setItem('factoryID', newID.toString())
    };
    const updateFactoryName = (newName: string) => {
        factoryName.value = newName
        localStorage.setItem('factoryName', newName)
    }
    const updateFactorySize = (newSize: IVector3) => {
        factorySize.value = newSize
        localStorage.setItem('factorySize', JSON.stringify(newSize))
    };
    const computedFactoryID = computed(() => {
        return factoryID.value === defaultID
            ? parseWithDefault(localStorage.getItem('factoryID'), defaultID)
            : factoryID.value;
    });

    const computedFactoryName = computed(() => {
        return factoryName.value === defaultName
            ? localStorage.getItem('factoryName') || defaultName
            : factoryName.value;
    });

    const computedFactorySize = computed(() => {
        return JSON.stringify(factorySize.value) === JSON.stringify(defaultSize)
            ? parseJSONWithDefault<IVector3>(localStorage.getItem('factorySize'), defaultSize)
            : factorySize.value;
    });

    return {
        factoryID: readonly(computedFactoryID),
        factoryName: readonly(computedFactoryName),
        factorySize: readonly(computedFactorySize),
        updateFactoryID,
        updateFactoryName,
        updateFactorySize
    };
}
