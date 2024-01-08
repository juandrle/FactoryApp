<script setup lang="ts">
import {computed, inject, onMounted, ref} from 'vue'
import type {Ref} from 'vue'
import Button from '../components/ui/Button.vue'
import type {IVector3} from "@/types/global"
import router from "@/router";
import {factoryCreateRequest} from "@/utils/backendComms/postRequests"
import type {IFactoryCreate} from "@/types/backendTypes"

const sizes = ref([
  {label: '30x50x8', value: {x: 30, y: 50, z: 8} as IVector3},
  {label: '60x100x12', value: {x: 60, y: 100, z: 12} as IVector3},
  {label: '90x150x16', value: {x: 90, y: 150, z: 16} as IVector3},
  {label: '120x200x20', value: {x: 120, y: 200, z: 20} as IVector3}
])
const factoryName = ref('')
const factoryPassword = ref('')
const selectedSize = ref()
let updateFactorySize: (newSize: IVector3) => void
let updateFactoryID: (newID: number) => void
computed((size) => {
  return {
    x: size.width as number,
    y: size.length as number,
    z: size.height as number
  }
});
const setupInjections = () => {
  const resultID = inject<{
    factoryID: Ref<number>
    updateFactoryID: (newID: number) => void
  }>('factoryID')
  if (resultID && typeof resultID === 'object') updateFactoryID = resultID.updateFactoryID
  const resultSize = inject<{
    factorySize: Ref<IVector3>,
    updateFactorySize: (newSize: IVector3) => void
  }>('factorySize')
  if (resultSize && typeof resultSize === 'object') updateFactorySize = resultSize.updateFactorySize
}
const isLoading: Ref<boolean> = ref(false)
function createFactory() {
  if (selectedSize.value && factoryName.value) {
    isLoading.value = true;
    updateFactorySize({
      x: selectedSize.value.x,
      y: selectedSize.value.y,
      z: selectedSize.value.z,
    });

    const factory: IFactoryCreate = {
      name: factoryName.value,
      password: factoryPassword.value,
      width: selectedSize.value.x,
      depth: selectedSize.value.y,
      height: selectedSize.value.z,
    };

    factoryCreateRequest(factory)
        .then((newID: number) => {
          updateFactoryID(newID);
          router.push('/factory');
        })
        .catch((error) => {
          console.error("Failed to create factory", error);
        })
        .finally(() => {
          isLoading.value = false;
        });
  } else {
    console.error("Please fill all fields before creating the factory.");
  }
}

onMounted(() => {
  setupInjections()
})
</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="content-s-item">
        <a href="/">
          <img src="/icons8-fabric-96.png" alt=""/>
          <p class="logo-title">Machine Deluxe 3000</p>
        </a>
      </div>
    </div>
    <div class="loading" v-if="isLoading">
      initializing {{factoryName}}, please wait...
      <div class="loader"></div> <!-- Buffer animation element -->
    </div>
    <div class="m-item" v-else>
      <h1 class="game-name">Fabrik erstellen</h1>
      <div class="factory-settings">
        <form @submit.prevent="createFactory">
          <div class="form">
            <div class="factory-name">
              <input v-model="factoryName" placeholder="Name"/>
              <input v-model="factoryPassword" placeholder="Passwort" type="password"/>
            </div>
            <div class="size-radio-container">
              <div class="radio-option" v-for="size in sizes" :key="size.label">
                <input type="radio" v-model="selectedSize" :id="size.label" :value="size.value"/>
                <label :for="size.label">{{ size.label }}</label>
              </div>
            </div>
          </div>
          <div class="button-create">
            <button class="v-button v-form-button" type="submit" link="">Fabrik erstellen</button>
          </div>
        </form>
      </div>
    </div>
    <div class="s-item"></div>
  </div>
</template>

<style>
.form {
  background-color: #342844;
  padding: 1.875rem 1.125rem;
  border-radius: 25px;
}
.v-form-button {
  border-color: transparent;
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
.content-s-item {
  display: flex;
  position: absolute;
  bottom: 90%;
  top: 2.5%;
}

.content-s-item img {
  width: 2.5rem;
  height: min-content;
}
.content-s-item a {
  display: flex;
  gap: 0.625rem;
  text-decoration: none;
  align-items: center;
  color: white;
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
  padding: 50px 20px 10px 20px;
  top: 75%;
}

.logo-title {
  font: normal normal bold 0.938rem/1.25rem Overpass;
  letter-spacing: 0;
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

.loading {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 1.5rem;
  position: absolute;
  z-index: 100;
  text-align: center;
}

.loader {
  border: 5px solid rgba(255, 255, 255, 0.3);
  border-top: 5px solid #683ce4;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  margin-top: 20px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}


</style>
