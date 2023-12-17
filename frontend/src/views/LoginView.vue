<script setup lang="ts">
import { VueElement, ref } from 'vue';
// import Button from '../components/Button.vue'

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
    <div class="container2">
      <form @submit.prevent="createFactory">
        <div class="form-container">
        
          <h3>Login</h3>
          <div class="factory-name">
            <input placeholder="Namen eingeben" />
            <input placeholder="Passwort eingeben" />
          </div>
          <div class="b-container">
            <button>Login</button>
          </div>
      </div>
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

.container2 {
  margin-top: auto;
  margin-bottom: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 100px;
}

.form-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center; /* Center vertically */
  justify-content: center; /* Center horizontally */
  flex-grow: 0; /* Take up all available space in the left half */
  /*margin-left: 120px;*/
}

.factory-name {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin: 1rem 2.5rem 1rem 2.5rem;
  align-items: center;
}

.b-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1rem; /* Adjust as needed */
}

button {
  text-align: center;
  background-color: #683CE4;
  display: inline-block;
  border-radius: 35px;
  cursor: pointer;
  color: #ffffff;
  font-size: 16px;
  text-decoration: none;
  outline: none;
  border: none;
  width: 150px;
  height: 30px;
}

button:hover {
  background-color: #4b2ba6;
}

input {
  display: block;
  width: 300px;
  background-color: transparent;
  padding: 12px 20px;
  box-sizing: border-box;
  border: 2px solid #683ce4;
  border-radius: 30px;
  color: white;
}

input:focus {
  outline: none;
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #342844;
  padding: 1.875rem 1.125rem;
  border-radius: 25px;
  width: 400px;
}
</style>
