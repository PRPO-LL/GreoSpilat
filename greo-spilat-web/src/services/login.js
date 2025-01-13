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
        console.log('Request payload:', {
            username: user.username,
            password: user.password,
        });
        return apiClient.post('/login', {
            username: user.username,
            password: user.password,
        });
    },
    validate(){
        const token = localStorage.getItem('authToken');
        return apiClient.post('/validate',{},{
            headers : {
                Authorization: token,
            }
        })
            .then(response => {
                const idHeader = response.headers.get('id'); // Extract the "Id" header (case-insensitive)
                return idHeader; // Return the response for further processing
            })
            .catch(error => {
                console.error('Validation error:', error);
                throw error; // Reject the promise with the error
            });

    }
};
