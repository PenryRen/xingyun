package com.mindskip.wdd.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.service.VmWareService;
import com.mindskip.wdd.viewmodel.excel.ExcelResult;
import com.mindskip.wdd.viewmodel.ueit.VmWareImport;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class VmWareListener extends AnalysisEventListener<VmWareImport>  {

    private List<VmWareImport> vmWareList;
//    private BeanValidator beanValidator;
    private VmWareService vmWareService;

    /**
     * 用于处理每行数据的导入操作
     * 根据校验结果进行相应的处理，包括导入数据库或记录校验错误信息
     * @param vmWareImport
     * @param analysisContext
     */
    @Override
    public void invoke(VmWareImport vmWareImport, AnalysisContext analysisContext) {
        //判空操作 如果vmWareImport为null，则直接返回
        if (null == vmWareImport) return;
        // 将数据导入到数据库中，将vmWareImport添加到vmWareList中
        vmWareService.vmWareImport(vmWareImport);
        vmWareList.add(vmWareImport);
    }

    /**
     * 重写方法
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
