<template>
    <div class="panel-container flex-box">
        <panel-navigation />
        <main class="main-container">
            <transition name="fade">
                <router-view />
            </transition>
        </main>
        <panel-sidebar />
    </div>
</template>

<script>
    import PanelNavigation from "../parts/PanelNavigation"
    import PanelSidebar from "../parts/PanelSidebar"

    export default {
        name: 'AdminLayout',
        components: {
            PanelNavigation,
            PanelSidebar
        }
    }
</script>