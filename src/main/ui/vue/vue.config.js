const { defineConfig } = require('@vue/cli-service')
const fs = require('fs')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '^/api': {
        target: 'https://localhost:8443',
        changeOrigin: true,
        ws: true,
        onProxyReq: function(request) {
          request.setHeader("origin", "https://localhost:8443");
        },
      },
    },
    // server: {
    //   type: 'https',
    //   // server: 'https',
    //   options: {
    //     key: fs.readFileSync('../../../../key.pem'),
    //     cert: fs.readFileSync('../../../../cert.pem'),
    //     passphrase: 'timrobot',
    //     requestCert: true
    //   },
    // },
  },
})
