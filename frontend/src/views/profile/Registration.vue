<template>
    <main class="main-container">
        <section class="page-content">
            <span class="decoration-block-title">/Регистрация</span>
            <div class="container">
                <h1 class="page-title">Регистрация</h1>
                <div class="row">
                    <form class="col s12" name="form-registration" action="" method="post" @submit.prevent="submitHandler">
                        <div class="row">
                            <div class="input-field col s12 m6">
                                <i class="material-icons prefix">person</i>
                                <input
                                        id="name"
                                        type="text"
                                        name="name"
                                        class="validate"
                                        v-model.trim="name"
                                        :class="{invalid: ($v.name.$dirty && !$v.name.required)}"
                                />
                                <label for="name">Имя</label>
                                <span
                                        class="helper-text invalid"
                                        v-if="$v.name.$dirty && !$v.name.required"
                                >Поле не должно быть пустым</span>
                            </div>
                            <div class="input-field col s12 m6">
                                <i class="material-icons prefix">account_circle</i>
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
                            <div class="col s12">
                                <label>
                                    <input
                                            id="agree"
                                            type="checkbox"
                                            name="agree"
                                            v-model="agree"
                                    />
                                    <span>Принимаю условия политики конфиденциальности</span>
                                </label>
                            </div>
                            <div class="input-field col s12">
                                <button
                                        class="btn waves-effect waves-light"
                                        type="submit"
                                        name="action"
                                >Зарегистрироваться<i class="material-icons right">person_add</i></button>
                            </div>
                        </div>
                    </form>
                    <div class="col s12 links">
                        <router-link
                                to="/login"
                                custom
                                v-slot="{ href, route, navigate }"
                        >
                            <a :href="href" @click="navigate"><i class="material-icons left">login</i>Войти</a>
                        </router-link>
                    </div>
                </div>
            </div>
        </section>
    </main>
</template>

<script>
    import { email, required, minLength } from 'vuelidate/dist/validators.min'
    import messages from '@/utils/plugins/messages'

    export default {
        name: "Registration",
        data: () => ({
            name: '',
            username: '',
            email: '',
            phone: '',
            password: '',
            agree: false
        }),
        validations: {
            name: {
                required
            },
            username: {
                required
            },
            email: {
                email,
                required
            },
            phone: {
                required
            },
            password: {
                required,
                minLength: minLength(6)
            },
            agree: {
                checked: v => v
            }
        },
        methods: {
            async submitHandler() {
                if (this.$v.$invalid) {
                    this.$v.$touch()
                    return
                }

                const formData = {
                    name: this.name,
                    username: this.username,
                    email: this.email,
                    phone: this.phone,
                    password: this.password
                }

                const credentials = {
                    username: this.username,
                    password: this.password
                }

                await this.$store.dispatch('AuthModule/userSignUp', formData)
                    .then((response) => {
                        if (messages[response.data.message])
                            this.$message(messages[response.data.message])

                        if (response.status === 200) {
                            this.$store.dispatch('AuthModule/userSignIn', credentials)
                                .then(() => {
                                    this.$message(messages['welcome'])
                                    this.$router.push('/panel')
                                })
                                .catch((error) => {
                                    if (error.response)
                                        console.log('Авторизация error.response', error.response)
                                    else if (error.request)
                                        console.log('Авторизация error.request', error.request)
                                    else
                                        console.log('Авторизация error.message', error.message)
                                })
                        }
                    })
                    .catch((error) => {
                        if (error.response)
                            console.log('Регистрация error.response', error.response)
                        else if (error.request)
                            console.log('Регистрация error.request', error.request)
                        else
                            console.log('Регистрация error.message', error.message)

                        console.log(error.message)
                    })
            }
        }
    }
</script>