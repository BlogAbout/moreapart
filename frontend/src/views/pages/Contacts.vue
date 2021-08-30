<template>
    <main class="main-container">
        <section class="page-content">
            <span class="decoration-block-title">/Контакты</span>
            <div class="container">
                <h1 class="page-title">Контакты</h1>
                <div class="page-description">
                    <p>Телефон: <a href="tel:+79384377037">+7 (938) 437-70-37</a></p>
                    <p>E-mail: </p>
                    <p>ИП ШКРЕДОВА ТАТЬЯНА МИХАЙЛОВНА<br />ИНН: 246308437476<br />ОГРНИП ‪319203600019582</p>
                </div>
                <feedback block-tite="Связаться с нами" />
            </div>
        </section>
    </main>
</template>

<script>
    import Feedback from "../../parts/Feedback";

    export default {
        name: 'Contacts',
        components: {
            Feedback
        }
    }
</script>