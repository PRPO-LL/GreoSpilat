
<template>
  <div id="home">
    <Header/>
    <main>
      <div class="search-bar">
        <input
            id="search"
            @input="getEvents($event.target.value)"
            type="text"
            placeholder="Search for events"
        />
        <button @click="novEvent()">Create event</button>
      </div>
      <div class="event-list">

        <h2 >All events</h2>
        <div
            class="event-card clickable-card"
            v-for="event in events"
            :key="event.id"
            @click="goToEvent(event.iid)"
        >
          <h3>{{ event.title }}</h3>
          <p><strong>Sport:</strong> {{ event.sport }}</p>
          <p><strong>Location:</strong> {{ event.location }}</p>
          <p><strong>Date:</strong> {{ formatDateTime(event.date) }}</p>
<!--          <p>{{ event.description }}</p>-->
<!--          <button @click="joinEvent(event)">Join</button>-->
<!--          <button @click="deleteEvent(event)">Delete</button>-->
        </div>
      </div>
    </main>
<!--    <Footer/>-->
  </div>
</template>

<script>
import apiService from '../services/events.js';
import Header from '../components/Header.vue';
// import Footer from '../components/Footer.vue';
import {handleError} from "vue";

export default {
  name: 'AppHome',
  components: {
    Header,
    // Footer,
  },
  data() {
    return {
      events: [],
      newEvent: {
        iid: '',
        title: '',
        sport: '',
        location: '',
        // max_participants: null,
        description: '',
      },
    };
  },
  created() {
    this.getEvents();
  },
  methods: {
    handleError,
    getEvents(filter = '') {
      apiService
          .getEvents(filter)
          .then(response => {
            this.events = response.data;
          })
          .catch(error => {
            console.error("Spodletel poskus filtracije:", error);
          });
    },
    deleteEvent(event) {
      console.log('Deleting event:', event);
      apiService
          .deleteEvent(event)
          .then(() => {
            this.getEvents();
          })
          .catch(error => {
            console.error(error);
          });
    },
    goToEvent(eventId) {
      // console.log("Ta event" + eventId);
      this.$router.push(`/event/${eventId}`); // Navigate to the event details page
    },
    formatDateTime(dateTimeString) {
      const date = new Date(dateTimeString);
      return date.toLocaleString('sl', {
        year: 'numeric',
        month: 'long',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      });
    },
    novEvent(){
      this.$router.push('/newEvent');
    }
  },
  mounted() {
    // this.events = apiService.getEvents();
  }
};
</script>

<style scoped>

#home {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  margin: 20px auto;
  max-width: 800px;
  text-align: center;
}

main {
  margin-top: 20px;
  padding: 20px;
}

header {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.add-event-form {
  margin: 20px 0;
  text-align: left;
}
.form-group {
  margin-bottom: 1rem;
  margin-top: 40px;
  text-align: left;
}

.add-event-form form {
  display: flex;
  flex-direction: column;
  margin: 0 auto;
  max-width: 300px;
}

.add-event-form label {
  margin: 8px 0 5px;
  font-weight: bold;
}

.add-event-form input,
.add-event-form textarea {
  padding: 8px;
  font-size: 14px;
  margin-bottom: 10px;
}

.add-event-form button {
  align-self: flex-start;
  background-color: #42b983;
  color: white;
  border: none;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 4px;
  cursor: pointer;
}

.add-event-form button:hover {
  background-color: #2a976e;
}

.search-bar {
  margin: 20px;
  text-align: center;
}

.search-bar input {
  width: 90%;
  max-width: 600px;
  padding: 12px 20px;
  font-size: 18px;
  border: 2px solid #ccc;
  border-radius: 8px;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
  transition: border-color 0.3s;
}

.search-bar input:focus {
  border-color: #42b983;
  outline: none;
}


.event-list {
  text-align: left;
  margin-top: 0px;
}

.event-list h2 {
  margin-bottom: 20px;
}

.event-card {
  background-color: #fff;
  margin-bottom: 15px;
  padding: 20px;
  border-radius: 6px;
  box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.1);
}
.event-card:hover{
  background-color: #d6f5d6;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.event-card h3 {
  margin: 0 0 10px;
}

.event-card p {
  margin: 5px 0;
}
.search-bar button{
  background-color: #097969;
  color: #fff;
  border: none;
  padding: 6px 10px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}
.search-bar button:hover {
  background-color: #AFE1AF;
}

.event-card button {
  background-color: #097969;
  color: #fff;
  border: none;
  padding: 6px 10px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

.event-card button:hover {
  background-color: #AFE1AF;
}
</style>
