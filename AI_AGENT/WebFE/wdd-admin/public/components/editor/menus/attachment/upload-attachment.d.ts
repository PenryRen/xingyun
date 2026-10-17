/**
 * @description 上传附件
 * @author mindskip
 */
import Editor from '../../editor/index';
declare type ResData = {
    url: string;
    alt: string;
};
export declare type ResType = {
    errno: number | string;
    data: ResData;
};
declare class UploadAttachment {
    private editor;
    constructor(editor: Editor);
    /**
     * 上传附件
     * @param files 文件列表
     */
    uploadAttachment(files: FileList | File[]): void;
    /**
     * 往编辑器区域插入附件
     * @param url 附件访问地址
     */
    insertAttachment(src: string, alt?: string): void;
}
export default UploadAttachment;
