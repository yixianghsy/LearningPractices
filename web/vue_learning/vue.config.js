module.exports = {
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
          $: "jquery",
          jQuery: "jquery",
          "windows.jQuery": "jquery"
      })
  ]
  }
}
