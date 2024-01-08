<script setup lang="ts">
import { ref,inject, type Ref, onMounted, onUnmounted, computed } from 'vue';

import { logoutUser} from "@/utils/backendComms/postRequests"
import router from "@/router"


import Button from '../components/Button.vue'
import { isAwaitKeyword } from 'typescript';
  const buttonData = ref([
    {text: 'Fabrik erstellen', link: "/create"},
    {text: 'Fabrik beitreten', link:"/enter"}, 
    {text: 'Einstellungen', link:"/login"}
    ])

const signUpClicked = ref(false);
const sessUser = ref('');
const updateSessUser = (newUser: string) => {
  console.log('Updating sessUser', newUser)
  sessUser.value = newUser
};

// const {sessUser, updateSessUser} = inject<{
//     sessUser: Ref<string>,
//     updateSessUser: (newUser: string) => void
// }>('sessUser')

const setUpInjections = () => {
  const injections = inject<{
    sessUser: Ref<string>, // Change the type as needed
    updateSessUser: (newUser: string) => void
  }>('sessUser');

  if (injections) {
    updateSessUser(injections.sessUser.value);
  }
};


const logout = async() => {
  switch(await logoutUser()){
    
    case "logout successful":
      updateSessUser('')
      await router.push('/login')
  }
}

const redirectToLogin = async() => {
  
  await router.push('/login');
}
const redirectToSignUp = async() => {
  signUpClicked.value = true;
  await router.push('/signup');
}
const loggedInUser = computed(() => sessUser.value);

onMounted(() => {
  setUpInjections()
});

onUnmounted(() =>{
  if(sessUser.value === '' && !signUpClicked.value){

    router.replace('/login')
  }
})

</script>

<template>
  <div class="container">
    
    <div class="s-item">
      
      <div class="button-container">
        <Button v-for="item in buttonData" :text="item.text" :link="item.link"></Button>
      </div>
    </div>
    <div class="m-item">
      <h1 class="game-name">Machine Deluxe 3000</h1>
      <h2 class="subtitle">create your own factory</h2>
    </div>
    <div class="s-item">
      <div class="header">
        <p v-if="sessUser !== ''">logged in as {{ loggedInUser }}</p>
        
        <form v-if="sessUser !== ''" @submit.prevent="logout">
          <button type="submit">Logout</button>
        </form>
        <button class="signupbutton" v-if="sessUser === ''" @click="redirectToSignUp">Sign Up</button>
        <button v-if="sessUser === ''" @click="redirectToLogin">Login</button>
      </div>
    </div>
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
  align-items: flex-start; 
  padding: 2rem;
}
.header {
  margin-top:30px;
  display:flex;
  position: relative;
  align-items: center;
  flex-direction: row;
  justify-content: space-between;
  
}


.header p{
  margin-right: 10px;
  font-size: 18px;
}
.header button{
  background-color:#683CE4;
	text-align: center;
	border-radius:35px;
	cursor:pointer;
	color:#ffffff;
	font-size:16px;
	text-decoration:none;
	margin-right: 12px;
	width: 110px; 
  height:28px;
	border:none;
  transition: background-color 0.4s ease;
  position: relative;

}
.header .signupbutton{
  background-color:#10E5B2;
}

.header .signupbutton:hover{
  background-color:#683CE4;
  
}

.header button:hover{
  background-color:#4b2ba6;
}
.header button:active{
  position:relative;
	top:1px;
}

.container .m-item {
  flex: 1 1 50%; 
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}
.game-name{
  position: absolute;
  top: 17%; 
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0px;
  font-weight: 400;
  margin-bottom: 0px;
}
.button-container{
  display: flex;
  flex-direction: column;
  position: absolute; 
  left: 45%;
  bottom: 15%; 
  gap: 1rem;
  
}

.subtitle{
  font: normal normal 28px/40px Overpass;
  margin-bottom: 300px;
  
}
</style>