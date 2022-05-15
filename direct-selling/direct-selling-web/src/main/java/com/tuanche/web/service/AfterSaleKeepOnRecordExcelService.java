package com.tuanche.web.service;

import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.directselling.model.AfterSaleKeepOnRecord;
import com.tuanche.manubasecenter.util.ManeBaseConsoleConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 备案、流失用户导入
 */
@Service
public class AfterSaleKeepOnRecordExcelService {
    private static final Pattern num = Pattern.compile("[0-9]*");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final Pattern userNameReg = Pattern.compile("^[\u4E00-\u9FA5]{2,5}$");

    /**
     * 判断上传的excel文件版本（xls为2003，xlsx为2017）
     *
     * @param fileName 文件路径
     * @return excel2007及以上版本返回true，excel2007以下版本返回false
     */
    private static boolean judegExcelEdition(String fileName) {
        if (fileName.matches("^.+\\.(?i)(xls)$")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否是excel
     *
     * @param fileName
     * @return boolean
     * @author HuangHao
     * @CreatTime 2020-06-01 14:28
     */
    public static boolean isExcel(String fileName) {
        if (fileName == null || (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx"))) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获取并解析excel文件，返回一个二维集合
     *
     * @param file 上传的文件
     * @return 二维集合（第一重集合为行，第二重集合为列，每一行包含该行的列集合，列集合包含该行的全部单元格的值）
     */
    public ExcelList<AfterSaleKeepOnRecord> analysis(MultipartFile file, Integer dealerId, Integer activityId, String createName, Map<String, List<AfterSaleKeepOnRecord>> existsCardsMap, int userType) {
        ExcelList<AfterSaleKeepOnRecord> excelList = new ExcelList();
//        List<List<String>> list = new ArrayList<>();
        List<String> invalidData = new ArrayList<>();
        List<AfterSaleKeepOnRecord> accountList = new ArrayList<>();

        //获取文件名称
        String fileName = file.getOriginalFilename();
        int total = 0;
        //获取输入流
        InputStream in = null;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Workbook workbook = null;
        try {

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
            Map<String, List<Integer>> phoneDuplicateMap = new HashMap((int) (total / 0.75 + 1));
            Map<String, List<Integer>> licencePlateDuplicateMap = new HashMap((int) (total / 0.75 + 1));
            for (int i = 1; i < total; i++) {
                Row sheetRow = sheet.getRow(i);
                if (sheetRow != null) {
                    Cell userPhoneCell = sheetRow.getCell(1);
                    String userPhone = getStringValue(userPhoneCell);
                    Cell licencePlateCell = sheetRow.getCell(2);
                    String licencePlate = getStringValue(licencePlateCell);
                    if (userPhone != null) {
                        if (phoneDuplicateMap.get(userPhone) == null) {
                            phoneDuplicateMap.put(userPhone, new ArrayList());
                        }
                        phoneDuplicateMap.get(userPhone).add(i + 1);
                    }
                    if (licencePlate != null) {
                        if (licencePlateDuplicateMap.get(licencePlate) == null) {
                            licencePlateDuplicateMap.put(licencePlate, new ArrayList());
                        }
                        licencePlateDuplicateMap.get(licencePlate).add(i + 1);
                    }
                }
            }
            for (int i = 1; i < total; i++) {
                AfterSaleKeepOnRecord account = new AfterSaleKeepOnRecord();
                //循环获取工作表的每一行
                Row sheetRow = sheet.getRow(i);
                if (sheetRow != null) {
                    Cell userNameCell = sheetRow.getCell(0);
                    String userName = getStringValue(userNameCell);
                    Cell userPhoneCell = sheetRow.getCell(1);
                    String userPhone = getStringValue(userPhoneCell);
                    Cell licencePlateCell = sheetRow.getCell(2);
                    String licencePlate = getStringValue(licencePlateCell);
                    Cell originalBrandNameCell = sheetRow.getCell(3);
                    String originalBrandName = getStringValue(originalBrandNameCell);
                    Cell originalCarSeriesNameCell = sheetRow.getCell(4);
                    String originalCarSeriesName = getStringValue(originalCarSeriesNameCell);
                    Cell buyCarDateCell = sheetRow.getCell(5);
                    Date buyCarDate = getDateValue(buyCarDateCell);
                    Cell latestEnterDateCell = sheetRow.getCell(6);
                    Date latestEnterDate = getDateValue(latestEnterDateCell);
                    Cell dataSourceCell = sheetRow.getCell(7);
                    String dataSource = getStringValue(dataSourceCell);

                    if (userType == 0 && userPhone == null && licencePlate == null) {
                        invalidData.add("第" + (i + 1) + "行的手机号和车牌号必填其一");
                        continue;
                    }

                    if (userType == 1) {
                        if (userPhone == null || userName == null) {
                            invalidData.add("第" + (i + 1) + "行的姓名、手机号必填");
                            continue;
                        }
                        if (dataSource == null) {
                            invalidData.add("第" + (i + 1) + "行的数据源必填");
                            continue;
                        } else {
                            if (!"本店".equals(dataSource) && !"他店".equals(dataSource)) {
                                invalidData.add("第" + (i + 1) + "行的数据源只能是“本店”或“他店”");
                                continue;
                            }
                        }
                    }

                    if (userName != null) {
                        Matcher matcher = userNameReg.matcher(userName);
                        if (!matcher.matches()) {
                            invalidData.add("第" + (i + 1) + "行的姓名格式不正确");
                            continue;
                        }
                    }
                    if (userPhone != null) {
                        if (!ManeBaseConsoleConstants.isMobile(userPhone)) {
                            invalidData.add("第" + (i + 1) + "行的手机号格式不正确");
                            continue;
                        }
                        if (existsCardsMap != null && isDuplicate(existsCardsMap.get(userPhone), userType)) {
                            invalidData.add("第" + (i + 1) + "行的手机号已经存在");
                            continue;
                        }
                        List<Integer> userPhoneList = phoneDuplicateMap.get(userPhone);
                        if (userPhoneList != null && userPhoneList.size() > 1) {
                            invalidData.add("第" + userPhoneList + "行的手机号重复");
                            continue;
                        }
                    }
                    if (licencePlate != null) {
                        if (!ManeBaseConsoleConstants.isCarLicensePlate(licencePlate)) {
                            invalidData.add("第" + (i + 1) + "行的车牌号格式不正确,请输入7-8位车牌号码");
                            continue;
                        }
                        if (existsCardsMap != null && isDuplicate(existsCardsMap.get(licencePlate), userType)) {
                            invalidData.add("第" + (i + 1) + "行的车牌号已经存在");
                            continue;
                        }
                        List<Integer> licencePlateList = licencePlateDuplicateMap.get(licencePlate);
                        if (licencePlateList != null && licencePlateList.size() > 1) {
                            invalidData.add("第" + licencePlateList + "行的车牌号重复");
                            continue;
                        }
                    }
                    if (originalBrandName != null && originalCarSeriesName != null) {
                        if (originalBrandName.length() > 50) {
                            invalidData.add("第" + (i + 1) + "行的品牌名过长,请输入50位以内字符");
                            continue;
                        }
                        if (originalCarSeriesName.length() > 50) {
                            invalidData.add("第" + (i + 1) + "行的车系名过长,请输入50位以内字符");
                            continue;
                        }
                    } else {
                        originalBrandName = null;
                        originalCarSeriesName = null;
                    }
                    if (buyCarDate != null) {
                        account.setBuyCarTime(buyCarDate);
                    }
                    if (latestEnterDate != null) {
                        account.setLatestEnterTime(latestEnterDate);
                    }
                    account.setDealerId(dealerId);
                    account.setActivityId(activityId);
                    account.setCreateName(createName);
                    account.setUserName(userName);
                    account.setUserPhone(userPhone);
                    account.setOriginalBrandName(originalBrandName);
                    account.setOriginalCarSeriesName(originalCarSeriesName);
                    account.setLicencePlate(licencePlate);
                    account.setUserType((byte) userType);
                    if ("本店".equals(dataSource)) {
                        account.setDataSource((byte) 0);
                    }
                    if ("他店".equals(dataSource)) {
                        account.setDataSource((byte) 1);
                    }
                    accountList.add(account);
                }

            }
        } catch (Exception e) {
            StaticLogUtils.info(SystemLogType.LOG_SYS_B, "manuWap", "AfterSaleKeepOnRecordExcelService", "analysis", "备案,流失用户导入失败", e);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        excelList.setData(accountList);
        excelList.setInvalidData(invalidData);
        excelList.setTotal(total);
        return excelList;
    }

    public String getStringValue(Cell cell) {
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            String strValue = cell.getStringCellValue() != null ? cell.getStringCellValue().trim() : null;
            if (StringUtils.isEmpty(strValue)) {
                return null;
            } else {
                return strValue;
            }
        } else {
            return null;
        }
    }

    public Date getDateValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                return cell.getDateCellValue();
            } else if (cell.getCellTypeEnum() == CellType.STRING) {
                return getDateValue(cell.getStringCellValue());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private boolean isDuplicate(List<AfterSaleKeepOnRecord> list, Integer userType) {
        if (list == null) {
            return false;
        }
        if (userType == 0) {
            for (AfterSaleKeepOnRecord record : list) {
                if (userType == (int) record.getUserType()) {
                    return true;
                }
            }
        } else {
            return !list.isEmpty();
        }

        return false;
    }

    private Date getDateValue(String dateStr) {
        if (dateStr.length() < 8) {
            return null;
        }
        String[] dates = new String[3];
        String[] dateStrs = dateStr.split("\\D+");
        if (isNumeric(dateStrs[0])) {
            dates[0] = dateStrs[0];
        } else {
            return null;
        }
        if (dateStrs.length < 3) {
            return null;
        }
        if (isNumeric(dateStrs[1])) {
            dates[1] = dateStrs[1].length() == 2 ? dateStrs[1] : 0 + dateStrs[1];
        } else {
            return null;
        }
        if (isNumeric(dateStrs[2])) {
            dates[2] = dateStrs[2].length() == 2 ? dateStrs[2] : 0 + dateStrs[2];
        } else {
            return null;
        }
        try {
            return sdf.parse(String.join("-", dates));
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean isNumeric(String str) {
        return num.matcher(str).matches();
    }

    public static class ExcelList<T> {
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
