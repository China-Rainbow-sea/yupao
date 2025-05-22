<template>
    <van-nav-bar
            title="标题"
            left-text="返回"
            right-text="按钮"
            left-arrow
            @click-left="onClickLeft"
            @click-right="onClickRight"
    >

        <template #right>
            <van-icon name="search" size="18"/>
        </template>
    </van-nav-bar>

    <div id="content">
        <!--        路由出口-->
        <!--        路由匹配到的组件将渲染在这里-->
        <router-view></router-view>
    </div>

    <van-tabbar route @change="onChange">
        <van-tabbar-item icon="home-o" name="index">主页</van-tabbar-item>
        <van-tabbar-item icon="search" name="team">队伍</van-tabbar-item>
        <van-tabbar-item icon="friends-o" name="user">个人</van-tabbar-item>
    </van-tabbar>


</template>

<script setup>

    import {Toast} from 'vant';
    import {useRouter} from 'vue-router';


    const router = useRouter()

    const onClickLeft = () => {
        router.back()
    };
    const onClickRight = () => {
        router.push("/search")
    };

    // 表示底部栏第一个主页亮起
    // const active = ref("index");
    const onChange = (index) => Toast(`标签 ${index}`);
</script>

<style scoped>
#content {
  padding-bottom: 50px;
}
</style>