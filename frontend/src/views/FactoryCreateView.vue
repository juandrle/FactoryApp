<script setup lang="ts">
import { VueElement, ref } from 'vue';
import Button from '../components/Button.vue'
const buttonData = ref([
    {text: 'Fabrik erstellen und bestellen'}]
  )

const sizes = ref([
  { label: 'Size blabla1', value: 'size1', name: 'factory_size' },
  { label: 'Size blabla2', value: 'size2', name: 'factory_size' },
  { label: 'Size blabla3', value: 'size3', name: 'factory_size' },
  { label: 'Size blabla4', value: 'size4', name: 'factory_size' },
]);
</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="content-s-item">
        <img src="/icons8-fabric-96.png"/>
        <p class="logo-titel">Machine Deluxe 3000</p>
      </div>
    </div>
    <div class="m-item">
      <h1 class="game-name">Fabrik erstellen</h1>
      <div class="fabrik-settings">
        <form action="/fabrik" method="">
          <div class="fabrik-name">
            <label for="fabrik-name">Fabrik Name:</label>
            <textarea></textarea>
          </div>
          <div class="size-radio-container">
            <div class="radio-option" v-for="size in sizes" :key="size.value">
              <input
                type="radio"
                :id="size.value"
                :name="size.name"
                :value="size.value"
              />
              <label :for="size.value">{{ size.label }}</label>
            </div>
          </div>
        </form>
      </div>
      <div class="button-container">
        <Button
          class="button-erstellen"
          v-for="item in buttonData"
          :text="item.text"
          :key="item.text"
        ></Button>
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
  background-image: url('../assets/single_rectangles.svg');
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
  top: 10%; 
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0px;
}

/*.size-radio-container .fabrik-name{
  left: 50%;
  bottom: 10%;
  gap: 1rem;
}*/

.radio-option {
  display: flex;
  align-items: center;
}

.radio-option input {
  margin-right: 0.5rem; /* Adjust spacing between radio button and label */
}

.button-erstellen{
    font-weight: 500;
    font-size: 18px;
    position: relative;
    white-space: nowrap;
    width: fit-content;
}

.logo-titel{
  font: normal normal bold 15px/20px Overpass;
  letter-spacing: 0px;
}

.content-s-item {
  display: flex;
  position: absolute;
  align-items: center;
  gap: 10px;
  bottom: 90%;
  top: 5%;
}

.content-s-item img{
  width: 40px;
  height: min-content;
}

.fabrik-settings {
  min-width: 100%; 
  background-image: url('../assets/rectangles.svg');
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: right bottom;
  flex-direction: column;
  display: flex;
}

</style>