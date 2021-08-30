<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 v-if="currentId === 0" class="tools-header">Создание новой роли: {{ title }}</h1>
            <h1 v-else class="tools-header">Редактирование роли: {{ title }}</h1>
        </div>
        <div class="row">
            <form class="col s12" name="form-role-add" action="" method="post" @submit.prevent="submitHandler">
                <div class="row">
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">title</i>
                        <input
                                id="title"
                                type="text"
                                name="title"
                                class="validate"
                                v-model.trim="title"
                                :class="{invalid: ($v.title.$dirty && !$v.title.required) || ($v.title.$dirty && !$v.title.minLength) || ($v.title.$dirty && !$v.title.maxLength)}"
                        />
                        <label for="title">Наименование роли</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.title.$dirty && !$v.title.required"
                        >Поле не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.title.$dirty && !$v.title.minLength"
                        >Наименование слишком короткое</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.title.$dirty && !$v.title.maxLength"
                        >Наименование слишком длиное</span>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">mode_edit</i>
                        <textarea
                                id="description"
                                name="description"
                                class="materialize-textarea validate"
                                v-model.trim="description"
                                :class="{invalid: $v.description.$dirty && (!$v.description.required || !$v.description.minLength || !$v.description.maxLength)}"
                        ></textarea>
                        <label for="description">Описание</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.description.$dirty && !$v.description.required"
                        >Поле не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.description.$dirty && !$v.description.minLength"
                        >Описание слишком короткое</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.description.$dirty && !$v.description.maxLength"
                        >Описание слишком длиное</span>
                    </div>
                </div>
                <h2>Права доступа</h2>
                <div class="flex-box flex-wrap flex-top">
                    <div
                            class="col col-3"
                            v-for="item in grantedList"
                            :key="item.id"
                    >
                        <label>
                            <input
                                    v-bind:id="item.id"
                                    type="checkbox"
                                    name="granted"
                                    v-bind:value="item.id"
                                    v-model="granted"
                            />
                            <span>{{ item.label }}</span>
                        </label>
                        <span class="checkbox-description">{{ item.desc }}</span>
                    </div>
                </div>
                <h2>Может назначать задачи для</h2>
                <div class="flex-box flex-wrap flex-top">
                    <div
                            class="col col-3"
                            v-for="item in listRights"
                            :key="item.id"
                    >
                        <label>
                            <input
                                    v-bind:id="item.id"
                                    type="checkbox"
                                    name="siblings"
                                    v-bind:value="item.id"
                                    v-model="siblings"
                            />
                            <span>{{ item.title }}</span>
                        </label>
                        <span class="checkbox-description">{{ item.desc }}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s12">
                        <button
                                v-if="currentId === 0"
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Создать роль<i class="material-icons right">send</i></button>
                        <button
                                v-else
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Обновить роль<i class="material-icons right">send</i></button>
                    </div>
                </div>
                <input type="hidden" name="id" :value="currentId" />
            </form>
        </div>
    </section>
</template>

<script>
    import { required, minLength, maxLength } from 'vuelidate/dist/validators.min'
    import { mapState } from 'vuex'
    import messages from '@/utils/plugins/messages'

    export default {
        name: 'PanelRoleAdd',
        data: () => ({
            currentId: 0,
            title: '',
            description: '',
            granted: [],
            siblings: []
        }),
        validations: {
            title: {
                required,
                minLength: minLength(3),
                maxLength: maxLength(100)
            },
            description: {
                required,
                minLength: minLength(3),
                maxLength: maxLength(256)
            }
        },
        computed: {
            ...mapState({
                listRights: state => state.RolesModule.rights,
                grantedList: state => state.RolesModule.grantedList
            })
        },
        mounted() {
            this.$store.dispatch("RolesModule/list")

            this.currentId = this.$route.params.id || 0
            if (this.currentId !== 0)
                this.$store.dispatch('RolesModule/info', this.currentId)
                    .then((response) => {
                        this.title = response.data.title
                        this.description = response.data.description
                        if (response.data.granted) {
                            JSON.parse(response.data.granted).forEach(grant => {
                                this.granted.push(grant)
                            })
                        }

                        if (response.data.siblings)
                            this.siblings = response.data.siblings
                    })
                    .catch(() => {
                        this.$router.push('/panel/personal/roles')
                    })
        },
        updated() {
            this.$M.updateTextFields();
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }

                const formData = {
                    id: this.currentId,
                    title: this.title,
                    description: this.description,
                    granted: this.granted,
                    siblings: this.siblings
                }

                await this.$store.dispatch('RolesModule/add', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])

                        if (response.status === 201) {
                            this.currentId = response.data.id
                            this.$message(messages['success-item-create'])
                            this.$router.push(`/panel/personal/roles/${this.currentId}-edit`)
                        } else if (response.status === 200) {
                            this.$message(messages['success-item-update'])
                        }
                    })
                    .catch((error) => {
                        if (error.response)
                            console.log('error.response', error.response)
                        else if (error.request)
                            console.log('error.request', error.request)
                        else
                            console.log('error.message', error.message)

                        console.log(error.message)
                    })
            }
        }
    }
</script>