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
	display: inline-block;
	border-radius:35px;
	cursor:pointer;
	color:#ffffff;
	font-size:22px;
	text-decoration:none;
    padding: 10px 10px 10px 25px;
	margin: auto;
	width: 290px; 
}
.v-button:hover {
	background-color:#4b2ba6;
}
.v-button:active {
	position:relative;
	top:1px;
}
</style>