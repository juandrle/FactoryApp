import {ref, readonly, computed, onMounted} from 'vue';
import {logoutUser} from "@/utils/backendComms/postRequests";
import router from "@/router";

const defaultSessUser = ''
const logoutDelay = 60000 * 35 // 35 minutes
const updateInterval = 1000 // Update every 1 second

const sessUser = ref(localStorage.getItem('sessUser') || defaultSessUser)
let logoutTimer: any
const remainingTime = ref(logoutDelay)
export function useSessUser() {
    const startLogoutTimer = () => {
        clearTimeout(logoutTimer)
        remainingTime.value = logoutDelay
        const updateRemainingTime = () => {
            remainingTime.value -= updateInterval;
            if (remainingTime.value < 0) {
                logout()
            }
            localStorage.setItem('currentTime', Date.now().toString())
        }
        logoutTimer = setInterval(updateRemainingTime, updateInterval)
    }

    const resetTimer = () => {
        startLogoutTimer();
    }
    const updateSessUser = (newUser: string) => {
        sessUser.value = newUser
        localStorage.setItem('sessUser', newUser)
        if (newUser !== defaultSessUser) startLogoutTimer()
    }

    const computedSessUser = computed(() => {
        return sessUser.value === defaultSessUser
            ? localStorage.getItem('sessUser') || defaultSessUser
            : sessUser.value
    })
    onMounted(() => {
        const lastActivityTime = parseInt(localStorage.getItem('currentTime') || '0', 10);
        const currentTime = Date.now();
        const elapsedTime = currentTime - lastActivityTime;
        if (elapsedTime < logoutDelay) return
            logout()

    })
    const logout = async () => {
        switch (await logoutUser()) {

            case "logout successful":
                updateSessUser('')
                await router.push('/login')
        }
    }
    return {
        sessUser: readonly(computedSessUser),
        remainingTime: readonly(remainingTime),
        updateSessUser,
        resetTimer,
        logout
    };
}
