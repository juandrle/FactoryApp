import { ref, readonly, type Ref } from 'vue';
import type { IVector3 } from "@/types/global";

const factorySize: Ref<IVector3> = ref({x: 80, y: 120, z: 8} as IVector3);

export function useFactorySize() {
    const updateFactorySize = (newSize: IVector3) => {
        factorySize.value = newSize;
    };

    return {
        factorySize: readonly(factorySize),
        updateFactorySize,
    };
}
