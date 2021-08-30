<template>
    <div id="modal-checks" class="modal modal-fixed-footer">
        <div v-if="item" class="modal-content">
            <h2>Чек-лист - {{ item.title }}</h2>
            <div class="divider"></div>
            <p><strong>Дата создания:</strong> {{ item.dateCreated }}</p>
            <p><strong>Дата изменения:</strong> {{ item.dateModify }}</p>
            <p><strong>Роль:</strong> {{ item.rights.title }}</p>
            <p>{{ item.description }}</p>
            <div class="divider"></div>
            <h4>Перечень вопросов:</h4>
            <ul class="collection">
                <li
                        v-for="elem in item.checksQuestions"
                        :key="elem.id"
                        class="collection-item avatar"
                >
                    <i
                            class="material-icons circle"
                            :class="{orange: elem.type === 'yes_no', blue: elem.type === 'count', green: elem.type === 'quality'}"
                            v-text="(elem.type === 'yes_no' ? 'contact_support' : elem.type === 'count' ? 'inventory_2' : 'high_quality')"
                    />
                    <span class="title"><strong>{{ elem.title }}</strong></span>
                    <p>
                        <i v-text="(elem.type === 'yes_no' ? 'Ответ как Да/Нет' : elem.type === 'count' ? 'Проверка количества' : 'Проверка качества')"></i>
                        <br />
                        {{ elem.values }}
                    </p>
                </li>
            </ul>
        </div>
        <div v-if="item" class="modal-footer">
            <a
                    href="#!"
                    @click.prevent="remove(item.id)"
                    class="modal-close waves-effect waves-green btn left"
            ><i class="material-icons left">delete</i>Удалить</a>
            <router-link
                    :to="'/panel/checks/' + item.id + '-edit'"
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
        name: 'CheckModal',
        props: {
            item: Object
        },
        methods: {
            remove: function(id) {
                this.$store.dispatch('ChecksModule/remove', id)
            },
            sortQuestions: function(questions) {
                return (questions || []).sort((a, b) => (a.category.id - b.category.id))
            }
        }
    }
</script>