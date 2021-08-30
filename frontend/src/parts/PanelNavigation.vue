<template>
    <nav class="navigation-container flex-box">
        <div class="panel-user">
            <router-link
                    to="/panel/profile"
                    custom
                    v-slot="{ href, route, navigate }"
            >
                <a :href="href" @click="navigate" class="flex-box">
                    <img src="../assets/images/avatar.jpg" alt="Адамовичус Павел" class="circle responsive-img" />
                </a>
            </router-link>
            <div class="panel-user-name"><strong>Адамовичус Павел</strong><br />Разработчик системы</div>
        </div>
        <div class="panel-timer">{{intervalTimer | date('datetime')}}</div>
        <ul class="panel-menu">
            <router-link
                    v-for="link in linksPanel"
                    :key="link.url"
                    :to="link.url"
                    custom
                    v-slot="{ href, route, navigate, isActive, isExactActive }"
            >
                <li :class="[isActive && 'active', isExactActive && 'exact-active']">
                    <a :href="href" @click="navigate"><i class="material-icons left">{{ link.icon }}</i>{{ link.title }}</a>
                    <ul
                            class="children-menu"
                            v-show="link.children"
                    >
                        <router-link
                                v-for="child in link.children"
                                :key="child.url"
                                :to="child.url"
                                custom
                                v-slot="{ href, route, navigate, isActive, isExactActive }"
                        >
                            <li :class="[isActive && 'active', isExactActive && 'exact-active']">
                                <a :href="href" @click="navigate">{{ child.title }}</a>
                            </li>
                        </router-link>
                    </ul>
                </li>
            </router-link>
        </ul>
    </nav>
</template>


<script>
    export default {
        name: 'PanelNavigation',
        data: () => ({
            intervalTimer: new Date(),
            linksPanel: [
                {
                    title: 'Панель',
                    url: '/panel',
                    icon: 'dashboard'
                },
                {
                    title: 'Персонал',
                    url: '/panel/personal',
                    icon: 'groups',
                    children: [
                        {
                            title: 'Добавить сотрудника',
                            url: '/panel/personal/add'
                        },
                        {
                            title: 'Роли сотрудников',
                            url: '/panel/personal/roles'
                        }
                    ]
                },
                {
                    title: 'Апартаменты',
                    url: '/panel/apartments',
                    icon: 'business'
                },
                {
                    title: 'Инвентарь',
                    url: '/panel/inventory',
                    icon: 'chair',
                    children: [
                        {
                            title: 'Добавить инвентарь',
                            url: '/panel/inventory/add'
                        },
                        {
                            title: 'Категории инвентаря',
                            url: '/panel/inventory/categories'
                        }
                    ]
                },
                {
                    title: 'Задачи',
                    url: '/panel/tasks',
                    icon: 'task_alt'
                },
                {
                    title: 'Чек-листы',
                    url: '/panel/checks',
                    icon: 'checklist_rtl',
                    children: [
                        {
                            title: 'Добавить чек-лист',
                            url: '/panel/checks/add'
                        }
                    ]
                },
                {
                    title: 'Бронирование',
                    url: '/panel/booking',
                    icon: 'book_online'
                },
                {
                    title: 'Сообщения',
                    url: '/panel/messages',
                    icon: 'question_answer'
                },
                {
                    title: 'Отчеты',
                    url: '/panel/reports',
                    icon: 'receipt_long',
                    children: [
                        {
                            title: 'По чек-листам',
                            url: '/panel/reports/checks'
                        }
                    ]
                },
                {
                    title: 'Логирование',
                    url: '/panel/logs',
                    icon: 'flag'
                }
            ]
        }),
        mounted() {
            this.intervalTimer = setInterval(() => {
                this.intervalTimer = new Date()
            }, 1000)

            this.$once('hook:beforeDestroy', function() {
                if (this.intervalTimer)
                    clearInterval(this.intervalTimer)
            })
        }
    }
</script>