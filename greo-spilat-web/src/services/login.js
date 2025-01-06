import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8085/v1/auth',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default {
    getUsers() {
        return apiClient.get('/all');
    },
    register(user) {
        return apiClient.post('/register', {
            username: user.username,
            password: user.password,
        });
    },
    login(user){
        return apiClient.post('/login', {
            username: user.username,
            password: user.password,
        });
    },
};
