<template>
    <div>
        <header class="header header-main">
            <div class="container">
                <div class="header-panel">
                    <div class="brand-logo">
                        <router-link
                                to="/"
                                custom
                                v-slot="{ href, route, navigate }"
                        >
                            <a :href="href" @click="navigate">
                                <img src="../assets/images/logo.svg" alt="Посуточная аренда квартир в Сочи" />
                            </a>
                        </router-link>
                    </div>
                    <ul class="main-menu right hide-on-med-and-down">
                        <router-link
                                v-for="link in linksHead"
                                :key="link.url"
                                :to="link.url"
                                custom
                                v-slot="{ href, route, navigate, isActive, isExactActive }"
                        >
                            <li :class="[isActive && 'active', isExactActive && 'exact-active']">
                                <a :href="href" @click="navigate">{{ link.title }}</a>
                            </li>
                        </router-link>
                    </ul>
                    <profile-menu />
                    <social />
                </div>
            </div>
        </header>
        <transition name="fade">
            <router-view />
        </transition>
        <footer class="page-footer">
            <div class="container">
                <div class="row">
                    <div class="col s6 m5">
                        <h5>Контакты</h5>
                        <div class="information">
                            <p>Телефон: <a href="tel:+79384377037">+7 (938) 437-70-37</a></p>
                            <p>E-mail: </p>
                            <p>ИП ШКРЕДОВА ТАТЬЯНА МИХАЙЛОВНА<br />ИНН: 246308437476<br />ОГРНИП ‪319203600019582</p>
                        </div>
                    </div>
                    <div class="col s6 m5">
                        <h5>Навигация</h5>
                        <ul class="footer-menu">
                            <router-link
                                    v-for="link in linksFooter"
                                    :key="link.url"
                                    :to="link.url"
                                    custom
                                    v-slot="{ href, route, navigate, isActive, isExactActive }"
                            >
                                <li :class="[isActive && 'active', isExactActive && 'exact-active']">
                                    <a :href="href" @click="navigate">{{ link.title }}</a>
                                </li>
                            </router-link>
                        </ul>
                    </div>
                    <div class="col s6 m1">
                        <social />
                    </div>
                    <div class="col s6 m1">
                        <div class="banks-logos">
                            <img src="../assets/images/banks/bank1_footer.svg" alt="Мир" />
                            <img src="../assets/images/banks/bank2_footer.svg" alt="Visa" />
                            <img src="../assets/images/banks/bank3_footer.svg" alt="MasterCard" />
                            <img src="../assets/images/banks/bank4_footer.svg" alt="Maestro" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="footer-copyright">
                <div class="container">
                    <div class="row">
                        <div class="col s12 m6">
                            <span class="copyright">&copy; 2018-2021. "Море апартаментов".</span>
                        </div>
                        <div class="col s12 m6">
                            <div class="stats"></div>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
        <a class="go-up btn-floating btn-large waves-effect waves-light" @click.prevent="goToTop">
            <i class="material-icons">keyboard_arrow_up</i>
        </a>
    </div>
</template>

<script>
    import ProfileMenu from '../parts/ProfileMenu.vue'
    import Social from "../parts/Social";

    export default {
        name: 'MainLayout',
        components: {
            ProfileMenu,
            Social
        },
        data: () => ({
            linksHead: [
                { title: 'Апартаменты', url: '/apartments', exact: true },
                { title: 'О нас', url: '/about' },
                { title: 'Контакты', url: '/contacts' }
            ],
            linksFooter: [
                { title: 'Главная', url: '/', exact: true },
                { title: 'Договор оферты', url: '/offer' },
                { title: 'Реквизиты', url: '/contacts' },
                { title: 'Политика конфиденциальности', url: '/policy' },
                { title: 'Личный кабинет', url: '/panel' },
            ]
        }),
        methods: {
            goToTop() {
                window.scrollTo(0, 0);
            }
        }
    }
</script>