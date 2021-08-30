<template>
    <div class="cropper-wrap">
        <h4>Миниатюра</h4>
        <div class="flex-box flex-top">
            <div class="cropper-area">
                <div class="cropper-block" :style="cropperBlockStyle">
                    <input
                            type="file"
                            ref="file"
                            hidden
                            accept="image/jpeg,image/jpg,image/png"
                            @change="readImage"
                    />
                    <div class="canvas-box" v-if="originalImg">
                        <canvas
                                :width="cropperCanvasSize.width"
                                :height="cropperCanvasSize.height"
                                ref="cropper"
                                :class="{'movement':movement}"
                        ></canvas>
                    </div>
                    <div v-else>
                        <a @click="selectPic" class="waves-effect waves-light btn">
                            <i class="material-icons left">add_photo_alternate</i>Загрузить изображение
                        </a>
                        <p class="tips">{{defaultTips}}</p>
                    </div>
                </div>
                <div v-if="originalImg" class="links">
                    <p class="tips">{{defaultTips}}</p>
                    <a @click="selectPic" class="waves-effect waves-light btn">
                        <i class="material-icons left">collections</i>Заменить
                    </a>
                    <a @click="clear" class="waves-effect waves-light btn">
                        <i class="material-icons left">delete_sweep</i>Удалить
                    </a>
                </div>
            </div>
            <div v-if="originalImg" class="preview-area">
                <div class="preview-block" :style="previewBlockStyle">
                    <canvas :width="previewSize" :height="previewSize" ref="preview"></canvas>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'cropper',
        props: {
            cropperSize: {
                type: Number,
                default: 400
            },
            previewSize: {
                type: Number,
                default: 200
            },
            mask: {
                default: 'dark'
            },
            shape:{
                default: 'square'
            },
            defaultTips: {
                type: String,
                default: 'Вы можете загрузить изображение в формате JPG и PNG'
            },
            uploadProps: {
                type: Object,
                validator(value) {
                    if (!Object.keys(value).includes('action'))
                        throw 'Выберите изображение для загрузки'

                    return true
                }
            }
        },
        data() {
            return {
                originalImg: null,
                originalImgData: {
                    src: '',
                    width: 0,
                    height: 0
                },
                cropperCanvasSize: {
                    width: this.cropperSize,
                    height: this.cropperSize
                },
                cropperLocation: {
                    X: 0,
                    Y: 0
                },
                selectRadius: this.previewSize / 2,
                movement: false
            }
        },
        computed: {
            cropperBlockStyle() {
                return {
                    width: `${this.cropperSize}px`
                }
            },
            previewBlockStyle() {
                return {
                    width: `${this.previewSize}px`,
                    height: `${this.previewSize}px`,
                    borderRadius: 0
                }
            },
            circleCenter: {
                get() {
                    return {
                        X: this.cropperLocation.X + this.selectRadius,
                        Y: this.cropperLocation.Y + this.selectRadius
                    }
                },
                set(newVal) {
                    let X = newVal.X - this.selectRadius
                    let Y = newVal.Y - this.selectRadius
                    if (this.cropperCanvasSize.width > 2 * this.selectRadius) {
                        X < 0 && (X = 0)
                        X + 2 * this.selectRadius > this.cropperCanvasSize.width &&
                        (X = this.cropperCanvasSize.width - 2 * this.selectRadius)
                    }
                    if (this.cropperCanvasSize.height > 2 * this.selectRadius) {
                        Y < 0 && (Y = 0);
                        Y + 2 * this.selectRadius > this.cropperCanvasSize.height &&
                        (Y = this.cropperCanvasSize.height - 2 * this.selectRadius)
                    }
                    this.cropperLocation = { X, Y }
                }
            }
        },
        methods: {
            selectPic() {
                this.$refs.file.click()
            },
            readImage(event) {
                const self = this
                const rawFile = event.target.files[0]
                const image = new Image()
                let file = {}
                image.src = URL.createObjectURL(rawFile)
                image.onload = () => {
                    self.originalImgData = {
                        src: image.src,
                        width: image.width,
                        height: image.height
                    }
                    file = { ...self.originalImgData, raw: rawFile }
                    let cropperFlag = true
                    this.$parent.beforeReadImg && (cropperFlag = this.$parent.beforeReadImg(file))
                    if (cropperFlag) {
                        self.originalImg = image
                        self.calcCanvasSize()
                        setTimeout(() => {
                            self.renderCropperImg()
                            self.drag()
                            this.upload()
                        }, 50)
                    }
                }
            },
            calcCanvasSize() {
                if (this.originalImgData.width > this.originalImgData.height) {
                    this.cropperCanvasSize.width = this.cropperSize
                    this.cropperCanvasSize.height = this.cropperSize * this.originalImgData.height / this.originalImgData.width
                } else {
                    this.cropperCanvasSize.height = this.cropperSize
                    this.cropperCanvasSize.width = this.cropperSize * this.originalImgData.width / this.originalImgData.height
                }
                this.cropperLocation = {
                    X: this.cropperCanvasSize.width / 2 - this.selectRadius,
                    Y: this.cropperCanvasSize.height / 2 - this.selectRadius
                }
            },
            renderCropperImg() {
                const canvas = this.$refs.cropper
                const ctx = canvas.getContext("2d")
                const tone = this.mask === "light" ? 255 : 0
                ctx.clearRect(
                    0,
                    0,
                    this.cropperCanvasSize.width,
                    this.cropperCanvasSize.height
                )
                ctx.save()
                ctx.drawImage(
                    this.originalImg,
                    0,
                    0,
                    this.cropperCanvasSize.width,
                    this.cropperCanvasSize.height
                )
                ctx.beginPath()
                ctx.fillStyle = `rgba(${tone},${tone},${tone},.5)`
                ctx.fillRect(
                    0,
                    0,
                    this.cropperCanvasSize.width,
                    this.cropperCanvasSize.height
                )
                ctx.save()
                ctx.beginPath()
                ctx.rect(
                    this.circleCenter.X - this.selectRadius,
                    this.circleCenter.Y - this.selectRadius,
                    2 * this.selectRadius,
                    2 * this.selectRadius
                )
                ctx.clip()
                ctx.closePath()
                ctx.drawImage(
                    this.originalImg,
                    0,
                    0,
                    this.cropperCanvasSize.width,
                    this.cropperCanvasSize.height
                )
                this.renderPreviewImg(canvas)
                ctx.fillStyle = `rgba(${tone},${tone},${tone},.5)`
                ctx.fillRect(
                    0,
                    0,
                    this.cropperCanvasSize.width,
                    this.cropperCanvasSize.height
                )
                ctx.beginPath()
                if (this.shape === "round") {
                    ctx.arc(
                        this.circleCenter.X,
                        this.circleCenter.Y,
                        this.selectRadius,
                        0,
                        Math.PI * 2,
                        true
                    )
                } else {
                    ctx.rect(this.cropperLocation.X, this.cropperLocation.Y,this.selectRadius * 2,this.selectRadius * 2)
                }

                ctx.clip()
                ctx.closePath()
                ctx.drawImage(
                    this.originalImg,
                    0,
                    0,
                    this.cropperCanvasSize.width,
                    this.cropperCanvasSize.height
                );
                ctx.restore()
            },
            renderPreviewImg(image) {
                const canvas = this.$refs.preview
                const ctx = canvas.getContext("2d")
                ctx.clearRect(0, 0, this.selectRadius * 2, this.selectRadius * 2)
                ctx.drawImage(
                    image,
                    this.cropperLocation.X,
                    this.cropperLocation.Y,
                    this.selectRadius * 2,
                    this.selectRadius * 2,
                    0,
                    0,
                    this.selectRadius * 2,
                    this.selectRadius * 2
                )
            },
            drag() {
                const self = this
                const canvas = this.$refs.cropper
                canvas.onmousedown = e => {
                    let [lastX, lastY] = [e.offsetX, e.offsetY]
                    self.movement = true
                    canvas.onmousemove = e => {
                        self.circleCenter = {
                            X:
                                self.cropperCanvasSize.width > 2 * self.selectRadius
                                    ? self.circleCenter.X + (e.offsetX - lastX)
                                    : self.cropperCanvasSize.width / 2,
                            Y:
                                self.cropperCanvasSize.height > 2 * self.selectRadius
                                    ? self.circleCenter.Y + (e.offsetY - lastY)
                                    : self.cropperCanvasSize.height / 2
                        }
                        self.renderCropperImg();
                        [lastX, lastY] = [e.offsetX, e.offsetY]
                    }
                    canvas.onmouseup = () => {
                        self.movement = false
                        canvas.onmousemove = null
                        canvas.onmouseup = null
                        this.upload()
                    }
                }
            },
            upload() {
                const canvas = this.$refs.preview
                if (!HTMLCanvasElement.prototype.toBlob) {
                    Object.defineProperty(HTMLCanvasElement.prototype, "toBlob", {
                        value(callback, type, quality) {
                            console.log(type, quality)
                            const canvas = this
                            setTimeout(() => {
                                let binStr = atob(canvas.toDataURL(type, quality).split(",")[1])
                                let len = binStr.length
                                let arr = new Uint8Array(len)
                                for (let i = 0; i < len; i++) {
                                    arr[i] = binStr.charCodeAt(i)
                                }
                                callback(new Blob([arr], { type: type || "image/png" }))
                            })
                        }
                    })
                }
                canvas.toBlob(blob => {
                    console.log(blob)
                    this.$emit('crop', blob)
                    /*let formData = new FormData()
                    formData.append('file', blob)
                    this.$axios.post(
                        '/inventory/test',
                        formData
                    )*/
                })
            },
            confirm() {
                if (this.$parent.confirm) {
                    this.$parent.confirm() && this.upload()
                    return
                }
                this.upload()
            },
            clear() {
                this.originalImg = null
                this.originalImgData = {
                    src: '',
                    width: 0,
                    height: 0
                },
                    this.$refs.cropper
                        .getContext("2d")
                        .clearRect(
                            0,
                            0,
                            this.cropperCanvasSize.width,
                            this.cropperCanvasSize.height
                        )
                this.$refs.preview.getContext("2d").clearRect(0, 0, this.previewSize, this.previewSize)
                this.$emit("clear")
            }
        }
    }
</script>