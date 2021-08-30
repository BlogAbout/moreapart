export default {
    namespaced: true,
    state: {
        isAuthenticated: localStorage.getItem('AuthState'),
        checks: null,
        current: null,
        count: 0,
        error: null,
        loading: false,
        sort: 'id'
    },
    getters: {
        getChecks(state) {
            return state.checks
        },
        sortedChecks(state) {
            //return (state.checks || []).sort((a, b) => (a[state.sort] - b[state.sort]))
            return state.checks
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
        setChecks(state, checks) {
            state.checks = checks
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
        setSort(state, sort) {
            state.sort = sort
        },
        removeCheckMutation(state, id) {
            const deletionIndex = state.checks.findIndex(item => item.id === id)
            state.checks = [
                ...state.checks.slice(0, deletionIndex),
                ...state.checks.slice(deletionIndex + 1)
            ]

            state.count = state.checks.length
        },
        addChecksPageMutation(state, checks) {
            const targetChecks = state.checks
                .concat(checks)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {})

            state.checks = Object.values(targetChecks)
        },
        updateTotalPagesMutation(state, totalPages) {
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            state.currentPage = currentPage
        },
        updateTotalCount(state, count) {
            state.count = count
        }
    },
    actions: {
        list({ commit, state }, page = 0) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true)
                this._vm.$axios
                    .get('/checks', {
                        params: {
                            page: page,
                            sort: state.sort
                        }
                    })
                    .then(response => {
                        commit('setChecks', response.data.checks)
                        commit('updateTotalPagesMutation', response.data.totalPages)
                        commit('updateCurrentPageMutation', Math.min(response.data.currentPage, response.data.totalPages - 1))
                        commit('updateTotalCount', response.data.totalItems)
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
                this._vm.$axios.get(`/checks/${id}`)
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
                    description: formData.description,
                    rights: formData.role,
                    checksQuestions: formData.questions
                }
                commit('setLoading', true)

                if (formData.id === 0) {
                    this._vm.$axios
                        .post('/checks', data)
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
                        .put('/checks/' + formData.id, data)
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
                    .delete('/checks/' + id)
                    .then(response => {
                        commit('removeCheckMutation', id)
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
        },
        async loadPageAction({ commit, state }) {
            commit('setLoading', true)
            const response = await this._vm.$axios
                .get('/checks', {
                    params: {
                        page: state.currentPage + 1,
                        sort: state.sort
                    }
                })

            commit('addChecksPageMutation', response.data.checks)
            commit('updateTotalPagesMutation', response.data.totalPages)
            commit('updateCurrentPageMutation', Math.min(response.data.currentPage, response.data.totalPages - 1))
            commit('updateTotalCount', response.data.totalItems)
            commit('setLoading', false)
        },
        async updateSort({ commit, state }, sort) {
            commit('setLoading', true)
            commit('setSort', sort)
            const response = await this._vm.$axios
                .get('/checks', {
                    params: {
                        page: 0,
                        sort: state.sort
                    }
                })

            commit('setChecks', response.data.checks)
            commit('updateTotalPagesMutation', response.data.totalPages)
            commit('updateCurrentPageMutation', Math.min(response.data.currentPage, response.data.totalPages - 1))
            commit('updateTotalCount', response.data.totalItems)
            commit('setLoading', false)
        }
    }
}