<script setup lang="ts">
import { defineProps, ref } from 'vue'
import type { Factory } from '@/views/FactoryEnterView.vue'


const props = defineProps({
  factory: {
    type: Object as () => Factory,
    required: true
  },
  rotateCard: {
    type: Object as () => (clickTarget: EventTarget | null) => void,
    required: true
  }
})

// Check password
const password = ref('')
</script>

<template>
  <div class="factorycard" @click="e => rotateCard(e.currentTarget)">
    <div class="card-front">
      <img
        src="https://damassets.autodesk.net/content/dam/autodesk/www/industry/manufacturing/integrated-factory-modeling/what-is-integrated-factory-modeling-thumb-1172x660.jpg"
        alt="Factoryimage"
      />
      <div class="factorycard-content">
        <p>{{ factory.name }}</p>
        <p>{{ factory.size }}</p>
        <p>{{ factory.author }}</p>
      </div>
    </div>
    <div class="card-back" style="display: none">
      <form class="password-form">
        <div class="input-wrapper">
          <input v-model="password" type="password" placeholder="Passwort eingeben" class="password-input"  @click="e => e.stopPropagation()"/>
          <button type="submit" class="arrow-button">
            <img src="/icons8-pfeil-rechts-48.png" alt="Pfeil" />
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.factorycard {
  position: relative;
  overflow: hidden;
  border-radius: 30px;
  height: 230px;
  width: 200px;
  perspective: 1000px;
  transition: transform 0.5s;
  border: 1px solid transparent;
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
/* .card-back input {
  max-width: 100%;
  padding: 12px 20px;
} */
.input-wrapper{
  display: flex;
}
.password-input{
  display: block;
  box-sizing: border-box;
  padding: 12px 20px;
  background-color: transparent;
  border: 2px solid #683ce4;
  border-radius: 30px;
  color: white; 
  width: 100%;
}
.password-form {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
}
.card-front img {
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
.arrow-button {
  border: none; 
  background: transparent;
  position: relative;
}
</style>
