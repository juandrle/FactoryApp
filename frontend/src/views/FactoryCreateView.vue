<script setup lang="ts">
import { VueElement, ref } from 'vue';
import Button from '../components/Button.vue'
const buttonData = ref([
    {text: 'Fabrik erstellen und bestellen'}]
  )

const sizes = ref([
  { label: '10 m²', value: 'size1', name: 'factory_size' },
  { label: '25 m²', value: 'size2', name: 'factory_size' },
  { label: '50 m²', value: 'size3', name: 'factory_size' },
  { label: '100 m²', value: 'size4', name: 'factory_size' },
]);
</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="content-s-item">
        <img src="/icons8-fabric-96.png" width="20px" height="auto"/>
        <p class="logo-titel">Machine Deluxe 3000</p>
      </div>
    </div>
    <div class="m-item">
      <h1 class="game-name">Fabrik erstellen</h1>
      <div class="fabrik-settings">
        <form action="/fabrik" method="">
          <div class="fabrik-name">
            <textarea class="text-input" placeholder="Name"></textarea>
            <textarea class="text-input" placeholder="Passwort"></textarea>
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

form {
  background-color: #342844; /* hier eigentlich das rectangle im hintergrund */
  padding: 1.875rem 1.125rem;
  border-radius: 25px;
}

.container {
  display: flex; 
  min-width: 100vw;
  min-height: 100vh;
  /*background-image: url('../assets/single_rectangles.svg'); 
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: right bottom; */
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
  font: normal normal super bold 3.75rem/4.688rem Overpass;
  letter-spacing: 0px;
}

.radio-option {
  width: 50%;
  align-items: center;
  padding: 2.5% 15%;
  font: normal normal bold 1.25rem/1.688rem Overpass;
  color: gb(119, 71, 240);
  accent-color: #683CE4;
  outline: 0.313rem ;
  border-color: red; /* wie geht diese & wirklich Vierecke? weil das eher Checkbox dann */
}

.radio-option:checked::before {
  color: green;
}

.size-radio-container {
  flex-wrap: wrap;
  display: flex;
  align-items: center;
}

.radio-option input {
  margin-right: 0.5rem; /* Adjust spacing between radio button and label */
}

.button-erstellen{
    font-weight: 500;
    font-size: 1.125rem;
    position: relative;
    white-space: nowrap;
    width: fit-content;
    padding-right: 1.375rem;
    padding-top: 0.75rem;
}

.logo-titel{
  font: normal normal bold 0.938rem/1.25rem Overpass;
  letter-spacing: 0px;
}

.content-s-item {
  display: flex;
  position: absolute;
  align-items: center;
  gap: 0.625rem;
  bottom: 90%;
  top: 5%;
}

.content-s-item img{
  width: 2.5rem;
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
  padding: 14.063rem 6.25rem;
}

.fabrik-name{
  display: flex;
  flex-direction: column;
}

.text-input{
  background-color: transparent;
  padding: 1.5em;
  margin: 1rem 1rem;
  height: 1.5rem;
  color: white;
  border-radius: 7rem;
  border-color: #683CE4;
  border-width: 0.13rem;
  resize: none;
  font: normal normal normal 1.2em/1.5em Overpass;
  padding-left: 2em;
}

.text-input:focus, input:focus{
    outline: none;
}

/*.text-input::placeholder {
  padding: 2em 10px;
  color: white;
  font: normal normal normal 15px/20px Overpass;
}*/


</style>