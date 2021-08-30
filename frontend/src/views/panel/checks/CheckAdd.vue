<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 v-if="currentId === 0" class="tools-header">Создание нового чек-листа: {{ title }}</h1>
            <h1 v-else class="tools-header">Редактирование чек-листа: {{ title }}</h1>
        </div>
        <div class="row">
            <form class="col s12" name="form-checks-add" action="" method="post" @submit.prevent="submitHandler">
                <div class="row">
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">title</i>
                        <input
                                id="title"
                                type="text"
                                name="title"
                                class="validate"
                                v-model.trim="title"
                                :class="{invalid: $v.title.$dirty && (!$v.title.required || !$v.title.minLength || !$v.title.maxLength)}"
                        />
                        <label for="title">Наименование чек-листа</label>
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
                                :class="{invalid: $v.description.$dirty && !$v.description.maxLength}"
                        ></textarea>
                        <label for="description">Описание</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.description.$dirty && !$v.description.maxLength"
                        >Описание слишком длиное</span>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">badge</i>
                        <select
                                id="role"
                                name="role"
                                class="validate"
                                v-model="role"
                                :class="{invalid: ($v.role.$dirty && !$v.role.required)}"
                        >
                            <option value="" disabled selected>Выберите роль</option>
                            <option
                                    v-for="right in rights"
                                    :key="right.id"
                                    :value="right.id"
                            >{{ right.title }}</option>
                        </select>
                        <label>Роль сотрудников:</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.role.$dirty && !$v.role.required"
                        >Выберите роль сотрудников, которым будет доступен чек-лист</span>
                    </div>
                </div>
                <h2>Вопросы:</h2>
                <div
                        v-for="(quest, index) in questions"
                        :key="index"
                        class="row"
                >
                    <input type="hidden" name="questions[id]" :value="quest.id" />
                    <div class="input-field col s6 m3">
                        <input
                                :id="'questions-title-' + index"
                                type="text"
                                name="questions[title]"
                                v-model="quest.title"
                        />
                        <label :for="'questions-title-' + index">Текст вопроса</label>
                    </div>
                    <div class="input-field col s6 m3">
                        <select
                                :id="'questions-category-' + index"
                                name="questions[category]"
                                v-model="quest.categoryId"
                        >
                            <option value="" disabled selected>Выберите категорию</option>
                            <option
                                    v-for="category in categories"
                                    :key="category.id"
                                    :value="category.id"
                            >{{ category.title }}</option>
                        </select>
                        <label>Категория вопроса:</label>
                    </div>
                    <div class="input-field col s6 m2">
                        <select
                                :id="'questions-type-' + index"
                                name="questions[type]"
                                v-model="quest.type"
                        >
                            <option value="" disabled selected>Выберите тип</option>
                            <option value="yes_no">Ответ как Да/Нет</option>
                            <option value="count">Проверка количества</option>
                            <option value="quality">Проверка качества</option>
                        </select>
                        <label>Тип вопроса:</label>
                    </div>
                    <div class="input-field col s4 m3">
                        <input
                                :id="'questions-values-' + index"
                                type="text"
                                name="questions[values]"
                                v-model="quest.values"
                        />
                        <label :for="'questions-values-' + index">Дополнительно</label>
                    </div>
                    <div class="input-field col s2 m1">
                        <a
                                href="#"
                                @click.prevent="removeQuest(index)"
                                class="btn-floating btn-small waves-effect waves-light right"
                        ><i class="material-icons">delete_forever</i></a>
                    </div>
                </div>
                <a
                        @click.prevent="addQuestion"
                        class="btn-floating btn-small waves-effect waves-light right"
                ><i class="material-icons">add</i></a>
                <div class="row">
                    <div class="input-field col s12">
                        <button
                                v-if="currentId === 0"
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Создать чек-лист<i class="material-icons right">send</i></button>
                        <button
                                v-else
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Обновить чек-лист<i class="material-icons right">send</i></button>
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
        name: 'PanelCheckAdd',
        data: () => ({
            currentId: 0,
            title: '',
            description: '',
            role: 0,
            questions: []
        }),
        validations: {
            title: {
                required,
                minLength: minLength(3),
                maxLength: maxLength(100)
            },
            description: {
                maxLength: maxLength(256)
            },
            role: {
                required
            }
        },
        computed: {
            ...mapState({
                rights: state => state.RolesModule.rights,
                categories: state => state.CategoriesModule.categories
            })
        },
        mounted() {
            this.$store.dispatch("RolesModule/list")
            this.$store.dispatch("CategoriesModule/list")

            this.initSelect()

            this.currentId = this.$route.params.id || 0
            if (this.currentId !== 0)
                this.$store.dispatch('ChecksModule/info', this.currentId)
                    .then((response) => {
                        this.title = response.data.title
                        this.description = response.data.description
                        this.role = (response.data.rights ? response.data.rights.id : 0)
                        this.questions = response.data.checksQuestions
                    })
                    .catch(() => {
                        this.$router.push('/panel/checks')
                    })
        },
        updated() {
            this.$M.updateTextFields()
            this.initSelect()
        },
        beforeDestroy() {
            if (this.select && this.select.destroy)
                this.select.destroy()
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }

                if (this.role === 0) {
                    this.$message(messages['role-required'])
                    return
                }

                for (let quest of this.questions) {
                    quest.id = (quest.id === 0 ? null : quest.id)
                    quest.category = this.getCategoryById(quest.categoryId)
                    quest.check = this.currentId
                }

                const formData = {
                    id: this.currentId,
                    title: this.title,
                    description: this.description,
                    role: this.getRoleById(this.role),
                    questions: this.questions
                }

                await this.$store.dispatch('ChecksModule/add', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])

                        if (response.status === 201) {
                            this.currentId = response.data.id
                            this.$message(messages['success-item-create'])
                            this.$router.push(`/panel/checks/${this.currentId}-edit`)
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
            },
            initSelect: function() {
                if (this.select && this.select.destroy)
                    this.select.destroy()

                this.select = this.$M.FormSelect.init(
                    document.querySelectorAll('select'),
                    {
                        isMultiple: false,
                    }
                )
            },
            getCategoryById: function(id) {
                return this.$store.state.CategoriesModule.categories.find(category => category.id === id)
            },
            getRoleById: function(id) {
                return this.$store.state.RolesModule.rights.find(right => right.id === id)
            },
            addQuestion: function() {
                this.questions.push({
                    id: 0,
                    title: '',
                    category: 0,
                    type: null,
                    values: ''
                })
            }
        }
    }
</script>