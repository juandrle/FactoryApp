<script setup lang="ts">
import {onMounted, ref, type Ref, watch} from 'vue'
import {Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot} from '@headlessui/vue'
import {XMarkIcon} from '@heroicons/vue/24/outline'
import router from "@/router";
import Button from "@/components/button/Button.vue";
import {leaveFactory} from "@/utils/backend-communication/postRequests";
import {useSessionUser} from "@/utils/composition-functions/useSessionUser";
import {getAllUsersInFactory} from '@/utils/backend-communication/getRequests';

const emit = defineEmits<{
  closeSideBar: [boolean];
}>()
const props = defineProps<{
  username: Ref<string>,
  factoryName: string
  factoryID: number
}>()

const open = ref(true)
watch(open, () => {
  if (!open.value) emit('closeSideBar', false)
})


const factoryUsers = ref<{ username: string }[]>([]);

const leaveAndNavigate = async (route : string) => {
 

  const factoryID = props.factoryID
  const userName = props.username.value

  const leaveResult = await leaveFactory(factoryID, userName);

  if (leaveResult === "Leaving unsuccessful") {
    console.error("Leaving unsuccessful")
  } else if (leaveResult === "Error leaving factory"){
    console.error("Error leaving factory")
  }else {
    router.push(route)
  }
}

onMounted(async () => {
  
  const factoryId = props.factoryID; 
  try {
    factoryUsers.value = await getAllUsersInFactory(factoryId);
  } catch (error) {
    console.error('Error fetching factory users:', error);
  }
});


</script>

<template>
  <TransitionRoot as="template" :show="open">
    <Dialog as="div" class="relative z-10" @close="open = false">
      <TransitionChild as="template" enter="ease-in-out duration-500" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in-out duration-500" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
      </TransitionChild>

      <div class="fixed inset-0 overflow-hidden" id="ignore">
        <div class="absolute inset-0 overflow-hidden" id="ignore">
          <div class="pointer-events-none fixed inset-y-0 right-0 flex max-w-full pl-10" id="ignore">
            <TransitionChild as="template" enter="transform transition ease-in-out duration-500 sm:duration-700" enter-from="translate-x-full" enter-to="translate-x-0" leave="transform transition ease-in-out duration-500 sm:duration-700" leave-from="translate-x-0" leave-to="translate-x-full">
              <DialogPanel class="pointer-events-auto relative w-screen max-w-sm">
                <TransitionChild as="template" enter="ease-in-out duration-500" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in-out duration-500" leave-from="opacity-100" leave-to="opacity-0">
                  <div class="absolute left-0 top-0 -ml-8 flex pr-2 pt-4 sm:-ml-10 sm:pr-4" id="ignore">
                    <button type="button" class="relative rounded-md text-gray-300 hover:text-white bg-transparent border-transparent focus:outline-none focus:ring-2 focus:ring-white" @click="open = false" id="ignore" link="">
                      <span class="absolute -inset-2.5" id="ignore"/>
                      <span class="sr-only" id="ignore">Close panel</span>
                      <XMarkIcon class="h-6 w-6" aria-hidden="true" id="ignore"/>
                    </button>
                  </div>
                </TransitionChild>
                <div class="flex h-full flex-col overflow-y-scroll bg-gray-800 py-6 shadow-xl" id="ignore">
                  <div class="px-4 sm:px-6 relative mt-3" id="ignore">
                    <div class="flex items-center justify-center mb-7 hover:cursor-pointer" id="ignore" @click="router.push('/')">
                      <img src="/icons8-fabric-96.png" class="h-14 w-14 " alt="Logo" id="ignore">
                      <span class="text-white font-bold ml-2" id="ignore">Machine Deluxe 3000</span>
                    </div>
                    <DialogTitle class="font-bold text-2xl leading-6 text-white" id="ignore">
                      Main Menu
                    </DialogTitle>
                    <DialogTitle class="font-bold text-lg leading-6 text-white" id="ignore">
                      factory: {{factoryName}}
                    </DialogTitle>
                  </div>
                  <div class="relative mt-6 flex-1 flex flex-col px-4 sm:px-6 gap-3" id="ignore">
                    <button type="button" class="text-white bg-gradient-to-r from-ourPurple to-ourPurpleDarker hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-purple-300 dark:focus:ring-purple-800 font-bold rounded-3xl text-lg px-5 py-2.5 text-center me-2 mb-2" id="ignore" @click="leaveAndNavigate('/')" link="">
                      back to home
                    </button>
                    <button type="button" class="text-white bg-gradient-to-r from-ourPurple to-ourPurpleDarker hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-purple-300 dark:focus:ring-purple-800 font-bold rounded-3xl text-lg px-5 py-2.5 text-center me-2 mb-2" id="ignore" @click="leaveAndNavigate('/enter')" link="">
                      back to enter factories
                    </button>
                    <button type="button" class="text-white bg-gradient-to-r from-ourPurple to-ourPurpleDarker hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-purple-300 dark:focus:ring-purple-800 font-bold rounded-3xl text-lg px-5 py-2.5 text-center me-2 mb-2" id="ignore" @click="leaveAndNavigate('/create')" link="">
                      back to create factories
                    </button>
                  </div>
                  <div class="relative mb-11 align-bottom text-lg flex flex-col px-4 sm:px-6 gap-2.5">
                    <p>Current users:</p> 
                      <ul>
                        <li v-for="user in factoryUsers" :key="user.username">{{ user.username }}</li>
                      </ul>
                  </div>
                  
                  <div class="relative mb-11 align-bottom text-lg flex flex-col px-4 sm:px-6 gap-2.5" id="ignore">
                    <p>Logged in as user {{props.username}}</p>
                    <button type="button" class="text-white bg-gradient-to-r from-red-400 via-red-500 to-red-600 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 font-bold rounded-3xl text-lg px-5 py-2.5 text-center me-2 mb-2" id="ignore" @click="useSessionUser().performLogout()" link="">
                      logout
                    </button>
                    
                  </div>
                </div>
              </DialogPanel>
            </TransitionChild>
          </div>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<style scoped>

</style>