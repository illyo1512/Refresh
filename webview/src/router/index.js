import { createRouter, createWebHashHistory } from 'vue-router'
import MainPage from '../views/MainPage.vue'
import MapPage from '../views/MapPage.vue'
import Search from '../views/Search.vue'
import Route from '../views/Route.vue'
import SearchResultPage from '../views/SearchResultPage.vue'
import MapSelect from '../views/MapSelect.vue'

const routes = [
  { path: '/', name: 'MainPage', component: MainPage },
  { path: '/map', name: 'MapPage', component: MapPage },
  { path: '/search', name: 'Search', component: Search },
  { path: '/route', name: 'route', component: Route },
  { path: '/result/:name', name: 'SearchResult', component: SearchResultPage },
  { path: '/select', name: 'MapSelect', component: MapSelect }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
