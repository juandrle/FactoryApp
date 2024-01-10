<script setup lang="ts">
import {computed, inject, onMounted, onUnmounted, type Ref, ref} from 'vue';
import Button from '../components/ui/Button.vue'
import {logoutUser} from "@/utils/backendComms/postRequests";
import router from "@/router";
import {useSessUser} from "@/utils/stateCompFunction/useSessUser";

const buttonData = ref([
  {text: 'Fabrik erstellen', link: "/create"},
  {text: 'Fabrik beitreten', link: "/enter"},
  //{text: 'Einstellungen', link:"/login"}
])

const signUpClicked = ref(false)
const {sessUser, updateSessUser} = useSessUser()
const showLogin = ref(false)


const logout = async () => {
  switch (await logoutUser()) {

    case "logout successful":
      updateSessUser('')
      await router.push('/login')
  }
}

const redirectToLogin = async () => {
  await router.push('/login');
}
const redirectToSignUp = async () => {
  signUpClicked.value = true;
  await router.push('/signup');
}

onMounted(() => {
  if (sessUser.value === '') showLogin.value = true
});

onUnmounted(() => {
  if (sessUser.value === '' && !signUpClicked.value) {
    router.replace('/login')
  }
})

</script>

<template>
  <div class="container">
    <div class="l-item">
      <div class="s-item">
        <div class="content-s-item">
          <a @click="router.push('/')">
            <img src="/icons8-fabric-96.png" width="20px" height="auto" alt=""/>
            <p class="logo-title">Machine Deluxe 3000</p>
          </a>
        </div>
        <div class="button-container">
          <Button v-for="item in buttonData" :text="item.text" :link="item.link"></Button>
        </div>
      </div>
    </div>
    <div class="m-item">
      <h1 class="game-name">Machine Deluxe 3000</h1>
      <h2 class="subtitle">create your own factory</h2>
    </div>
    <div class="r-item">
      <div class="header">
        <p v-if="!showLogin">logged in as {{ sessUser }}</p>

        <form v-if="!showLogin" @submit.prevent="logout">
          <button type="submit" link="">Logout</button>
        </form>
        <button class="signupbutton" v-if="sessUser === ''" @click="redirectToSignUp" link="">Sign Up</button>
        <button v-if="showLogin" @click="redirectToLogin" link="">Login</button>
      </div>
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

.container .r-item {
  display: flex;
  flex: 1 1 25%;
  position: relative;
  align-items: flex-start;
  justify-content: flex-end;
  padding: 2rem;
}
.container .l-item {
  display: flex;
  flex: 1 1 25%;
  position: relative;
  align-items: flex-start;
}

.header {
  margin-top: 17px;
  display: flex;
  position: relative;
  align-items: center;
  flex-direction: row;
  justify-content: space-between;

}


.header p {
  margin-right: 10px;
  font-size: 18px;
}

.header button {
  background-color: #683CE4;
  text-align: center;
  border-radius: 35px;
  cursor: pointer;
  color: #ffffff;
  font-size: 16px;
  text-decoration: none;
  margin-right: 12px;
  width: 110px;
  height: 28px;
  border: none;
  transition: background-color 0.4s ease;
  position: relative;

}

.header .signupbutton {
  background-color: #10E5B2;
}

.header .signupbutton:hover {
  background-color: #683CE4;

}

.header button:hover {
  background-color: #4b2ba6;
}

.header button:active {
  position: relative;
  top: 1px;
}

.container .m-item {
  flex: 1 1 50%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
}

.game-name {
  position: absolute;
  top: 17%;
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0;
  font-weight: 400;
  margin-bottom: 0;
}

.button-container {
  display: flex;
  flex-direction: column;
  position: absolute;
  left: 45%;
  bottom: 15%;
  gap: 1rem;

}

.subtitle {
  font: normal normal 28px/40px Overpass;
  margin-bottom: 300px;

}

.logo-title {
  font: normal normal bold 0.938rem/1.25rem Overpass;
  letter-spacing: 0;
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
</style>