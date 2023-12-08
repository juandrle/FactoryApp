<template>
  <div>
    <!-- <button :class="{ 'visible': isButtonVisible }" @click="buttonClicked">Click Me</button> -->
    <nav :class="{ 'visible': isButtonVisible }">
        <!-- <input  class="menu-toggler" type="checkbox">
        <label for="menu-toggler"></label> -->
        <ul>
        <li class="menu-item">
            <a class="fa-solid fa-arrows-rotate" @click='rotateClicked'></a>
            <p>Drehen</p>
        </li>
        <li class="menu-item">
            <a class="fa-solid fa-arrows-up-down-left-right" @click='moveClicked'></a>
            <p>Versetzen</p>
        </li>
        <li class="menu-item">
            <a class="fa-solid fa-trash" @click='delClicked'></a>
            <p>Löschen</p>
        </li>
        <li class="menu-item">
            <a class="fa-solid fa-code" @click='scriptClicked'></a>
            <p>Skripten</p>
        </li>
        <li class="menu-item">
            <a class="fa-regular fa-clone" @click='cloneClicked'></a>
            <p>Klonen</p>
        </li>
        
        </ul>
    </nav>
  </div>
</template>

<script setup lang="ts">
import {onMounted, inject, Ref} from 'vue';
const emit = defineEmits<{
  changeEntity: [string];
  delEntity: [true];
  rotateEntity: [true];
  moveEntity: [true];
  scriptEntity: [true];
  cloneEntity: [true];
}>()
const delClicked = () =>{
  emit('delEntity', true)
}
const rotateClicked = () => {
  emit('rotateEntity', true)
}
const moveClicked = () => {
  emit('moveEntity', true)
}
const scriptClicked = () => {
  emit('scriptEntity', true)
}
const cloneClicked = () => {
  emit('cloneEntity', true)
}
const isButtonVisible: Ref<Boolean> = inject('showCircleMenu')
console.log(isButtonVisible.value)

</script>

<style scoped>
/* ul {
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
} */

.visible .menu-item {
  opacity: 1;
}

.visible .menu-item:nth-child(1) {
  transform: rotate(90deg) translate(-150px);
}

.visible .menu-item:nth-child(2) {
  transform: rotate(162deg) translateX(-150px);
}

.visible .menu-item:nth-child(3) {
  transform: rotate(234deg) translateX(-150px);
}

.visible .menu-item:nth-child(4) {
  transform: rotate(306deg) translateX(-150px);
}

.visible .menu-item:nth-child(5) {
  transform: rotate(378deg) translateX(-150px);
}

.visible .menu-item a {
    pointer-events:auto;
  }
.menu-item:nth-child(1) a {
    transform: rotate(-90deg);
  }
  .menu-item:nth-child(2) a {
    transform: rotate(-162deg);
  }
  .menu-item:nth-child(3) a {
    transform: rotate(-234deg);
  }
  .menu-item:nth-child(4) a {
    transform: rotate(-306deg);
  }
  .menu-item:nth-child(5) a {
    transform: rotate(-378deg);
  }
.menu-item {
    position: absolute;
    display: block;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
    width: 120px;
    height: 120px;
    opacity: 0;
    transition: 0.5s;
  }
 .menu-item a {
    display: block;
    width: inherit;
    height: inherit;
    line-height: 100px;
    color: rgba(255, 255, 255, 0.7);
    background: rgba(230, 230, 250, 0.7);
    border-radius: 50%;
    text-align: center;
    text-decoration: none;
    font-size: 50px;
    pointer-events: none;
    transition: 0.2s;
}

.menu-item p{
  font-size: 14px;
  transition: 0.2s;
  text-align: center;
}

.menu-item:nth-child(1) p{
  
  transform: rotate(-90deg) translate(72px) translateY(35px);
}
.menu-item:nth-child(2) p{
  
  transform: rotate(-162deg) translate(23px) translateY(101px);
}
.menu-item:nth-child(3) p{
  
  transform: rotate(-234deg) translate(-55px) translateY(78px);
}
.menu-item:nth-child(4) p{
  
  transform: rotate(-306deg) translate(-56px) translateY(-6px);
}
.menu-item:nth-child(5) p{
  
  transform: rotate(-378deg) translate(22px) translateY(-31px);
}

.menu-item a:hover {
    box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.3);
    color: white;
    background: rgba(255, 255, 255, 0.3);
    font-size: 44.44px
}

.menu-item a:hover + p {
    font-size: 12px;
    text-align: center;
}

</style>
