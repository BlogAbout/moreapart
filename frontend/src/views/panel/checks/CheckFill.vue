<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 class="tools-header">Заполнение чек-листа: {{ title }}</h1>
        </div>
        <div class="row">
            <div class="col s12">
                <p>{{ description }}</p>
            </div>
            <form class="col s12" name="form-checks-fill" action="" method="post" @submit.prevent="submitHandler">
                <div class="row">
                    <div class="input-field col s12 m6">
                        <select name="apartments">
                            <option value="" disabled selected>Выберите апартаменты</option>
                        </select>
                        <label>Апартаменты:</label>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">feedback</i>
                        <textarea
                                id="comment"
                                name="comment"
                                class="materialize-textarea"
                                v-model.trim="comment"
                        ></textarea>
                        <label for="comment">Оставленные вещи гостей и другие комментарии</label>
                    </div>
                </div>
                <div class="flex-box flex-wrap flex-top">
                    <div
                            v-for="(quest, index) in questions"
                            :key="index"
                            class="input-field col col-3"
                    >
                        <div class="checkbox-label flex-box flex-top">
                            <span>{{ quest.title }}</span>
                            <label>
                                <input
                                        type="checkbox"
                                        :id="'note-' + index"
                                        :name="'note-' + index"
                                        v-model="answers[index]['note']"
                                /><span>прим</span>
                            </label>
                        </div>
                        <div class="flex-box flex-top">
                            <div class="col" :class="{'col-2': answers[index]['note']}">
                                <div v-if="quest.type === 'yes_no'" class="switch">
                                    <label>
                                        Нет
                                        <input
                                                type="checkbox"
                                                :id="'stock-' + index"
                                                :name="'stock-' + index"
                                                v-model="answers[index]['stock']"
                                        />
                                        <span class="lever"></span>
                                        Да
                                    </label>
                                </div>
                                <input
                                        v-else-if="quest.type === 'count'"
                                        type="text"
                                        :id="'count-' + index"
                                        :name="'count-' + index"
                                        placeholder="Количество (шт.)"
                                        v-model="answers[index]['count']"
                                />
                                <select
                                        v-else-if="quest.type === 'quality'"
                                        :id="'quality-' + index"
                                        :name="'quality-' + index"
                                        v-model="answers[index]['quality']"
                                >
                                    <option value="" disabled selected>Выберите состояние</option>
                                    <option value="new">Новое</option>
                                    <option value="good">Хорошее</option>
                                    <option value="repair">Требует ремонт</option>
                                </select>
                            </div>
                            <div v-if="answers[index]['note']" class="col col-2">
                                <input
                                        type="text"
                                        :id="'note-text-' + index"
                                        :name="'note-text-' + index"
                                        placeholder="Примечание"
                                        v-model="answers[index]['note-text']"
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s12">
                        <button
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Отправить на обработку<i class="material-icons right">send</i></button>
                    </div>
                </div>
                <input type="hidden" name="id" :value="currentId" />
            </form>
        </div>
    </section>
</template>

<script>
    import { mapState } from 'vuex'
    import messages from '@/utils/plugins/messages'

    export default {
        name: 'PanelCheckFill',
        data: () => ({
            currentId: 0,
            title: '',
            description: '',
            questions: [],
            comment: '',
            answers: []
        }),
        computed: {
            ...mapState({
                categories: state => state.CategoriesModule.categories
            })
        },
        mounted() {
            this.initSelect()

            this.currentId = this.$route.params.id || 0
            if (this.currentId !== 0)
                this.$store.dispatch('ChecksModule/info', this.currentId)
                    .then((response) => {
                        for (let question of response.data.checksQuestions) {
                            this.answers.push({
                                'id': question.id,
                                'stock': false,
                                'count': '',
                                'quality': '',
                                'note-text': '',
                                'note': false
                            })
                        }

                        this.title = response.data.title
                        this.description = response.data.description
                        this.questions = response.data.checksQuestions
                    })
                    .catch(() => {
                        this.$router.push('/panel/checks')
                    })
        },
        updated() {
            this.initSelect()
        },
        beforeDestroy() {
            if (this.select && this.select.destroy)
                this.select.destroy()
        },
        methods: {
            async submitHandler() {
                const formData = {
                    check: this.currentId,
                    answers: this.answers,
                    comment: this.comment
                }

                await this.$store.dispatch('ChecksReportsModule/add', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])

                        if (response.status === 201) {
                            this.currentId = response.data.id
                            this.$message(messages['success-item-create'])
                            this.$router.push('/panel/checks')
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
            }
        }
    }
</script>