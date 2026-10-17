/**
 * @description 音频相关的配置
 * @author hutianhao
 */
import Editor from '../editor/index';
import { EMPTY_FN } from '../utils/const';
import { ResType } from '../menus/audio/upload-audio';
export declare type UploadAudioHooksType = {
    before?: (xhr: XMLHttpRequest, editor: Editor, files: File[]) => {
        prevent: boolean;
        msg: string;
    } | void;
    success?: (xhr: XMLHttpRequest, editor: Editor, result: ResType) => void;
    fail?: (xhr: XMLHttpRequest, editor: Editor, err: ResType | string) => void;
    error?: (xhr: XMLHttpRequest, editor: Editor) => void;
    timeout?: (xhr: XMLHttpRequest, editor: Editor) => void;
    customInsert?: (inserAudio: (this: Editor, src: string) => void, result: ResType, editor: Editor) => void;
};
declare const _default: {
    onlineAudioCheck: (audio: string) => string | boolean;
    onlineAudioCallback: typeof EMPTY_FN;
    showLinkAudio: boolean;
    uploadAudioAccept: string[];
    uploadAudioServer: string;
    uploadAudioMaxSize: number;
    uploadAudioName: string;
    uploadAudioParams: {};
    uploadAudioParamsWithUrl: boolean;
    uploadAudioHeaders: {};
    uploadAudioHooks: {};
    uploadAudioTimeout: number;
    withAudioCredentials: boolean;
    customUploadAudio: null;
    customInsertAudio: null;
};
export default _default;
