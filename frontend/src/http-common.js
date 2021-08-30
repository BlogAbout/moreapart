import axios from 'axios'
//import store from "./store"
//import router from "./router";

const SERVER_URL = 'http://localhost:9000/api';

const AXIOS = axios.create({
    baseURL: SERVER_URL,
    timeout: 5000,
    withCredentials: false
})
/*
AXIOS.interceptors.response.use(undefined, function (err) {
    return new Promise((resolve, reject) => {
        console.log('Vue Created block')
        console.log(resolve)
        console.log(reject)

        const typeAuth = localStorage.getItem('AuthType')
        const token = localStorage.getItem('AuthToken')
        if (typeAuth && token) {
            AXIOS.headers.common["Authorization"] = typeAuth + ' ' + token;
            console.log(AXIOS.headers.common)
        }
        console.log('hren')

        if (err.status === 401 && err.config && !err.config.__isRetryRequest) {
            this.$store.dispatch("logout")
        }
        throw err;
    })
})*/

AXIOS.interceptors.request.use(
    function(config) {
        const typeAuth = localStorage.getItem('AuthType')
        const token = localStorage.getItem('AuthToken')
        if (typeAuth && token) {
            config.headers["Authorization"] = typeAuth + ' ' + token;
        }
        return config;
    }, function(error) {
        console.log(error)
        return Promise.reject(error);
    }
);
/*
HTTP.interceptors.response.use((response) => Promise.resolve(response), (error) => {
    if (error.response.status === 401) {
        console.log('Unauthorized, logging out ...');
        //store.dispatch('userSignOut');
        //router.replace('signIn');
        return Promise.reject(error);
    }

    return Promise.reject(error.response);
});
*//*
const typeAuth = store.getters["AuthModule/getTypeAuth"]
const token = store.getters["AuthModule/getToken"]

if (typeAuth && token) {
    AXIOS.defaults.headers.common['Authorization'] = this.$store.getters["AuthModule/getTypeAuth"] + ' ' + this.$store.getters["AuthModule/getToken"]
}*/

export default AXIOS;