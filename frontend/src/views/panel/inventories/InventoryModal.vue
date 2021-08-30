<template>
    <div id="modal-inventory" class="modal modal-fixed-footer">
        <div v-if="item" class="modal-content">
            <h2>Инвентарь - {{ item.title }}</h2>
            <div class="divider"></div>
            <p><strong>Дата создания:</strong> {{ item.dateCreated }}</p>
            <p><strong>Дата изменения:</strong> {{ item.dateModify }}</p>
            <p>{{ item.description }}</p>
            <div class="divider"></div>
            <h4>Располагается в апартаментах:</h4>
        </div>
        <div v-if="item" class="modal-footer">
            <a
                    href="#!"
                    @click.prevent="remove(item.id)"
                    class="modal-close waves-effect waves-green btn left"
            ><i class="material-icons left">delete</i>Удалить</a>
            <router-link
                    :to="'/panel/inventory/' + item.id + '-edit'"
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
        name: 'InventoryModal',
        props: {
            item: Object
        },
        methods: {
            remove: function(id) {
                this.$store.dispatch('InventoryModule/remove', id)
            }
        }
    }
</script>