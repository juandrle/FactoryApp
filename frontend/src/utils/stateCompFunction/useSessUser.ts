import { ref, readonly, computed } from 'vue';

const defaultSessUser = '';

const sessUser = ref(localStorage.getItem('sessUser') || defaultSessUser);

export function useSessUser() {
    const updateSessUser = (newUser: string) => {
        sessUser.value = newUser;
        localStorage.setItem('sessUser', newUser);
        console.log('Updating sessUser', sessUser.value);
    };

    const computedSessUser = computed(() => {
        return sessUser.value === defaultSessUser
            ? localStorage.getItem('sessUser') || defaultSessUser
            : sessUser.value;
    });

    return {
        sessUser: readonly(computedSessUser),
        updateSessUser,
    };
}
