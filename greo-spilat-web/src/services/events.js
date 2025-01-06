import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8083/v1/events',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default {
    getEvents() {
        return apiClient.get('/');
    },
    addEvent(event) {
        return apiClient.post('/add', {
            title: event.title,
            sport: event.sport,
            description: event.description,
            location:   event.location,
            max_participants: event.max_participants,
        });
    },
    deleteEvent(event){
        return apiClient.delete('/delete/' + event.id);
    },
    getEvent(event){
        return apiClient.get('/' + event.id);
    }
};
