export default {
    install(Vue) {
        Vue.prototype.$message = function(html) {
            this.$M.toast({
                html,
                displayLength: 4000,
                inDuration: 400,
                outDuration: 400,
                classes: 'success'
            })
        }

        Vue.prototype.$error = function(html) {
            this.$M.toast({
                html: `[Ошибка] ${html}`,
                displayLength: 4000,
                inDuration: 400,
                outDuration: 400,
                classes: 'error'
            })
        }
    }
}