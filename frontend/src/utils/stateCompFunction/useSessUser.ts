import { ref, readonly, type Ref } from 'vue';

const sessUser: Ref<string> = ref('');

export function useSessUser() {
    const updateSessUser = (newUser: string) => {
        sessUser.value = newUser;
        console.log('Updating sessUser', sessUser.value);
    };

    return {
        sessUser: readonly(sessUser),
        updateSessUser,
    };
}
