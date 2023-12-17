<template>
  <div class="entity-bar">
    <div :class="{entity: true, active: activeEntity.id === entities[index].id}" v-for="entity, index in entities" @click="$emit('updateActiveEntity', index)">
      <img :src="backendUrl + entity.icon" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import type { IBackendEntityPreview } from '@/types/backendEntity'
import {backendUrl} from '@/utils/config/config.js'

const props = defineProps<{
  entities: IBackendEntityPreview[],
  activeEntity: IBackendEntityPreview
}>()
</script>

<style>
.entity-bar {
  display: flex;
  flex-direction: column;
  background-color: #f1ece2;
  gap: 12px;
  padding: 10px;
  position: absolute;
  bottom: 260px;
  right: 60px;
  border-radius: 10px;
  box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
}

.entity-bar .entity{
  cursor: pointer;
  color: black;
  width: 50px;
  height: 50px;
  border-radius: 10px;
  padding: 10px;
}

.entity-bar .entity:hover{
  background-color: #c3c8db;
}

.entity-bar .entity.active{
  background-color: #7289da;
}
</style>
