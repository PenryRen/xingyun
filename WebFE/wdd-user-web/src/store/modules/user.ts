import {defineStore} from 'pinia';
import {LoginResponseData} from '@/types/api/login';
import {UserState} from '@/types/store/user';

import {logout} from '@/api/login';
import Cookies from 'js-cookie';

const key = {
    userNameKey: 'ueit-user-web-user-name',
    realNameKey: 'ueit-user-web-real-name',
    expirationKey: 'ueit-user-web-expiration',
    errMsgKey: 'ueit-user-web-err-msg',
};

const useUserStore = defineStore({
    id: 'user',
    state: (): UserState => ({
        userName: Cookies.get(key.userNameKey) || '',
        realName: Cookies.get(key.realNameKey) || '',
        token: Cookies.get(import.meta.env.VITE_APP_TOKEN_KEY) || '',
        expiration: Cookies.get(key.expirationKey) === 'true'|| false,
        errMsg: Cookies.get(key.errMsgKey) || '',
    }),
    actions: {
        async RESET_STATE() {
            this.$reset();
        },
        login(loginData: LoginResponseData) {
            const expires = new Date(loginData.endTime);
            Cookies.set(key.userNameKey, loginData.userName, {expires: expires});
            Cookies.set(key.realNameKey, loginData.realName, {expires: expires});
            Cookies.set(import.meta.env.VITE_APP_TOKEN_KEY, loginData.token, {expires: expires});
            this.userName = loginData.userName;
            this.realName = loginData.realName;
            this.token = loginData.token;
        },
        updateRealName(realName: string) {
            Cookies.set(key.realNameKey, realName, {expires: 15});
            this.realName = realName;
        },
        removeLoginCookies() {
            Cookies.remove(key.userNameKey);
            Cookies.remove(key.realNameKey);
            Cookies.remove(import.meta.env.VITE_APP_TOKEN_KEY);
        },
        setExpiration(flag: string, msg: string) {
          Cookies.set(key.expirationKey, flag);
          Cookies.set(key.errMsgKey, msg);
        },
        removeExpiration(){
          Cookies.remove(key.expirationKey);
          Cookies.remove(key.errMsgKey);
        },
        logout() {
            return new Promise((resolve, reject) => {
                logout()
                    .then(() => {
                        this.removeLoginCookies();
                        this.RESET_STATE();
                        resolve(null);
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        },
        resetToken() {
            return new Promise((resolve) => {
                Cookies.remove(import.meta.env.VITE_APP_TOKEN_KEY);
                this.RESET_STATE();
                resolve(null);
            });
        }
    }
});

export default useUserStore;
