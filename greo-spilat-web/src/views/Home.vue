
<template>
  <div id="home">
    <Header @logout="logout()"/>
    <main>
      <div class="event-list">
        <h2>Upcoming Events</h2>
        <div
            class="event-card"
            v-for="event in events"
            :key="event.id"
        >
          <h3>{{ event.title }}</h3>
          <p><strong>Sport:</strong> {{ event.sport }}</p>
          <p><strong>Location:</strong> {{ event.location }}</p>
  <!--        <p><strong>Max Participants:</strong> {{ event.max_participants }}</p>-->
          <p>{{ event.description }}</p>

          <button @click="deleteEvent(event)">Delete</button>
        </div>
      </div>
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
  name: 'AppHome',
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
    getEvents() {
      apiService
          .getEvents()
          .then(response => {
            this.events = response.data;
          })
          .catch(error => {
            console.error(error);
          });
    },
    addEvent() {
      apiService
          .addEvent(this.newEvent)
          .then(() => {
            // sprazni forms
            this.newEvent = {
              title: '',
              sport: '',
              location: '',
              max_participants: null,
              description: '',
            };
            //refreša
            this.getEvents();
          })
          .catch(error => {
            console.error(error);
          });
    },
    deleteEvent(event) {
      apiService
          .deleteEvent(event)
          .then(() => {
            this.getEvents();
          })
          .catch(error => {
            console.error(error);
          });
    },
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


.event-list {
  text-align: left;
  margin-top: 40px;
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

.event-card h3 {
  margin: 0 0 10px;
}

.event-card p {
  margin: 5px 0;
}

.event-card button {
  background-color: #ff4d4d;
  color: #fff;
  border: none;
  padding: 6px 10px;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 10px;
}

.event-card button:hover {
  background-color: #ff1a1a;
}
</style>
