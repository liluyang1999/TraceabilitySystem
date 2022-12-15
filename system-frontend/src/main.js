import {createApp} from 'vue';
import App from '@/App';
import router from '@/router';
import store from '@/store';
import initElementPlus from '@/utils/element-plus';

const app = createApp(App);

initElementPlus(app);

app.use(store).use(router).mount('#app');
