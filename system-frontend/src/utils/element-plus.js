import ElementPlus from 'element-plus'
import localeEN from 'element-plus/lib/locale/lang/en'
import 'element-plus/dist/index.css'
import * as Icons from '@element-plus/icons-vue'

export default (app) => {
    app.use(ElementPlus, { locale:localeEN })
    for(let name in Icons) {
      app.component(name, Icons[name])
    }
};
