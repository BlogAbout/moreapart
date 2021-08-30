export default {
    namespaced: true,
    state: {
        inventories: null,
        count: 0,
        sort: 'id'
    },
    getters: {
        sortedInventories(state) {
            //return (state.inventories || []).sort((a, b) => (a[state.sort] - b[state.sort]))
            return state.inventories
        }
    },
    mutations: {
        setInventories(state, inventories) {
            state.inventories = inventories
        },
        setSort(state, sort) {
            state.sort = sort
        },
        removeInventoryMutation(state, id) {
            const deletionIndex = state.inventories.findIndex(item => item.id === id)
            state.inventories = [
                ...state.inventories.slice(0, deletionIndex),
                ...state.inventories.slice(deletionIndex + 1)
            ]

            state.count = state.inventories.length
        },
        addInventoriesPageMutation(state, inventories) {
            const targetInventories = state.inventories
                .concat(inventories)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {})

            state.inventories = Object.values(targetInventories)
        },
        updatePageMutation(state, page) {
            state.page = page
        },
        updatePagesMutation(state, pages) {
            state.pages = pages
        },
        updateTotalMutation(state, count) {
            state.count = count
        }
    },
    actions: {
        list({ commit, state }, page = 0) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true, { root: true })
                this._vm.$axios
                    .get('/inventory', {
                        params: {
                            page: page,
                            sort: state.sort
                        }
                    })
                    .then(response => {
                        commit('setInventories', response.data.list)
                        commit('updatePageMutation', Math.min(response.data.page, response.data.pages - 1))
                        commit('updatePagesMutation', response.data.pages)
                        commit('updateTotalMutation', response.data.total)
                        commit('setError', null, { root: true })
                        commit('setLoading', false, { root: true })
                        resolve(response)
                    })
                    .catch(error => {
                        commit('setError', error.message, { root: true })
                        commit('setLoading', false, { root: true })
                        reject(error)
                    })
            })
        },
        info({ commit }, id) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true, { root: true })
                this._vm.$axios.get(`/inventory/${id}`)
                    .then((response) => {
                        commit('setCurrent', response.data, { root: true })
                        commit('setError', null, { root: true })
                        commit('setLoading', false, { root: true })
                        resolve(response)
                    })
                    .catch((error) => {
                        commit('setCurrent', null, { root: true })
                        commit('setError', error.message, { root: true })
                        commit('setLoading', false, { root: true })
                        reject()
                    })
            })
        },
        add({ commit }, formData) {
            return new Promise((resolve, reject) => {
                const formDataInfo = new FormData()
                const data = {
                    title: formData.title,
                    description: formData.description,
                    category: formData.category
                }

                if (formData.file != null) {
                    formDataInfo.append('model', JSON.stringify(data))
                    formDataInfo.append('file', formData.file)
                }

                commit('setLoading', true, { root: true })
                if (formData.id === 0) {
                    this._vm.$axios
                        .post('/inventory', (formData.file != null ? formDataInfo : data))
                        .then(response => {
                            commit('setError', null, { root: true })
                            commit('setLoading', false, { root: true })
                            resolve(response)
                        })
                        .catch(error => {
                            commit('setError', error.message, { root: true })
                            commit('setLoading', false, { root: true })
                            reject(error)
                        })
                } else {
                    /*this._vm.$axios
                        .put('/inventory/' + formData.id, data)
                        .then(response => {
                            commit('setError', null, { root: true })
                            commit('setLoading', false, { root: true })
                            resolve(response)
                        })
                        .catch(error => {
                            commit('setError', error.message, { root: true })
                            commit('setLoading', false, { root: true })
                            reject(error)
                        })*/
                }
            })
        },
        remove({ commit }, id) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true, { root: true })
                this._vm.$axios
                    .delete('/inventory/' + id)
                    .then(response => {
                        commit('removeInventoryMutation', id)
                        commit('setError', null, { root: true })
                        commit('setLoading', false, { root: true })
                        resolve(response)
                    })
                    .catch(error => {
                        commit('setError', error.message, { root: true })
                        commit('setLoading', false, { root: true })
                        reject()
                    })
            })
        },
        async loadPageAction({ commit, state }) {
            commit('setLoading', true, { root: true })
            const response = await this._vm.$axios
                .get('/inventory', {
                    params: {
                        page: state.page + 1,
                        sort: state.sort
                    }
                })

            commit('addInventoriesPageMutation', response.data.list)
            commit('updatePageMutation', Math.min(response.data.page, response.data.pages - 1))
            commit('updatePagesMutation', response.data.pages)
            commit('updateTotalMutation', response.data.total)
            commit('setLoading', false, { root: true })
        },
        async updateSort({ dispatch, commit }, sort) {
            commit('setSort', sort)
            dispatch('list')
        }
    }
}