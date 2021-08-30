const state = {
    isAuthenticated: localStorage.getItem('AuthState'),
    current: null,
    error: null,
    loading: false
};

export default state;