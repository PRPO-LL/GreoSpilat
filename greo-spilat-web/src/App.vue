<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>
<script>
export default {
  name: 'App',
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  margin: 0;
  background: linear-gradient(to right, #A7F1A8, #BFF4BE);
  min-height: 150vh;
  display: flex;
  flex-direction: column;
}
form {
  margin-bottom: 20px;
}
</style>
<script setup lang="ts">
</script>