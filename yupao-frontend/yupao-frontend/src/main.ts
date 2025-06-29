import {createApp} from 'vue'
// import './style.css'
import App from './App.vue'
// 1. 引入你需要的组件
import {Button, NavBar, Icon, Tabbar, TabbarItem} from 'vant';
// 2. 引入组件样式
import 'vant/lib/index.css';

import Vant from 'vant';


import * as VueRouter from 'vue-router';
import routes from "./config/route";

const app = createApp(App);
app.use(Vant);

// 3. 注册你需要的组件
app.use(Button);
app.use(NavBar);
app.use(Icon);
app.use(Tabbar);
app.use(TabbarItem);
app.use(Vant);





// 3. 创建路由实例并传递 'routes' 配置
// 你可以在这里输入更多的配置,但我们在这里，暂时保持简单

const router = VueRouter.createRouter({
    // 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: VueRouter.createWebHistory(),
    routes, // `routes: routes` 的缩写
})


app.use(router);
app.mount('#app');

