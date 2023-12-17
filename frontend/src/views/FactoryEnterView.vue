<script setup lang="ts">
import { computed, ref } from 'vue'
import FactoryCard from '@/components/FactoryCard.vue'

export interface Factory {
  id: number
  name: string
  size: string
  author: string
  link: string
}

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

const existing_factories = [
  { id: 1, name: 'Erens Fabrik', size: '30x50x8', author: 'Eren Flamingo', link: '/factory' },
  { id: 2, name: 'Julias Fabrik', size: '20x40x60', author: 'Julia Flamingo', link: '/factory' },
  { id: 3, name: 'Davids Fabrik', size: '60x100x12', author: 'David Flamingo', link: '/factory' },
  {
    id: 4,
    name: 'Vincents Fabrik',
    size: '120x200x20',
    author: 'Vincent Flamingo',
    link: '/factory'
  },
  { id: 5, name: 'Viviens Fabrik', size: '20x40x60', author: 'Vivien Esel', link: '/factory' },
  { id: 6, name: 'Esels Fabrik', size: '20x40x60', author: 'Esel Esel', link: '/factory' },
  {
    id: 7,
    name: 'Vincents Fabrik',
    size: '60x100x12',
    author: 'Vincent Flamingo',
    link: '/factory'
  },
  { id: 8, name: 'Viviens Fabrik', size: '60x100x12', author: 'Vivien Esel', link: '/factory' },
  { id: 9, name: 'Esels Fabrik', size: '60x100x12', author: 'Esel Esel', link: '/factory' }
] as Factory[]

// Nach Fabriknamen filtern
const searchTerm = ref('')
const currentlyRotatedCard = ref<HTMLElement | null>(null)

const filteredFactories = computed(() => {
  let matchesSize, matchesOwner, matchesSearchTerm
  return existing_factories.filter((factory) => {
    console.log(currSize.value, currOwner.value)
    if (currSize.value === '') {
      matchesSize = true
    } else {
      matchesSize = currSize.value ? factory.size === currSize.value : true
    }
    if (currOwner.value === '') {
      matchesOwner = true
    } else {
      matchesOwner = currOwner.value ? factory.author === currOwner.value : true
    }
    matchesSearchTerm = factory.name.toLowerCase().includes(searchTerm.value.toLowerCase())
    return matchesSearchTerm && matchesSize && matchesOwner
  })
})

const rotateCard = (clickTarget: EventTarget | null) => {
  if (!clickTarget) return
  const card = clickTarget as HTMLElement
  if (card) {
    const front = card.querySelector('.card-front') as HTMLElement
    const back = card.querySelector('.card-back') as HTMLElement

    // Karte drehen
    if (currentlyRotatedCard.value === clickTarget) {
      back.style.display = 'none' // Rückseite ausblenden
      front.style.display = 'flex' // Vorderseite anzeigen
      currentlyRotatedCard.value = null // Zustand aktualisieren
    } else if (currentlyRotatedCard.value === null) {
      back.style.display = 'flex' // Rückseite anzeigen
      front.style.display = 'none' // Vorderseite ausblenden
      currentlyRotatedCard.value = clickTarget as HTMLElement // Zustand aktualisieren
    } else {
      ;(currentlyRotatedCard.value.querySelector('.card-back') as HTMLElement).style.display =
        'none' // Rückseite ausblenden
      ;(currentlyRotatedCard.value.querySelector('.card-front') as HTMLElement).style.display =
        'flex'
      back.style.display = 'flex' // Rückseite anzeigen
      front.style.display = 'none' // Vorderseite ausblenden
      currentlyRotatedCard.value = clickTarget as HTMLElement // Zustand aktualisieren
    }
  }
}
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
            :rotateCard="rotateCard"
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
  width: min-content;
  position: relative;
  top: 20%;
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
input:focus {
  outline: none;
}
.filter-div {
  display: flex;
  gap: 1rem;
  padding-bottom: 3rem;
}
.custom-select {
  position: relative;
  width: 150px;
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
