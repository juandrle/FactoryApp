<script setup lang="ts">
import { RouterLink } from 'vue-router';
defineProps({
    text: String,
	link: {
      type: String,
      required: true
    }
})
</script>

<template>
  <RouterLink :to="link" class="v-button">{{text}}</RouterLink>
</template>

<style>
.v-button {
  background-color: #683CE4;
  display: inline-block;
  border-radius: 35px;
  cursor: pointer;
  color: #ffffff;
  font-size: 22px;
  text-decoration: none;
  text-align: center;
  padding: 10px 10px 10px 10px;
  margin: auto;
  width: 290px;
  border: none;
}

.v-button:hover {
  background-color: #4b2ba6;
}

.v-button:active {
  position: relative;
  top: 1px;
}
</style>