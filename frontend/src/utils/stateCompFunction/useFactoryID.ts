import { ref, readonly, type Ref } from 'vue';

const factoryID: Ref<number> = ref(0);

export function useFactoryID() {
    const updateFactoryID = (newID: number) => {
        factoryID.value = newID;
    };

    return {
        factoryID: readonly(factoryID),
        updateFactoryID,
    };
}
