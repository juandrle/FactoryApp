<script setup lang="ts">
import {RouterView} from 'vue-router'
import {provide, readonly, ref, Ref} from "vue";
import type {IVector3} from "@/types/global";

const factorySize: Ref<IVector3> = ref({x: 80, y: 120, z: 8} as IVector3)
const updateFactorySize = (newSize: IVector3) => {
  factorySize.value = newSize
}
provide('factorySize', {
  factorySize: readonly(factorySize as Ref<IVector3>),
  updateFactorySize: updateFactorySize
})
</script>

<template>
  <RouterView/>
</template>

<style scoped>
</style>
