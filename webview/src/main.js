import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';

Vue.prototype.$eventBus = new Vue();
Vue.prototype.$axios = axios //this.$axios

createApp(App).use(router).mount('#app');
