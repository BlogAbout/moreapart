<template>
    <div id="modal-user" class="modal modal-fixed-footer">
        <div v-if="item" class="modal-content">
            <h2>Сотрудник - {{ item.fullName }}</h2>
            <div class="divider"></div>
            <p><strong>Фамилия и имя:</strong> {{ item.fullName }}</p>
            <p><strong>Логин:</strong> {{ item.username }}</p>
            <p><strong>E-mail:</strong> {{ item.email }}</p>
            <p><strong>Телефон:</strong> {{ item.phone }}</p>
            <p><strong>Дата регистрации:</strong> {{ item.registration }}</p>
            <p><strong>Последний визит:</strong> {{ item.lastVisit }}</p>
            <p v-if="item.birthDate"><strong>Дата рождения:</strong> {{ item.birthDate }}</p>
            <p v-if="item.age"><strong>Полных лет:</strong> {{ item.age }}</p>
            <p v-if="item.rights"><strong>Роль:</strong> {{ item.rights.title }}</p>
            <div class="divider"></div>
            <h4>Карткая характеристика о сотруднике:</h4>
            <p>{{ item.about }}</p>
        </div>
        <div v-if="item" class="modal-footer">
            <a
                    href="#!"
                    @click.prevent="remove(item.id)"
                    class="modal-close waves-effect waves-green btn left"
            ><i class="material-icons left">delete</i>Удалить</a>
            <router-link
                    :to="'/panel/personal/' + item.id + '-edit'"
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
    export default {
        name: 'RoleModal',
        props: {
            item: Object
        },
        methods: {
            remove: function(id) {
                this.$store.dispatch('RolesModule/remove', id)
            }
        }
    }
</script>