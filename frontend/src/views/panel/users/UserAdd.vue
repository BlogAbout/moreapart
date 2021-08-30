<template>
    <section class="panel-content">
        <div class="tools-bar flex-box">
            <h1 v-if="currentId === 0" class="tools-header">Создание нового сотрудника: {{ last_name }} {{ first_name }}</h1>
            <h1 v-else class="tools-header">Редактирование сотрудника: {{ last_name }} {{ first_name }}</h1>
        </div>
        <div class="row">
            <form class="col s12" name="form-user-add" action="" method="post" @submit.prevent="submitHandler">
                <div class="row">
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">person</i>
                        <input
                                id="last_name"
                                type="text"
                                name="last_name"
                                class="validate"
                                v-model.trim="last_name"
                                :class="{invalid: ($v.last_name.$dirty && !$v.last_name.required) || ($v.last_name.$dirty && !$v.last_name.minLength) || ($v.last_name.$dirty && !$v.last_name.maxLength)}"
                        />
                        <label for="last_name">Фамилия</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.last_name.$dirty && !$v.last_name.required"
                        >Поле не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.last_name.$dirty && !$v.last_name.minLength"
                        >Фамилия слишком короткая</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.last_name.$dirty && !$v.last_name.maxLength"
                        >Фамилия слишком длинная</span>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">person</i>
                        <input
                                id="first_name"
                                type="text"
                                name="first_name"
                                class="validate"
                                v-model.trim="first_name"
                                :class="{invalid: ($v.first_name.$dirty && !$v.first_name.required) || ($v.first_name.$dirty && !$v.first_name.minLength) || ($v.first_name.$dirty && !$v.first_name.maxLength)}"
                        />
                        <label for="first_name">Имя</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.first_name.$dirty && !$v.first_name.required"
                        >Поле не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.first_name.$dirty && !$v.first_name.minLength"
                        >Имя слишком короткое</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.first_name.$dirty && !$v.first_name.maxLength"
                        >Имя слишком длинное</span>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">account_circle</i>
                        <input
                                id="username"
                                type="text"
                                name="username"
                                class="validate"
                                v-model.trim="username"
                                :class="{invalid: ($v.username.$dirty && !$v.username.required) || ($v.username.$dirty && !$v.username.minLength) || ($v.username.$dirty && !$v.username.maxLength)}"
                        />
                        <label for="username">Логин</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.username.$dirty && !$v.username.required"
                        >Поле не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.username.$dirty && !$v.username.minLength"
                        >Логин слишком короткий</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.username.$dirty && !$v.username.maxLength"
                        >Логин слишком длинный</span>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">email</i>
                        <input
                                id="email"
                                type="email"
                                name="email"
                                class="validate"
                                v-model.trim="email"
                                :class="{invalid: ($v.email.$dirty && !$v.email.required) || ($v.email.$dirty && !$v.email.email) || ($v.email.$dirty && !$v.email.maxLength)}"
                        />
                        <label for="email">E-mail</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.email.$dirty && !$v.email.required"
                        >Поле не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.email.$dirty && !$v.email.email"
                        >Введите корректный E-mail</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.email.$dirty && !$v.email.maxLength"
                        >E-mail слишком длинный</span>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">phone</i>
                        <input
                                id="phone"
                                type="tel"
                                name="phone"
                                class="validate"
                                v-model.trim="phone"
                                v-phone
                                autocomplete="tel"
                                maxlength="18"
                                :class="{invalid: ($v.phone.$dirty && !$v.phone.required)}"
                        />
                        <label for="phone">Телефон</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.phone.$dirty && !$v.phone.required"
                        >Поле не должно быть пустым</span>
                    </div>
                    <div class="input-field col s12 m6">
                        <i class="material-icons prefix">mode_edit</i>
                        <textarea
                                id="about"
                                name="about"
                                class="materialize-textarea validate"
                                v-model.trim="about"
                                :class="{invalid: $v.about.$dirty && !$v.about.maxLength}"
                        ></textarea>
                        <label for="about">Краткая характеристика</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.about.$dirty && !$v.about.maxLength"
                        >Характеристика слишком длинная</span>
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
                        <label>Роль сотрудника:</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.role.$dirty && !$v.role.required"
                        >Выберите роль для сотрудника</span>
                    </div>
                    <div v-if="currentId === 0" class="input-field col s12 m6">
                        <i class="material-icons prefix">password</i>
                        <input
                                id="password"
                                type="password"
                                name="password"
                                class="validate"
                                v-model.trim="password"
                                :class="{invalid: ($v.password.$dirty && !$v.password.required) || ($v.password.$dirty && !$v.password.minLength)}"
                        />
                        <label for="password">Пароль</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.password.$dirty && !$v.password.required"
                        >Поле Пароль не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.password.$dirty && !$v.password.minLength"
                        >Минимальная длина пароля 6 символов, текущая длина {{ password.length }} символов</span>
                    </div>
                    <div v-if="currentId === 0" class="col s12">
                        <label>
                            <input
                                    id="send_password"
                                    type="checkbox"
                                    name="send_password"
                                    v-model="send_password"
                            />
                            <span>Отправить сотруднику данные для входа</span>
                        </label>
                    </div>
                </div>
                <div v-if="currentId !== 0" class="row">
                    <div class="col s12">
                        <label>
                            <input
                                    id="change_password"
                                    type="checkbox"
                                    name="change_password"
                                    v-model="change_password"
                            />
                            <span>Изменить пароль</span>
                        </label>
                    </div>
                    <div v-if="change_password" class="col s12">
                        <label>
                            <input
                                    id="send_password_new"
                                    type="checkbox"
                                    name="send_password"
                                    v-model="send_password"
                            />
                            <span>Уведомить сотрудника об изменении пароля</span>
                        </label>
                    </div>
                    <div v-if="change_password" class="input-field col s12 m6">
                        <i class="material-icons prefix">password</i>
                        <input
                                id="password_new"
                                type="password"
                                name="password"
                                class="validate"
                                v-model.trim="password"
                                :class="{invalid: ($v.password.$dirty && !$v.password.required) || ($v.password.$dirty && !$v.password.minLength)}"
                        />
                        <label for="password">Пароль</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.password.$dirty && !$v.password.required"
                        >Поле Пароль не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.password.$dirty && !$v.password.minLength"
                        >Минимальная длина пароля 6 символов, текущая длина {{ password.length }} символов</span>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s12">
                        <button
                                v-if="currentId === 0"
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Создать сотрудника<i class="material-icons right">send</i></button>
                        <button
                                v-else
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Обновить сотрудника<i class="material-icons right">send</i></button>
                    </div>
                </div>
                <input type="hidden" name="id" :value="currentId" />
            </form>
        </div>
    </section>
</template>

<script>
    import { email, required, requiredIf, minLength, maxLength } from 'vuelidate/dist/validators.min'
    import { mapState } from 'vuex'
    import messages from '@/utils/plugins/messages'

    export default {
        name: 'PanelUserAdd',
        data: () => ({
            currentId: 0,
            last_name: '',
            first_name: '',
            username: '',
            email: '',
            phone: '',
            about: '',
            role: 0,
            password: '',
            change_password: false,
            send_password: false,
            select: null
        }),
        validations: {
            last_name: {
                required,
                minLength: minLength(2),
                maxLength: maxLength(100)
            },
            first_name: {
                required,
                minLength: minLength(2),
                maxLength: maxLength(100)
            },
            username: {
                required,
                minLength: minLength(5),
                maxLength: maxLength(100)
            },
            email: {
                email,
                required,
                maxLength: maxLength(100)
            },
            phone: {
                required
            },
            about: {
                maxLength: maxLength(256)
            },
            role: {
                required
            },
            password: {
                required: requiredIf(function() {
                    return !!(this.change_password || this.currentId === 0)
                }),
                minLength: requiredIf(function() {
                    return (this.change_password || this.currentId === 0) ? minLength(6) : false
                })
            },
        },
        computed: {
            ...mapState({
                rights: state => state.RolesModule.rights
            })
        },
        mounted() {
            this.$store.dispatch("RolesModule/list")

            this.initSelect()

            this.currentId = this.$route.params.id || 0
            if (this.currentId !== 0)
                this.$store.dispatch('UsersModule/info', this.currentId)
                    .then((response) => {
                        this.last_name = response.data.lastName
                        this.first_name = response.data.firstName
                        this.username = response.data.username
                        this.email = response.data.email
                        this.phone = response.data.phone
                        this.about = response.data.about
                        this.role = (response.data.rights ? response.data.rights.id : 0)
                    })
                    .catch(() => {
                        this.$router.push('/panel/personal')
                    })
        },
        updated() {
            this.$M.updateTextFields();
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

                const formData = {
                    id: this.currentId,
                    last_name: this.last_name,
                    first_name: this.first_name,
                    username: this.username,
                    email: this.email,
                    phone: this.phone,
                    about: this.about,
                    role: this.role,
                    password: this.currentId === 0 ? this.password : this.change_password ? this.password : '',
                    send_password: this.send_password
                }

                await this.$store.dispatch('UsersModule/add', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])

                        if (response.status === 201) {
                            this.currentId = response.data.id
                            this.$message(messages['success-item-create'])
                            this.$router.push(`/panel/personal/${this.currentId}-edit`)
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