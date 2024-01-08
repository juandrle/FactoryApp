<script setup lang="ts">
import {RouterView, useRoute} from 'vue-router'
import {provide, readonly, ref, type Ref, watch} from "vue";
import type {IVector3} from "@/types/global";
const route = useRoute();

watch(() => route.path, (newPath, oldPath) => {
  console.log('Route changed from', oldPath, 'to', newPath);
  // You can add your logic here to react to the route change
});
</script>

<template>
  <RouterView/>
</template>

<style scoped>
</style>
