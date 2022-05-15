package com.tuanche.web.util;

import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2021-09-17 17:40
 */
public class ExportExcelMultipleSheet<E> {

    private String sheetName;
    private Map<String, String> titleValueMap;
    private List<E> list;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Map<String, String> getTitleValueMap() {
        return titleValueMap;
    }

    public void setTitleValueMap(Map<String, String> titleValueMap) {
        this.titleValueMap = titleValueMap;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
