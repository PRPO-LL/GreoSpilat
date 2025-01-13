<template>
  <div class="login-page">
    <div class="login-container">
      <h1>Welcome Back</h1>
      <form @submit.prevent="login">
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
        <div v-if="loginError" class="error-message">
          Login unsuccessful. Please try again.
        </div>
        <Button type="submit" class="nav-button">Login</Button>
      </form>
<!--      register redirect-->
      <div class="register-redirect">
        <p>Don't have an account yet?</p>
        <Button @click="goToRegister" class="nav-button">Register</Button>
      </div>
    </div>
    <Footer/>
  </div>
</template>
  
  <script>
  import apiService from '../services/login.js';
  import Button from 'primevue/button';
  import Footer from '../components/Footer.vue';

  export default {
    name: 'AppLogin',
    components: {
      Button,
      Footer,
    },
    data() {
      return {
        newUser: {
          username: '',
          password: '',
        },
        loginError: false,
      };
    },
    methods: {
      async login() {
        try {
          const response = await apiService.login(this.newUser);
          const token = response.headers.get('authorization') || response.headers.get('Authorization');
          if(token){
            localStorage.setItem('authToken', token);
            this.newUser = {username: '', password: ''};
            this.loginError = false;
            this.$router.push('/home');
          }else{
            this.loginErrorHandler();
          }

        } catch (error) {
          console.error('Error loging user:', error);
          this.loginErrorHandler();
        }
      },
      loginErrorHandler(){
        this.newUser = {username : '', password: ''}; // sprazni polja
        this.loginError = true;
      },
      goToRegister(){
        this.$router.push('/register');
      }
    },
    mounted() {
      //če se še enkrat zlowda pa je token že v local te vrže na home spet
      const token = localStorage.getItem('authToken');
      if(token){
        this.$router.push('/home');
      }
    }
  };
  </script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #A7F1A8, #BFF4BE);
}

.login-container {
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

.error-message {
  color: red;
  margin: 1rem 0;
  font-size: 0.9rem;
}
.register-redirect {
  margin-top: 1.5rem;
  text-align: center;
}

.register-btn:hover {
  background: #6a11cb;
  color: white;
}
</style>
  