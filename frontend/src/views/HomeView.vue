<script setup lang="ts">
import { VueElement, ref } from 'vue';
import Button from '../components/Button.vue'
  const buttonData = ref([
    {text: 'Fabrik erstellen'}, 
    {text: 'Fabrik beitreten'}, 
    {text: 'Einstellungen'}
    ])
</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="button-container">
        <Button v-for="item in buttonData" :text="item.text"></Button>
      </div>
    </div>
    <div class="m-item"><h1 class="game-name">Machine Deluxe 3000</h1></div>
    <div class="s-item"><img src="../assets/rectangles.svg" alt="" style="transform: translate(30px, 30px); width: 100%;"></div>
  </div> 
</template>

<style>
.container {
  display: flex; 
  min-width: 100vw;
  min-height: 100vh;
}
.container .s-item {
  display: flex;
  flex: 1 1 25%;
  position: relative;
  align-items: flex-end; 
  padding: 2rem;
}
.container .m-item {
  flex: 1 1 50%; 
  display: flex;
  justify-content: center;
  /* border: solid 1px green; */
}
.button-container{
  display: flex;
  flex-direction: column;
  position: absolute; 
  left: 45%;
  bottom: 15%; 
  gap: 1rem;
}
.game-name{
  position: absolute;
  top: 20%;
  letter-spacing: 1.5px;
  font-size: 40px;
  font-weight: 800;
}
</style>