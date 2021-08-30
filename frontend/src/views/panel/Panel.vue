<template>
    <section class="panel-content">
        <h1 class="page-title">Панель управления</h1>
    </section>
</template>

<script>
    export default {
        name: 'Profile',
        created() {
            this.getTest()
        },
        methods: {
            getTest() {
                /*this.$axios
                    .get('/test/user')
                    .then((response) => {
                        console.log('Get response: ', response.data);
                        ///this.data = response.data;
                        console.log(this)
                    })
                    .catch((error) => {
                        //this.alert = true;
                        console.error(error);
                    });*/
            }
        }
    }
</script>