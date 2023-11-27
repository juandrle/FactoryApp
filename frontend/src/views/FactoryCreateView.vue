<script setup lang="ts">
import {ref} from 'vue'
import Button from '../components/Button.vue'

const buttonData = ref([{text: 'Fabrik erstellen und bestellen', link: '/factory'}])

const sizes = ref([
  {label: '30x50x8', value: 'size1', width: 30, length: 50, height: 8},
  {label: '60x100x12', value: 'size2', width: 60, length: 100, height: 12},
  {label: '90x150x16', value: 'size3', width: 90, length: 150, height: 16},
  {label: '120x200x20', value: 'size4', width: 120, length: 200, height: 20}
])
const selectedSize = ref()

function createFactory() {
  if (selectedSize.value) {
    console.log(selectedSize.value)
  } else {
    console.error("Please select a size before creating the factory.")
  }
}


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
      <div class="factory-settings">
        <form @submit.prevent="createFactory">
          <div class="factory-name">
            <input placeholder="Name"/>
            <input placeholder="Passwort"/>
          </div>
          <div class="size-radio-container">
            <div class="radio-option" v-for="size in sizes" :key="size.value">
              <input type="radio" v-model="selectedSize" :id="size.value" :value="size.value"/>
              <label :for="size.value">{{ size.label }}</label>
            </div>
          </div>
          <Button
              class="button-create"
              v-for="item in buttonData"
              :text="item.text"
              :key="item.text"
              :link="item.link"></Button>
        </form>
      </div>

    </div>
    <div class="s-item"></div>
  </div>
</template>

<style>
form {
  background-color: #342844;
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
}

.game-name {
  position: absolute;
  top: 10%;
  font-weight: 400;
}

.radio-option {
  display: flex;
  align-items: center;
  width: 50%;
  padding: 2.5% 15%;
  font-size: large;
  accent-color: #683ce4;
  gap: 0.5rem;
}

/* .radio-option:checked::before {
  color: green;
} */

.size-radio-container {
  flex-wrap: wrap;
  display: flex;
  align-items: center;
}

.button-create {
  display: flex;
  flex-direction: column;
  width: fit-content;
  padding: 10px 20px 10px 20px;
  top: 75%;
}

.logo-titel {
  font: normal normal bold 0.938rem/1.25rem Overpass;
  letter-spacing: 0px;
}

.content-s-item {
  display: flex;
  position: absolute;
  align-items: center;
  gap: 0.625rem;
  bottom: 90%;
  top: 2.5%;
}

.content-s-item img {
  width: 2.5rem;
  height: min-content;
}

.factory-settings {
  position: relative;
  min-width: 100%;
  top: 4rem;
  padding: 11.5rem 6.25rem;
}

.factory-name {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin: 1rem 2.5rem 1rem 2.5rem;
}

.factory-name input {
  background-color: transparent;
  padding: 12px 20px;
  box-sizing: border-box;
  border: 2px solid #683ce4;
  border-radius: 30px;
  color: white;
}

.text-input:focus,
input:focus {
  outline: none;
}
</style>
