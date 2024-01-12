<script setup lang="ts">
import {RouterView} from 'vue-router'
import {useSessUser} from "@/utils/stateCompFunction/useSessUser";
import {ref, watch} from "vue";
import LogoutAlert from "@/components/ui/LogoutAlert.vue";

const showLogoutPopup = ref(false)

watch(useSessUser().remainingTime, (newValue) => {
  if (newValue < 6000 * 5) showLogoutPopup.value = true
})
watch(showLogoutPopup, (newValue) => {
  console.log(newValue)
  if (!newValue) useSessUser().resetTimer()
})
</script>

<template>
  <div class="fixed inset-x-0 bottom-0 px-4 py-3 flex justify-center items-center z-50">
    <LogoutAlert v-if="showLogoutPopup" :time-left-till-logout="useSessUser().remainingTime" id="ignore" @click.stop="showLogoutPopup = false"></LogoutAlert>
  </div>
  <RouterView/>
</template>

<style scoped>
</style>
