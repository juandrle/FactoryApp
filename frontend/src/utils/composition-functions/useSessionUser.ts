import {ref, readonly, computed, onMounted, type Ref} from 'vue';
import {logoutUser} from "@/utils/backend-communication/postRequests";
import router from "@/router";
import { useError } from '@/utils/composition-functions/useError'

const defaultSessionUser = ''
const logoutDelay: number = 60000 * 35 // 35 minutes
const updateInterval: number = 1000 // Update every 1 second
const sessionUser: Ref<string> = ref(localStorage.getItem('sessUser') || defaultSessionUser)
let logoutTimer: any
const remainingTime = ref(logoutDelay)

/**
 * Retrieves and manages the session user information.
 *
 * @return - An object containing the following methods and properties:
 *   - sessionUser: The session user value.
 *   - remainingTime: The remaining time in milliseconds until logout.
 *   - updateSessionUser: Updates the session user value.
 *   - manageLogoutTimer: Manages the timeout for automatic logout.
 *   - performLogout: Performs the logout operation.
 */
export function useSessionUser() {
    const manageLogoutTimer = () => {
        clearTimeout(logoutTimer)
        remainingTime.value = logoutDelay
        const updateRemainingTime = () => {
            remainingTime.value -= updateInterval;
            if (remainingTime.value < 0) {
                performLogout()
            }
            localStorage.setItem('currentTime', Date.now().toString())
        }
        logoutTimer = setInterval(updateRemainingTime, updateInterval)
    }

    const updateSessionUser = (newUser: string) => {
        sessionUser.value = newUser
        localStorage.setItem('sessionUser', newUser)
        if (newUser !== defaultSessionUser) manageLogoutTimer()
    }

    const getSessionUser = computed(() => {
        return sessionUser.value === defaultSessionUser
            ? localStorage.getItem('sessionUser') || defaultSessionUser
            : sessionUser.value
    })

    onMounted(() => {
        const lastActivityTime = parseInt(localStorage.getItem('currentTime') || '0', 10);
        const currentTime = Date.now();
        const elapsedTime = currentTime - lastActivityTime;
        if (elapsedTime > logoutDelay) {
            performLogout().then(() => {
                localStorage.setItem('currentTime', Date.now().toString())
            })
        }
    })

    const performLogout = async () => {
        switch (await logoutUser()) {
            case "logout successful":
                updateSessionUser('')
                localStorage.setItem('sessionUser', defaultSessionUser)
                console.log("user: ", sessionUser.value)
                await router.push('/login')
        }
    }

    return {
        sessionUser: readonly(getSessionUser),
        remainingTime: readonly(remainingTime),
        updateSessionUser,
        manageLogoutTimer,
        performLogout
    };
}