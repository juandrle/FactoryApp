<script setup lang="ts">
import { ref } from 'vue';
import Button from '../components/ui/Button.vue'
  const buttonData = ref([
    {text: 'Fabrik erstellen', link: "/create"},
    {text: 'Fabrik beitreten', link:"/enter"}, 
    {text: 'Einstellungen', link:"/settings"}
    ])
</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="button-container">
        <Button v-for="item in buttonData" :text="item.text" :link="item.link"></Button>
      </div>
    </div>
    <div class="m-item"><h1 class="game-name">Machine Deluxe 3000</h1></div>
    <div class="s-item"></div>
  </div> 
</template>

<style>
.container {
  display: flex; 
  min-width: 100vw;
  min-height: 100vh;
  background-image: url('../assets/rectangles.svg');
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: right bottom;
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
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0;
}
</style>