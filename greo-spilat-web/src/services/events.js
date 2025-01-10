import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8083/v1/events',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default {
    getEvents(filter = '') {
        // const url = filter ? `?(filter=title:LIKE:'%${filter}%' OR filter=sport:LIKE:'%${filter}%' OR filter=location:LIKE:'%${filter}%')` : '';
        const url = filter ? `?filter=title:LIKE:'%${filter}%'` : '/';
        return apiClient.get(url);
    },
    // http://localhost:{{portEvent}}/v1/events?filter=title:LIKE:'%lom%'
    addEvent(event) {
        const token = localStorage.getItem('authToken');
        console.log('Request payload:', {
            title: event.title,
            sport: event.sport,
            description: event.description,
            location: event.location,
            max_participants: event.max_participants,
        });
        return apiClient.post('/add', {
            title: event.title,
            sport: event.sport,
            description: event.description,
            location:   event.location,
            max_participants: event.max_participants,
        }, {
            headers: {
                Authorization: token, // Append the Authorization header
            },
        });
    },
    deleteEvent(event){
        const token = localStorage.getItem('authToken');
        return apiClient.delete('/delete/' + event.iid, {
            headers : {
                Authorization: token,
            }
        });
    },
    joinEvent(){
        //logika za join event
    },
    getEvent(event){
        return apiClient.get('/' + event.iid);
    }
};
