<script setup lang="ts">
import {ref, onMounted} from 'vue';

const isMenuOpen = ref(false);

onMounted(() => {
    window.addEventListener('keydown', (event) =>{
        if (event.key === 'v' || event.key === 'V') {
            if(isMenuOpen.value == false){
                isMenuOpen.value = true;
            }else {
                isMenuOpen.value = false;
            }
            
    }
    });
});

</script>


<template>
    <nav class="menu" v-show="isMenuOpen">
        <input  class="menu-toggler" type="checkbox">
        <label for="menu-toggler"></label>
        <ul>
        <li class="menu-item">
            <a class="fas fa-cat" href=""></a>
        </li>
        <li class="menu-item">
            <a class="fas fa-cookie-bite" href="#"></a>
        </li>
        <li class="menu-item">
            <a class="fab fa-earlybirds" href="#"></a>
        </li>
        <li class="menu-item">
            <a class="fab fa-fly"></a>
        </li>
        <li class="menu-item">
            <a class="far fa-gem" href="#"></a>
        </li>
        
        </ul>
    </nav>
</template>

<style scoped>
body {
    overflow: hidden;
    background: #E6E6FA;
    background: linear-gradient(to right, #b24592, #f15f79)
  }
  .menu-toggler {
    position: absolute;
    display: block;
    top:0;
    bottom:0;
    right:0;
    left:0;
    margin:auto;
    width:40px;
    height:40px;
    z-index:2;
    opacity:0;
    cursor:pointer;
  }
  .menu-toggler:hover + label,
  .menu-toggler:hover + label:before,
  .menu-toggler:hover + label:after{
    background: white;
  }
  .menu-toggler:checked + label {
    background: transparent;
  }
  .menu-toggler:checked + label:before,
  .menu-toggler:checked + label:after{
    top:0;
    width:40px;
    transform-origin: 50% 50%;
  }
  .menu-toggler:checked + label:before {
    transform: rotate(45deg) translateY(-15px) translateX(-15px);
  }
  .menu-toggler:checked + label:after {
    transform: rotate(-45deg);
  }
  .menu-toggler:checked ~ ul .menu-item { 
    opacity: 1;
  }
  .menu-toggler:checked ~ ul .menu-item:nth-child(1) {
    transform: rotate(90deg) translate(-150px);
  }
  .menu-toggler:checked ~ ul .menu-item:nth-child(2) {
    transform: rotate(162deg) translateX(-150px);
  }
  .menu-toggler:checked ~ ul .menu-item:nth-child(3) {
    transform: rotate(234deg) translateX(-150px);
  }
  .menu-toggler:checked ~ ul .menu-item:nth-child(4) {
    transform: rotate(306deg) translateX(-150px);
  }
  .menu-toggler:checked ~ ul .menu-item:nth-child(5) {
    transform: rotate(378deg) translateX(-150px);
  }
  
  .menu-toggler:checked ~ ul .menu-item a {
    pointer-events:auto;
  }
  .menu-toggler + label {
    width: 40px;
    height: 5px;
    display: block;
    z-index: 1;
    border-radius: 2.5px;
    background: rgba(230, 239, 250, 0.9);
    transition: transform 0.5s top 0.5s;
    position: absolute;
    display: block;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
  }
  .menu-toggler + label:before,
  .menu-toggler + label:after {
    width: 40px;
    height: 5px;
    display: block;
    z-index: 1;
    border-radius: 2.5px;
    background: rgba(255, 255, 255, 0.7);
    transition: transform 0.5s top 0.5s;
    content: "";
    position: absolute;
    display: block;
    left: 0;
  }
  .menu-toggler + label:before {
    top: 10px;
  }
  .menu-toggler + label:after {
    top: -10px;
  }
  .menu-item:nth-child(1) a {
    transform: rotate(0deg);
  }
  .menu-item:nth-child(2) a {
    transform: rotate(-72deg);
  }
  .menu-item:nth-child(3) a {
    transform: rotate(-144deg);
  }
  .menu-item:nth-child(4) a {
    transform: rotate(-206deg);
  }
  .menu-item:nth-child(5) a {
    transform: rotate(-278deg);
  }
  
  .menu-item {
    position: absolute;
    display: block;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
    width: 120px;
    height: 120px;
    opacity: 0;
    transition: 0.5s;
  }
  .menu-item a {
    display: block;
    width: inherit;
    height: inherit;
    line-height: 80px;
    color: rgba(255, 255, 255, 0.7);
    background: rgba(230, 230, 250, 0.7);
    border-radius: 50%;
    text-align: center;
    text-decoration: none;
    font-size: 40px;
    pointer-events: none;
    transition: 0.2s;
  }
  .menu-item a:hover {
    box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.3);
    color: white;
    background: rgba(255, 255, 255, 0.3);
    font-size: 44.44px
  }
</style>