const getters = {
    appTitle(state) {
        return state.appTitle
    },
    isAuthenticated(state) {
        return state.isAuthenticated === 'true'
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
};

export default getters;