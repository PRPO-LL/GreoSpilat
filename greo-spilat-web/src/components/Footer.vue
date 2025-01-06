<template>
  <footer class="app-footer">
    <p>&copy; 2025 GreoSpilat. All rights reserved.</p>
    <div class="social-links">
      <a href="https://github.com/PRPO-LL/GreoSpilat" aria-label="Github"><i class="fab fa-github"></i></a>
      <a href="https://www.instagram.com/lukabele/" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
      <!-- Add more social links as needed -->
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter',
};
</script>

<style scoped>
.app-footer {
  background-color: #f8f9fa;
  padding: 14px 20px;
  text-align: center;
  border-top: 1px solid #eaeaea;
  width: 100%;
  position: fixed;
  bottom: 0;
  left: 0;
}

.app-footer p {
  margin: 0 0 10px;
  color: #555;
}

.social-links a {
  margin: 0 10px;
  color: #555;
  text-decoration: none;
  font-size: 18px;
}

.social-links a:hover {
  color: #42b983;
}

/* Optional: Include Font Awesome for social icons */
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css');
</style>
