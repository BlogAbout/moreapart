<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 v-if="currentId === 0" class="tools-header">Создание нового инвентаря: {{ title }}</h1>
            <h1 v-else class="tools-header">Редактирование инвентаря: {{ title }}</h1>
        </div>
        <div class="row">
            <form class="col s12" name="form-inventory-add" action="" method="post" @submit.prevent="submitHandler">
                <div v-if="image" class="row">
                    <div class="input-field col s12 m12">
                        <img :src="'/uploads/' + image" />
                    </div>
                </div>
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
                        <label for="title">Наименование инвентаря</label>
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
                        <i class="material-icons prefix">category</i>
                        <select
                                id="category"
                                name="category"
                                class="validate"
                                v-model="category"
                                :class="{invalid: ($v.category.$dirty && !$v.category.required)}"
                        >
                            <option value="" disabled selected>Выберите категорию</option>
                            <option
                                    v-for="category in categories"
                                    :key="category.id"
                                    :value="category.id"
                            >{{ category.title }}</option>
                        </select>
                        <label>Категория:</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.category.$dirty && !$v.category.required"
                        >Выберите категорию для инвентаря</span>
                    </div>
                </div>
                <cropper @crop="setFile" @clear="clearFile" />
                <div class="row">
                    <div class="input-field col s12">
                        <button
                                v-if="currentId === 0"
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Создать инвентарь<i class="material-icons right">send</i></button>
                        <button
                                v-else
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Обновить инвентарь<i class="material-icons right">send</i></button>
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
    import Cropper from '@/parts/Cropper'

    export default {
        name: 'PanelInventoryAdd',
        components: {
            Cropper
        },
        data: () => ({
            currentId: 0,
            title: '',
            description: '',
            category: 0,
            image: '',
            file: null
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
            category: {
                required
            }
        },
        computed: {
            ...mapState({
                categories: state => state.CategoriesModule.categories
            })
        },
        mounted() {
            this.$store.dispatch("CategoriesModule/list")

            this.initSelect()

            this.currentId = this.$route.params.id || 0
            if (this.currentId !== 0)
                this.$store.dispatch('InventoryModule/info', this.currentId)
                    .then((response) => {
                        this.title = response.data.title
                        this.description = response.data.description
                        this.category = (response.data.category ? response.data.category.id : 0)
                    })
                    .catch(() => {
                        this.$router.push('/panel/inventory')
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

                if (this.category === 0) {
                    this.$message(messages['category-required'])
                    return
                }

                const formData = {
                    id: this.currentId,
                    title: this.title,
                    description: this.description,
                    category: this.getCategoryById(this.category),
                    file: this.file
                }

                await this.$store.dispatch('InventoryModule/add', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])

                        if (response.status === 201) {
                            this.currentId = response.data.id
                            this.$message(messages['success-item-create'])
                            //this.$router.push(`/panel/inventory/${this.currentId}-edit`)
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
            setFile: function(blob) {
                this.file = blob
            },
            clearFile: function() {
                this.file = null
            }
        }
    }
</script>