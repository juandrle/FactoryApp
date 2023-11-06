<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import threeJS from './components/threeJS.vue'
</script>

<template>
  <header>
    <div class="wrapper">
      <h1>DEMO :)</h1>
      <threeJS></threeJS>
    </div>
  </header>

  <RouterView />
</template>

<style scoped>
</style>
