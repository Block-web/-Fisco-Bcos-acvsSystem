<script setup>


</script>

<template>
    <router-view></router-view>
</template>

<style>
/* 全局样式，确保页面占满整个浏览器 */
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

#app {
  height: 100%;
  width: 100%;
  display: block;
}

/* 重置所有元素的默认边距和盒模型 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
</style>
