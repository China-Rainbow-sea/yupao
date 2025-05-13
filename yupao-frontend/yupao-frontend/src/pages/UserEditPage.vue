<template>
    <van-form @submit="onSubmit">
        <van-field
                v-model="editUser.currentValue"
                :name="editUser.editKey"
                :label="editUser.editName"
                :placeholder="`请输入${editUser.editName}`"
        />
        <div style="margin: 16px;">
            <van-button round block type="primary" native-type="submit">
                提交
            </van-button>
        </div>
    </van-form>
</template>


<script setup lang="ts">

    import {useRoute, useRouter} from "vue-router";
    import {ref} from "vue";

    const router = useRouter();
    const route = useRoute();

    const editUser = ref({
        editKey: route.query.editKey,
        currentValue: route.query.currentValue,
        editName: route.query.editName,
    })


    const onSubmit = (values) => {
        // to do 把 editKey,currentValue,editName 提交到后端
        console.log('onSubmit', values);
    }


    // console.log(route)
    // console.log(route.params)
    // console.log(route.query)


</script>

<style scoped>

</style>