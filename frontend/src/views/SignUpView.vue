<script setup lang="ts">
import { VueElement, ref } from 'vue';
import type { UserForm } from '@/types/UserForm';
// import Button from '../components/Button.vue'

const buttonData = ref([
  { text: 'Sign Up', link: "/signup" }
])
const userForm = ref({
  username: '',
  password: '',
  passwordCheck: '',
});


// export async function signupUser(userForm: UserForm): Promise<void> {
//   try {
//     const url = '/api/users/signup';

//     const response = await fetch(url, {
//       method: 'POST',
//       headers: {
//         'Content-Type': 'application/json',
//       },
//       body: JSON.stringify(userForm),
//     });

//     if (!response.ok) {
//       if (response.status === 400) {
//         const validationError = await response.json();
//         console.error('Validation error:', validationError);
//       } else {
//         console.error('Error during signup. Status:', response.status);
//       }
//       return;
//     }

//     const responseData: UserResponse = await response.json();
//     console.log('User signup successful:', responseData);

//     // Optionally, you can redirect the user to the home page or perform other actions
//   } catch (error) {
//     console.error('Failed to signup', error);
//     // Handle other errors, such as network issues
//   }
// }


const signupUser = async () => {
  const userData = {
    username: userForm.value.username,
    password: userForm.value.password,
    passwordCheck: userForm.value.passwordCheck, // Add other necessary fields
  };

  try {
    const response = await fetch('/api/users/signup', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),

      
      
    });
    console.log("das is von backend", response)
    // const responseData = await response.json();
    //   console.log('User signup successful:', responseData);
    if (response.status === 400) {
      const errorData = await response;
      console.error('Validation error:', errorData);
    }
    if (!response.ok) {
      // Handle non-successful responses
      console.error('Error during signup. Status:', response.status);
      return;
    }

    if (response.ok) {
      const responseData = await response;
      console.log('User signup successful:', responseData);

      // Optionally, you can redirect the user to the home page or perform other actions
    } else {
      const errorData = await response;
      console.error('User signup failed:', errorData);

      // You can display an error message to the user or perform other error handling
    }
    
  } catch (error ) {
    console.error('Error during signup:', error);
    
   

    // Handle other errors, such as network issues
  }
};


</script>

<template>
  <div>
    <div class="container-left">
      <div class="container2">
        <form @submit.prevent="signupUser">
          <div class="form-container">
            <h2>Sign Up</h2>
            <div class="factory-name">
              <input v-model="userForm.username" name="username" placeholder="Namen eingeben" />
              <input v-model="userForm.password" name="password" placeholder="Passwort eingeben" />
              <input v-model="userForm.passwordCheck" name="passwordCheck" placeholder="Passwort bestätigen" />

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

.title{
  margin-top: 200px; 
  font: normal normal bold 70px/84px Overpass;
  letter-spacing: 0px;
  font-weight: 400;
  margin-bottom: 0px;
}

.subtitle{
  font: normal normal 28px/40px Overpass;
  margin-bottom: 100px;
}

.form-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  /* Center vertically */
  justify-content: center;
  /* Center horizontally */
  flex-grow: 0;
  /* Take up all available space in the left half */
  /*margin-left: 120px;*/


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
  justify-content:space-between;
  margin-top: 1.5rem;
  font-size: 14px;
  margin-right: 10px;
  margin-bottom: 0px;
}

.b-container2 a {
   margin-left: 10px;
   margin-left: 10px;
  color: #10E5B2;
  font-size: 16px; /* Adjust the font size as needed */
   /* Add bold styling */
  text-decoration: none;
}

.b-container2 a:hover{
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
</style>
