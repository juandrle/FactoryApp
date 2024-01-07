// auth.js

import { inject, type Ref } from 'vue';

// Use inject to get the sessUser value
// const {updateSessUser} = inject<{
//   sessUser: Ref<string>,
//   updateSessUser: (newUser: string) => void
// }>('sessUser')
const sessUser = inject('sessUser');
console.log("weird", sessUser)


export const isUserAuthenticated = () => {

  if(sessUser == '' || sessUser == undefined){
    return false;
  }else{
    return true;
  }

  // return sessUser !== '';
};
