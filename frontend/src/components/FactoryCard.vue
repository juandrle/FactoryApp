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
  <div class="factorycard" @click="(e) => rotateCard(e.currentTarget)">
    <div class="card-front">
      <img
        class="factory-image"
        src="https://damassets.autodesk.net/content/dam/autodesk/www/industry/manufacturing/integrated-factory-modeling/what-is-integrated-factory-modeling-thumb-1172x660.jpg"
        alt="Factoryimage"
      />
      <div class="factorycard-content">
        <div style="width: max-content">
          <p>{{ factory.name }}</p>
          <p>{{ factory.size }}</p>
          <p>{{ factory.author }}</p>
        </div>
        <button class="dustbin-btn" onclick="#">
          <img class="dustbin" src="../assets/icons8-mülleimer-48.png" alt="Papierkorb" />
        </button>
      </div>
    </div>
    <div class="card-back" style="display: none">
      <form class="password-form">
        <div class="input-wrapper">
          <input
            v-model="password"
            type="password"
            placeholder="Passwort eingeben"
            class="password-input"
            @click="(e) => e.stopPropagation()"
          />
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
  border-radius: 30px;
  height: 260px;
  width: 230px;
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
  padding: 0px 10px 0px 10px;
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
  /* position: absolute; */
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  border-radius: 30px;
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
}
.dustbin {
  height: 30px;
  width: auto;
  object-fit: cover;
}
.arrow-button {
  border: none;
  background: transparent;
}
</style>
