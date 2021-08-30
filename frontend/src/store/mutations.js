const mutations = {
    setCurrent(state, current) {
        state.current = current
    },
    setError(state, error) {
        state.error = error
    },
    setLoading(state, loading) {
        state.loading = loading
    }
};

export default mutations;