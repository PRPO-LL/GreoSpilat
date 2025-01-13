import { createRouter, createWebHistory } from 'vue-router'

import LoginPage from '../views/Login.vue'
// import Register from '../views/Register.vue'
import HomePage from '../views/Home.vue'
import RegisterPage from '../views/Register.vue'
import NewEventPage from '../views/NewEvent.vue'
import EventDetailsPage from '../views/EventDetails.vue'

const routes = [
    {
        path: '/',
        name: 'Login',
        component: LoginPage,
    },
    {
        path: '/register',
        name: 'Register',
        component: RegisterPage,
    },
    {
        path: '/:pathMatch(.*)*', // redirekta vse na login
        redirect: '/',
    },
    {
        path: '/home',
        name: 'Home',
        component: HomePage,
    },
    {
        path: '/newEvent',
        name: 'NewEvent',
        component: NewEventPage,
    },
    {
        path: '/event/:id',
        name: 'EventDetails',
        component: EventDetailsPage,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});


router.beforeEach((to,from, next) =>{
   const token = localStorage.getItem('authToken');
   if(to.name === 'Home' && !token){
       //brez avtorizacije gres nazaj na login
       next({name: 'Login'});
   }
   else{
       //z avtorizacijo pa kamor hočeš
       next();
   }
});
export default router;
