import {readonly, type Ref, ref} from "vue";
import {useFactory} from "@/utils/composition-functions/useFactory";

const errorMessage: Ref<string> = ref('')
const showErrorMessage: Ref<boolean> = ref(false)

export function useError() {
    const updateErrorMessage = (newMessage: string, situation?: string) => {
        errorMessage.value = newMessage
        switch (situation) {
            case 'factoryMissing':
                errorMessage.value += ` because factory ${useFactory().factoryName.value} doesn't exist`
                break
            default:
                break
        }
        showErrorMessage.value = !showErrorMessage.value
    }
    const toggleShowErrorMessage = () => {
        showErrorMessage.value = !showErrorMessage.value
    }

    return {
        errorMessage: readonly(errorMessage),
        showErrorMessage: readonly(showErrorMessage),
        updateErrorMessage,
        toggleShowErrorMessage
    }
}