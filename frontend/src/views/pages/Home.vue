<template>
    <main class="main-container">
        <section class="slider-container">
            <div class="container">
                <div class="flex-box">
                    <div class="main-page-title">
                        <h3>Безопасное бронирование без посредников, комиссий и переплат</h3>
                        <div class="slogan">Дизайнерские апартаменты в лучших районах</div>
                    </div>
                    <div class="slider" ref="mainSlider">
                        <ul class="slides">
                            <li>
                                <img src="../../assets/images/slider/slide_001.jpg" alt="">
                                <div class="caption center-align">
                                    <h3>Квартира 1</h3>
                                    <h5 class="light grey-text text-lighten-3">Слоган, краткое описание, цена.</h5>
                                    <a href="#">Подробнее</a>
                                </div>
                            </li>
                            <li>
                                <img src="../../assets/images/slider/slide_002.jpg" alt="">
                                <div class="caption center-align">
                                    <h3>Квартира 2</h3>
                                    <h5 class="light grey-text text-lighten-3">Слоган, краткое описание, цена.</h5>
                                    <a href="#">Подробнее</a>
                                </div>
                            </li>
                            <li>
                                <img src="../../assets/images/slider/slide_001.jpg" alt="">
                                <div class="caption center-align">
                                    <h3>Квартира 1</h3>
                                    <h5 class="light grey-text text-lighten-3">Слоган, краткое описание, цена.</h5>
                                    <a href="#">Подробнее</a>
                                </div>
                            </li>
                            <li>
                                <img src="../../assets/images/slider/slide_002.jpg" alt="">
                                <div class="caption center-align">
                                    <h3>Квартира 2</h3>
                                    <h5 class="light grey-text text-lighten-3">Слоган, краткое описание, цена.</h5>
                                    <a href="#">Подробнее</a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </section>
        <apartments-popular />
        <advantages />
        <last-news />
        <partners />
        <section class="feedback-container">
            <span class="decoration-block-title">/Контакты</span>
            <div class="container">
                <feedback block-tite="05. Связаться с нами" block-subtitle="Какой-то подзаголовок" />
            </div>
        </section>
    </main>
</template>

<script>
    import ApartmentsPopular from "../../parts/ApartmentsPopular";
    import Advantages from "../../parts/Advantages";
    import LastNews from "../../parts/LastNews";
    import Partners from "../../parts/Partners";
    import Feedback from "../../parts/Feedback";

    export default {
        name: 'Home',
        components: {
            ApartmentsPopular,
            Advantages,
            LastNews,
            Partners,
            Feedback
        },
        data: () => ({

        }),
        created() {
            this.getHomePageInformation();
        },
        mounted() {
            let mainSlider = this.$M.Slider.init(
                this.$refs.mainSlider,
                {
                    indicators: true,
                    //height: 500,
                    duration: 500,
                    interval: 6000
                }
            )

            this.$once('hook:beforeDestroy', function() {
                if (mainSlider && mainSlider.destroy)
                    mainSlider.destroy()
            })
        },
        methods: {
            getHomePageInformation() {
                //axios.get(webApiUrl, { headers: {"Authorization" : `Bearer ${tokenStr}`} });
                this.$axios
                    .get('/test/test-repo')
                    .then((response) => {
                        console.log('Get response: ', response.data);
                        ///this.data = response.data;
                        console.log(this)
                    })
                    .catch((error) => {
                        //this.alert = true;
                        console.error(error);
                    });
            }
        }
    }
</script>
