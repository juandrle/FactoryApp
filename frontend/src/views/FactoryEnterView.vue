<script setup lang="ts">
import {computed, onMounted, type Ref, ref} from 'vue'
import FactoryCard from '@/components/ui/FactoryCard.vue'
import type {IFactory, IFactoryDelete} from '@/types/backendTypes'
import {getAllFactories, getAllUsers} from '@/utils/backendComms/getRequests'
import router from "@/router";
import DeletionPopup from "@/components/ui/DeletionPopup.vue";
import {factoryDeleteRequest} from "@/utils/backendComms/deleteRequest";

const sizes = ref([
  {label: 'All', value: ''},
  {label: '30x50x8', value: '30x50x8'},
  {label: '60x100x12', value: '60x100x12'},
  {label: '90x150x16', value: '90x150x16'},
  {label: '120x200x20', value: '120x200x20'}
])
const factoryToDel: Ref<IFactoryDelete | undefined> = ref(undefined)
const factoryNameToDel: Ref<string> = ref('')
const currSize = ref('')
const showPopup = ref(false)
const owner = ref([
  {label: 'All', value: ''}
])
const currOwner = ref('')

//fetch all existing factories
const existing_factories: Ref<IFactory[]> = ref([])

onMounted(() => {
  getAllUsers().then((json: { username: string }[]) => {
    json.forEach((user: { username: string }) => {
      owner.value.push({label: user.username, value: user.username})
    })
  })
  getAllFactories().then((json: IFactory[]) => {
    existing_factories.value = json
    console.log(json)
  })
})

// Nach Fabriknamen filtern
const searchTerm = ref('')

const filteredFactories = computed(() => {
  return existing_factories.value.filter((factory) => {
    const matchesSize = currSize.value === '' || `${factory.width}x${factory.depth}x${factory.height}` === currSize.value;
    const matchesOwner = currOwner.value === '' || factory.author === currOwner.value;
    const matchesSearchTerm = factory.name.toLowerCase().includes(searchTerm.value.toLowerCase());
    return matchesSize && matchesOwner && matchesSearchTerm;
  });
});
const togglePopup = (payload?: {factoryDelete: IFactoryDelete, factoryName: string}) => {
  if (payload !== undefined) {
    factoryToDel.value = payload.factoryDelete
    factoryNameToDel.value = payload.factoryName
  }
  showPopup.value = !showPopup.value
}
const onPopupClosed = (toDelete?: boolean) => {
  if (toDelete) factoryDeleteRequest(factoryToDel.value)
  togglePopup()
}

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
      <h1 class="headline">enter factory</h1>
      <div class="contentDiv">
        <div class="filter-div">
          <input placeholder="Suche..." v-model="searchTerm"/>
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
              @deleteClicked="togglePopup"
          ></FactoryCard>
        </div>
      </div>
    </div>
    <div class="s-item"></div>
  </div>
  <DeletionPopup :factory-name="factoryNameToDel" v-if="showPopup" @delete="onPopupClosed"></DeletionPopup>
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
  user-select: none;
}

.container .s-item {
  display: flex;
  flex: 1 1 25%;
  position: relative;
  align-items: flex-start;
  padding-top: 1rem;
  padding-bottom: 3rem;
}

.container .m-item {
  flex: 1 1 50%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
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
  margin-left: 64px;
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
  width: 50%;
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
  width: 20%;
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
  display: flex;
  flex-wrap: wrap;
  gap: 2%;
  justify-content: left;
}
.factory-cards > * {
  cursor: pointer;
  margin-bottom: 3%; /* Vertical gap */
}


</style>
