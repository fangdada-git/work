package com.tuanche.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tuanche.directselling.enums.BrankAccountType;
import com.tuanche.directselling.model.FissionDealerSettlementAccount;
import com.tuanche.manubasecenter.api.CompanyBaseService;
import com.tuanche.manubasecenter.model.CsCompany;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：HuangHao
 * @CreatTime 2020-06-01 11:23
 */
@Service
public class DealerSettlementAccountExcelService {

    @Reference
    CompanyBaseService companyBaseService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DealerSettlementAccountExcelService.class);
    /**
     * 获取并解析excel文件，返回一个二维集合
     * @param file 上传的文件
     * @return 二维集合（第一重集合为行，第二重集合为列，每一行包含该行的列集合，列集合包含该行的全部单元格的值）
     */
    public ExcelList<FissionDealerSettlementAccount> analysis(MultipartFile file,Integer fissionId, String fissionActivityName, String createName, Map<Integer,Integer> existsCardsMap) {
        ExcelList<FissionDealerSettlementAccount> excelList = new ExcelList();
//        List<List<String>> list = new ArrayList<>();
        List<String> invalidData = new ArrayList<>();
        List<FissionDealerSettlementAccount> accountList = new ArrayList<>();

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
            Map<Integer,Integer> dealerMap = new HashMap<>(total);
            //从第二行开始获取
            for (int i = 1; i < total; i++) {
                FissionDealerSettlementAccount account = new FissionDealerSettlementAccount();
                //循环获取工作表的每一行
                Row sheetRow = sheet.getRow(i);
                Integer dealerId = null;
                if(sheetRow != null){
                    Cell activityNameCell = sheetRow.getCell(0);
                    String activityName = getStringValue(activityNameCell);;
                    if(activityName == null || !fissionActivityName.equals(activityName)){
                        invalidData.add("第"+(i+1)+"行的活动名称与本场次活动不相同");
                        continue;
                    }
                    Cell dealerNameCell = sheetRow.getCell(1);
                    String dealerName = getStringValue(dealerNameCell);;
                    if(dealerName == null){
                        invalidData.add("第"+(i+1)+"行的经销商全称为空");
                        continue;
                    }else{
                        CsCompany csCompany = new CsCompany();
                        csCompany.setCompanyName(dealerName);
                        List<CsCompany> csCompanies = companyBaseService.getCsCompanyByName(csCompany);
                        if (csCompanies == null || csCompanies.isEmpty()) {
                            invalidData.add("第" + (i + 1) + "行的经销商全称不正确");
                            continue;
                        } else if (existsCardsMap != null && existsCardsMap.get(csCompanies.get(0).getId()) != null) {
                            invalidData.add("第" + (i + 1) + "行的经销商已经设置过结算账户");
                            continue;
                        } else {
                            dealerId = csCompanies.get(0).getId();
                            account.setDealerId(dealerId);
                            account.setDealerName(dealerName);
                        }
                    }
                    Cell bankCardNumberCell = sheetRow.getCell(2);
                    String msg= "第"+(i+1)+"行的门店对公账号为空";
                    String bankCardNumber = getStringValue(bankCardNumberCell);
                    if(bankCardNumber == null){
                        invalidData.add(msg);
                        continue;
                    }else{
                        account.setBankCardNumber(bankCardNumber);
                    }
                    Cell bankNameCell = sheetRow.getCell(3);
                    String bankName = getStringValue(bankNameCell);
                    if(bankName == null){
                        invalidData.add("第"+(i+1)+"行的开户行全称为空");
                        continue;
                    }else{
                        account.setBankName(bankName);
                    }
                    //开户行地址
                    Cell bankAddressCell = sheetRow.getCell(4);
                    String bankAddress = getStringValue(bankAddressCell);
                    if(bankAddress != null){
                        account.setBankAddress(bankAddress);
                    }
                    if(dealerMap.get(dealerId) != null){
                        invalidData.add("第"+(i+1)+"行的经销商与"+dealerMap.get(dealerId)+"行的经销商重复了");
                        continue;
                    }else{
                        dealerMap.put(dealerId, (i+1));
                    }
                    account.setAccountType(BrankAccountType.ACCOUNT_TYPE_1.getCode());
                    account.setFissionId(fissionId);
                    accountList.add(account);
                }
                //关闭资源
                workbook.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        excelList.setData(accountList);
        excelList.setInvalidData(invalidData);
        excelList.setTotal(total);
        return excelList;
    }

    public String getStringValue(Cell cell){
        if(cell != null){
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String strValue = cell.getStringCellValue()!=null?cell.getStringCellValue().trim():null;
            if(StringUtils.isEmpty(strValue)){
                return null;
            }else{
                return strValue;
            }
        }else{
            return null;
        }
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
