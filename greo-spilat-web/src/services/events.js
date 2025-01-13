import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8083/v1/events',
    headers: {
        'Content-Type': 'application/json',
    },
});

const apiClientComment = axios.create({
    baseURL: 'http://localhost:8402/v1/comments',
    headers: {
        'Content-Type': 'application/json',
    },
});

const apiClientJoin = axios.create({
    baseURL: 'http://localhost:8403/v1/join',
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
    getJoinedEvents(eventId){
        return apiClientJoin.get('/user/' + eventId);
    },
    getMyEvents(userId){
        const url = userId ? `?filter=creator_id:EQ:${userId}` : '/';
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
            maxParticipants: event.maxParticipants,
            date: event.date,
        });
        return apiClient.post('/add', {
            title: event.title,
            sport: event.sport,
            description: event.description,
            location:   event.location,
            maxParticipants: event.maxParticipants,
            date: event.date,
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
    getComments(event){
        return apiClientComment.get('/event/'+ event);
    },
    addComment(comment){
        return apiClientComment.post('?userId='+comment.id+'&eventId='+comment.eventId, {
            content: comment.content,
        })
    },
    joinEvent(eventid, user){
        return apiClientJoin.post('?userId='+user.id+'&eventId='+eventid)
    },
    leaveEvent(eventid, user) {
        return apiClientJoin.delete('/'+user.id+'/event/'+eventid)
    },
    amIJoined(userId){
        return apiClientJoin.get('/user/' + userId.id);
    },
    getEvent(event){
        return apiClient.get('/' + event);
    }
};
