<script setup lang="ts">
import { VueElement, ref } from 'vue';
import Button from '../components/Button.vue'
const buttonData = ref([
    {text: 'Fabrik erstellen und bestellen'}])
</script>

<template>
  <div class="container">
    <div class="s-item">
        <div class="logo"><img src="" alt=""></div>
    </div>
    <div class="m-item"><h1 class="game-name">Fabrik erstellen</h1>
        <div class="button-container">
        <Button v-for="item in buttonData" :text="item.text"></Button>
      </div>
    </div>
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
  letter-spacing: 0px;
}
/* .container .s-item .svg-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
} */
</style>