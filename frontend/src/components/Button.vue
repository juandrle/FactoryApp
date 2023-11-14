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
    background-color:#683CE4;
	border-radius:30px;
	cursor:pointer;
	color:#ffffff;
	font-size:16px;
	text-decoration:none;
    max-width: 250px;
    padding: 10px 45px;
}
.v-button:hover {
	background-color:#4b2ba6;
}
.v-button:active {
	position:relative;
	top:1px;
}
</style>