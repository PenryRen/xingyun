import axios from 'axios';
import {ElMessage, ElLoading} from 'element-plus';
import useUserStore from '@/store/modules/user';
import Cookies from 'js-cookie'
import {localStorage} from '@/utils/storage';

const isLocalDevelopment = import.meta.env.VITE_APP_LOCAL_DEVELOPMENT === 'true';

const post = function (url, params) {
    const query = {
        url: url,
        method: 'post',
        withCredentials: true,
        timeout: 30000,
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'request-ajax': true,
            'token': Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY)
        }
    }
    return request(false, query)
}

const postSync = async function (url: any, params: any) {
    const query: any = {
        url: url,
        method: 'post',
        withCredentials: true,
        timeout: 30000,
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'request-ajax': true,
            token: Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY)
        }
    }
    const res = await axios.request(query).catch(e => {
        ElMessage({
            message: e.message,
            type: 'error'
        });
        return Promise.reject(e.message)
    })
    if (!isLocalDevelopment) {
      useUserStore().removeExpiration();
      if (res.data.code === 403) {
        useUserStore().setExpiration("true",res.data.message);
      }
    }
    if (res.data.code === 451) {
      return Promise.reject(res.data);
    }
    return res.data
}

const postWithLoadTip = function (url, params) {
    const query = {
        url: url,
        method: 'post',
        withCredentials: true,
        timeout: 30000,
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'request-ajax': true,
            'token': Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY)
        }
    }
    return request(true, query)
}

const postWithTimeOut = function (url, params) {
    const query = {
        url: url,
        method: 'post',
        withCredentials: true,
        timeout: 3000000,
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'request-ajax': true,
            'token': Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY)
        }
    }
    return request(true, query)
}

const postWithOutLoadTip = function (url, params) {
    const query = {
        url: url,
        method: 'post',
        withCredentials: true,
        timeout: 30000,
        data: params,
        headers: {
            'Content-Type': 'application/json',
            'request-ajax': true,
            'token': Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY)
        }
    }
    return request(false, query)
}

const get = function (url, params) {
    const query = {
        url: url,
        method: 'get',
        withCredentials: true,
        timeout: 30000,
        params: params,
        headers: {'request-ajax': true, 'token': Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY)}
    }
    return request(false, query)
}

const form = function (url, params) {
    const query = {
        url: url,
        method: 'post',
        withCredentials: true,
        timeout: 30000,
        data: params,
        headers: {
            'Content-Type': 'multipart/form-data',
            'request-ajax': true,
            'token': Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY)
        }
    }
    return request(false, query)
}


const request = function (loading, query) {
    let loadingShow: any = {}
    if (loading) {
        loadingShow = ElLoading.service({
            lock: false,
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.5)'
        })
    }
    return axios.request(query)
        .then(res => {
            if (!isLocalDevelopment) {
              useUserStore().removeExpiration();
            }
            if (res.data.code === 400 || res.data.code === 401) {
              useUserStore().removeLoginCookies()
              localStorage.clear();
              window.location.href = '/';
            } else if (res.data.code === 403) {
              if (isLocalDevelopment) {
                return Promise.reject(res.data)
              }
              useUserStore().setExpiration("true",res.data.message);
            } else if (res.data.code === 451) {
                return Promise.reject(res.data)
            } else if (res.data.code === 500) {
                return Promise.reject(res.data)
            } else if (res.data.code === 501) {
                return Promise.reject(res.data)
            } else if (res.data.code === 502) {
                useUserStore().removeLoginCookies()
                localStorage.clear();
                window.location.href = '/';
                return Promise.reject(null)
            } else {
                return Promise.resolve(res.data)
            }
        })
        .catch(e => {
            if (e !== null) {
                ElMessage({
                    message: e.message,
                    type: 'error'
                });
                return Promise.reject(e.message)
            } else {
                return Promise.reject(null)
            }
        }).finally(() => {
            if (loading) {
                loadingShow.close()
            }
        })
}


export {
    post,
    postWithLoadTip,
    postWithOutLoadTip,
    postWithTimeOut,
    postSync,
    get,
    form
}



