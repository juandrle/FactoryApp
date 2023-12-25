<template>
  <div class="entity-bar">
    <div :class="{entity: true, active: activeEntity.id === entities[index].id}"
         v-for="entity, index in entities" @click="$emit('updateActiveEntity', index)">
      <img id="ignore" :src="backendUrl + entity.icon" />
      <div class="entity-name">{{ entity.name }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import type { IBackendEntityPreview } from '@/types/backendEntity'
import {backendUrl} from '@/utils/config/config'

const props = defineProps<{
  entities: IBackendEntityPreview[],
  activeEntity: IBackendEntityPreview
}>()

</script>

<style>
.entity-bar {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(50px, 1fr));
  gap: 12px;
  padding: 10px;
  position: absolute;
  bottom: 260px;
  right: 60px;
  border-radius: 10px;
  box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
  background-color: #f1ece2;
  overflow-y: auto;
  max-height: 500px;
}

.entity-bar .entity {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  padding: 10px;
  border-radius: 10px;
  background-color: #f1ece2;
  transition: background-color 0.3s;
}

.entity-bar .entity img {
  width: 40px;
  height: 40px;
}

.entity-bar .entity .entity-name {
  font-size: 8px;
  display: none;
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 5px;
  border-radius: 5px;
  text-align: center;
  max-width: 80px;
  word-wrap: break-word;
}

.entity-bar .entity:hover .entity-name {
  display: block;
}

.entity-bar .entity:hover {
  background-color: #c3c8db;
}

.entity-bar .entity.active {
  background-color: #7289da;
}

</style>
