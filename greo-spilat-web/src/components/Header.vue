<!--<template>-->
<!--  <header class="app-header">-->
<!--    <div class="header-content">-->
<!--      <h1 class="logo">GreoSpilat</h1>-->
<!--      <nav>-->
<!--        <ul class="nav-links">-->
<!--          <Button label="Home" icon="pi pi-home" @click="home">Home</Button>-->
<!--          <Button label="About"  @click="about">About</Button>-->
<!--          &lt;!&ndash; Add more navigation links as needed &ndash;&gt;-->
<!--          <Button label="Submit"  @click="logout">Logout</Button>-->
<!--          <Button label="Messages">Massages</Button>-->
<!--        </ul>-->
<!--      </nav>-->
<!--&lt;!&ndash;      <button @click="logout" class="logout-btn">Logout</button>&ndash;&gt;-->

<!--    </div>-->
<!--  </header>-->
<!--</template>-->

<!--<script>-->
<!--import Button from 'primevue/button';-->
<!--export default {-->
<!--  name: 'AppHeader',-->
<!--  components: {-->
<!--    Button,-->
<!--  },-->
<!--  methods: {-->
<!--    logout() {-->
<!--      // Emit a logout event to the parent component-->
<!--      this.$emit('logout');-->
<!--    },-->
<!--    home(){-->
<!--      this.$router.push('/');-->
<!--    },-->
<!--    about(){-->
<!--      this.$router.push('/about');-->
<!--    },-->
<!--  },-->
<!--};-->
<!--</script>-->

<!--<style scoped>-->
<!--.app-header {-->
<!--  background-color: #42b983;-->
<!--  padding: 15px 30px;-->
<!--  color: white;-->
<!--  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);-->
<!--}-->

<!--.header-content {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: space-between;-->
<!--}-->

<!--.logo {-->
<!--  margin: 0;-->
<!--  font-size: 24px;-->
<!--  font-weight: bold;-->
<!--}-->

<!--.nav-links {-->
<!--  list-style: none;-->
<!--  display: flex;-->
<!--  gap: 20px;-->
<!--  margin: 0;-->
<!--}-->

<!--.nav-links a {-->
<!--  color: white;-->
<!--  text-decoration: none;-->
<!--  font-size: 16px;-->
<!--}-->

<!--.nav-links a:hover {-->
<!--  text-decoration: underline;-->
<!--}-->

<!--.logout-btn {-->
<!--  background-color: #ff4d4d;-->
<!--  border: none;-->
<!--  padding: 5px 12px;-->
<!--  border-radius: 4px;-->
<!--  cursor: pointer;-->
<!--  font-size: 14px;-->
<!--  color: white;-->
<!--}-->

<!--.logout-btn:hover {-->
<!--  background-color: #ff1a1a;-->
<!--}-->
<!--</style>-->



<!-- -&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;-->
<template>
  <header class="app-header">
    <div class="header-container">
      <!-- Left Logo -->
      <div class="logo">GreoSpilat</div>

      <!-- Center Navigation Buttons -->
      <nav class="nav-buttons">
        <Button
            label="Home"
            @click="home"
            class="nav-button"
            outlined
            rounded
        />
        <Button
            label="About"
            @click="about"
            class="nav-button"
            outlined
            rounded
        />
        <Button
            label="Notifications"
            @click="messages"
            class="nav-button"
            outlined
            rounded
        />
        <Button
            label="Logout"
            @click="logout"
            class="nav-button"
            outlined
            rounded
        />
      </nav>

      <!-- Right Spacer to Center Buttons -->
      <div class="right-spacer"></div>
    </div>
  </header>
</template>

<script>
import Button from 'primevue/button';

export default {
  name: 'AppHeader',
  components: {
    Button,
  },
  methods: {
    logout() {
      localStorage.removeItem('authToken');
      this.$router.push('/');
    },
    home() {
      this.$router.push('/home');
    },
    about() {
      window.location.href = 'https://prpo-ll.github.io/GreoSpilat/';
    },
    messages() {
      this.$router.push('/messages');
    },
  },
};
</script>

<style scoped>
.app-header {
  background-color: whitesmoke;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-container {
  display: flex;
  align-items: center;
  justify-content:  right;
  max-width: 1200px;
  margin: 0 auto;
  padding: 10px 20px;
  height: 60px;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.nav-buttons {
  display: flex;
  gap: 15px;
  flex: 1;
  justify-content: center;

}

.nav-button {
  min-width: 100px;
}

</style>

