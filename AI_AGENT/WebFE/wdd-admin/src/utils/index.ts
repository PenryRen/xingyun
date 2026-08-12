import {JSEncrypt} from "jsencrypt";
import $ from 'jquery'

/**
 * Check if an element has a class
 * @param {HTMLElement} elm
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele: HTMLElement, cls: string) {
    return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}

/**
 * Add class to element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function addClass(ele: HTMLElement, cls: string) {
    if (!hasClass(ele, cls)) ele.className += ' ' + cls;
}

/**
 * Remove class from element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function removeClass(ele: HTMLElement, cls: string) {
    if (hasClass(ele, cls)) {
        const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
        ele.className = ele.className.replace(reg, ' ');
    }
}

export function mix(color1: string, color2: string, weight: number) {
    weight = Math.max(Math.min(Number(weight), 1), 0);
    const r1 = parseInt(color1.substring(1, 3), 16);
    const g1 = parseInt(color1.substring(3, 5), 16);
    const b1 = parseInt(color1.substring(5, 7), 16);
    const r2 = parseInt(color2.substring(1, 3), 16);
    const g2 = parseInt(color2.substring(3, 5), 16);
    const b2 = parseInt(color2.substring(5, 7), 16);
    const r = Math.round(r1 * (1 - weight) + r2 * weight);
    const g = Math.round(g1 * (1 - weight) + g2 * weight);
    const b = Math.round(b1 * (1 - weight) + b2 * weight);
    const rStr = ('0' + (r || 0).toString(16)).slice(-2);
    const gStr = ('0' + (g || 0).toString(16)).slice(-2);
    const bStr = ('0' + (b || 0).toString(16)).slice(-2);
    return '#' + rStr + gStr + bStr;
}


export function rsaEncryptStr(publicKey, text) {
    const pubKey = `-----BEGIN PUBLIC KEY-----${publicKey}-----END PUBLIC KEY-----`
    const jsEncrypt = new JSEncrypt();
    jsEncrypt.setPublicKey(pubKey);
    return jsEncrypt.encrypt(text)
}


export function setEmptyToNull(object, parameter) {
    const value = object[parameter]
    if (value !== null && value.length === 0) {
        object[parameter] = null
    }
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

export function disabledDate(time) {
    const now = new Date()
    now.setDate(now.getDate() - 1)
    return time <= new Date(now)
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


