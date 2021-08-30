<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 class="tools-header">Чек-листы</h1>
            <div class="tools-count">{{ count }} записей</div>
            <div class="tools-sorting input-field">
                <select id="sort" name="sort" ref="sortSelect" @change="updateSort($event.target.value)">
                    <option value="id" :selected="(sort === 'id')">Id</option>
                    <option value="title" :selected="(sort === 'title')">Наименованию</option>
                    <option value="dateCreated" :selected="(sort === 'dateCreated')">Дате создания</option>
                </select>
                <label>Сортировать по:</label>
            </div>
            <div class="buttons">
                <div class="item">
                    <a class="btn waves-effect waves-light blue lighten-5 light-blue-text text-darken-1"><i class="material-icons left">filter_list</i> Фильтр</a>
                </div>
                <div class="item">
                    <router-link
                            to="/panel/checks/add"
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
                <div class="item-col item-col-title">Название</div>
                <div class="item-col item-col-info">Роль</div>
                <div class="item-col item-col-number">Вопросы</div>
                <div class="item-col item-col-date">Дата создания</div>
                <div class="item-col item-col-actions"></div>
            </div>
            <div
                    v-for="item in checks"
                    :key="item.id"
                    class="item flex-box"
            >
                <div class="item-col item-col-id">#{{ item.id }}</div>
                <div class="item-col item-col-title">
                    <a
                            href="#modal-checks"
                            @click.prevent="getCurrent(item.id)"
                            class="modal-trigger"
                    >{{ item.title }}</a>
                </div>
                <div class="item-col item-col-info">{{ item.rights.title }}</div>
                <div class="item-col item-col-number">{{ item.checksQuestions.length }}</div>
                <div class="item-col item-col-date">{{ item.dateCreated }}</div>
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
                                    href="#modal-checks"
                                    @click.prevent="getCurrent(item.id)"
                                    class="modal-trigger"
                            ><i class="material-icons">format_align_left</i>Подробнее</a>
                        </li>
                        <li>
                            <router-link
                                    :to="'/panel/checks/' + item.id + '-fill'"
                                    custom
                                    v-slot="{ href, route, navigate }"
                            >
                                <a :href="href" @click="navigate"><i class="material-icons left">bookmark_added</i>Заполнить</a>
                            </router-link>
                        </li>
                        <li>
                            <router-link
                                    :to="'/panel/checks/' + item.id + '-edit'"
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
        <check-modal :item="current" />
    </section>
</template>

<script>
    import { mapState, mapGetters, mapActions } from 'vuex'
    import CheckModal from "./CheckModal";

    export default {
        name: 'PanelChecks',
        components: {
            CheckModal
        },
        data: () => ({
            current: null,
            dropdown: null,
            sortedSelect: null,
            modals: null
        }),
        computed: {
            ...mapState({
                sort: state => state.ChecksModule.sort,
                count: state => state.ChecksModule.count
            }),
            ...mapGetters({
                loading: 'ChecksModule/getLoading',
                checks: 'ChecksModule/sortedChecks'
            })
        },
        mounted() {
            this.$store.dispatch("ChecksModule/list")
            this.initDropdown()
            this.initSortedSelect()
            this.initModal()
            /*window.onscroll = () => {
                const el = document.querySelector('.main-container')
                const isBottomOfScreen = document.documentElement.scrollTop + window.innerHeight >= el.offsetHeight

                if (isBottomOfScreen)
                    this.loadMore()
            }*/
        },
        updated() {
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
            ...mapActions('ChecksModule', [
                'updateSort'
            ]),
            getCurrent: function(id) {
                this.current = this.$store.state.ChecksModule.checks.find(inventory => inventory.id === id)
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
                this.$store.dispatch('ChecksModule/remove', id)
            },
            /*loadMore: function() {
                this.$store.dispatch('ChecksModule/loadPageAction')
            }*/
        }
    }
</script>