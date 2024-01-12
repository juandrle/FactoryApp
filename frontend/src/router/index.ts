import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Factory from "@/views/Factory.vue";
import FactoryCreateView from "@/views/FactoryCreateView.vue";
import FactoryEnterView from "@/views/FactoryEnterView.vue";
import LoginView from "@/views/LoginView.vue";

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
            path: '/enter',
            name: 'enter',
            component: FactoryEnterView
        },
        {
            path: '/create',
            name: 'create',
            component: FactoryCreateView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,

        },
        {
            path: '/signup',
            name: 'signup',
            component: () => import('@/views/SignUpView.vue'),
            beforeEnter: (to, from, next) => {

                next();
            }
        }
    ]
})

export default router
