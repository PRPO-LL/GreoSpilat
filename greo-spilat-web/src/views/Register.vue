<template>
  <div class="register-page">
    <div class="register-container">
      <h1>Register</h1>

      <form v-if="!registerSuccess" @submit.prevent="register">
        <div class="form-group">
          <label for="username">Username</label>
          <input
              id="username"
              v-model="newUser.username"
              type="text"
              placeholder="Enter your username"
              required
          />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
              id="password"
              v-model="newUser.password"
              type="password"
              placeholder="Enter your password"
              required
          />
        </div>
        <div v-if="registerError" class="error-message">
          Username already exists, please try again.
        </div>
        <button type="submit" class="btn">Register</button>
      </form>

      <div v-else class="success-message">
        <h2>User successfully created!</h2>
        <button @click="goToLogin" class="btn">Go to Login</button>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import apiService from '../services/login.js';
import Footer from '../components/Footer.vue';


export default {
  name: 'App',
  components: {
    Footer,
  },
  data() {
    return {
      newUser: {
        username: '',
        password: '',
      },
      registerError: false,
      registerSuccess: false,
    };
  },
  methods: {
    async register() {
      try {
        const response = await apiService.register(this.newUser);
        if(response.status.valueOf() === 500){
          this.registerErrorHandler();
        }else{
          this.newUser = {username: '', password: ''};
          this.registerError = false;
          this.registerSuccess = true;
          // this.$router.push('/');
        }

      } catch (error) {
        this.registerErrorHandler();
      }
    },
    registerErrorHandler(){
      this.newUser = {username : '', password: ''}; // sprazni polja
      this.registerError = true;
    },
    goToLogin(){
      this.$router.push('/');
    }
  },
};
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #6a11cb, #2575fc);
}

.register-container {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 300px;
}

h1 {
  margin-bottom: 1.5rem;
  color: #333;
}

.form-group {
  margin-bottom: 1rem;
  text-align: left;
}

label {
  font-size: 0.9rem;
  color: #555;
}

input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-top: 0.5rem;
}

input:focus {
  outline: none;
  border-color: #6a11cb;
}

.btn {
  background: #6a11cb;
  color: white;
  padding: 0.8rem 1.5rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
}

.btn:hover {
  background: #2575fc;
}

.error-message {
  color: red;
  margin: 1rem 0;
  font-size: 0.9rem;
}

.success-message {
  color: #333;
  text-align: center;
}

.success-message h2 {
  color: #28a745; /* Green color for success */
  margin-bottom: 1rem;
}
</style>
