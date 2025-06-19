import { createRouter, createWebHashHistory } from 'vue-router'
import MainPage from '../views/MainPage.vue'
import MapPage from '../views/MapPage.vue'
import Search from '../views/Search.vue'
import Route from '../views/Route.vue'
import SearchResultPage from '../views/SearchResultPage.vue'
import MapSelect from '../views/MapSelect.vue'
import self_route from '../views/self_route.vue'
import route_board from '../views/route_board.vue'
import RouteBoardDetail from '../views/RouteBoardDetail.vue'
import LoginPage from '../views/LoginPage.vue'
import RegisterPage from '../views/RegisterPage.vue'
import FindIdPage from '../views/FindIdPage.vue'
import FoundIdPage from '../views/FoundIdPage.vue'
import FindPasswordPage from '../views/FindPasswordPage.vue'
import ChangePasswordPage from '../views/ChangePasswordPage.vue'
import ReviewListPage from '../views/ReviewListPage.vue'
import WriteReviewPage from '../views/WriteReviewPage.vue'
import ColourPage from '../views/ColourPage.vue'
import WritePost from '../views/WritePost.vue'
import SavedRouteSelect from '../views/SavedRouteSelect.vue'

const routes = [
  { path: '/', name: 'MainPage', component: MainPage },
  { path: '/map', name: 'MapPage', component: MapPage },
  { path: '/search', name: 'Search', component: Search },
  { path: '/route', name: 'route', component: Route },
  { path: '/result/:name', name: 'SearchResult', component: SearchResultPage },
  { path: '/select', name: 'MapSelect', component: MapSelect },
  { path: '/self_route', name: 'self_route', component: self_route},
  { path: '/route_board', name: 'route_board', component: route_board},
  { path: '/route-board/:id', name:'RouteBoardDetail', component: RouteBoardDetail},
    { path: '/login', name: 'LoginPage', component: LoginPage },
  { path: '/register', name: 'RegisterPage', component: RegisterPage },
  { path: '/findid', name: 'FindIdPage', component: FindIdPage },
  { path: '/foundid', name: 'FoundIdPage', component: FoundIdPage },
  { path: '/findpassword', name: 'FindPasswordPage', component: FindPasswordPage },
  { path: '/changepassword', name: 'ChangePasswordPage', component: ChangePasswordPage },
  { path: '/reviewlist', name: 'ReviewListPage', component: ReviewListPage },
  { path: '/writereview', name: 'WriteReviewPage', component: WriteReviewPage },
  { path: '/colour', name: 'ColourPage', component: ColourPage },
  { path: '/writepost', name: 'WritePost', component: WritePost },
  { path: '/savedroute', name: 'SavedRouteSelect', component: SavedRouteSelect }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
