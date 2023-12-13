<script setup lang="ts">
import { VueElement, ref } from 'vue';
import Button from '../components/Button.vue'

const buttonData = ref([ 
    {text: 'Login', link:"/login"}
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
      <div class="form-container">
        <form @submit.prevent="createFactory">
          <div class="factory-name">
            <input placeholder="Namen eingeben" />
            <input placeholder="Passwort eingeben" />
          </div>
          <!-- <div class="button-container"> -->
          <Button v-for="item in buttonData" :text="item.text" :link="item.link" ></Button>
        <!-- </div> -->
        </form>
      </div>
    </div>
  </template>

<style scoped>
.container {
    display: flex; 
    min-width: 100vw;
    min-height: 100vh;
    background-image: url('../assets/rectangles.svg');
    background-repeat: no-repeat;
    background-attachment: fixed;
    background-position: right bottom;
  }

  .form-container {
    display: flex;
    align-items: center; /* Center vertically */
    justify-content: center; /* Center horizontally */
    flex-grow: 0; /* Take up all available space in the left half */
    margin-left: 120px;
}



form {
    background-color: #342844;
    padding: 1.875rem 1.125rem;
    border-radius: 25px;
    width: 400px;
  }
</style>