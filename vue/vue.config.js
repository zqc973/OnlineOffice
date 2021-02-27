module.exports = {
  configureWebpack: {
    resolve: {
      alias: {
        assets: "@/assets",
        global: "@/global",
        components: "@/components",
        network: "@/network",
        views: "@/views"
      }
    }
  }
};
