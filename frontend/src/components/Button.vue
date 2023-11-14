<script setup lang="ts">
defineProps({
    text: String
})
</script>

<template>
  <a href="#" class="v-button">{{text}}</a>
</template>

<style>
.v-button{
    background-color:#8845c7;
	border-radius:30px;
	cursor:pointer;
	color:#ffffff;
	font-size:16px;
	text-decoration:none;
    max-width: 250px;
    padding: 10px 20px;
}
.v-button:hover {
	background-color:#6a3a99;
}
.v-button:active {
	position:relative;
	top:1px;
}
</style>