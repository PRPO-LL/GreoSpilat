
<template>
  <div id="add">
    <Header/>
    <main class="form-container">
      <h1>Add New Event</h1>
      <form @submit.prevent="addEvent" class="add-event-form">
        <div class="form-group">
          <label for="title">Event Title</label>
          <input
              id="title"
              v-model="newEvent.title"
              type="text"
              placeholder="Enter the event title"
              required
          />
        </div>

        <div class="form-group">
          <label for="sport">Sport</label>
          <input
              id="sport"
              v-model="newEvent.sport"
              type="text"
              placeholder="Enter the sport type"
              required
          />
        </div>

        <div class="form-group">
          <label for="location">Location</label>
          <input
              id="location"
              v-model="newEvent.location"
              type="text"
              placeholder="Enter the event location"
              required
          />
        </div>

        <div class="form-group">
          <label for="max-participants">Max Participants</label>
          <input
              id="max-participants"
              v-model="newEvent.max_participants"
              type="number"
              min="1"
              placeholder="Enter the maximum number of participants"
          />
        </div>

        <div class="form-group">
          <label for="description">Description</label>
          <textarea
              id="description"
              v-model="newEvent.description"
              placeholder="Enter a description for the event"
              rows="4"
          ></textarea>
        </div>

        <button type="submit" class="submit-button">Add Event</button>
        <div v-if="addEventError" class="error-message">
          Login unsuccessful. Please try again.
        </div>
      </form>
    </main>
    <Footer/>
  </div>
</template>

<script>
import apiService from '../services/events.js';
import Header from '../components/Header.vue';
import Footer from '../components/Footer.vue';
import {handleError} from "vue";

export default {
  name: 'NewEvent',
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      events: [],
      newEvent: {
        title: '',
        sport: '',
        location: '',
        max_participants: null,
        description: '',
      },
      addEventError: false,
    };
  },
  methods: {
    handleError,
    addEvent() {
      try{
        apiService
            .addEvent(this.newEvent)
            .then(() => {
              this.newEvent = {
                title: '',
                sport: '',
                location: '',
                max_participants: null,
                description: '',
              };
            })
            .catch(error => {
              console.error('Error adding event:', error);
            });
        this.domu();
      } catch (error){
        console.error('Error creating event:', error);
        this.loginErrorHandler();
      }
    },
    loginErrorHandler(){
      this.newUser ={
        title: '',
            sport: '',
            location: '',
            max_participants: null,
            description: '',
      }; // sprazni polja
      this.addEventError = true;
    },
    create(){
      this.$router.push('/newEvent');
    },
    domu(){
      this.$router.push('/home');
    }
  },
};
</script>

<style scoped>
#add-event-page {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  margin: 20px auto;
  max-width: 800px;
  text-align: center;
  background: linear-gradient(to right, #f9fafb, #ffffff); /* Subtle gradient background */
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* Add a shadow for depth */
  padding: 20px;
}

.form-container {
  margin-top: 50px;
  padding: 20px;
}

h1 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.add-event-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
  max-width: 600px;
  margin: 0 auto;
  text-align: left;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  font-weight: bold;
  margin-bottom: 5px;
  color: #555;
}

input,
textarea {
  padding: 10px;
  font-size: 16px;
  border: 2px solid #ddd;
  border-radius: 6px;
  background-color: #f9f9f9;
  outline: none;
  transition: border-color 0.3s ease-in-out;
}

input:focus,
textarea:focus {
  border-color: #42b983;
  background-color: #fff;
}

textarea {
  resize: vertical; /* Allow resizing vertically but not horizontally */
}

.submit-button {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 12px 20px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover {
  background-color: #2a976e;
}
.error-message {
  color: red;
  margin: 1rem 0;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  #add-event-page {
    padding: 15px;
  }

  h1 {
    font-size: 20px;
  }

  .add-event-form {
    gap: 10px;
  }

  input,
  textarea {
    font-size: 14px;
    padding: 8px;
  }

  .submit-button {
    font-size: 14px;
    padding: 10px 16px;
  }
}

</style>

