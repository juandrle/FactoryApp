<script setup lang="ts">
import { ref,inject, type Ref } from 'vue';
import { isUserAuthenticated } from '@/utils/auth';
import { logoutUser} from "@/utils/backendComms/postRequests"
import router from "@/router"
import { fetchCurrentUser } from "@/utils/backendComms/getRequests"

import Button from '../components/Button.vue'
import { isAwaitKeyword } from 'typescript';
  const buttonData = ref([
    {text: 'Fabrik erstellen', link: "/create"},
    {text: 'Fabrik beitreten', link:"/enter"}, 
    {text: 'Einstellungen', link:"/login"}
    ])


const {updateSessUser} = inject<{
  sessUser: Ref<string>,
  updateSessUser: (newUser: string) => void
}>('sessUser')

const logout = async() => {
  switch(await logoutUser()){
    
    case "logout successful":
      updateSessUser(null)
      await router.push('/login')
  }
}

const redirectToLogin = async() => {
  
  await router.push('/login');
}

</script>

<template>
  <div class="container">
    <form @submit.prevent="logout">
      <button type="submit">Logout</button>
    </form>
    
    <button @click="redirectToLogin">Login</button>
    
    <div class="s-item">
      <div class="button-container">
        <Button v-for="item in buttonData" :text="item.text" :link="item.link"></Button>
      </div>
    </div>
    <div class="m-item"><h1 class="game-name">Machine Deluxe 3000</h1></div>
    <div class="s-item"></div>
  </div> 
</template>

<style>
.container {
  display: flex; 
  min-width: 100vw;
  min-height: 100vh;
  background-image: url('../assets/rectangles.svg');
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: right bottom;
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
.game-name{
  position: absolute;
  top: 17%; 
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0px;
  font-weight: 400;
}
.button-container{
  display: flex;
  flex-direction: column;
  position: absolute; 
  left: 45%;
  bottom: 15%; 
  gap: 1rem;
}
</style>