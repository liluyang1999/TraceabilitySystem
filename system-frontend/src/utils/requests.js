import axios from 'axios';

const create = axios.create({
    // baseURL: "http://localhost:8080",
    timeout: 30000,
});

create.interceptors.request.use(config => {
    config.headers["Content-Type"] = "application/json;charset=utf-8";
    if (config.url !== "/server/login/authenticate") {
        config.headers["Authorization"] = localStorage.getItem("token");
    }
    return config;
}, err => {
    return Promise.reject(err);
});

export default {create};
