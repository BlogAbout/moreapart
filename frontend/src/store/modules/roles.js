export default {
    namespaced: true,
    state: {
        isAuthenticated: localStorage.getItem('AuthState'),
        rights: null,
        current: null,
        count: 0,
        error: null,
        loading: false,
        grantedList: [
            {
                id: 'manage_personal',
                label: 'Управление персоналом',
                desc: 'Разрешает создавать, редактировать и удалять сотрудников, а также назначать и изменять назначенные роли.'
            },
            {
                id: 'manage_role',
                label: 'Управление ролями',
                desc: 'Разрешает создавать, редактировать и удалять роли.'
            },
            {
                id: 'manage_apartment',
                label: 'Управление апартаментами',
                desc: 'Разрешает создавать, редактировать и удалять апартаменты.'
            },
            {
                id: 'manage_equipment',
                label: 'Управление инвентарем',
                desc: 'Разрешает создавать, редактировать и удалять инвентарь, а также категории и виды инвентаря.'
            },
            {
                id: 'move_equipment',
                label: 'Перемещение инвентаря',
                desc: 'Разрешает производить перемещение инвентаря между апартаментами.'
            },
            {
                id: 'create_check_list',
                label: 'Создание чек-листов',
                desc: 'Разрешает создавать, редактировать и удалять чек-листы.'
            },
            {
                id: 'create_comment',
                label: 'Создание комментариев',
                desc: 'Разрешает добавлять комментарии к задачам.'
            },
            {
                id: 'manage_booking',
                label: 'Управление бронированием',
                desc: 'Разрешает управлять системой бронирования, регистрировать, удалять и переносить брони клиентов.'
            },
            {
                id: 'allow_messages',
                label: 'Личные сообщения',
                desc: 'Разрешает сотруднику отправлять и получать личные сообщений и вести диалоги с другими сотрудниками.'
            },
            {
                id: 'tracking_date',
                label: 'Отслеживание даты',
                desc: 'Разрешает отслеживать дату постановки задачи.'
            },
            {
                id: 'see_all_task',
                label: 'Видит все задачи',
                desc: 'Разрешает отслеживать все поставленные задачи от любого сотрудника сотруднику.'
            },
            {
                id: 'see_reports',
                label: 'Просмотр отчетов',
                desc: 'Разрешает просматривать существующие отчеты и чек-листы.'
            },
            {
                id: 'see_logs',
                label: 'Просмотр логов',
                desc: 'Разрешает просматривать логи действий сотрудников внутри системы.'
            }
        ]
    },
    getters: {
        getRights(state) {
            return state.rights
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
        setRights(state, rights) {
            state.count = rights.length
            state.rights = rights
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
        removeRightMutation(state, id) {
            const deletionIndex = state.rights.findIndex(item => item.id === id)
            state.rights = [
                ...state.rights.slice(0, deletionIndex),
                ...state.rights.slice(deletionIndex + 1)
            ]

            for (let item of state.rights)
                item.siblings = item.siblings.filter(val => val !== id)

            state.count = state.rights.length
        }
    },
    actions: {
        list({ commit }) {
            return new Promise((resolve, reject) => {
                commit('setLoading', true)
                this._vm.$axios
                    .get('/roles')
                    .then(response => {
                        commit('setRights', response.data)
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
                this._vm.$axios.get(`/roles/${id}`)
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
                    granted: JSON.stringify(formData.granted),
                    siblings: JSON.stringify(formData.siblings)
                }
                commit('setLoading', true)
                if (formData.id === 0) {
                    this._vm.$axios
                        .post('/roles', data)
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
                        .put('/roles/' + formData.id, data)
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
                    .delete('/roles/' + id)
                    .then(response => {
                        commit('removeRightMutation', id)
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