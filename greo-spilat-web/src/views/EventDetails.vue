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
        <label>Date and Time:</label>
      <p> {{ formatDateTime(event.date) }}</p>
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
      <button v-if="!isJoined && !creator" @click="joinEvent()" class="back-button">Join</button>
      <button v-if="isJoined && !creator" @click="leaveEvent()" class="leave-button">Leave</button>

      <div class="comments-section">
        <h2>Comments</h2>
        <div v-if="comments.length === 0" class="comment-placeholder">
          <p>No comments yet. Be the first to leave one!</p>
        </div>
        <div v-else>
          <div v-for="comment in comments" :key="comment.id" class="comment">
            <p><strong>{{ comment.user.username }}</strong>: {{ comment.content }}</p>
          </div>
        </div>

        <div class="add-comment">
          <input
              type="text"
              v-model="newCommentText"
              placeholder="Write a comment..."
              class="comment-input"
          />
          <button @click="addComment" class="add-comment-button">Submit</button>
        </div>
      </div>

    </main>
<!--    <Footer />-->
  </div>
</template>


<script>
import Header from '../components/Header.vue';
// import Footer from '../components/Footer.vue';
import apiService from '../services/events.js';
import validation from '../services/login.js';

export default {
  name: 'EventDetails',
  components: {
    Header,
    // Footer,
  },
  data() {
    return {
      event: {},
      comments: [],
      newCommentText: '',
      isJoined: false,
      creator: false,
    };
  },
  async created() {
    const eventId = this.$route.params.id; // Get the event ID from the route
    await this.getEvent(eventId);
    this.loadComments(eventId);
    // this.isJoined = this.amIJoined();
    this.isJoined = await this.amIJoined();
    this.creator = await this.semCreator();
  },
  methods: {
    getEvent(eventId) {
      console.log("Ta event" + eventId);
      apiService.getEvent( eventId ).then(response => {
        this.event = response.data; // Load the event details
      });
    },
    loadComments(eventId) {
      apiService.getComments(eventId).then((response) => {
        this.comments = response.data;
      });
    },
    async addComment() {
      if (this.newCommentText.trim() === '') {
        alert('Comment text cannot be empty.');
        return;
      }
      const newComment = {
        id: await validation.validate(),
        content: this.newCommentText.trim(),
        eventId: this.$route.params.id,
      };
      apiService.addComment(newComment);
      this.newCommentText = ''; // Clear the input field
      this.$router.go();
    },
    async joinEvent() {
      const user = {
        id: await validation.validate(),
      };
      apiService.joinEvent(this.$route.params.id, user);
      this.$router.go();
    },
    async leaveEvent() {
      const user = {
        id: await validation.validate(),
      };
      apiService.leaveEvent(this.$route.params.id, user);
      this.$router.go();
    },
    async amIJoined() {
      const user = {
        id: await validation.validate(),
      };
      const response = await apiService.amIJoined(user);
      const smJoinan = response.data.some(join => {
        return join.event.iid === parseInt(this.$route.params.id, 10);
      });
      // console.log("sm joinan; " + smJoinan);
      // console.log('this.event.creatorId:', this.event.creatorId, typeof(this.event.creatorId));
      // console.log('user id', parseInt(user.id, 10), typeof(parseInt(user.id, 10)));
      // console.log('creatorId comparison:', this.event.creatorId === parseInt(user.id, 10));
      // console.log('smJoinan || comparison:', smJoinan);
      return smJoinan;
    },
    async semCreator(){
      const user = {
        id: await validation.validate(),
      };
      return this.event.creatorId === parseInt(user.id, 10);
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
  margin-right: 20px;
}
.back-button:last-child {
  margin-right: 0;
}

.back-button:hover {
  background-color: #2a976e;
}

.leave-button {
  margin-top: 20px;
  background-color: #ea0a0a;
  color: white;
  border: none;
  padding: 12px 20px;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 20px;
}
.leave-button:last-child {
  margin-right: 0;
}

.leave-button:hover {
  background-color: #cd1c1c;
}

 .comments-section {
   margin-top: 30px;
   text-align: left;
 }

.comments-section h2 {
  font-size: 22px;
  margin-bottom: 15px;
  color: #333;
}

.comment-placeholder {
  padding: 15px;
  font-size: 16px;
  background-color: #f5f5f5;
  border-radius: 8px;
  border: 1px solid #ddd;
  color: #777;
  text-align: center;
}

.comment {
  padding: 10px;
  font-size: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #ddd;
  margin-bottom: 10px;
}

.add-comment {
  display: flex;
  margin-top: 15px;
}

.comment-input {
  flex: 1;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.add-comment-button {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 8px;
  cursor: pointer;
  margin-left: 10px;
  transition: background-color 0.3s ease;
}

.add-comment-button:hover {
  background-color: #2a976e;
}


</style>
