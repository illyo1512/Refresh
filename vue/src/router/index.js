import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SearchResult from '../components/SearchResult.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    
    component: () => import( '../views/AboutView.vue')
  },
  {
    path: '/search',
    name: 'search',
    
    component: () => import( '../components/SearchPage.vue')
  },
  { path: '/search/:name', name: 'SearchResult', component: SearchResult, props: true },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
