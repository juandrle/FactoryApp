<script setup lang="ts">
// import { RouterLink, RouterView } from 'vue-router'
import factory from './components/factory.vue'
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <header>

  </header>

  <div class="wrapper">
    <factory grid_width="120" grid_lenght="200" grid_height="20" />
  </div>
  <RouterView />
</template>

<style scoped></style>
