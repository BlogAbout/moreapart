<template>
    <main class="main-container">
        <section class="page-content">
            <span class="decoration-block-title">/Восстановление</span>
            <div class="container">
                <h1 class="page-title">Восстановить пароль</h1>
                <div class="row">
                    <h1 class="col s12 page-title"></h1>
                    <form class="col s12" name="form-forgot" action="" method="post" @submit.prevent="submitHandler">
                        <div class="row">
                            <div class="input-field col s12">
                                <i class="material-icons prefix">email</i>
                                <input
                                        id="email"
                                        type="email"
                                        name="email"
                                        class="validate"
                                        v-model.trim="email"
                                        :class="{invalid: ($v.email.$dirty && !$v.email.required) || ($v.email.$dirty && !$v.email.email)}"
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
                            </div>
                            <div class="input-field col s12">
                                <button
                                        class="btn waves-effect waves-light"
                                        type="submit"
                                        name="action"
                                >Отправить<i class="material-icons right">send</i></button>
                            </div>
                        </div>
                    </form>
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
    import { email, required } from 'vuelidate/dist/validators.min'
    import messages from '@/utils/plugins/messages'

    export default {
        name: "Forgot",
        data: () => ({
            email: ''
        }),
        validations: {
            email: {
                email,
                required
            }
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }

                const formData = {
                    email: this.email
                }

                await this.$store.dispatch('AuthModule/userForgotPassword', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])
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