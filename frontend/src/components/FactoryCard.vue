<script setup lang="ts">
import { onMounted, type Ref, ref, watch } from 'vue'
import type {IFactory, IFactoryDelete} from '@/types/backendTypes'
import {getFactoryImage} from '@/utils/backend-communication/getRequests'
import router from '@/router'
import {backendUrl} from '@/utils/config/config'
import type {IVector3} from "@/types/global";
import {useFactory} from "@/utils/composition-functions/useFactory";
import {useSessionUser} from "@/utils/composition-functions/useSessionUser";
import { enterFactory } from '@/utils/backend-communication/postRequests'

const props = defineProps<{
  factory: IFactory
}>()
const emit = defineEmits<{
  deleteClicked: (payload: { factoryDelete: IFactoryDelete, factoryName: string }) => void
}>()
const isFactoryUpToDate: Ref<boolean> = useFactory().isFactoryImageUpToDate
const factoryCardRef = ref(null)
const factoryEnterPassword = ref('')
const currentPicture = ref(
    'https://damassets.autodesk.net/content/dam/autodesk/www/industry/manufacturing/integrated-factory-modeling/what-is-integrated-factory-modeling-thumb-1172x660.jpg'
)
const isInputValid = ref(true)
const currentlyRotatedCard = ref<HTMLElement | null>(null)
let updateFactorySize: (newSize: IVector3) => void = useFactory().updateFactorySize
let updateFactoryID: (newID: number) => void = useFactory().updateFactoryID
let updateFactoryName = useFactory().updateFactoryName
const sessUser = useSessionUser().sessionUser

const rotateCard = async (clickTarget: EventTarget | null) => {
  if (!clickTarget) return
  const card = clickTarget as HTMLElement
  const newSize = {
    x: props.factory?.width,
    y: props.factory?.depth,
    z: props.factory?.height
  } as IVector3
  updateFactoryID(props.factory?.id)
  updateFactorySize(newSize)
  updateFactoryName(props.factory.name)
  if (!props.factory.hasPassword || props.factory?.author === sessUser.value) {
    await enterFactory(props.factory?.id, sessUser.value)
    router.push('/factory')
  } else {
    if (card) {
      const front = card.querySelector('.card-front') as HTMLElement
      const back = card.querySelector('.card-back') as HTMLElement
      (back.children[0].children[0].children[0] as HTMLInputElement).value = ''
      // Karte drehen
      if (currentlyRotatedCard.value === clickTarget) {
        isInputValid.value = true
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
}
const deleteButtonClicked = () => {
   const factoryDelete = {
     id: props.factory?.id,
     element: factoryCardRef.value
   }  as IFactoryDelete
  emit('deleteClicked', { factoryDelete: factoryDelete, factoryName: props.factory.name })
}

onMounted(() => {
  getFactoryImage(props.factory?.id).then((dataURL) => {
    if (dataURL !== 'failed') currentPicture.value = dataURL.toString()
  })
})
watch(isFactoryUpToDate, (newValue) => {
  if (newValue && useFactory().factoryID.value === props.factory.id)
    getFactoryImage(props.factory?.id).then((dataURL) => {
    console.log("fetching new Picture")
    if (dataURL !== 'failed') currentPicture.value = dataURL.toString()
  })
})

// Check password
async function submitPassword(factoryId: number, factoryEnterPassword: string) {
  try {
    const response = await fetch(backendUrl + '/api/factory/checkPassword', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({id: factoryId, password: factoryEnterPassword})
    })

    if (response.ok) {
      const data = await response.json()
      if (data) {
        await enterFactory(props.factory?.id, sessUser.value)
        await router.push('/factory')
      } else {
        isInputValid.value = false
      }
    }
  } catch (error) {
    console.error('Error while checking password: ', error)
  }
}
</script>

<template>
  <div class="factorycard" ref="factoryCardRef" @click="(e) => rotateCard(e.currentTarget)">
    <div class="card-front">
      <img class="factory-image" :src="currentPicture" alt=""/>
      <div class="loading" v-if="!isFactoryUpToDate && useFactory().factoryID.value === factory.id">
        <div class="loader"></div>
      </div>
      <div class="factorycard-content">
        <div style="width: max-content">
          <p>{{ factory.name }}</p>
          <p>{{ factory.width }}x{{ factory.depth }}x{{ factory.height }}</p>
          <p v-if="sessUser !== factory.author">{{ factory.author }}</p>
          <p v-else>your factory</p>
        </div>
        <button v-if="props.factory?.author === sessUser" class="dustbin-btn" @click.stop="deleteButtonClicked">
          <img class="dustbin" src="../assets/icons8-mülleimer-48.png" alt="Papierkorb"/>
        </button>
      </div>
    </div>
    <div class="card-back" style="display: none">
      <form
          class="password-form"
          @submit.prevent="submitPassword(props.factory?.id, factoryEnterPassword)"
      >
        <div class="input-wrapper" :class="{'invalid': !isInputValid}">
          <input
              v-model="factoryEnterPassword"
              type="password"
              placeholder="enter password"
              class="password-input"
              @click="(e) => e.stopPropagation()"
              @input="isInputValid = true"
          />
          <button type="submit" class="arrow-button" @click.stop>
            <img src="/icons8-pfeil-rechts-48.png" alt="Pfeil"/>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.factorycard {
  position: relative;
  border-radius: 30px;
  height: 260px;
  width: 250px;
  transition: transform 0.5s;
  border: 1px solid transparent;
}

.factorycard:hover {
  border: 1px solid #683ce4;
}

.card-front {
  position: relative;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
}

.card-back {
  align-items: center;
  height: 100%;
  background-color: #342844;
  padding: 0 10px 0 10px;
  border-radius: 30px;
}

.input-wrapper {
  display: flex;
  border: 1.5px solid #683ce4;
  border-radius: 30px;
  padding: 10px 5px 10px 15px;
  align-items: center;
  gap: 5px;
}

.input-wrapper.invalid {
  animation: wiggle 0.2s ease;
  border-color: red;
}

@keyframes wiggle {
  0%, 100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-5px);
  }
  75% {
    transform: translateX(5px);
  }
}

.password-input {
  /* display: block;
  box-sizing: border-box; */
  background-color: transparent;
  color: white;
  width: 100%;
  border: none;
}

.password-input:focus {
  outline: none;
}

.password-form {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
}

.factory-image {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  border-radius: 30px;
  z-index: 0;
}

.factorycard-content {
  display: flex;
  position: absolute;
  justify-content: space-between;
  bottom: 0;
  padding: 12.5px;
  border-radius: 0 0 30px 30px;
  box-sizing: border-box;
  width: 100%;
  background-color: #342844;
}

.factorycard-content p {
  margin: 0;
}

.factorycard-content p:not(:first-child) {
  font-size: 12px;
}

.factorycard-content > :first-child {
  text-transform: uppercase;
}

.dustbin-btn {
  background-color: transparent;
  border: none;
  cursor: pointer;
  height: min-content;
  align-self: end;
  padding: 6px;
  border-radius: 50%; /* damit Button rund ist */
}

.dustbin-btn:hover {
  background-color: red;
  color: white;
}

.dustbin {
  height: 22.5px;
  width: auto;
  object-fit: cover;
}

.arrow-button {
  border: none;
  background: transparent;
}
.loading {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 67%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 1.5rem;
  position: absolute;
  z-index: 100;
  text-align: center;
  border-top-left-radius: 30px;
  border-top-right-radius: 30px;
}

.loader {
  border: 5px solid rgba(255, 255, 255, 0.3);
  border-top: 5px solid #683ce4;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  margin-top: 20px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
