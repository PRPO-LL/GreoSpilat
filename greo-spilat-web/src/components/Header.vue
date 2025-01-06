<template>
  <header class="app-header">
    <div class="header-container">
      <div class="logo">GreoSpilat</div>

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

