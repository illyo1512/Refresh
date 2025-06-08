import { createRouter, createWebHashHistory } from 'vue-router'
import MainPage from '../views/MainPage.vue'
import MapPage from '../views/MapPage.vue'
import Search from '../views/Search.vue'
import Route from '../views/Route.vue'
import SearchResultPage from '../views/SearchResultPage.vue'
import MapSelect from '../views/MapSelect.vue'
import LoginPage from '../views/LoginPage.vue'
import RegisterPage from '../views/RegisterPage.vue'
import FindIdPage from '../views/FindIdPage.vue'
import FoundIdPage from '../views/FoundIdPage.vue'
import FindPasswordPage from '../views/FindPasswordPage.vue'
import ChangePasswordPage from '../views/ChangePasswordPage.vue'
import ReviewListPage from '../views/ReviewListPage.vue'
import WriteReviewPage from '../views/WriteReviewPage.vue'

const routes = [
  { path: '/', name: 'MainPage', component: MainPage },
  { path: '/map', name: 'MapPage', component: MapPage },
  { path: '/search', name: 'Search', component: Search },
  { path: '/route', name: 'route', component: Route },
  { path: '/result/:name', name: 'SearchResult', component: SearchResultPage },
  { path: '/select', name: 'MapSelect', component: MapSelect },
  { path: '/login', name: 'LoginPage', component: LoginPage },
  { path: '/register', name: 'RegisterPage', component: RegisterPage },
  { path: '/findid', name: 'FindIdPage', component: FindIdPage },
  { path: '/foundid', name: 'FoundIdPage', component: FoundIdPage },
  { path: '/findpassword', name: 'FindPasswordPage', component: FindPasswordPage },
  { path: '/changepassword', name: 'ChangePasswordPage', component: ChangePasswordPage },
  { path: '/reviewlist', name: 'ReviewListPage', component: ReviewListPage },
  { path: '/writereview', name: 'WriteReviewPage', component: WriteReviewPage }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
