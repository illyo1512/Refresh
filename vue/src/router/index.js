import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SearchResult from '../components/SearchResult.vue'
import Map from '../components/RouteMapView.vue'
import point from '../components/WaypointListView.vue'

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
  { path: '/search/:name', name: 'SearchResult', component: SearchResult, props: true 

  },
  {
    path: '/map',
    name: 'map',
    component: Map
  },
  {
    path: '/point',
    name: 'point',
    component: point
  },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
