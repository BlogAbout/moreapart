import router from '@/router'

const AUTH_KEY = 'AuthState'
const AUTH_TYPE = 'AuthType'
const TOKEN_KEY = 'AuthToken'
const USERNAME_KEY = 'AuthUsername'
const AUTHORITIES_KEY = 'AuthAuthorities'

export default {
    namespaced: true,
    state: {
        isAuthenticated: localStorage.getItem('AuthState'),
        error: null,
        loading: false,
    },
    getters: {
        getAuth() {
            return localStorage.getItem(AUTH_KEY)
        },
        getTypeAuth() {
            return localStorage.getItem(AUTH_TYPE)
        },
        getToken() {
            return localStorage.getItem(TOKEN_KEY)
        },
        getUsername() {
            return localStorage.getItem(USERNAME_KEY)
        },
        getAuthorities() {
            let roles = [];

            if (this.getToken()) {
                console.log(AUTHORITIES_KEY)
                console.log(localStorage.getItem(AUTHORITIES_KEY))
                JSON.parse(localStorage.getItem(AUTHORITIES_KEY)).forEach(authority => {
                    roles.push(authority);
                })
            }

            return roles
        },
        getError(state) {
            return state.error
        },
        getLoading(state) {
            return state.loading
        }
    },
    mutations: {
        setAuth(state, auth) {
            localStorage.setItem(AUTH_KEY, auth)
        },
        setTypeAuth(state, type) {
            localStorage.removeItem(AUTH_TYPE)
            localStorage.setItem(AUTH_TYPE, type)
        },
        setToken(state, token) {
            localStorage.removeItem(TOKEN_KEY)
            localStorage.setItem(TOKEN_KEY, token)
        },
        setUsername(state, username) {
            localStorage.removeItem(USERNAME_KEY)
            localStorage.setItem(USERNAME_KEY, username)
        },
        setAuthorities(state, authorities) {
            localStorage.removeItem(AUTHORITIES_KEY);
            localStorage.setItem(AUTHORITIES_KEY, JSON.stringify(authorities));
        },
        setError(state, error) {
            state.error = error
        },
        setLoading(state, loading) {
            state.loading = loading
        },
        signOut() {
            localStorage.clear()
        }
    },
    actions: {
        userSignIn({ commit }, credentials) {
            return new Promise((resolve, reject) => {
                const data = {
                    username: credentials.username,
                    password: credentials.password
                }
                commit('setLoading', true)
                this._vm.$axios
                    .post('/auth/signin', data)
                    .then(response => {
                        commit('setAuth', true)
                        commit('setTypeAuth', response.data.type)
                        commit('setToken', response.data.token)
                        commit('setUsername', response.data.username)
                        commit('setAuthorities', response.data.roles)
                        commit('setError', null)
                        commit('setLoading', false)

                        this._vm.$axios.defaults.headers.common['Authorization'] = this.getters["AuthModule/getTypeAuth"] + ' ' + this.getters["AuthModule/getToken"]
                        //EventBus.$emit('authenticated', 'User authenticated')
                        resolve(response);
                    })
                    .catch(error => {
                        commit('setError', error.message)
                        commit('setLoading', false)
                        reject(error);
                    })
            })
        },
        userSignUp({ commit }, formData) {
            return new Promise((resolve, reject) => {
                const data = {
                    name: formData.name,
                    username: formData.username,
                    email: formData.email,
                    phone: formData.phone,
                    password: formData.password
                }
                commit('setLoading', true)
                this._vm.$axios
                    .post('/auth/signup', data)
                    .then(response => {
                        /*commit('setAuth', true)
                        commit('setTypeAuth', response.data.type)
                        commit('setToken', response.data.token)
                        commit('setUsername', response.data.username)
                        commit('setAuthorities', response.data.roles)*/
                        commit('setError', null)
                        commit('setLoading', false)
                        resolve(response)
                    })
                    .catch(error => {
                        commit('setError', error.message)
                        commit('setLoading', false)
                        reject(error)
                    })
            })
        },
        userForgotPassword({ commit }, formData) {
            return new Promise((resolve, reject) => {
                const data = {
                    email: formData.email
                }
                commit('setLoading', true)
                this._vm.$axios
                    .post('/auth/forgotpassword', data)
                    .then(response => {
                        commit('setError', null)
                        commit('setLoading', false)
                        resolve(response)
                    })
                    .catch(error => {
                        commit('setError', error.message)
                        commit('setLoading', false)
                        reject(error)
                    })
            })
        },
        userResetPassword({ commit }, formData) {
            return new Promise((resolve, reject) => {
                const data = {
                    reset_key: formData.reset_key,
                    password: formData.password,
                    password_repeat: formData.password_repeat
                }
                commit('setLoading', true)
                this._vm.$axios
                    .post('/auth/resetpassword', data)
                    .then(response => {
                        commit('setError', null)
                        commit('setLoading', false)
                        resolve(response)
                    })
                    .catch(error => {
                        commit('setError', error.message)
                        commit('setLoading', false)
                        reject(error)
                    })
            })
        },
        userSignOut({ commit }) {
            commit('signOut')
            delete this._vm.$axios.defaults.headers.common['Authorization']
            //EventBus.$emit('authenticated', 'User not authenticated')
            router.push('/login?message=logout')
        }
    }
}