<script setup lang="ts">
import {RouterView} from 'vue-router'
import {provide, readonly, ref, Ref} from "vue";
import type {IVector3} from "@/types/global";

const factorySize: Ref<IVector3> = ref({x: 80, y: 120, z: 8} as IVector3)
const updateFactorySize = (newSize: IVector3) => {
  factorySize.value = newSize
}
const factoryID: Ref<number> = ref(0)
const updateFactoryID = (newID: number) => {
  factoryID.value = newID
}
provide('factorySize', {
  factorySize: readonly(factorySize as Ref<IVector3>),
  updateFactorySize: updateFactorySize
})
provide('factoryID', {
  factoryID: readonly(factoryID as Ref<number>),
  updateFactoryID: updateFactoryID
})
</script>

<template>
  <RouterView/>
</template>

<style scoped>
</style>
