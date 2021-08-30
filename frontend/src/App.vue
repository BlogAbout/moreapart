<template>
    <div id="app">
        <component :is="layout">
            <transition name="fade">
                <router-view />
            </transition>
        </component>
    </div>
</template>

<script>
    import MainLayout from './layouts/MainLayout.vue'
    import AdminLayout from "./layouts/AdminLayout"
    //import EventBus from "./event-bus"

    export default {
        name: 'App',
        components: {
            MainLayout,
            AdminLayout
        },
        computed: {
            layout() {
                return (this.$route.meta.layout || 'main') + '-layout'
            }
        },
        created() {
            /*EventBus.$on('authenticated', () => {
                this.isAuthenticated = localStorage.getItem('auth');
            });*/
            /*EventBus.$on('authenticated', () => {
                //this.isAuthenticated = localStorage.getItem('auth');

            });*/
            /*this.$axios.interceptors.response.use(undefined, function (err) {
                return new Promise((resolve, reject) => {
                    console.log('Vue Created block')
                    console.log(resolve)
                    console.log(reject)
                    if (err.status === 401 && err.config && !err.config.__isRetryRequest) {
                        this.$store.dispatch("logout")
                    }
                    throw err;
                })
                /*return new Promise(function(resolve, reject) {
                    console.log('Vue Created block')
                    console.log(resolve)
                    console.log(reject)
                    if (err.status === 401 && err.config && !err.config.__isRetryRequest) {
                        this.$store.dispatch("logout")
                    }
                    throw err;
                });*/

                /*if (response && response.status === 302) {
                if (!isAlreadyFetchingAccessToken) {
                isAlreadyFetchingAccessToken = true
                store.dispatch(«auth/fetchAccessToken»)
                .then((access_token) => {
                isAlreadyFetchingAccessToken = false
                onAccessTokenFetched(access_token)
                })
                }

                const retryOriginalRequest = new Promise((resolve) => {
                addSubscriber(access_token => {
                originalRequest.headers.Authorization = ‘Bearer ‘ + access_token.headers.authorization
                localStorage.setItem(«accessToken»,access_token.headers.authorization)
                resolve(axios(originalRequest))
                })
                })
                return retryOriginalRequest
                }*/
            //});
        }
    }
</script>

<style>

</style>