<template>
    <aside class="sidebar-container">
        <div class="user-buttons flex-box">
            <div class="item">
                <a
                        href="#modal-search"
                        class="btn-floating btn-small waves-effect waves-light tooltipped modal-trigger"
                        data-position="left"
                        data-tooltip="Поиск"
                ><i class="material-icons">search</i></a>
            </div>
            <div class="item">
                <a
                        class="btn-floating btn-small waves-effect waves-light tooltipped"
                        data-position="left"
                        data-tooltip="Чаты"
                ><i class="material-icons">question_answer</i></a>
            </div>
            <div class="item">
                <a
                        class="btn-floating btn-small waves-effect waves-light tooltipped"
                        data-position="left"
                        data-tooltip="Сообщения"
                ><i class="material-icons">email</i></a>
            </div>
            <div class="item">
                <a
                        class="btn-floating btn-small waves-effect waves-light tooltipped"
                        data-position="left"
                        data-tooltip="Уведомления"
                ><i class="material-icons">notifications</i></a>
            </div>
            <div class="item">
                <a
                        href="#modal-settings"
                        class="btn-floating btn-small waves-effect waves-light tooltipped modal-trigger"
                        data-position="left"
                        data-tooltip="Настройки"
                ><i class="material-icons">tune</i></a>
            </div>
            <div class="item">
                <a
                        class="btn-floating btn-small waves-effect waves-light"
                        @click.prevent="logout"
                ><i class="material-icons">logout</i></a>
            </div>
        </div>
        <div id="modal-settings" class="modal modal-fixed-footer">
            <div class="modal-content">
                <h4>Настройки</h4>
                <p>Перечень настроек. В разработке.</p>
            </div>
            <div class="modal-footer">
                <a href="#!" class="modal-close waves-effect waves-green btn-flat">Отменить</a>
                <a href="#!" class="modal-close waves-effect waves-green btn-flat">Сохранить</a>
            </div>
        </div>
        <div id="modal-search" class="modal bottom-sheet">
            <div class="modal-content">
                <form class="col s12" name="form-search" action="" method="post" @submit.prevent="submitHandler">
                    <div class="input-field col s12 m6">
                        <input
                                id="search"
                                type="search"
                                name="search"
                                placeholder="Введите поисковую фразу и нажмите Enter"
                        />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <a href="#!" class="modal-close waves-effect waves-green btn-flat">Закрыть</a>
            </div>
        </div>
    </aside>
</template>

<script>
    export default {
        name: 'PanelSidebar',
        methods: {
            logout() {
                this.$store.dispatch('AuthModule/userSignOut')
            }
        },
        mounted() {
            let tooltip = this.$M.Tooltip.init(
                document.querySelectorAll('.tooltipped'),
                {
                    inDuration: 300,
                    outDuration: 150
                }
            )

            let modals = this.$M.Modal.init(
                document.querySelectorAll('.modal'),
                {
                    opacity: 0.5
                }
            )

            this.$once('hook:beforeDestroy', function() {
                if (tooltip && tooltip.destroy)
                    tooltip.destroy()

                if (modals && modals.destroy)
                    modals.destroy()
            })
        }
    }
</script>