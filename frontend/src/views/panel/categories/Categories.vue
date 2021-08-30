<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 class="tools-header">Категории инвентаря</h1>
            <div class="tools-count">{{ count }} записи</div>
            <div class="tools-sorting input-field">
                <select id="sort" name="sort" ref="sortSelect">
                    <option value="id">Id</option>
                    <option value="title" selected>Наименованию</option>
                    <option value="date_created">Дате</option>
                </select>
                <label>Сортировать по:</label>
            </div>
            <div class="buttons">
                <div class="item">
                    <a class="btn waves-effect waves-light blue lighten-5 light-blue-text text-darken-1"><i class="material-icons left">filter_list</i> Фильтр</a>
                </div>
                <div class="item">
                    <router-link
                            to="/panel/inventory/categories/add"
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
                <div class="item-col item-col-title">Наименование</div>
                <div class="item-col item-col-number">Инвентарь</div>
                <div class="item-col item-col-date">Дата создания</div>
                <div class="item-col item-col-actions"></div>
            </div>
            <div
                    v-for="item in categories"
                    :key="item.id"
                    class="item flex-box"
            >
                <div class="item-col item-col-id">#{{ item.id }}</div>
                <div class="item-col item-col-title">{{ item.title }}</div>
                <div class="item-col item-col-number">{{ item.countInventory }}</div>
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
                                    :to="'/panel/inventory/categories/' + item.id + '-edit'"
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
    </section>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'PanelRoles',
        data: () => ({
            current: null,
            dropdown: null,
            sortedSelect: null
        }),
        computed: {
            ...mapState({
                categories: state => state.CategoriesModule.categories,
                count: state => state.CategoriesModule.count
            })
        },
        mounted() {
            this.$store.dispatch("CategoriesModule/list")
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
                this.$store.dispatch('CategoriesModule/remove', id)
            }
        }
    }
</script>