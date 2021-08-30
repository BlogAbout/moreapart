export default {
    namespaced: true,
    state: {
        isAuthenticated: localStorage.getItem('AuthState'),
        categories: null,
        current: null,
        count: 0,
        error: null,
        loading: false
    },
    getters: {
        getCategories(state) {
            return state.categories
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
        setCategories(state, categories) {
            state.count = categories.length
            state.categories = categories
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
        removeCategoryMutation(state, id) {
            const deletionIndex = state.categories.findIndex(item => item.id === id)
            state.categories = [
                ...state.categories.slice(0, deletionIndex),
                ...state.categories.slice(deletionIndex + 1)
            ]

            state.count = state.categories.length
        }
    },
    actions: {
        list({ commit }) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true)
                this._vm.$axios
                    .get('/categories')
                    .then(response => {
                        commit('setCategories', response.data)
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
                this._vm.$axios.get(`/categories/${id}`)
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
                    title: formData.title,
                    description: formData.description
                }
                commit('setLoading', true)
                if (formData.id === 0) {
                    this._vm.$axios
                        .post('/categories', data)
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
                        .put('/categories/' + formData.id, data)
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
                    .delete('/categories/' + id)
                    .then(response => {
                        commit('removeCategoryMutation', id)
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