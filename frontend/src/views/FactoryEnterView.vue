<script setup lang="ts">
import { ref } from 'vue'
import FactoryCard from '@/components/FactoryCard.vue'

const sizes = ref([
  { label: '30x50x8', value: '30x50x8' },
  { label: '60x100x12', value: '60x100x12' },
  { label: '90x150x16', value: '90x150x16' },
  { label: '120x200x20', value: '120x200x20' }
])

const owner = ref([
  { label: 'Erstellt von: ', value: '' },
  { label: 'Vivien', value: 'Vivien' },
  { label: 'Dschulia', value: 'Dschulia' },
  { label: 'Dill', value: 'Dill' },
  { label: 'David', value: 'David' }
])
</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="content-s-item">
        <img src="/icons8-fabric-96.png" width="20px" height="auto" />
        <p class="logo-titel">Machine Deluxe 3000</p>
      </div>
    </div>
    <div class="m-item">
      <h1 class="game-name">Fabrik betreten</h1>
      <div class="contentDiv">
        <div class="filter-div">
          <input placeholder="Suche..." />
          <div class="custom-select">
            <select v-model="owner">
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
          <FactoryCard></FactoryCard>
          <FactoryCard></FactoryCard>
          <FactoryCard></FactoryCard>
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
}
.game-name {
  position: absolute;
  top: 10%;
  font-weight: 400;
}
.contentDiv {
  border: 1px solid green;
  position: relative;
  width: 100%;
  top: 13rem;
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
