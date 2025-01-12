<template>
  <div id="event-details">
    <Header />
    <main class="details-container">
      <h1>{{ event.title }}</h1>

      <div class="details-group">
        <label>Sport:</label>
        <p>{{ event.sport }}</p>
      </div>

      <div class="details-group">
        <label>Location:</label>
        <p>{{ event.location }}</p>
      </div>

      <div class="details-group">
        <label>Capacity:</label>
        <p>{{event.participants}}/{{ event.maxParticipants }}</p>
      </div>

      <div class="details-group">
        <label>Description:</label>
        <p>{{ event.description }}</p>
      </div>

      <button @click="$router.push('/home')" class="back-button">Back to Home</button>
    </main>
    <Footer />
  </div>
</template>


<script>
import Header from '../components/Header.vue';
import Footer from '../components/Footer.vue';
import apiService from '../services/events.js';

export default {
  name: 'EventDetails',
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      event: {},
    };
  },
  created() {
    const eventId = this.$route.params.id; // Get the event ID from the route
    this.getEvent(eventId);
  },
  methods: {
    getEvent(eventId) {
      console.log("Ta event" + eventId);
      apiService.getEvent( eventId ).then(response => {
        this.event = response.data; // Load the event details
      });
    },
  },
};
</script>

<style scoped>
#event-details {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  margin-top: 80px;
  margin-left: 350px;
  margin-bottom: 200px;
  max-width: 800px;
  max-height: 700px;
  text-align: center;
  background: linear-gradient(to right, #f9fafb, #ffffff);
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.details-container {
  padding: 10px;
}

h1 {
  font-size: 28px;
  margin-bottom: 20px;
  color: #333;
}

.details-group {
  margin-bottom: 15px;
  text-align: left;
}

.details-group label {
  font-weight: bold;
  display: block;
  margin-bottom: 5px;
  color: #555;
}

.details-group p {
  padding: 10px;
  font-size: 16px;
  background-color: #f5f5f5;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.back-button {
  margin-top: 20px;
  background-color: #42b983;
  color: white;
  border: none;
  padding: 12px 20px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.back-button:hover {
  background-color: #2a976e;
}
</style>
