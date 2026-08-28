package com.mindskip.wdd.viewmodel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class VmTypeConverter implements Converter<String> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) throws Exception {
        return cellData.getStringValue();
    }

    @Override
    public CellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty,
                                          GlobalConfiguration globalConfiguration) throws Exception {
        String convertedValue;
        switch (value) {
            case "00":
                convertedValue = "主机";
                break;
            case "01":
                convertedValue = "子机";
                break;
            default:
                convertedValue = value;
                break;
        }
        return new CellData<>(convertedValue);
    }
}
