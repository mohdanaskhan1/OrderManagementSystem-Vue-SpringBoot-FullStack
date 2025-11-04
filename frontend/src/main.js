import { createApp } from 'vue'
import App from './App.vue'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'

import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

import '@mdi/font/css/materialdesignicons.css'

import { mdi, aliases } from 'vuetify/iconsets/mdi'

const vuetify = createVuetify({
    components,
    directives,
    icons: {
        defaultSet: 'mdi',
        aliases,
        sets: { mdi },
        theme: {
            themes: {
                light: {
                    colors: {
                        primary: '#3F51B5',
                        secondary: '#607D8B',
                    },
                },
            },
        },
    },
})

createApp(App).use(vuetify).mount('#app')