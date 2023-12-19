<script setup lang="ts">
import {computed, inject, Ref, ref, watch} from 'vue'
import ButtonVue from '@/components/temp/Button.vue';
import type {IVector3} from "@/types/global";
import router from "@/router";
import {factoryCreateRequest} from "@/utils/backendComms/postRequests";
import type {IFactoryCreate} from "@/types/backendEntity";
import Button from '@/components/temp/Button.vue';

const buttonData = ref([{text: 'Fabrik erstellen und bestellen', link: '/factory'}])

const sizes = ref([
  {label: '30x50x8', value: {x: 30, y: 50, z: 8} as IVector3},
  {label: '60x100x12', value: {x: 60, y: 100, z: 12} as IVector3},
  {label: '90x150x16', value: {x: 90, y: 150, z: 16} as IVector3},
  {label: '120x200x20', value: {x: 120, y: 200, z: 20} as IVector3}
])
const factoryName = ref('')
const factoryPassword = ref('')
const selectedSize = ref()
const {updateFactorySize} = inject<{
  factorySize: Ref<IVector3>,
  updateFactorySize: (newSize: IVector3) => void
}>('factorySize')
const {updateFactoryID} = inject<{
  factoryID: Ref<number>,
  updateFactoryID: (newID: number) => void
}>('factoryID')
const combinedSize = computed((size) => {
  return {
    x: size.width as number,
    y: size.length as number,
    z: size.height as number
  }
})

function createFactory() {
  if (selectedSize.value) {
    updateFactorySize({
      x: selectedSize.value.x,
      y: selectedSize.value.y,
      z: selectedSize.value.z
    })
    const factory: IFactoryCreate = {
      name: factoryName.value,
      password: factoryPassword.value,
      width: selectedSize.value.x,
      depth: selectedSize.value.y,
      height: selectedSize.value.z
    }
    factoryCreateRequest(factory).then((newID: number) => {
      updateFactoryID(newID)
      console.log(newID)
      router.push('/factory')
    })
  } else {
    console.error("Please select a size before creating the factory.")
  }
}

</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="content-s-item">
      <a href="/">
        <img src="/icons8-fabric-96.png" width="20px" height="auto"/>
        <p class="logo-titel">Machine Deluxe 3000</p>
      </a>
      </div>
    </div>
    <div class="m-item">
      <h1 class="headline">Fabrik erstellen</h1>
      <div class="factory-settings">
        <form @submit.prevent="createFactory">
          <div class="form">
            <div class="factory-name">
              <input v-model="factoryName" placeholder="Name"/>
              <input v-model="factoryPassword" placeholder="Passwort"/>
            </div>
            <div class="size-radio-container">
              <div class="radio-option" v-for="size in sizes" :key="size.label">
                <input type="radio" v-model="selectedSize" :id="size.label" :value="size.value"/>
                <label :for="size.label">{{ size.label }}</label>
              </div>
            </div>
          </div>
          <div class="button-create">
            <button class="v-button v-form-button" type="submit">Fabrik erstellen</button>
          </div>
        </form>
      </div>

    </div>
    <div class="s-item"></div>
  </div>
</template>

<style scoped>
.container .m-item {
  align-items: center;
  flex-direction: column;
}
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  background-color: #342844;
  padding: 1.875rem 1.125rem;
  border-radius: 30px;
}
.radio-option {
  display: flex;
  /* align-items: center; */
  width: 40%;
  /* padding: 2.5% 15%; */
  font-size: large;
  accent-color: #683ce4;
  gap: 0.5rem; 
}

.radio-option input{
  width: auto;
}
.size-radio-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.5rem;
  margin: 1rem 2.5rem 1rem 2.5rem;
}

.button-create {
  display: flex;
  flex-direction: column;
  padding: 50px 20px 10px 20px;
  top: 75%;
  outline:none;
}
.headline {
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0px;
  font-weight: 400;
}
.logo-titel {
  font: normal normal bold 0.938rem/1.25rem Overpass;
  letter-spacing: 0px;
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

.content-s-item a{
  display: flex;
  gap: 0.625rem;
  text-decoration: none;
  align-items: center;
  color: white;
}

.factory-settings {
  position: relative;
  min-width: 100%;
  /* top: 4rem; */
  /* padding: 11.5rem 6.25rem; */
}

.factory-name {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin: 1rem 2.5rem 1rem 2.5rem;
  align-items: center;
}

.factory-name input {
  background-color: transparent;
  padding: 12px 20px;
  box-sizing: border-box;
  border: 2px solid #683ce4;
  border-radius: 30px;
  color: white;
  width: 600px;
}

.text-input:focus,
input:focus {
  outline: none;
}

</style>
