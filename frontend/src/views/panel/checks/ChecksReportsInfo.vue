<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 class="tools-header">{{ report.checksInfo.title }}</h1>
        </div>
        <p v-if="report.comment">Забытые вещи: {{ report.comment }}</p>
        <div class="row">
            <form class="col s12" name="form-checks-add" action="" method="post" @submit.prevent="submitHandler">
                <table class="striped highlight responsive-table">
                    <thead>
                        <tr>
                            <th>Вопрос</th>
                            <th>Ответ</th>
                            <th>Примечание</th>
                            <th>Перепроверить</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr
                                v-for="(question, index) in report.checksInfo.checksQuestions"
                                :key="question.id"
                        >
                            <td>{{ question.title }}</td>
                            <td v-if="question.type === 'yes_no'">{{ labelStock(answersJson.find(answer => answer.id === question.id ).stock) }}</td>
                            <td v-else-if="question.type === 'count'">{{ answersJson.find(answer => answer.id === question.id ).count }} шт.</td>
                            <td v-else-if="question.type === 'quality'">{{ labelQuality(answersJson.find(answer => answer.id === question.id ).quality) }}</td>
                            <td>{{ answersJson.find(answer => answer.id === question.id )['note-text'] }}</td>
                            <td>
                                <label>
                                    <input
                                            type="checkbox"
                                            :id="'refill-' + index"
                                            :name="'refill-' + index"
                                            v-model="answers[index]['refill']"
                                    />
                                    <span>выбрать</span>
                                </label>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="row">
                    <div class="input-field col s12">
                        <button
                                v-if="!isRefill"
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Принять чек-лист<i class="material-icons right">done</i></button>
                        <button
                                v-else
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Отправить на перепроверку<i class="material-icons right">published_with_changes</i></button>
                    </div>
                </div>
                <input type="hidden" name="id" :value="currentId" />
            </form>
        </div>
    </section>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'PanelChecksReportsInfo',
        data: () => ({
            current: null,
            currentId: 0,
            answers: []
        }),
        computed: {
            ...mapState({
                report: state => state.ChecksReportsModule.current
            }),
            answersJson: function() {
                const list = []
                JSON.parse(this.report.answers).forEach(answer => {
                    list.push(answer)
                })
                return list
            },
            isRefill: function() {
                for (let item of this.answers) {
                    if (item.refill)
                        return true
                }
                return false
            }
        },
        mounted() {
            this.currentId = this.$route.params.id || 0
            this.$store.dispatch("ChecksReportsModule/info", this.currentId)
                .then((response) => {
                    console.log()
                    for (let quest of response.data.checksInfo.checksQuestions) {
                        this.answers.push({
                            'id': quest.id,
                            'refill': false
                        })
                    }
                })
                .catch(() => {
                    this.$router.push('/panel/checks')
                })
        },
        methods: {
            async submitHandler() {
                this.$error('Функция в стадии разработки')
            },
            labelStock(value) {
                return value ? 'да' : 'нет'
            },
            labelQuality(value) {
                if (value === 'new')
                    return 'Новый'
                else if (value === 'good')
                    return 'Хорошее'
                else
                    return 'Требует ремонт'
            }
        }
    }
</script>