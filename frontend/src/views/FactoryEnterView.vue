<script setup lang="ts">
import { computed, onMounted, type Ref, ref } from 'vue'
import FactoryCard from '@/components/ui/FactoryCard.vue'
import type { IFactory } from '@/types/backendTypes'
import { getAllFactories } from '@/utils/backendComms/getRequests'
import router from "@/router";

const sizes = ref([
  { label: 'All', value: '' },
  { label: '30x50x8', value: '30x50x8' },
  { label: '60x100x12', value: '60x100x12' },
  { label: '90x150x16', value: '90x150x16' },
  { label: '120x200x20', value: '120x200x20' }
])
const currSize = ref('')

const owner = ref([
  { label: 'All', value: '' },
  { label: 'Vivien Esel', value: 'Vivien Esel' },
  { label: 'Esel Esel', value: 'Esel Esel' },
  { label: 'Vincent Flamingo', value: 'Vincent Flamingo' },
  { label: 'David Flamingo', value: 'David Flamingo' }
])
const currOwner = ref('')

//fetch all existing factories
const existing_factories: Ref<IFactory[]> = ref([])

onMounted(() => {
  getAllFactories().then((json: any) => {
    existing_factories.value = json
    console.log(json)
  })
})

// Nach Fabriknamen filtern
const searchTerm = ref('')

const filteredFactories = computed(() => {
  let matchesSize, matchesOwner, matchesSearchTerm
  return existing_factories.value.filter((factory) => {
    // console.log(currSize.value, currOwner.value)
    // filter factory size
    if (currSize.value === '') {
      matchesSize = true
    } else {
      const sizeString = `${factory.width}x${factory.depth}x${factory.height}`
      matchesSize = currSize.value ? sizeString === currSize.value : true
    }
    // filter factory author
    if (currOwner.value === '') {
      matchesOwner = true
    }
    // else {
    //   matchesOwner = currOwner.value ? factory.author === currOwner.value : true
    // }
    // filter factory name
    matchesSearchTerm = factory.name.toLowerCase().includes(searchTerm.value.toLowerCase())
    // return matchesSearchTerm && matchesSize && matchesOwner
    return matchesSearchTerm && matchesSize
  })
})

</script>

<template>
  <div class="container">
    <div class="s-item">
      <div class="content-s-item">
        <a @click="router.push('/')">
          <img src="/icons8-fabric-96.png" alt=""/>
          <p class="logo-title">Machine Deluxe 3000</p>
        </a>
      </div>
    </div>
    <div class="m-item">
      <h1 class="headline">Fabrik betreten</h1>
      <div class="contentDiv">
        <div class="filter-div">
          <input placeholder="Suche..." v-model="searchTerm" />
          <div class="custom-select">
            <select v-model="currOwner">
              <option v-for="o in owner" :value="o.value">{{ o.label }}</option>
            </select>
          </div>
          <div class="custom-select">
            <select v-model="currSize">
              <option v-for="size in sizes" :value="size.value">{{ size.label }}</option>
            </select>
          </div>
        </div>
        <div class="factory-cards">
          <FactoryCard
            v-for="factory in filteredFactories"
            :key="factory.id"
            :factory="factory"
          ></FactoryCard>
        </div>
      </div>
    </div>
    <div class="s-item"></div>
  </div>
</template>

<style scoped>
.container {
  align-items: baseline;
}
.container .s-item {
  display: flex;
  flex: 1 1 25%;
  position: relative;
  align-items: flex-end;
  padding-top: 1rem;
  padding-bottom: 3rem;
  transform: translateY(50px);
}
.container .m-item {
  flex: 1 1 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  transform: translateY(30px);
}
.headline {
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0;
  font-weight: 400;
}
.content-s-item {
  display: flex;
  position: absolute;
  bottom: 90%;
  top: 2.5%;
  cursor: pointer;
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
.contentDiv {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  /* position: relative;
  top: 20%; */
}
input {
  display: block;
  width: 390px;
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
.filter-div {
  display: flex;
  gap: 1rem;
  padding-bottom: 3rem;
  justify-content: space-between;
}
.custom-select {
  position: relative;
  width: 200px;
}
select {
  width: 100%;
  padding: 12px 20px;
  border: 2px solid #683ce4;
  border-radius: 30px;
  background-color: transparent;
  color: white;
}
option {
  background-color: #683ce4;
  color: white;
  padding: 8px;
}
.factory-cards {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 40px;
  justify-items: center;
}
</style>
