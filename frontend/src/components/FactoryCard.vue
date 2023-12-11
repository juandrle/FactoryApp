<script setup lang="ts">
import { defineProps, ref } from 'vue'
import type { Factory } from '@/views/FactoryEnterView.vue';
const isRotated = ref(false)

const rotateCard = () => {
  const card = document.querySelector('.factorycard') as HTMLDivElement
  if (card) {
    const front = card.querySelector('.card-front') as HTMLElement
    const back = card.querySelector('.card-back') as HTMLElement

    if (isRotated.value) {
      front.style.display = 'flex' // Vorderseite anzeigen
      back.style.display = 'none' // Rückseite ausblenden
    } else {
      front.style.display = 'none' // Vorderseite ausblenden
      back.style.display = 'flex' // Rückseite anzeigen
    }
    isRotated.value = !isRotated.value // Zustand aktualisieren
  }
}

const props = defineProps({
  factory: {
    type: Object as () => Factory,
    required: true,
  },
});

</script>

<template>
  <div class="factorycard" @click="rotateCard">
    <div class="card-front">
      <img
        src="https://damassets.autodesk.net/content/dam/autodesk/www/industry/manufacturing/integrated-factory-modeling/what-is-integrated-factory-modeling-thumb-1172x660.jpg"
        alt="Factoryimage"
      />
      <div class="factorycard-content">
        <p>{{ factory.name }}</p>
        <p>{{ factory.size }}</p>
        <p>{{ factory.author }}</p>
        <!-- <p>Erens Fabrik</p>
        <p>20x40x80</p>
        <p>Eren Ceviz</p> -->
      </div>
    </div>
    <div class="card-back" style="display: none">
      <input type="password" placeholder="Passwort eingeben" />
    </div>
  </div>
</template>

<style>
.factorycard {
  position: relative;
  overflow: hidden;
  border-radius: 30px;
  height: 230px;
  width: 200px;
  perspective: 1000px;
  transition: transform 0.5s;
}
.factorycard:hover {
  border: 1px solid #683ce4;
}

.card-front {
  position: absolute;
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
}
.card-back input {
  max-width: 100%;
  padding: 12px 20px;
}

.factorycard img {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.factorycard-content {
  position: absolute;
  bottom: 0;
  padding: 10px;
  border-radius: 0 0 30px 30px;
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
</style>
