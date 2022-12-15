import {createStore} from 'vuex';

export default createStore({
    state: {
        tagsList: [],
        isCollapsed: false,
        loginUser: {userId: "", uuid: "", loginIp: "", loginLocation: "", browser: "", os: ""},
        user: {id: "", username: "", displayName: "", deptId: "", email: "", phone: "", lastLoginIp: "", lastLoginDate: "", status: ""},
        role: {id: "", name: "", status: ""},
        perms: [],
        dept: {id: "", name: "", type: "", leader: "", email: "", address: ""},
    },
    mutations: {
        handleCollapse(state, data) {
            state.isCollapsed = data;
        },
        setTagsItem(state, data) {
            state.tagsList.push(data)
        },
        delTagsItem(state, data) {
            state.tagsList.splice(data.index, 1);
        },
        closeAllTags(state) {
            state.tagsList = []
        },
        closeOtherTags(state, data) {
            state.tagsList = data;
        },
    },
    actions: { },
    modules: { },
});