<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 class="tools-header">Отчеты по чек-листам</h1>
            <div class="tools-count">{{ count }} отчетов</div>
            <div class="tools-sorting input-field">
                <select id="sort" name="sort" ref="sortSelect" v-model="sort" @change="updateSort($event.target.value)">
                    <option value="id">Id</option>
                    <option value="title">Наименованию</option>
                    <option value="dateCreated">Дате создания</option>
                </select>
                <label>Сортировать по:</label>
            </div>
            <div class="buttons">
                <div class="item">
                    <a class="btn waves-effect waves-light blue lighten-5 light-blue-text text-darken-1"><i class="material-icons left">filter_list</i> Фильтр</a>
                </div>
            </div>
        </div>
        <div class="list-items">
            <div class="item list-header flex-box">
                <div class="item-col item-col-id">Id</div>
                <div class="item-col item-col-title">Название</div>
                <div class="item-col item-col-info">Сотрудник</div>
                <div class="item-col item-col-info">Тип</div>
                <div class="item-col item-col-info">Состояние</div>
                <div class="item-col item-col-date">Дата создания</div>
                <div class="item-col item-col-actions"></div>
            </div>
            <div
                    v-for="item in reports"
                    :key="item.id"
                    class="item flex-box"
            >
                <div class="item-col item-col-id">#{{ item.id }}</div>
                <div class="item-col item-col-title">
                    <router-link
                            :to="'/panel/reports/' + item.id + '-check'"
                            custom
                            v-slot="{ href, route, navigate }"
                    >
                        <a :href="href" @click="navigate">{{ item.checksInfo.title }}</a>
                    </router-link>
                </div>
                <div class="item-col item-col-info">{{ item.author.fullName}}</div>
                <div class="item-col item-col-info">{{ item.type }}</div>
                <div class="item-col item-col-info">{{ item.status }}</div>
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
                            <router-link
                                    :to="'/panel/reports/' + item.id + '-check'"
                                    custom
                                    v-slot="{ href, route, navigate }"
                            >
                                <a :href="href" @click="navigate"><i class="material-icons left">format_align_left</i>Подробнее</a>
                            </router-link>
                        </li>
                        <li>
                            <a @click.prevent="remove(item.id)"><i class="material-icons">delete</i>Удалить</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    import { mapState, mapGetters, mapActions } from 'vuex'

    export default {
        name: 'PanelChecksReports',
        data: () => ({
            current: null,
            dropdown: null,
            sortedSelect: null,
        }),
        computed: {
            ...mapState({
                sort: state => state.ChecksReportsModule.sort,
                count: state => state.ChecksReportsModule.count
            }),
            ...mapGetters({
                loading: 'ChecksReportsModule/getLoading',
                reports: 'ChecksReportsModule/sortedReports'
            })
        },
        mounted() {
            this.$store.dispatch("ChecksReportsModule/list")
            this.initDropdown()
            this.initSortedSelect()
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
            ...mapActions('ChecksReportsModule', [
                'updateSort'
            ]),
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
            remove: function(id) {
                this.$store.dispatch('ChecksReportsModule/remove', id)
            }
        }
    }
</script>