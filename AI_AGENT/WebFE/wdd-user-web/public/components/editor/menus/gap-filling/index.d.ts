/**
 * @description 插入空
 * @author mindskip
 */
import BtnMenu from '../menu-constructors/BtnMenu';
import Editor from '../../editor/index';
import { MenuActive } from '../menu-constructors/Menu';
declare class gapFilling extends BtnMenu implements MenuActive {
    constructor(editor: Editor);
    /**
     * 菜单点击事件
     */
    clickHandler(): void;
    /**
     * 创建 gapFilling
     */
    private createGapFilling;
    private generateUUID;
    /**
     * 尝试修改菜单激活状态
     */
    tryChangeActive(): void;
}
export default gapFilling;
