<template>
<!--    user 存在才展示信息-->
   <template v-if="user">
       <van-cell title="昵称" is-link to="/user/edit" :value="user.username"/>
       <van-cell title="账号" is-link :value="user.userAccount"/>
       <van-cell title="头像" is-link to="/user/edit">
           <img style="height: 48px" :src="user.avatarUrl"/>
       </van-cell>
       <van-cell title="性别" is-link to="/user/edit" :value="user.gender" @click="toEdit('gender','性别',user.gender)"/>
       <!--    同理修改，后面的-->
       <van-cell title="电话" is-link to="/user/edit" :value="user.phone" @click="toEdit('phone','电话',user.phone)"/>
       <van-cell title="邮箱" is-link to="/user/edit" :value="user.email" @click="toEdit('email','邮箱',user.email)" />
       <van-cell title="星球编号" :value="user.planetCode"/>
       <van-cell title="注册时间" :value="user.createTime().toISOString()"/>
   </template>

</template>


<script setup lang="ts">


    import {useRoute, useRouter} from "vue-router";
    import {getCurrentUser} from "../services/user";
    import {onMounted, ref} from "vue";
    import {Toast} from "vant";
    //
    const user = ref();

    const route = useRoute();
    // const user = {
    //     id: 1,
    //     username: '鱼皮',
    //     userAccount: 'dogYupi',
    //     avatarUrl: 'https://p26-passport.byteacctimg.com/img/user-avatar/b2735693d9870a3e1f2526a84d8f137c~80x80.awebp',
    //     gender: '男',
    //     phone: '123112312',
    //     email: '12345@qq.com',
    //     planetCode: '1234',
    //     createTime: new Date(),
    // }

    onMounted(async () => {
        const res = await getCurrentUser();
        user.value = await getCurrentUser();
        //
        console.log("res",res);
        console.log("user",user);
        console.log("user.value",user.value);

        if (res.data) {
            user.value = res.data;
            Toast.success("获取用户信息成功");
        } else {
            Toast.fail("获取用户信息失败");
        }
    })


    const router = useRouter();

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