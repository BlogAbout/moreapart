<template>
    <div id="modal-role" class="modal modal-fixed-footer">
        <div v-if="item" class="modal-content">
            <h2>Роль - {{ item.title }}</h2>
            <div class="divider"></div>
            <p><strong>Дата создания:</strong> {{ item.dateCreated }}</p>
            <p><strong>Сотрудников:</strong> {{ item.countPersons }}</p>
            <p>{{ item.description }}</p>
            <div class="divider"></div>
            <h4>Доступные права:</h4>
            <div class="item-col item-col-roles">
                    <span
                            v-for="elem in grantedJson(item.granted)"
                            :key="elem"
                            class="tag-item"
                    >{{ getGrantTitleById(elem) }}</span>
            </div>
            <div class="divider"></div>
            <h4>Может назначать задачи для:</h4>
            <div class="item-col item-col-roles">
                    <span
                            v-for="elem in item.siblings"
                            :key="elem"
                            class="tag-item"
                    >{{ getRightTitleById(elem) }}</span>
            </div>
        </div>
        <div v-if="item" class="modal-footer">
            <a
                    href="#!"
                    @click.prevent="remove(item.id)"
                    class="modal-close waves-effect waves-green btn left"
            ><i class="material-icons left">delete</i>Удалить</a>
            <router-link
                    :to="'/panel/personal/roles/' + item.id + '-edit'"
                    custom
                    v-slot="{ href, route, navigate }"
            >
                <a
                        :href="href"
                        @click="navigate"
                        class="modal-close waves-effect waves-green btn left"
                ><i class="material-icons left">edit</i>Редактировать</a>
            </router-link>
            <a
                    href="#!"
                    class="modal-close waves-effect waves-green btn-flat"
            >Закрыть</a>
        </div>
    </div>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'RoleModal',
        props: {
            item: Object
        },
        computed: {
            ...mapState({
                grantedList: state => state.RolesModule.grantedList
            })
        },
        methods: {
            grantedJson: function(granted) {
                const list = []
                JSON.parse(granted).forEach(grant => {
                    list.push(grant)
                })
                return list
            },
            getRightTitleById: function(id) {
                return this.$store.state.RolesModule.rights.find(right => right.id === id).title
            },
            getGrantTitleById: function(id) {
                return this.$store.state.RolesModule.grantedList.find(grant => grant.id === id).label
            },
            remove: function(id) {
                this.$store.dispatch('RolesModule/remove', id)
            }
        }
    }
</script>