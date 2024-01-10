<template>
  <div class="menu-item menu-item2" id="ignore">
    <a href="#">
      <div class="dot" :class="{ active: activeEntity.type === category.type }" />
      <img class="menu-item-img" :src="backendUrl + category.icon" alt="test" />
      <div v-show="!isOpen" class="entity-name">{{ category.name }}</div>
    </a>
    <div class="sub-menu" v-if="isOpen">
      <div class="triangle"></div>
      <div
        v-for="(item, i) in category.items"
        :key="i"
        class="menu-item"
        :class="{ active: activeEntity.name === item.name }"
        @click="$emit('updateActiveEntity', item.name)"
      >
        <div class="dot" :class="{ active: activeEntity.name === item.name }" />
        <img :src="backendUrl + item.icon" alt="icon" />
        <p class="item-name">{{ item.name.charAt(0).toUpperCase() + item.name.slice(1) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { backendUrl } from '@/utils/config/config'

const { category, isOpen, activeEntity } = defineProps(['category', 'isOpen', 'activeEntity'])
</script>

<style>
.entity-name {
  font-size: 0.85em;
  display: none;
  position: absolute;
  bottom: 62%;
  left: 70%;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  border-radius: 1.5em;
  text-align: center;
  width: fit-content;
  word-wrap: break-word;
  user-select: none;
}

.menu-item2 .dot {
  top: 0;
  left: 0;
  position: absolute;
  display: none;
  width: 10px;
  height: 10px;
  background-color: black;
  border-radius: 10px;
}

.menu-item2 .dot.active {
  display: block;

}

.menu-item .triangle {
  position: absolute;
  border-style: solid;
  border-width: 1.5em 0 1.5em 2em;
  border-color: transparent transparent transparent #ffffff;
  left: -1.5em;
  top: 2.4em;
  transform: rotate(180deg) scaleX(175%);
}

.menu-item .sub-menu {
  cursor: pointer;
  position: absolute;
  background-color: #ffffff;
  left: 100px;
  top: 0;
  padding: 20px;
  border-radius: 1em;
  display: flex;
  flex-wrap: wrap;
}

.sub-menu {
  display: grid !important;
  grid-template-columns: 1fr 1fr 1fr;
}

.menu-item .sub-menu {
  gap: 5px 20px;
}

.menu-item .sub-menu p {
  margin: 0;
  user-select: none;
}

.menu-item {
  color: #000000;
  position: relative;
  text-align: center;
  display: flex;
  align-items: center;
  gap: 20px;
  margin: 10px;
  padding: 10px 5px 10px 5px;
}


nav .menu-item:hover {
  background-color: #d7d7d7;
  border-radius: 1em;
}

nav .menu-item a {
  color: inherit;
  text-decoration: none;
}

img {
  width: 3em;
  height: fit-content;
  margin-top: 3%;
  user-select: none;
}

.menu-item-img {
  margin: auto 0 ;
  padding: 5px;
}
.menu-item img {
  margin: 0;
}

a:hover {
  background-color: transparent; /* war die ganze zeit so komisch grün */
}
</style>
