import {createApp} from 'vue'
// import './style.css'
import App from './App.vue'
// 1. 引入你需要的组件
import {Button, NavBar, Icon,Tabbar,TabbarItem} from 'vant';
// 2. 引入组件样式
import 'vant/lib/index.css';

const app = createApp(App);

// 3. 注册你需要的组件
app.use(Button);
app.use(NavBar);
app.use(Icon);
app.use(Tabbar);
app.use(TabbarItem);

app.mount('#app')

