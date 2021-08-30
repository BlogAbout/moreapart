<template>
    <div class="row">
        <div class="col s12 m6">
            <div class="form-container-block">
                <div class="block-subtitle" v-if="blockSubtitle">{{ blockSubtitle }}</div>
                <h3 class="block-title" v-if="blockTite">{{ blockTite }}</h3>
                <p>Отправте заявку, мы перезвоним вам и ответим на все интересующие вопросы.</p>
                <form class="col s12" name="form-feedback" action="" method="post" @submit.prevent="submitHandler">
                    <div class="input-field">
                        <i class="material-icons prefix">person</i>
                        <input
                                id="name"
                                type="text"
                                name="name"
                                class="validate"
                                v-model.trim="name"
                                :class="{invalid: ($v.name.$dirty && !$v.name.required)}"
                        />
                        <label for="name">Имя:</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.name.$dirty && !$v.name.required"
                        >Поле Имя не должно быть пустым</span>
                    </div>
                    <div class="input-field">
                        <i class="material-icons prefix">email</i>
                        <input
                                id="email"
                                type="email"
                                name="email"
                                class="validate"
                                v-model.trim="email"
                                :class="{invalid: ($v.email.$dirty && !$v.email.required) || ($v.email.$dirty && !$v.email.email)}"
                        />
                        <label for="email">E-mail:</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.email.$dirty && !$v.email.required"
                        >Поле E-mail не должно быть пустым</span>
                        <span
                                class="helper-text invalid"
                                v-else-if="$v.email.$dirty && !$v.email.email"
                        >Введите корректный E-mail</span>
                    </div>
                    <div class="input-field">
                        <i class="material-icons prefix">phone</i>
                        <input
                                id="phone"
                                type="tel"
                                name="phone"
                                class="validate"
                                v-model.trim="phone"
                                :class="{invalid: ($v.phone.$dirty && !$v.phone.required)}"
                        />
                        <label for="phone">Телефон:</label>
                        <span
                                class="helper-text invalid"
                                v-if="$v.phone.$dirty && !$v.phone.required"
                        >Поле Телефон не должно быть пустым</span>
                    </div>
                    <label>
                        <input
                                id="agree"
                                type="checkbox"
                                name="agree"
                                v-model="agree"
                        />
                        <span>Принимаю условия политики конфиденциальности</span>
                    </label>
                    <div class="input-field">
                        <button
                                class="btn waves-effect waves-light"
                                type="submit"
                                name="action"
                        >Отправить<i class="material-icons right">send</i></button>
                    </div>
                </form>
            </div>
        </div>
        <div class="col s12 m6">
            <!--div class="row">
                <div class="col m12">
                    <ul class="tabs" ref="tabsMap">
                        <li class="tab col s3">
                            <a class="active" href="#map1">Офис в Сочи</a>
                        </li>
                        <li class="tab col s3">
                            <a href="#map2">Офис в Адлере</a>
                        </li>
                    </ul>
                </div>
                <div id="map1" class="col m12">
                    <div class="map">
                        <iframe src="https://yandex.ru/map-widget/v1/?um=constructor%3Aab714eb6d05a6e58f5b48cb0fa10120ef90757223144c40aa62a6e55ed723900&amp;source=constructor" width="100%" height="450" frameborder="0"></iframe>
                    </div>
                </div>
                <div id="map2" class="col m12">
                    <div class="map">
                        <iframe src="https://yandex.ru/map-widget/v1/?um=constructor%3Aa46759d723dfd4f4482a3ed377a2d564a202ed67db4a7dd277b353e0716404dd&amp;source=constructor" width="100%" height="450" frameborder="0"></iframe>
                    </div>
                </div>
            </div-->
        </div>
    </div>
</template>

<script>
    import { email, required } from 'vuelidate/dist/validators.min'

    export default {
        name: 'Feedback',
        props: ['blockTite', 'blockSubtitle'],
        data: () => ({
            name: '',
            email: '',
            phone: '',
            agree: false,
            tabsMap: null
        }),
        validations: {
            name: {
                required
            },
            email: {
                email,
                required
            },
            phone: {
                required
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
                    email: this.email,
                    phone: this.phone
                }
                console.log(formData)

                // Шлем на почту заявку
            }
        },
        mounted() {
            this.tabsMap = this.$M.Tabs.init(
                this.$refs.tabsMap,
                {
                    duration: 300
                }
            )
        },
        beforeDestroy() {
            if (this.tabsMap && this.tabsMap.destroy) {
                this.tabsMap.destroy()
            }
        }
    }
</script>