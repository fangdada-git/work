package com.tuanche.web.util;

import com.tuanche.directselling.model.GiftRefuelingcard;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-01 11:23
 */
public class RefuelCardExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefuelCardExcelUtil.class);
    /**
     * 获取并解析excel文件，返回一个二维集合
     * @param file 上传的文件
     * @return 二维集合（第一重集合为行，第二重集合为列，每一行包含该行的列集合，列集合包含该行的全部单元格的值）
     */
    public static ExcelList<GiftRefuelingcard> analysis(MultipartFile file, String createName, Map<String,Integer> existsCardsMap) {
        ExcelList<GiftRefuelingcard> excelList = new ExcelList();
//        List<List<String>> list = new ArrayList<>();
        List<String> invalidData = new ArrayList<>();
        List<GiftRefuelingcard> cards = new ArrayList<>();
        //获取文件名称
        String fileName = file.getOriginalFilename();
        int total = 0;
        try {
            //获取输入流
            InputStream in = file.getInputStream();
            //判断excel版本
            Workbook workbook = null;
            if (judegExcelEdition(fileName)) {
                workbook = new XSSFWorkbook(in);
            } else {
                workbook = new HSSFWorkbook(in);
            }
            String invalidMsg = null;
            //获取第一张工作表
            Sheet sheet = workbook.getSheetAt(0);
            total = sheet.getPhysicalNumberOfRows();
            //从第二行开始获取
            for (int i = 1; i < total; i++) {
                //循环获取工作表的每一行
                Row sheetRow = sheet.getRow(i);
                if(sheetRow != null){
                //循环获取每一列
                    List<String> cell = new ArrayList<>();
                    //只获取第一列的值
                    for (int j = 0; j < 1; j++) {
                        Cell cell1 = sheetRow.getCell(j);
                        if (cell1 != null){
                            String value = cell1.getStringCellValue();
                            if(StringUtils.isEmpty(value) && StringUtils.isEmpty(value=value.trim())){
                                invalidData.add("第"+(i+1)+"行第"+(j+1)+"列的卡号为空");
                            }else{
                                //检查在数据库中是否存在
                                if(existsCardsMap != null && existsCardsMap.get(value) != null){
                                    invalidData.add("第"+(i+1)+"行第"+(j+1)+"列的卡号在数据库中已经存在");
                                }else{
                                    //将每一个单元格的值装入列集合
//                                cell.add(value);
                                    GiftRefuelingcard refuelingcard = new GiftRefuelingcard();
                                    refuelingcard.setRefuelingCode(value);
                                    refuelingcard.setCreateName(createName);
                                    cards.add(refuelingcard);
                                }
                            }
                        }else{
                            invalidData.add("第"+(i+1)+"行第"+(j+1)+"列的卡号为空");
                        }
                    }
                }
                //关闭资源
                workbook.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        excelList.setData(cards);
        excelList.setInvalidData(invalidData);
        excelList.setTotal(total);
        return excelList;
    }
    /**
     * 判断上传的excel文件版本（xls为2003，xlsx为2017）
     * @param fileName 文件路径
     * @return excel2007及以上版本返回true，excel2007以下版本返回false
     */
    private static boolean judegExcelEdition(String fileName){
        if (fileName.matches("^.+\\.(?i)(xls)$")){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 是否是excel
     * @author HuangHao
     * @CreatTime 2020-06-01 14:28
     * @param fileName
     * @return boolean
     */
    public static boolean isExcel(String fileName){
        if (fileName==null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx"))){
            return false;
        }else {
            return true;
        }
    }

    public static class ExcelList<T>{
        //总行数
        private int total;
        //有效数据
        private List<T> data;
        //无效数据
        private List<String> invalidData;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<T> getData() {
            return data;
        }

        public void setData(List<T> data) {
            this.data = data;
        }

        public List<String> getInvalidData() {
            return invalidData;
        }

        public void setInvalidData(List<String> invalidData) {
            this.invalidData = invalidData;
        }
    }

}
