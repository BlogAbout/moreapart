<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 class="tools-header">Инвентарь</h1>
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
                            to="/panel/inventory/add"
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
                <div class="item-col item-col-info">Категория</div>
                <div class="item-col item-col-date">Дата создания</div>
                <div class="item-col item-col-actions"></div>
            </div>
            <div
                    v-for="item in inventories"
                    :key="item.id"
                    class="item flex-box"
            >
                <div class="item-col item-col-id">#{{ item.id }}</div>
                <div class="item-col item-col-title">
                    <a href="#modal-inventory" @click.prevent="getCurrent(item.id)" class="flex-box modal-trigger">
                        <img :src="'/uploads/' + (item.image ? item.image : 'noimage.png')" class="icon-avatar" />
                        <span>{{ item.title }}</span>
                    </a>
                </div>
                <div class="item-col item-col-info">{{ (item.category ? item.category.title : '') }}</div>
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
                                    href="#modal-inventory"
                                    @click.prevent="getCurrent(item.id)"
                                    class="modal-trigger"
                            ><i class="material-icons">format_align_left</i>Подробнее</a>
                        </li>
                        <li>
                            <router-link
                                    :to="'/panel/inventory/' + item.id + '-edit'"
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
        <div class="loader-block">
            <div class="preloader-wrapper small" :class="{ active: loading }">
                <div class="spinner-layer spinner-blue-only">
                    <div class="circle-clipper left"><div class="circle"></div></div>
                    <div class="gap-patch"><div class="circle"></div></div>
                    <div class="circle-clipper right"><div class="circle"></div></div>
                </div>
            </div>
        </div>
        <inventory-modal :item="current" />
    </section>
</template>

<script>
    import { mapState, mapGetters, mapActions } from 'vuex'
    import InventoryModal from "./InventoryModal";

    export default {
        name: 'PanelInventory',
        components: {
            InventoryModal
        },
        data: () => ({
            current: null,
            dropdown: null,
            sortedSelect: null,
            modals: null
        }),
        computed: {
            ...mapState({
                sort: state => state.InventoryModule.sort,
                count: state => state.InventoryModule.count
            }),
            ...mapGetters({
                loading: 'getLoading',
                inventories: 'InventoryModule/sortedInventories'
            })
        },
        mounted() {
            this.$store.dispatch("InventoryModule/list")
            this.initDropdown()
            this.initSortedSelect()
            this.initModal()
            window.onscroll = () => {
                const el = document.querySelector('.main-container')
                const isBottomOfScreen = document.documentElement.scrollTop + window.innerHeight >= el.offsetHeight

                if (isBottomOfScreen)
                    this.loadMore()
            }
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
            ...mapActions('InventoryModule', [
                'updateSort'
            ]),
            getCurrent: function(id) {
                this.current = this.$store.state.InventoryModule.inventories.find(inventory => inventory.id === id)
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
                this.$store.dispatch('InventoryModule/remove', id)
            },
            loadMore: function() {
                this.$store.dispatch('InventoryModule/loadPageAction')
            }
        }
    }
</script>