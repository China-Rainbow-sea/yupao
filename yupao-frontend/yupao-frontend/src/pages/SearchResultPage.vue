<template>
<!--    <van-card-->
<!--            v-for="user in userList"-->
<!--            :desc="user.prfile"-->
<!--            :title="`${user.username} (${user.planetCode}})`"-->
<!--            :thumb="user.avatarUrl"-->

<!--    >-->
<!--        <template #tags>-->
<!--            <van-tag plain type="danger" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px">-->
<!--                {{tag}}-->
<!--            </van-tag>-->
<!--        </template>-->
<!--        <template #footer>-->
<!--            <van-button size="mini">联系我</van-button>-->
<!--        </template>-->
<!--    </van-card>-->

  <user-card-list :user-list="userList"/>
    <!-- 搜索提示 -->
    <van-empty v-if="!userList || userList.length < 1" description="暂无结果"/>
</template>

<script setup>
    import {onMounted, ref} from 'vue';
    import {useRoute} from "vue-router";
    import myAxios from "../plugins/myAxios";
    import {Toast} from "vant";
    import qs from 'qs';
    import UserCardList from "../components/UserCardList.vue";

    const route = useRoute();
    const {tags} = route.query;

    const userList = ref([]);

    onMounted(async () => {
        const userListData = await myAxios.get('/user/search/tags', {
            params: {
                tagNameList: tags
            },
            paramsSerializer: params => {
                return qs.stringify(params, {indices: false})
            }
        })
            .then(function (response) {
                // console.log('/user/search/tags succeed', response);
                // Toast.success("请求成功")
                return response?.data;
            })
            .catch(function (error) {
                // console.error('/user/search/tags error', error);
                // Toast.fail('请求失败')
            })
        console.log("数据数据:", userListData)
        if (userListData) {
            userListData.forEach(user => {
                if (user.tags) {
                    user.tags = JSON.parse(user.tags);
                }
            })
            userList.value = userListData;
        }
    })


    // const mockUser = {
    //   id: 12345,
    //   username: '鱼皮',
    //   userAccount: '12314',
    //   profile: '一名精神小伙，目前还有头发，谢谢大家，阿爸爸阿爸爸阿巴阿巴阿巴',
    //   avatarUrl: 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png',
    //   gender: 0,
    //   phone: '13113113111',
    //   email: '592342843721987@xzcxzczxcz.com',
    //   userRole: 0,
    //   planetCode: '1234',
    //   tags: ['java', 'emo', '打工中', 'emo', '打工中'],
    //   createTime: new Date(),
    // }


</script>

<style scoped>

</style>
