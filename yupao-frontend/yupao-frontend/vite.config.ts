import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
})

// 导出配置对象，使用ES模块语法
// export default defineConfig({
//   plugins: [vue()], // 启用Vue插件
//   server: { // 注意：在Vite的新版本中，配置项`devServer`已更名为`server`
//     proxy: {
//       '/api': {
//         target: 'http://localhost:8080/api', // 目标服务器地址
//         changeOrigin: true, // 是否改变源
//         // 如果需要路径重写，可以取消以下行的注释
//         // pathRewrite: { '^/api': '' }
//       }
//     }
//   }
// });
// module.exports = {
//   devServer: {
//     proxy: {
//       '/api': {
//         target: 'http://localhost:8080/api', // 目标服务器
//         changeOrigin: true, // 是否改变源
//         // pathRewrite: { '^/api': '' } // 路径重写
//       }
//     }
//   }
// };
//
