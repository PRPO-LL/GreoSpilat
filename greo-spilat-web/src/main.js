import { createApp } from 'vue'
import App from './App.vue'
import router from './router'


import PrimeVue from 'primevue/config';
import Aura from '@primevue/themes/aura';
import Button from 'primevue/button';

const app = createApp(App);
app.use(router);
app.mount('#app');
app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            darkModeSelector: 'false',
        }
    }
});
app.component('HomeButton', Button);

