<template>
    <main class="main-container">
        <section class="page-content">
            <span class="decoration-block-title">/Смена пароля</span>
            <div class="container">
                <h1 class="page-title">Изменить пароль</h1>
                <div class="row">
                    <form class="col s12" v-if="reset_key" name="form-reset" action="" method="post" @submit.prevent="submitHandler">
                        <div class="row">
                            <div class="input-field col s12 m6">
                                <i class="material-icons prefix">password</i>
                                <input
                                        id="password"
                                        type="password"
                                        name="password"
                                        class="validate"
                                        v-model.trim="password"
                                        :class="{invalid: ($v.password.$dirty && !$v.password.required) || ($v.password.$dirty && !$v.password.minLength)}"
                                />
                                <label for="password">Новый пароль</label>
                                <span
                                        class="helper-text invalid"
                                        v-if="$v.password.$dirty && !$v.password.required"
                                >Поле не должно быть пустым</span>
                                <span
                                        class="helper-text invalid"
                                        v-else-if="$v.password.$dirty && !$v.password.minLength"
                                >Минимальная длина пароля 6 символов, текущая длина {{ password.length }} символов</span>
                            </div>
                            <div class="input-field col s12 m6">
                                <i class="material-icons prefix">password</i>
                                <input
                                        id="password_repeat"
                                        type="password"
                                        name="password_repeat"
                                        class="validate"
                                        v-model.trim="password_repeat"
                                        :class="{invalid: ($v.password_repeat.$dirty && !$v.password_repeat.required) || ($v.password_repeat.$dirty && !$v.password_repeat.minLength) || ($v.password_repeat.$dirty && !$v.password_repeat.equals)}"
                                />
                                <label for="password_repeat">Повторите пароль</label>
                                <span
                                        class="helper-text invalid"
                                        v-if="$v.password_repeat.$dirty && !$v.password_repeat.required"
                                >Поле не должно быть пустым</span>
                                <span
                                        class="helper-text invalid"
                                        v-else-if="$v.password_repeat.$dirty && !$v.password_repeat.minLength"
                                >Минимальная длина пароля 6 символов, текущая длина {{ password_repeat.length }} символов</span>
                                <span
                                        class="helper-text invalid"
                                        v-else-if="$v.password_repeat.$dirty && !$v.password_repeat.equals"
                                >Пароли не совпадают</span>
                            </div>
                            <div class="input-field col s12">
                                <button
                                        class="btn waves-effect waves-light"
                                        type="submit"
                                        name="action"
                                >Войти<i class="material-icons right">login</i></button>
                            </div>
                        </div>
                        <input type="hidden" id="reset_key" name="reset_key" :value="reset_key" />
                    </form>
                    <p v-else>Ключ для восстановления пароля не найден, возможно Вы использовали некорректную ссылку для восстановления</p>
                    <div class="col s12 links">
                        <router-link
                                to="/login"
                                custom
                                v-slot="{ href, route, navigate }"
                        >
                            <a :href="href" @click="navigate"><i class="material-icons left">login</i>Вход</a>
                        </router-link>
                        <router-link
                                to="/registration"
                                custom
                                v-slot="{ href, route, navigate }"
                        >
                            <a :href="href" @click="navigate"><i class="material-icons left">person_add</i>Регистрация</a>
                        </router-link>
                    </div>
                </div>
            </div>
        </section>
    </main>
</template>

<script>
    import { required, minLength } from 'vuelidate/dist/validators.min'
    import messages from '@/utils/plugins/messages'

    export default {
        name: "Login",
        data: () => ({
            reset_key: false,
            password: '',
            password_repeat: ''
        }),
        mounted() {
            this.reset_key = this.$route.query.key || false
            if (!this.reset_key)
                this.$error(messages['not-query-reset-key'])
        },
        validations: {
            password: {
                required,
                minLength: minLength(6)
            },
            password_repeat: {
                required,
                minLength: minLength(6),
                equals: function() {
                    return this.password_repeat == this.password
                }
            }
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }

                const formData = {
                    reset_key: this.reset_key,
                    password: this.password,
                    password_repeat: this.password_repeat
                }

                await this.$store.dispatch('AuthModule/userResetPassword', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])

                        if (response.status == 200)
                            this.$router.push('/login')
                    })
                    .catch((error) => {
                        if (error.response)
                            console.log('error.response', error.response)
                        else if (error.request)
                            console.log('error.request', error.request)
                        else
                            console.log('error.message', error.message)
                    })
            }
        }
    }
</script>