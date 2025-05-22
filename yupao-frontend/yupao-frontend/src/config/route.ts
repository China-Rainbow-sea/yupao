// 定义一些路由
// 注册路由
import Team from "../pages/TeamPage.vue";
import Index from "../pages/IndexPage.vue";
import User from "../pages/UserPage.vue";
import SearchPage from "../pages/SearchPage.vue";
import SearchResultPage from "../pages/SearchResultPage.vue";
import UserEditPage from "../pages/UserEditPage.vue";
import UserLoginPage from "../pages/UserLoginPage.vue";


// 2. 定义一些路由
// 每个路由都需要映射到一个组件
// 我们后面再讨论嵌套路由
const routes = [
    {path: '/', component: Index},
    {path: '/team', component: Team},
    {path: '/user', component: User},
    {path: '/search', component: SearchPage},
    {path: '/user/list', component: SearchResultPage},
    {path: '/user/edit', component: UserEditPage},
    {path: '/user/login', component: UserLoginPage}
]

export default routes;