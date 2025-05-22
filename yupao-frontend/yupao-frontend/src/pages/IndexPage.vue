<template>

  <user-card-list :user-list="userList"/>
<!--  <van-card-->
<!--      v-for="user in userList"-->
<!--      :desc="user.prfile"-->
<!--      :title="`${user.username} (${user.planetCode}})`"-->
<!--      :thumb="user.avatarUrl"-->

<!--  >-->
<!--    <template #tags>-->
<!--      <van-tag plain type="danger" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px">-->
<!--        {{tag}}-->
<!--      </van-tag>-->
<!--    </template>-->
<!--    <template #footer>-->
<!--      <van-button size="mini">联系我</van-button>-->
<!--    </template>-->
<!--  </van-card>-->
  <!-- 搜索提示 -->
  <van-empty v-if="!userList || userList.length < 1" description="数据为空"/>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import UserCardList from "../components/UserCardList.vue";

const route = useRoute();
const {tags} = route.query;

const userList = ref([]);

onMounted(async () => {
  const userListData = await myAxios.get('/user/recommend', {
    params: {
      tagNameList: tags
    },
  })
      .then(function (response) {
        console.log('/user/search/tags succeed', response);
        Toast.success("请求成功")
        return response?.data;
      })
      .catch(function (error) {
        console.error('/user/search/tags error', error);
        Toast.fail('请求失败')
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



</script>

<style scoped>

</style>
