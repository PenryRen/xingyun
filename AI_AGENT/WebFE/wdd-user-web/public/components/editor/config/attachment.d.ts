/**
 * @description 附件相关的配置
 * @author hutianhao
 */
import Editor from '../editor/index';
import { EMPTY_FN } from '../utils/const';
import { ResType } from '../menus/attachment/upload-attachment';
export declare type UploadAttachmentHooksType = {
    before?: (xhr: XMLHttpRequest, editor: Editor, files: File[]) => {
        prevent: boolean;
        msg: string;
    } | void;
    success?: (xhr: XMLHttpRequest, editor: Editor, result: ResType) => void;
    fail?: (xhr: XMLHttpRequest, editor: Editor, err: ResType | string) => void;
    error?: (xhr: XMLHttpRequest, editor: Editor) => void;
    timeout?: (xhr: XMLHttpRequest, editor: Editor) => void;
    customInsert?: (inserAttachment: (this: Editor, src: string) => void, result: ResType, editor: Editor) => void;
};
declare const _default: {
    onlineAttachmentCheck: (attachment: string) => string | boolean;
    onlineAttachmentCallback: typeof EMPTY_FN;
    showLinkAttachment: boolean;
    uploadAttachmentAccept: string[];
    uploadAttachmentServer: string;
    uploadAttachmentMaxSize: number;
    uploadAttachmentName: string;
    uploadAttachmentParams: {};
    uploadAttachmentParamsWithUrl: boolean;
    uploadAttachmentHeaders: {};
    uploadAttachmentHooks: {};
    uploadAttachmentTimeout: number;
    withAttachmentCredentials: boolean;
    customUploadAttachment: null;
    customInsertAttachment: null;
};
export default _default;
