package com.tuanche.web.util;

import com.tuanche.arch.util.log.StaticLogUtils;
import com.tuanche.arch.util.log.SystemLogType;
import com.tuanche.arch.util.utils.DateUtils;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ExportExcel {
    public final static <E> String exportExcel(String fileName, String header,
                                               Map<String, String> titleValueMap, Map<String, ExportExcelCallback> callBackMap, List<E> listContent,
                                               HttpServletRequest request, HttpServletResponse response) {
        String result = "系统提示：Excel文件导出成功！";
        //注释原因： 避免空数据页面报错
//		if (null == listContent || listContent.size() == 0) {
//			return null;
//		}
        List<E> listContentTem = listContent;
        int listSize = listContentTem.size();
        int forSize = 1;
        int excelSize = 1000;
        if (!(excelSize >= listSize)) {
            // 一页多
            if (listSize % excelSize == 0) {
                forSize = listSize / excelSize;
            } else {
                forSize = (int) Math.floor(listSize / excelSize + 1);
            }
        }
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="
                    + getCorrectFileName(request, fileName));
            response.setContentType("application/msexcel");
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            for (int i = 0; i < forSize; i++) {
                if (forSize == 1) {
                    listContent = listContentTem.subList(0, listSize);
                } else {
                    if (forSize - 1 == i) {
                        // 末尾
                        listContent = listContentTem.subList(i * excelSize,
                                listSize);
                    } else {
                        if (0 == i) {
                            listContent = listContentTem.subList(0, excelSize);
                        } else {
                            listContent = listContentTem.subList(i * excelSize,
                                    (i * excelSize) + excelSize);
                        }
                    }
                }

                WritableSheet sheet = workbook.createSheet("Sheet" + i, i);
                jxl.SheetSettings sheetset = sheet.getSettings();
                sheetset.setProtected(false);
                WritableFont NormalFont = new WritableFont(WritableFont.ARIAL,
                        10);
                WritableFont BoldFont = new WritableFont(WritableFont.ARIAL,
                        10, WritableFont.BOLD);
                WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);

                wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
                wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf_center.setAlignment(Alignment.CENTRE);
                wcf_center.setWrap(false);

                WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
                wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN);
                wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf_left.setAlignment(Alignment.CENTRE);
                wcf_left.setWrap(false);

//				CellView cellView = new CellView();
//				cellView.setAutosize(true); //设置自动大小
//				sheet.setColumnView(i, cellView);//根据内容自动设置列宽

                int m = 0;
                if (header != null) {
                    sheet.addCell(new Label(m++, 0, header, wcf_center));
                    m = 0;
                }
                // 表头
                for (String s : titleValueMap.values()) {
                    sheet.addCell(new Label(m++, header == null ? 0 : 1, s, wcf_center));
                }

                Class classObj = null;
                Method meth = null;
                m = 1 + (header == null ? 0 : 1);
//				DateFormat df = new DateFormat("yyyy-MM-dd HH:mm:ss");
//				WritableCellFormat cf1 = new WritableCellFormat(df);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                int j = 0;
                for (E obj : listContent) {
                    classObj = obj.getClass();
                    int columnView = 0;
                    for (String s : titleValueMap.keySet()) {
                        try {
                            meth = classObj.getDeclaredMethod("get" + s);
                        } catch (Exception e) {
                            try {
                                meth = classObj.getSuperclass().getDeclaredMethod("get" + s);
                            }catch (Exception e1) {
                                meth = classObj.getSuperclass().getSuperclass().getDeclaredMethod("get" + s);
                            }
                        }
                        String text = "";
                        if (callBackMap == null || callBackMap.get(s) == null) {
                            text = meth.invoke(obj) == null ? "" : meth.invoke(obj) instanceof Date ? sdf.format((Date) meth.invoke(obj)) : meth.invoke(obj).toString();
                        } else {
                            text = callBackMap.get(s).valueHandler(obj);
                        }

                        sheet.addCell(new Label(j++, m, text, wcf_left));
                        int bstrLength = (text.getBytes()).length + 2 < 15 ? 15 : (text.getBytes()).length + 2;
                        sheet.setColumnView(columnView, bstrLength);
                        columnView++;
                    }
                    m++;
                    j = 0;
                }
            }

            workbook.write();
            workbook.close();
            os.close();
            if (null != listContentTem) {
                listContentTem = null;
            }
            if (null != listContent) {
                listContent = null;
            }
        } catch (Exception e) {
            result = "系统提示：Excel文件导出失败，原因：" + e.toString();
            e.printStackTrace();
        }
        return result;
    }

    public final static <E> String exportExcel(String fileName,
                                               Map<String, String> titleValueMap, List<E> listContent,
                                               HttpServletRequest request, HttpServletResponse response) {
        return exportExcel(fileName, null, titleValueMap, null, listContent, request, response);

    }

    public final static <E> String exportExcel(String fileName, String header,
                                               Map<String, String> titleValueMap, List<E> listContent,
                                               HttpServletRequest request, HttpServletResponse response) {
        return exportExcel(fileName, header, titleValueMap, null, listContent, request, response);

    }

    public final static <E> String exportExcel(String fileName,
                                               Map<String, String> titleValueMap, Map<String, ExportExcelCallback> callBackMap, List<E> listContent,
                                               HttpServletRequest request, HttpServletResponse response) {
        return exportExcel(fileName, null, titleValueMap, callBackMap, listContent, request, response);
    }


    public static String getCorrectFileName(HttpServletRequest request,
                                            String fileName) throws UnsupportedEncodingException {
        final String userAgent = request.getHeader("USER-AGENT");
        if (userAgent.indexOf("MSIE") > -1) {// IE浏览器
            return URLEncoder.encode(fileName, "UTF8");
        } else if (userAgent.indexOf("Mozilla") > -1) {// google,火狐浏览器
            return new String(fileName.getBytes(), "ISO8859-1");
        } else {
            return URLEncoder.encode(fileName, "UTF8");// 其他浏览器
        }
    }

    /**
     * 导出多个sheet
     * @author HuangHao
     * @CreatTime 2021-10-15 17:44
     * @param filename
     * @param list
     * @param request
     * @param response
     * @return java.lang.String
     */
    public final static <E> String multipleSheet(String filename,List<ExportExcelMultipleSheet> list,
                                                     HttpServletRequest request, HttpServletResponse response) {
        String result = "系统提示：Excel文件导出成功！";
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename="
                    + getCorrectFileName(request, filename));
            response.setContentType("application/msexcel");
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            for (ExportExcelMultipleSheet multipleSheet : list) {
                setSheet(multipleSheet.getSheetName(), multipleSheet.getList(), workbook, multipleSheet.getTitleValueMap());
            }
            workbook.write();
            workbook.close();
            os.close();
        } catch (Exception e) {
            result = "系统提示：Excel文件导出失败，原因：" + e.toString();
            e.printStackTrace();
        }
        return result;
    }

    private final static <E> void setSheet (String sheetName, List<E> listContent, WritableWorkbook workbook, Map<String, String> titleValueMap) {
        try {
            if (CollectionUtils.isNotEmpty(listContent)) {
                List<E> listContentTem = listContent;
                int listSize = listContentTem.size();
                int forSize = 1;
                int excelSize = 10000;
                if (!(excelSize >= listSize)) {
                    // 一页多
                    if (listSize % excelSize == 0) {
                        forSize = listSize / excelSize;
                    } else {
                        forSize = (int) Math.floor(listSize / excelSize + 1);
                    }
                }
                for (int i = 0; i < forSize; i++) {
                    String pageNum = "";
                    if (forSize == 1) {
                        listContent = listContentTem.subList(0, listSize);
                    } else {
                        pageNum = String.valueOf((i+1));
                        if (forSize - 1 == i) {
                            // 末尾
                            listContent = listContentTem.subList(i * excelSize,
                                    listSize);
                        } else {
                            if (0 == i) {
                                listContent = listContentTem.subList(0, excelSize);
                            } else {
                                listContent = listContentTem.subList(i * excelSize,
                                        (i * excelSize) + excelSize);
                            }
                        }
                    }
                    WritableSheet sheet = workbook.createSheet(sheetName + pageNum, i);
                    jxl.SheetSettings sheetset = sheet.getSettings();
                    sheetset.setProtected(false);
                    WritableFont NormalFont = new WritableFont(WritableFont.ARIAL,
                            10);
                    WritableFont BoldFont = new WritableFont(WritableFont.ARIAL,
                            10, WritableFont.BOLD);
                    WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);

                    wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
                    wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
                    wcf_center.setAlignment(Alignment.CENTRE);
                    wcf_center.setWrap(false);

                    WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
                    wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN);
                    wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
                    wcf_left.setAlignment(Alignment.CENTRE);
                    wcf_left.setWrap(false);
                    int m = 0;
                    // 表头
                    for (String s : titleValueMap.values()) {
                        sheet.addCell(new Label(m++, 0, s, wcf_center));
                    }

                    Class classObj = null;
                    Method meth = null;
                    m = 1;
                    int j = 0;
                    for (E obj : listContent) {
                        classObj = obj.getClass();
                        int columnView = 0;
                        for (String s : titleValueMap.keySet()) {
                            try {
                                meth = classObj.getDeclaredMethod("get" + s);
                            } catch (Exception e) {
                                meth = classObj.getSuperclass().getDeclaredMethod("get" + s);
                            }
                            String text = meth.invoke(obj) == null ? "" : meth.invoke(obj) instanceof Date ? DateUtils.dateToString((Date) meth.invoke(obj), "yyyy-MM-dd HH:mm:ss") : meth.invoke(obj).toString();;
                            sheet.addCell(new Label(j++, m, text, wcf_left));
                            int bstrLength = (text.getBytes()).length + 2 < 15 ? 15 : (text.getBytes()).length + 2;
                            sheet.setColumnView(columnView, bstrLength);
                            columnView++;
                        }
                        m++;
                        j = 0;
                    }
                }
            }
        }catch (Exception e) {
            StaticLogUtils.error(SystemLogType.LOG_SYS_B, "direct-selling-web","AfterSaleExportExcel","setSheet", "多sheet导出error", e);
        }
    }
}
