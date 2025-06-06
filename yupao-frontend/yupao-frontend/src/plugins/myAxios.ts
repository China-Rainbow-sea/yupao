import axios from "axios";

// const isDev = process.env.NODE_ENV === 'development';

// 创建实例时配置默认值
const myAxios = axios.create({
    LookupAddress: undefined, LookupAddressEntry: undefined,
    baseURL: 'http://localhost:8080/api'
});

// const myAxios: AxiosInstance = axios.create({
//     baseURL: isDev ? 'http://localhost:8080/api' : '线上地址',
// });

myAxios.defaults.withCredentials = false; // 配置为true，表示前端向后端发送请求的时候，需要携带上凭证cookie
// 创建实例后修改默认值


// 添加请求拦截器
myAxios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    console.log('我要发请求了')
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
myAxios.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    console.log('我收到你的响应了',response)
    return response.data;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});

// Add a request interceptor
// myAxios.interceptors.request.use(function (config) {
//     console.log('我要发请求啦', config)
//     // Do something before request is sent
//     return config;
// }, function (error) {
//     // Do something with request error
//     return Promise.reject(error);
// });
//
//
// // Add a response interceptor
// myAxios.interceptors.response.use(function (response) {
//     console.log('我收到你的响应啦', response)
//     // 未登录则跳转到登录页
//     if (response?.data?.code === 40100) {
//         const redirectUrl = window.location.href;
//         window.location.href = `/user/login?redirect=${redirectUrl}`;
//     }
//     // Do something with response data
//     return response.data;
// }, function (error) {
//     // Do something with response error
//     return Promise.reject(error);
// });

export default myAxios;
