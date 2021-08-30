<template>
    <main class="main-container">
        <section class="page-content">
            <span class="decoration-block-title">/Вход</span>
            <div class="container">
                <h1 class="page-title">Вход</h1>
                <div class="row">
                    <form class="col s12" name="form-login" action="" method="post" @submit.prevent="submitHandler">
                        <div class="row">
                            <div class="input-field col s12 m6">
                                <i class="material-icons prefix">email</i>
                                <input
                                        id="username"
                                        type="text"
                                        name="username"
                                        class="validate"
                                        v-model.trim="username"
                                        :class="{invalid: ($v.username.$dirty && !$v.username.required)}"
                                />
                                <label for="username">Логин</label>
                                <span
                                        class="helper-text invalid"
                                        v-if="$v.username.$dirty && !$v.username.required"
                                >Поле не должно быть пустым</span>
                            </div>
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
                                <label for="password">Пароль</label>
                                <span
                                        class="helper-text invalid"
                                        v-if="$v.password.$dirty && !$v.password.required"
                                >Поле не должно быть пустым</span>
                                <span
                                        class="helper-text invalid"
                                        v-else-if="$v.password.$dirty && !$v.password.minLength"
                                >Минимальная длина пароля 6 символов, текущая длина {{ password.length }} символов</span>
                            </div>
                            <div class="input-field col s12">
                                <button
                                        class="btn waves-effect waves-light"
                                        type="submit"
                                        name="action"
                                >Войти<i class="material-icons right">login</i></button>
                            </div>
                        </div>
                    </form>
                    <div class="col s12 links">
                        <router-link
                                to="/registration"
                                custom
                                v-slot="{ href, route, navigate }"
                        >
                            <a :href="href" @click="navigate"><i class="material-icons left">person_add</i>Регистрация</a>
                        </router-link>
                        <router-link
                                to="/forgot"
                                custom
                                v-slot="{ href, route, navigate }"
                        >
                            <a :href="href" @click="navigate"><i class="material-icons left">restart_alt</i>Восстановить пароль</a>
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
            username: '',
            password: ''
        }),
        validations: {
            username: { required },
            password: { required, minLength: minLength(6) }
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }

                const credentials = {
                    username: this.username,
                    password: this.password
                }

                await this.$store.dispatch('AuthModule/userSignIn', credentials)
                    .then(() => {
                        this.$message(messages['welcome'])
                        this.$router.push('/panel')
                    })
                    .catch((error) => {
                        if (error.response) {
                            if (messages[error.response.data.status])
                                this.$error(messages[error.response.data.status])

                            // client received an error response (5xx, 4xx)
                        } else if (error.request) {
                            this.$error(error.message)
                            // client never received a response, or request never left
                        } else {
                            this.$error(error.message)
                            // anything else
                        }
                    })
            }
        },
        mounted() {
            if (messages[this.$route.query.message])
                this.$message(messages[this.$route.query.message])
        }
    }
</script>