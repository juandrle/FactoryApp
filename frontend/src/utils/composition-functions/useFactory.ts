import {ref, readonly, type Ref, computed} from 'vue';
import type {IVector3} from "@/types/global";
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

const defaultID: number = 0
const defaultName: string = ''
const defaultSize: IVector3 = { x: 80, y: 120, z: 8 }

const factoryID: Ref<number> = ref(parseWithDefault(localStorage.getItem('factoryID'), defaultID))
const factoryName: Ref<string> = ref(localStorage.getItem('factoryName') || defaultName)
const factorySize: Ref<IVector3> = ref(parseJSONWithDefault<IVector3>(localStorage.getItem('factorySize'), defaultSize))
const isFactoryImageUpToDate: Ref<boolean> = ref(true)

/**
 * Returns an object with methods and computed properties to manage factory values and update them in localStorage.
 *
 *  @returns an object with the following properties:
 *   - factoryID: A computed property that retrieves the value of factoryID from localStorage using a default value if not set.
 *   - factoryName: A computed property that retrieves the value of factoryName from localStorage using a default value if not set.
 *   - factorySize: A computed property that retrieves the value of factorySize from localStorage using a default value if not set.
 *   - updateFactoryID: A method to update the value of factoryID and store it in localStorage.
 *   - updateFactoryName: A method to update the value of factoryName and store it in localStorage.
 *   - updateFactorySize: A method to update the value of factorySize and store it in localStorage.
 */
export function useFactory() {
    const updateValueAndLocalStorage = (valueRef: Ref<any>, newValue: any, key: string) => {
        valueRef.value = newValue;
        localStorage.setItem(key, JSON.stringify(newValue));
    }

    const computedValue = (valueRef: Ref<any>, defaultVal: any, key: string, parseFn: (value: string | null, defaultValue: any) => any) => {
        return computed(() => {
            return JSON.stringify(valueRef.value) === JSON.stringify(defaultVal)
                ? parseFn(localStorage.getItem(key), defaultVal)
                : valueRef.value;
        });
    }
    const toggleIsFactoryImageUpToDate = () => {
        isFactoryImageUpToDate.value = !isFactoryImageUpToDate.value;
    }
    return {
        factoryID: readonly(computedValue(factoryID, defaultID, 'factoryID', parseWithDefault)),
        factoryName: readonly(computedValue(factoryName, defaultName, 'factoryName', (val, defVal) => val || defVal)),
        factorySize: readonly(computedValue(factorySize, defaultSize, 'factorySize', parseJSONWithDefault)),
        isFactoryImageUpToDate: readonly(isFactoryImageUpToDate),
        toggleIsFactoryImageUpToDate,
        updateFactoryID: (newID: number) => updateValueAndLocalStorage(factoryID, newID, 'factoryID'),
        updateFactoryName: (newName: string) => updateValueAndLocalStorage(factoryName, newName, 'factoryName'),
        updateFactorySize: (newSize: IVector3) => updateValueAndLocalStorage(factorySize, newSize, 'factorySize')
    };
}
