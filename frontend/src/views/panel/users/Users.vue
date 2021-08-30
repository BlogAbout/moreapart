<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 class="tools-header">Персонал</h1>
            <div class="tools-count">{{ count }} сотрудников</div>
            <div class="tools-sorting input-field">
                <select id="sort" name="sort" ref="sortSelect">
                    <option value="id">Id</option>
                    <option value="username" selected>Логину</option>
                    <option value="firstName">Имени</option>
                    <option value="date_created">Дате регистрации</option>
                </select>
                <label>Сортировать по:</label>
            </div>
            <div class="buttons">
                <div class="item">
                    <a class="btn waves-effect waves-light blue lighten-5 light-blue-text text-darken-1"><i class="material-icons left">filter_list</i> Фильтр</a>
                </div>
                <div class="item">
                    <router-link
                            to="/panel/personal/add"
                            custom
                            v-slot="{ href, route, navigate }"
                    >
                        <a :href="href" @click="navigate" class="btn waves-effect waves-light"><i class="material-icons left">add</i>Добавить</a>
                    </router-link>
                </div>
            </div>
        </div>
        <div class="list-items">
            <div class="item list-header flex-box">
                <div class="item-col item-col-id">Id</div>
                <div class="item-col item-col-title">Имя</div>
                <div class="item-col item-col-info">Логин</div>
                <div class="item-col item-col-info">Роль</div>
                <div class="item-col item-col-phone">Телефон</div>
                <div class="item-col item-col-date">Дата регистрации</div>
                <div class="item-col item-col-actions"></div>
            </div>
            <div
                    v-for="item in users"
                    :key="item.id"
                    class="item flex-box"
            >
                <div class="item-col item-col-id">#{{ item.id }}</div>
                <div class="item-col item-col-title">
                    <a
                            href="#modal-user"
                            @click.prevent="getCurrent(item.id)"
                            class="modal-trigger"
                    >{{ item.fullName }}</a>
                </div>
                <div class="item-col item-col-info">{{ item.username }}</div>
                <div class="item-col item-col-info">{{ (item.rights ? item.rights.title : '') }}</div>
                <div class="item-col item-col-phone">{{ item.phone }}</div>
                <div class="item-col item-col-date">{{ item.registration }}</div>
                <div class="item-col item-col-actions">
                    <a
                            class="btn-more dropdown-trigger"
                            v-bind:data-target="'menu-actions-' + item.id"
                    ><i class="material-icons">more_horiz</i></a>
                    <ul
                            class="dropdown-content"
                            v-bind:id="'menu-actions-' + item.id"
                    >
                        <li>
                            <a
                                    href="#modal-user"
                                    @click.prevent="getCurrent(item.id)"
                                    class="modal-trigger"
                            ><i class="material-icons">format_align_left</i>Подробнее</a>
                        </li>
                        <li>
                            <router-link
                                    :to="'/panel/personal/' + item.id + '-edit'"
                                    custom
                                    v-slot="{ href, route, navigate }"
                            >
                                <a :href="href" @click="navigate"><i class="material-icons left">edit</i>Редактировать</a>
                            </router-link>
                        </li>
                        <li>
                            <a @click.prevent="remove(item.id)"><i class="material-icons">delete</i>Удалить</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <user-modal :item="current" />
    </section>
</template>

<script>
    import { mapState } from 'vuex'
    import UserModal from "./UserModal";

    export default {
        name: 'PanelUsers',
        components: {
            UserModal
        },
        data: () => ({
            current: null,
            dropdown: null,
            sortedSelect: null,
            modals: null
        }),
        computed: {
            ...mapState({
                users: state => state.UsersModule.users,
                count: state => state.UsersModule.count
            })
        },
        mounted() {
            this.$store.dispatch("UsersModule/list")
            this.initDropdown()
            this.initSortedSelect()
            this.initModal()
        },
        updated() {
            //if (this.dropdown.length === 0)
                this.initDropdown()

            if (this.sortedSelect.length === 0)
                this.initSortedSelect()
        },
        beforeDestroy() {
            if (this.dropdown && this.dropdown.destroy)
                this.dropdown.destroy()

            if (this.sortedSelect && this.sortedSelect.destroy)
                this.sortedSelect.destroy()
        },
        methods: {
            getCurrent: function(id) {
                this.current = this.$store.state.UsersModule.users.find(user => user.id === id)
            },
            initDropdown: function() {
                this.dropdown = this.$M.Dropdown.init(
                    document.querySelectorAll('.dropdown-trigger'),
                    {
                        alignment: 'left',
                        constrainWidth: false,
                        coverTrigger: false,
                        inDuration: 400,
                        outDuration: 400
                    }
                )
            },
            initSortedSelect: function() {
                this.sortedSelect = this.$M.FormSelect.init(
                    this.$refs.sortSelect,
                    {
                        isMultiple: false,
                    }
                )
            },
            initModal: function() {
                this.modals = this.$M.Modal.init(
                    document.querySelectorAll('.modal'),
                    {
                        opacity: 0.5
                    }
                )
            },
            remove: function(id) {
                this.$store.dispatch('UsersModule/remove', id)
            }
        }
    }
</script>