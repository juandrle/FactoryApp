<script setup lang="ts">
import { ref, type Ref, onMounted} from 'vue'
import router from "@/router"
import type {IUserForm} from "@/types/backendTypes"
import {signupUser} from "@/utils/backend-communication/postRequests"
import {useSessionUser} from "@/utils/composition-functions/useSessionUser";

const sessUser: Readonly<Ref<string>> = useSessionUser().sessionUser
const updateSessUser: (newUser: string) => void = useSessionUser().updateSessionUser

const userForm: Ref<IUserForm> = ref({
  username: '',
  password: '',
  passwordCheck: ''
})
const passwordNotEqual: Ref<Boolean> = ref(false)
const usernameTaken: Ref<Boolean> = ref(false)

const signUp = async () => {
  let password: HTMLInputElement | null = document.getElementById('password') as HTMLInputElement
  let passwordCheck: HTMLInputElement | null = document.getElementById('checkPassword') as HTMLInputElement
  let username: HTMLInputElement | null = document.getElementById('username') as HTMLInputElement
  switch (await signupUser(userForm.value)) {
    case "username taken":
      if (username) {
        username.classList.add('input-invalid')
        usernameTaken.value = true

      }
      break
    case "passwords don't match":
      if (password && passwordCheck) {
        password.classList.add('input-invalid')
        passwordCheck.classList.add('input-invalid')
        passwordNotEqual.value = true
      }
      break
    case "successful":
      if (password && passwordCheck && username) {
        username.classList.remove('input-invalid')
        password.classList.remove('input-invalid')
        passwordCheck.classList.remove('input-invalid')
        usernameTaken.value = false
        passwordNotEqual.value = false
      }
      updateSessUser(username.value)
      await router.push('/')
      break
    default:
      break
  }
}

onMounted(() => {
  if (sessUser.value !== '') {
    router.replace('/')
  }
})
</script>

<template>
  <div>
    <div class="container-left">
      <div class="content-s-item">
      <a @click="router.push('/')">
        <img src="/icons8-fabric-96.png" alt=""/>
        <p class="logo-title">Machine Deluxe 3000</p>
      </a>
      </div>
      <div class="container2">
        <form @submit.prevent="signUp">
          <div class="form-container">
            <h2>Sign Up</h2>
            <div class="factory-name">
              <input v-model="userForm.username" id="username" name="username" placeholder="Type in name" required/>
              <p v-if="usernameTaken" class="input-feedback">username already taken</p>
              <input type="password" v-model="userForm.password" id="password" name="password"
                     placeholder="Type in password" required/>
              <input type="password" v-model="userForm.passwordCheck" id="checkPassword" name="passwordCheck"
                     placeholder="Repeat password" required/>
              <p id="passwordNotEqual" v-if="passwordNotEqual" class="input-feedback">passwords don't match</p>

            </div>
            <div class="b-container">
              <button type="submit">Sign Up</button>
            </div>
            <div class="b-container2">
              <p>Already have an account?</p>
              <a href="/login">Sign In</a>
            </div>
          </div>
        </form>
      </div>
      <div class="container-right">
        <h1 class="title">MachineDeluxe3000</h1>
        <h2 class="subtitle">create your own factory</h2>
      </div>
    </div>


  </div>
</template>

<style scoped>
.container-left {
  display: flex;
  min-width: 100vw;
  min-height: 100vh;

  background-image: url('../assets/rectangles.svg');
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: right bottom;
}

.container2 {
  margin-top: auto;
  margin-bottom: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 120px;
}

.container-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;

}

.container-right .title {
  margin-top: 200px;
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0;
  font-weight: 400;
  margin-bottom: 0;
}

.container-right .subtitle {
  font: normal normal 28px/40px Overpass;
  margin-top: 50px;

}

.form-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex-grow: 0;
}

.factory-name {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin: 1rem 2.5rem 1rem 2.5rem;
  align-items: center;
}

.b-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 1.3rem;

}

.b-container2 {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 1.5rem;
  font-size: 14px;
  margin-right: 10px;
  margin-bottom: 0;
}

.b-container2 a {
  margin-left: 10px;
  color: #10E5B2;
  font-size: 16px;
  text-decoration: none;
  transition: color 0.4s ease;
}

.b-container2 a:hover {
  color: #683CE4;
  cursor: pointer;
}

button {
  text-align: center;
  background-color: #683CE4;
  display: inline-block;
  border-radius: 35px;
  cursor: pointer;
  color: #ffffff;
  font-size: 16px;
  text-decoration: none;
  outline: none;
  border: none;
  width: 150px;
  height: 35px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #4b2ba6;
}

input {
  display: block;
  width: 300px;
  background-color: transparent;
  padding: 12px 20px;
  box-sizing: border-box;
  border: 2px solid #683ce4;
  border-radius: 30px;
  color: white;
}

.input-invalid {
  border: 2px solid red;
  background-color: #483d5d;
}

.input-feedback {
  color: red;
  margin: -5px;
  
}

input:focus {
  outline: none;
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #342844;
  padding: 1.875rem 1.125rem;
  border-radius: 25px;
  width: 400px;
  margin-left: 100px;
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

.content-s-item a{
  display: flex;
  gap: 0.625rem;
  text-decoration: none;
  align-items: center;
  color: white;
}
</style>
