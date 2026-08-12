/**
 * @description 上传音频
 * @author mindskip
 */
import Editor from '../../editor/index';
declare type ResData = {
    url: string;
};
export declare type ResType = {
    errno: number | string;
    data: ResData;
};
declare class UploadAudio {
    private editor;
    constructor(editor: Editor);
    /**
     * 上传音频
     * @param files 文件列表
     */
    uploadAudio(files: FileList | File[]): void;
    /**
     * 往编辑器区域插入音频
     * @param url 音频访问地址
     */
    insertAudio(url: string): void;
}
export default UploadAudio;
