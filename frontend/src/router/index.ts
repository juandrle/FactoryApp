import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Factory from "@/components/Factory.vue";
import FactoryCreateView from "@/views/FactoryCreateView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/factory',
      name: 'factory',
      component: Factory
    },
    {
      path: '/create',
      name: 'create',
      component: FactoryCreateView
    }
  ]
})

export default router
