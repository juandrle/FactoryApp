<script setup lang="ts">
import {RouterView} from 'vue-router'
import {useSessionUser} from "@/utils/composition-functions/useSessionUser";
import {type Ref, ref, watch} from "vue";
import LogoutAlert from "@/components/alert/LogoutAlert.vue";
import ErrorAlert from "@/components/alert/ErrorAlert.vue";
import {useError} from "@/utils/composition-functions/useError";

const showLogoutPopup: Ref<boolean> = ref(false)
const showErrorMessage: Readonly<Ref<boolean>> = useError().showErrorMessage

watch(useSessionUser().remainingTime, (newValue) => {
  if (newValue < 6000 * 5) showLogoutPopup.value = true
})
watch(showLogoutPopup, (newValue) => {
  if (!newValue) useSessionUser().manageLogoutTimer()
})
</script>

<template>
  <LogoutAlert v-if="showLogoutPopup" :time-left-till-logout="useSessionUser().remainingTime"
               @click.stop="showLogoutPopup = false"></LogoutAlert>
  <ErrorAlert v-if="showErrorMessage" :error-message="useError().errorMessage.value"
              @click.stop="useError().toggleShowErrorMessage()"></ErrorAlert>
  <RouterView/>
</template>

<style scoped>
</style>
