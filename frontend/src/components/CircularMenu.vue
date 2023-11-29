<script setup lang="ts">
import { ref } from 'vue';

const isMenuOpen = ref(false);

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value;
}
</script>


<template>
  <div class="menu-container">
    <div class="menu-wrapper">
      <a href="#" class="menu-button button-1" style="background-color: #F44336">
        <div></div>
      </a>

      <a href="#" class="menu-button button-2" style="background-color: #E91E63">
        <div></div>
      </a>

      <a href="#" class="menu-button button-3" style="background-color: #9C27B0">
        <div></div>
      </a>

      <a href="#" class="menu-button button-4" style="background-color: #673AB7">
        <div></div>
      </a>

      <a href="#" class="menu-button button-5" style="background-color: #3F51B5">
        <div></div>
      </a>
    </div>
  </div>
</template>

<style scoped>
.menu-container {
  display: flex;
  align-items: center;
  justify-content: center;
}
.menu-button {
  display: block;
  height: 130px;
  width: 130px;
  position:relative;
  border-radius: 50%;
  background-color: #E3DFD2;
  
  overflow: hidden;
  /*transform: translate(-50%, -50%) scale(0);*/
  transition: transform 0.3s, opacity 0.3s;
}

.menu-button div {
  width: 100%;
  height: 100%;
  background-size: cover;
  position:relative;
  background-position: center;
  transition: opacity 0.5s;
  opacity: 0;
}

.menu-wrapper a:hover {
  scale:1.1;
}

.menu-wrapper a:hover div {
  opacity: 1;
}

.menu-button:nth-child(1) {
  transform:translate(0%, -10%) scale(1);
  transform-origin: 50% 100%;
}

.menu-button:nth-child(2) {
  transform:translate(-150%, 20%) scale(1);
  transform-origin: 20% 80%;
}

.menu-button:nth-child(3) {
  transform: translate(150%, -80%) scale(1);
  transform-origin: 0% -5%;
}

.menu-button:nth-child(4) {
  
  transform: translate(-95%, 0%) scale(1);
  transform-origin: 50% -50%;
}
.menu-button:nth-child(5) {
  
  transform: translate(105%, -100%) scale(1);
  transform-origin: 60% -110%;
}

</style>