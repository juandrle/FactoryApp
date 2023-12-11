<script setup lang="ts">
import { computed, ref } from 'vue'
import FactoryCard from '@/components/FactoryCard.vue'

export interface Factory {
  id: number;
  name: string;
  size: string;
  author: string;
}

const sizes = ref([
  { label: '30x50x8', value: '30x50x8' },
  { label: '60x100x12', value: '60x100x12' },
  { label: '90x150x16', value: '90x150x16' },
  { label: '120x200x20', value: '120x200x20' }
])

const owner = ref([
  { label: 'Vivien', value: 'Vivien' },
  { label: 'Dschulia', value: 'Dschulia' },
  { label: 'Dill', value: 'Dill' },
  { label: 'David', value: 'David' }
])

const existing_factories = [
  { id: 1, name: 'Erens Fabrik', size: '20x40x60', author: 'Eren Flamingo'},
  { id: 2, name: 'Julias Fabrik', size: '20x40x60', author: 'Julia Flamingo'},
  { id: 3, name: 'Davids Fabrik', size: '20x40x60', author: 'David Flamingo'},
  { id: 4, name: 'Vincents Fabrik', size: '20x40x60', author: 'Vincent Flamingo'},
  { id: 5, name: 'Viviens Fabrik', size: '20x40x60', author: 'Vivien Esel'},
  { id: 6, name: 'Esels Fabrik', size: '20x40x60', author: 'Esel Esel'},
] as Factory[]; 

// Nach Fabriknamen filtern 
const searchTerm = ref(''); 

const filteredFactories = computed( () => {
  return existing_factories.filter(factory => {
    return factory.name.toLowerCase().includes(searchTerm.value.toLocaleLowerCase()); 
  }); 
}); 

</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="content-s-item">
        <a href="/">
        <img src="/icons8-fabric-96.png" width="20px" height="auto" />
        <p class="logo-titel">Machine Deluxe 3000</p>
      </a>
      </div>
    </div>
    <div class="m-item">
      <h1 class="game-name">Fabrik betreten</h1>
      <div class="contentDiv">
        <div class="filter-div">
          <input placeholder="Suche..." v-model="searchTerm"/>
          <div class="custom-select">
            <select v-model="owner">
              <option value="" disabled selected>Select your option</option>
              <option v-for="o in owner" :value="o.label">{{ o.label }}</option>
            </select>
          </div>
          <div class="custom-select">
            <select v-model="sizes">
              <option v-for="size in sizes" :value="size.label">{{ size.label }}</option>
            </select>
          </div>
        </div>
        <div class="factory-cards">
          <FactoryCard v-for=" factory in filteredFactories" :key="factory.id" :factory="factory"></FactoryCard>
        </div>
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
  align-items: center;
}
.game-name {
  position: absolute;
  top: 5%;
  font-weight: 400;
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
.contentDiv {
  border: 1px solid green;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: min-content;
}
input {
  display: block;
  width: 350px;
  background-color: transparent;
  padding: 12px 20px;
  box-sizing: border-box;
  border: 2px solid #683ce4;
  border-radius: 30px;
  color: white;
}
.filter-div {
  display: flex;
  gap: 1rem;
  padding-bottom: 3rem;
}
.custom-select{
  position: relative;
  width: 150px;
}
.custom-select select{
  width: 100%;
  padding: 12px 20px;
  border: 2px solid #683ce4; 
  border-radius: 30px;
  background-color: transparent;
  color: white;
}
.custom-select option{
  background-color: #683ce4;
  color: white;
  padding: 8px;
}
.factory-cards{
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
}
</style>
