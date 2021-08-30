import Vue from 'vue'
import VueRouter from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        meta: {
            title: 'Дизайнерские апартаменты в лучших районах',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/pages/Home.vue')
    },
    {
        path: '/login',
        name: 'login',
        meta: {
            title: 'Вход',
            layout: 'main',
            loginPage: true,
            nonRequiresAuth: true
        },
        component: () => import('../views/profile/Login.vue')
    },
    {
        path: '/forgot',
        name: 'forgot',
        meta: {
            title: 'Восстановление пароля',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/profile/Forgot.vue')
    },
    {
        path: '/registration',
        name: 'registration',
        meta: {
            title: 'Регистрация',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/profile/Registration.vue')
    },
    {
        path: '/reset',
        name: 'reset',
        meta: {
            title: 'Смена пароля',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/profile/Reset.vue')
    },
    {
        path: '/about',
        name: 'about',
        meta: {
            title: 'О компании',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/pages/About.vue')
    },
    {
        path: '/contacts',
        name: 'contacts',
        meta: {
            title: 'Контакты',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/pages/Contacts.vue')
    },
    {
        path: '/offer',
        name: 'offer',
        meta: {
            title: 'Договор оферты',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/pages/Offer.vue')
    },
    {
        path: '/policy',
        name: 'policy',
        meta: {
            title: 'Политика конфиденциальности',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/pages/Policy.vue')
    },
    {
        path: '/apartments',
        name: 'apartments',
        meta: {
            title: 'Апартаменты',
            layout: 'main',
            nonRequiresAuth: true
        },
        component: () => import('../views/pages/Apartments.vue')
    },
    {
        path: '/panel',
        name: 'panel',
        meta: {
            title: 'Панель управления',
            layout: 'admin'
        },
        component: () => import('../views/panel/Panel.vue'),
    },
    {
        path: '/panel/profile',
        name: 'panel-profile',
        meta: {
            title: 'Профиль пользователя',
            layout: 'admin'
        },
        component: () => import('../views/panel/Profile.vue'),
    },
    {
        path: '/panel/personal',
        name: 'panel-personal',
        meta: {
            title: 'Персонал компании',
            layout: 'admin'
        },
        component: () => import('../views/panel/users/Users.vue'),
    },
    {
        path: '/panel/personal/add',
        name: 'panel-personal-add',
        meta: {
            title: 'Новый сотрудник',
            layout: 'admin'
        },
        component: () => import('../views/panel/users/UserAdd.vue'),
    },
    {
        path: '/panel/personal/:id-edit',
        name: 'panel-personal-edit',
        meta: {
            title: 'Редактировать сотрудника',
            layout: 'admin'
        },
        component: () => import('../views/panel/users/UserAdd.vue'),
    },
    {
        path: '/panel/personal/roles',
        name: 'panel-roles',
        meta: {
            title: 'Роли и права доступа',
            layout: 'admin'
        },
        component: () => import('../views/panel/roles/Roles.vue'),
    },
    {
        path: '/panel/personal/roles/add',
        name: 'panel-roles-add',
        meta: {
            title: 'Новая роль',
            layout: 'admin'
        },
        component: () => import('../views/panel/roles/RoleAdd.vue'),
    },
    {
        path: '/panel/personal/roles/:id-edit',
        name: 'panel-roles-edit',
        meta: {
            title: 'Редактировать роль',
            layout: 'admin'
        },
        component: () => import('../views/panel/roles/RoleAdd.vue'),
    },
    {
        path: '/panel/apartments',
        name: 'panel-apartments',
        meta: {
            title: 'Каталог апартаментов',
            layout: 'admin'
        },
        component: () => import('../views/panel/Apartments.vue'),
    },
    {
        path: '/panel/inventory',
        name: 'panel-inventory',
        meta: {
            title: 'Каталог инвентаря',
            layout: 'admin'
        },
        component: () => import('../views/panel/inventories/Inventories.vue'),
    },
    {
        path: '/panel/inventory/add',
        name: 'panel-inventory-add',
        meta: {
            title: 'Новый инвентарь',
            layout: 'admin'
        },
        component: () => import('../views/panel/inventories/InventoryAdd.vue'),
    },
    {
        path: '/panel/inventory/:id-edit',
        name: 'panel-inventory-edit',
        meta: {
            title: 'Редактировать инвентарь',
            layout: 'admin'
        },
        component: () => import('../views/panel/inventories/InventoryAdd.vue'),
    },
    {
        path: '/panel/inventory/categories',
        name: 'panel-categories',
        meta: {
            title: 'Категории инвентаря',
            layout: 'admin'
        },
        component: () => import('../views/panel/categories/Categories.vue'),
    },
    {
        path: '/panel/inventory/categories/add',
        name: 'panel-categories-add',
        meta: {
            title: 'Новая категория',
            layout: 'admin'
        },
        component: () => import('../views/panel/categories/CategoryAdd.vue'),
    },
    {
        path: '/panel/inventory/categories/:id-edit',
        name: 'panel-categories-edit',
        meta: {
            title: 'Редактировать категорию',
            layout: 'admin'
        },
        component: () => import('../views/panel/categories/CategoryAdd.vue'),
    },
    {
        path: '/panel/tasks',
        name: 'panel-tasks',
        meta: {
            title: 'Задачи сотрудников',
            layout: 'admin'
        },
        component: () => import('../views/panel/Panel.vue'),
    },
    {
        path: '/panel/checks',
        name: 'panel-checks',
        meta: {
            title: 'Чек-листы',
            layout: 'admin'
        },
        component: () => import('../views/panel/checks/Checks.vue'),
    },
    {
        path: '/panel/checks/add',
        name: 'panel-checks-add',
        meta: {
            title: 'Новый чек-лист',
            layout: 'admin'
        },
        component: () => import('../views/panel/checks/CheckAdd.vue'),
    },
    {
        path: '/panel/checks/:id-edit',
        name: 'panel-checks-edit',
        meta: {
            title: 'Редактировать чек-лист',
            layout: 'admin'
        },
        component: () => import('../views/panel/checks/CheckAdd.vue'),
    },
    {
        path: '/panel/checks/:id-fill',
        name: 'panel-checks-fill',
        meta: {
            title: 'Заполнить чек-лист',
            layout: 'admin'
        },
        component: () => import('../views/panel/checks/CheckFill.vue'),
    },
    {
        path: '/panel/booking',
        name: 'panel-booking',
        meta: {
            title: 'Бронирование апартаментов',
            layout: 'admin'
        },
        component: () => import('../views/panel/Panel.vue'),
    },
    {
        path: '/panel/messages',
        name: 'panel-messages',
        meta: {
            title: 'Личные сообщения',
            layout: 'admin'
        },
        component: () => import('../views/panel/Panel.vue'),
    },
    {
        path: '/panel/reports',
        name: 'panel-reports',
        meta: {
            title: 'Отчеты и статистика',
            layout: 'admin'
        },
        component: () => import('../views/panel/Reports.vue'),
    },
    {
        path: '/panel/reports/checks',
        name: 'panel-reports-checks',
        meta: {
            title: 'Отчеты по чек-листам',
            layout: 'admin'
        },
        component: () => import('../views/panel/checks/ChecksReports.vue'),
    },
    {
        path: '/panel/reports/:id-check',
        name: 'panel-reports-checks-info',
        meta: {
            title: 'Подробности заполненного чек-листа',
            layout: 'admin'
        },
        component: () => import('../views/panel/checks/ChecksReportsInfo.vue'),
    },
    {
        path: '/panel/logs',
        name: 'panel-logs',
        meta: {
            title: 'Лог действий сотрудников',
            layout: 'admin'
        },
        component: () => import('../views/panel/Panel.vue'),
    },
    {
        path: '*',
        meta: {
            title: '404: Страница не найдена',
            layout: 'main'
        },
        component: () => import('../views/pages/NotFound.vue'),
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
    scrollBehavior() {
        return { x: 0, y: 0 }
    }
})

router.beforeResolve((to, from, next) => {
    if (to.name)
        NProgress.start()
    next()
})

router.afterEach(() => {
    NProgress.done()
})

router.beforeEach((to, from, next) => {
    const { title } = to.meta
    const brand = 'Море апартаментов'
    document.title = `${title ? title + ' | ' : ''}${brand}`

    if (to.matched.some(route => !route.meta.nonRequiresAuth)) {
        if (localStorage.getItem('AuthState')) {
            next()
            return
        } else {
            next('/login')
        }
    } else {
        next()
    }
})




/*
router.beforeEach((to, from, next) => {
    const requiresAuth = !to.matched.some((record) => record.meta.nonRequiresAuth)
    const isLoginPage = to.matched.some((record) => record.meta.loginPage)
    const isAuthenticated = localStorage.getItem('auth')
    if (requiresAuth && !isAuthenticated) {
        next('/login')
    } else if (isLoginPage && isAuthenticated) {
        router.push('/')
    }
    next()
})*/

export default router