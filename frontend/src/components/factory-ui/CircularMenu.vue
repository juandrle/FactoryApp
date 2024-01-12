<template>
  <div>
    <nav :class="{ 'visible': isButtonVisible }">
      <ul>
        <li class="menu-item">
          <div class="menu-content" :class="{ 'hoverable': hoverEnabled }" @click='rotateClicked'>
            <a class="fa-solid fa-arrows-rotate"></a>
            <p>Rotate</p>
          </div>
        </li>
        <li class="menu-item">
          <div class="menu-content" :class="{ 'hoverable': hoverEnabled }" @click='moveClicked'>
            <a class="fa-solid fa-arrows-up-down-left-right"></a>
            <p>Move</p>
          </div>
        </li>
        <li class="menu-item">
          <div class="menu-content" :class="{ 'hoverable': hoverEnabled }" @click='delClicked'>
            <a class="fa-solid fa-trash"></a>
            <p>Delete</p>
          </div>
        </li>
        <li class="menu-item">
          <div class="menu-content" :class="{ 'hoverable': hoverEnabled }" @click='scriptClicked'>
            <a class="fa-solid fa-code"></a>
            <p>Script</p>
          </div>
        </li>
        <li class="menu-item">
          <div class="menu-content" :class="{ 'hoverable': hoverEnabled }" @click='cloneClicked'>
            <a class="fa-regular fa-clone"></a>
            <p>Clone</p>
          </div>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup lang="ts">

import {ref, watch} from "vue";

const props = defineProps<{
  isButtonVisible: Boolean
}>()
const emit = defineEmits<{
  changeEntity: [string];
}>()
const delClicked = () => {
  if (props.isButtonVisible)
    emit('changeEntity', 'delete')
}
const rotateClicked = () => {
  if (props.isButtonVisible)
    emit('changeEntity', 'rotate')
}
const moveClicked = () => {
  if (props.isButtonVisible)
    emit('changeEntity', 'move')
}
const scriptClicked = () => {
  if (props.isButtonVisible)
    emit('changeEntity', 'script')
}
const cloneClicked = () => {
  if (props.isButtonVisible)
    emit('changeEntity', 'clone')
}
const hoverEnabled = ref(false);

watch(() => props.isButtonVisible, (newValue) => {
  if (newValue) {
    setTimeout(() => {
      hoverEnabled.value = true;
    }, 500);
  } else {
    hoverEnabled.value = false;
  }
});

</script>

<style scoped>

.visible .menu-item {
  opacity: 1;
  user-select: none;
}

.visible .menu-item a {
  pointer-events: auto;
}

.visible .menu-item:nth-child(1) {
  transform: translate(-50%, -50%) rotate(18deg) translateX(-150px);
}

.visible .menu-item:nth-child(2) {
  transform: translate(-50%, -50%) rotate(90deg) translateX(-150px);
}

.visible .menu-item:nth-child(3) {
  transform: translate(-50%, -50%) rotate(162deg) translateX(-150px);
}

.visible .menu-item:nth-child(4) {
  transform: translate(-50%, -50%) rotate(234deg) translateX(-150px);
}

.visible .menu-item:nth-child(5) {
  transform: translate(-50%, -50%) rotate(306deg) translateX(-150px);
}

.menu-item:nth-child(1) .menu-content {
  transform: rotate(-18deg);
}

.menu-item:nth-child(2) .menu-content {
  transform: rotate(-90deg);
}

.menu-item:nth-child(3) .menu-content {
  transform: rotate(-162deg);
}

.menu-item:nth-child(4) .menu-content {
  transform: rotate(-234deg);
}

.menu-item:nth-child(5) .menu-content {
  transform: rotate(-306deg);
}

.menu-item {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80px;
  height: 120px;
  opacity: 0;
  transition: transform 0.5s, opacity 0.5s;
}

.menu-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(230, 230, 250, 0.7);
  transition: 0.2s;
  user-select: none;
}

.menu-content.hoverable:hover {
  box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.3);
  cursor: pointer;
}

.menu-content.hoverable:hover a {
  color: white;
  font-size: 44.44px;
}

.menu-content.hoverable:hover p {
  font-size: 12px;
  color: white;
}

.menu-content a {
  font-size: 50px;
  color: rgba(255, 255, 255, 0.7);
  position: absolute;
  top: 30px;
}

.menu-content p {
  position: absolute;
  font-size: 14px;
  text-align: center;
  bottom: 0;
}

ul {
  list-style: none;
  padding: 0;
}
</style>
