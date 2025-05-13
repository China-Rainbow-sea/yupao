<template>
    <van-cell title="昵称" is-link to="/user/edit" :value="user.username"/>
    <van-cell title="账号" is-link :value="user.userAccount"/>
    <van-cell title="头像" is-link to="/user/edit">
        <img style="height: 48px" :src="user.avatarUrl"/>
    </van-cell>
    <van-cell title="性别" is-link to="/user/edit" :value="user.gender" @click="toEdit('gender','性别',user.gender)"/>
<!--    同理修改，后面的-->
    <van-cell title="电话" is-link to="/user/edit" :value="user.phone"  @click="toEdit('phone','电话',user.phone)" />
    <van-cell title="邮箱" is-link to="/user/edit" :value="user.email"/>
    <van-cell title="星球编号" :value="user.planetCode"/>
    <van-cell title="注册时间" :value="user.createTime.toISOString()"/>

</template>


<script setup lang="ts">


    import {useRoute, useRouter} from "vue-router";

    const router = useRouter();
    const route = useRoute();
    const user = {
        id: 1,
        username: '鱼皮',
        userAccount: 'dogYupi',
        avatarUrl: 'https://p26-passport.byteacctimg.com/img/user-avatar/b2735693d9870a3e1f2526a84d8f137c~80x80.awebp',
        gender: '男',
        phone: '123112312',
        email: '12345@qq.com',
        planetCode: '1234',
        createTime: new Date(),
    }


    const toEdit = (editKey: string, editName: string, currentValue: string) => {
        router.push({
            path: '/user/edit',
            query: {
                editKey,
                editName,
                currentValue,

            }
        })
    }

    // console.log(route.query)
</script>

<style scoped>

</style>