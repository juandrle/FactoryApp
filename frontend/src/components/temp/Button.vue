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

</style>