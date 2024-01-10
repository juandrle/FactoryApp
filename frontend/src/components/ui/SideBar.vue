<script setup lang="ts">
import {ref, watch} from 'vue'
import { Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'
import { XMarkIcon } from '@heroicons/vue/24/outline'
import router from "@/router";
const emit = defineEmits<{
  closeSideBar: [boolean];
}>()
const open = ref(true)
watch(open, () => {
  if (!open.value) emit('closeSideBar', false)
})
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
                    <button type="button" class="relative rounded-md text-gray-300 hover:text-white focus:outline-none focus:ring-2 focus:ring-white" @click="open = false" id="ignore">
                      <span class="absolute -inset-2.5" id="ignore"/>
                      <span class="sr-only" id="ignore">Close panel</span>
                      <XMarkIcon class="h-6 w-6" aria-hidden="true" id="ignore"/>
                    </button>
                  </div>
                </TransitionChild>
                <div class="flex h-full flex-col overflow-y-scroll bg-gray-800 py-6 shadow-xl" id="ignore">
                  <div class="px-4 sm:px-6" id="ignore">
                    <DialogTitle class="text-base font-semibold leading-6 text-white" id="ignore">Main Menu</DialogTitle>
                  </div>
                  <div class="relative mt-6 flex-1 flex flex-col px-4 sm:px-6 gap-2.5" id="ignore">
                    <button class="bg-ourPurple hover:bg-ourPurpleDarker text-white font-bold py-2 px-4 border-r-4 border-b-4 border-ourPurpleDarker hover:border-ourPurple rounded-2xl" id="ignore" @click="router.push('/')">
                      back to home
                    </button>
                    <button class="bg-ourPurple hover:bg-ourPurpleDarker text-white font-bold py-2 px-4 border-r-4 border-b-4 border-ourPurpleDarker hover:border-ourPurple rounded-2xl" id="ignore" @click="router.push('/enter')">
                      back to enter factories
                    </button>
                    <button class="bg-ourPurple hover:bg-ourPurpleDarker text-white font-bold py-2 px-4 border-r-4 border-b-4 border-ourPurpleDarker hover:border-ourPurple rounded-2xl" id="ignore" @click="router.push('/create')">
                      back to create factories
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