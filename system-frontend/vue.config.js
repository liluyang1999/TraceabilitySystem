const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    proxy: {
      '/server': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        ws: false,
      },
      '/blockchain': {
        target: 'http://localhost:9001',
        changeOrigin: true,
        ws: false,
      }
    }
  }
})
