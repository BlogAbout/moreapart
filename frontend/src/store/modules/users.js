export default {
    namespaced: true,
    state: {
        isAuthenticated: localStorage.getItem('AuthState'),
        users: null,
        current: null,
        count: 0,
        error: null,
        loading: false
    },
    getters: {
        getUsers(state) {
            return state.users
        },
        getCurrent(state) {
            return state.current
        },
        getError(state) {
            return state.error
        },
        getLoading(state) {
            return state.loading
        }
    },
    mutations: {
        setUsers(state, users) {
            state.count = users.length
            state.users = users
        },
        setCurrent(state, current) {
            state.current = current
        },
        setError(state, error) {
            state.error = error
        },
        setLoading(state, loading) {
            state.loading = loading
        },
        removeUserMutation(state, id) {
            const deletionIndex = state.users.findIndex(item => item.id === id)
            state.users = [
                ...state.users.slice(0, deletionIndex),
                ...state.users.slice(deletionIndex + 1)
            ]

            state.count = state.users.length
        }
    },
    actions: {
        list({ commit }) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true)
                this._vm.$axios
                    .get('/users')
                    .then(response => {
                        commit('setUsers', response.data)
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
        info({ commit }, id) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true)
                this._vm.$axios.get(`/users/${id}`)
                    .then((response) => {
                        commit('setCurrent', response.data)
                        commit('setError', null)
                        commit('setLoading', false)
                        resolve(response)
                    })
                    .catch((error) => {
                        commit('setCurrent', null)
                        commit('setError', error.message)
                        commit('setLoading', false)
                        reject()
                    })
            })
        },
        add({ commit }, formData) {
            return new Promise((resolve, reject) => {
                const data = {
                    lastName: formData.last_name,
                    firstName: formData.first_name,
                    username: formData.username,
                    email: formData.email,
                    phone: formData.phone,
                    about: formData.about,
                    rights: formData.role,
                    password: formData.password,
                    sendPassword: formData.send_password
                }

                commit('setLoading', true)
                if (formData.id === 0) {
                    this._vm.$axios
                        .post('/users', data)
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
                } else {
                    this._vm.$axios
                        .put('/users/' + formData.id, data)
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
                }
            })
        },
        remove({ commit }, id) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true)
                this._vm.$axios
                    .delete('/users/' + id)
                    .then(response => {
                        commit('removeUserMutation', id)
                        commit('setError', null)
                        commit('setLoading', false)
                        resolve(response)
                    })
                    .catch(error => {
                        commit('setError', error.message)
                        commit('setLoading', false)
                        reject()
                    })
            })
        }
    }
}