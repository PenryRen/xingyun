import {JSEncrypt} from "jsencrypt";
import CryptoJS from 'crypto-js'
import $ from 'jquery'


export function rsaEncryptStr(publicKey, text) {
    const pubKey = `-----BEGIN PUBLIC KEY-----${publicKey}-----END PUBLIC KEY-----`
    const jsEncrypt = new JSEncrypt();
    jsEncrypt.setPublicKey(pubKey);
    return jsEncrypt.encrypt(text)
}

export function rsaDecryptStr(privateKey, text) {
    const priKey = `-----BEGIN RSA PRIVATE KEY-----${privateKey}-----END RSA PRIVATE KEY-----`
    const jsEncrypt = new JSEncrypt();
    jsEncrypt.setPrivateKey(priKey);
    return jsEncrypt.decrypt(text)
}


export function aesEncryptStr(keyStr, textStr) {
    const key = CryptoJS.enc.Utf8.parse(keyStr);
    const text = CryptoJS.enc.Utf8.parse(textStr);
    return CryptoJS.AES.encrypt(text, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    }).toString()
}


export function aesDecryptStr(keyStr, textStr) {
    const key = CryptoJS.enc.Utf8.parse(keyStr);
    return CryptoJS.AES.decrypt(textStr, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    }).toString(CryptoJS.enc.Utf8)
}

export function paperEncrypt(publicKey, valueObject) {
    const key = randomKey()
    const valueStr = aesEncryptStr(key, JSON.stringify(valueObject))
    const keyStr = rsaEncryptStr(publicKey, key)
    return {key: keyStr, value: valueStr}
}


export function paperDecrypt(privateKey, keyStr, valueStr) {
    const aesKey = rsaDecryptStr(privateKey, keyStr)
    const content = aesDecryptStr(aesKey, valueStr)
    return JSON.parse(content)
}


export function formatSeconds(theTime: number) {
    let theTime1 = 0
    let theTime2 = 0
    if (theTime > 60) {
        theTime1 = parseInt((theTime / 60) as any)
        theTime = parseInt((theTime % 60) as any)
        if (theTime1 > 60) {
            theTime2 = parseInt((theTime1 / 60) as any)
            theTime1 = parseInt((theTime1 % 60) as any)
        }
    }
    let result = `${parseInt(theTime as any)}秒`
    if (theTime1 > 0) {
        result = `${parseInt(theTime1 as any)}分${result}`
    }
    if (theTime2 > 0) {
        result = `${parseInt(theTime2 as any)}小时${result}`
    }
    return result
}

function action(e) {
    e.preventDefault()
}

export function enablePageEvent() {
    document.removeEventListener('contextmenu', action)
    document.removeEventListener('copy', action)
    document.removeEventListener('paste', action)
    document.removeEventListener('cut', action)
}

export function disablePageEvent() {
    document.addEventListener('contextmenu', action)
    document.addEventListener('copy', action)
    document.addEventListener('paste', action)
    document.addEventListener('cut', action)
}

export function setPageTitle(title) {
    document.title = `${title} | ${import.meta.env.VITE_APP_SYSTEM_NAME}`
}

export function clearTagP(content) {
    try {
        const contentEl = $(content)
        if (contentEl.length === 1) {
            const firstItem = contentEl[0]
            if (firstItem.nodeName.toLowerCase() === 'p') {
                return firstItem.innerHTML
            }
        }
        return content
    } catch (err) {
        return content
    }
}

export function numberConvert(number: string) {
    if (null !== number) {
        if (typeof number === 'string') {
            if (number.length > 0) {
                return Number(number)
            }
        }
    }
    return null;
}

export function randomKey() {
    const str = 'abcdefghijklmnopqrstuvwxyz9876543210'
    let tmp = ''
    let i = 0
    const l = str.length
    for (i = 0; i < 16; i++) {
        tmp += str.charAt(Math.floor(Math.random() * l))
    }
    return tmp
}
