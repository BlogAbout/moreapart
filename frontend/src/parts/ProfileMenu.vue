<template>
    <div class="profile-menu right-align">
        <a
                class="dropdown-trigger"
                href="#"
                data-target="profile-menu"
                ref="toggleProfile"
        ><i class="material-icons">person</i></a>
        <ul id="profile-menu" class="dropdown-content">
            <router-link
                    to="/login"
                    custom
                    v-slot="{ href, route, navigate, isActive, isExactActive }"
            >
                <li :class="[isActive && 'active', isExactActive && 'exact-active']">
                    <a :href="href" @click="navigate"><i class="material-icons left">login</i>Вход</a>
                </li>
            </router-link>
            <router-link
                    to="/registration"
                    custom
                    v-slot="{ href, route, navigate, isActive, isExactActive }"
            >
                <li :class="[isActive && 'active', isExactActive && 'exact-active']">
                    <a :href="href" @click="navigate"><i class="material-icons left">person_add</i>Регистрация</a>
                </li>
            </router-link>
            <li class="divider" tabindex="-1"></li>
            <router-link
                    to="/panel"
                    custom
                    v-slot="{ href, route, navigate, isActive, isExactActive }"
            >
                <li :class="[isActive && 'active', isExactActive && 'exact-active']">
                    <a :href="href" @click="navigate"><i class="material-icons left">person_outline</i>Профиль</a>
                </li>
            </router-link>
            <li>
                <a href="#" @click.prevent="logout"><i class="material-icons left">logout</i>Выход</a>
            </li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: 'ProfileMenu',
        methods: {
            logout() {
                this.$store.dispatch('AuthModule/userSignOut')
            }
        },
        mounted() {
            let profileDropdown = this.$M.Dropdown.init(
                this.$refs.toggleProfile,
                {
                    alignment: 'left',
                    constrainWidth: false,
                    coverTrigger: false,
                    inDuration: 400,
                    outDuration: 400
                }
            )

            this.$once('hook:beforeDestroy', function() {
                if (profileDropdown && profileDropdown.destroy)
                    profileDropdown.destroy()
            })
        }
    }
</script>