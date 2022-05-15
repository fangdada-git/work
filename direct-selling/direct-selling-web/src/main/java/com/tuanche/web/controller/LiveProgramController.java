package com.tuanche.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.tuanche.commons.util.ResultDto;
import com.tuanche.directselling.api.LiveProgramService;
import com.tuanche.directselling.api.LiveSceneService;
import com.tuanche.directselling.dto.LiveProgramDto;
import com.tuanche.directselling.model.LiveScene;
import com.tuanche.directselling.model.LiveSceneDealerBrand;
import com.tuanche.directselling.model.PosterModel;
import com.tuanche.directselling.utils.PageResult;
import com.tuanche.directselling.vo.LiveProgramVo;
import com.tuanche.inner.sso.core.user.XxlUser;
import com.tuanche.web.util.CommonLogUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangxing
 * @Description 节目控制类
 * @date 2020年3月5日上午11:33:03
 */
@RestController
@RequestMapping("/program")
public class LiveProgramController extends BaseController {

    @Reference
    LiveProgramService liveProgramService;
    @Reference
    LiveSceneService liveSceneService;

    @Value("${direct.selling.poster.pic.url}")
    private String posterPicUrl;
    /**
     * 节目列表数据加载
     *
     * @param pageResult
     * @return
     */
    @RequestMapping("/searchMaterialList")
    public PageResult searchMaterialList(PageResult<LiveProgramDto> pageResult, LiveProgramVo liveProgramVo) {
        PageResult PageList = liveProgramService.findProgramList(pageResult, liveProgramVo);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * 场次活动下品牌数据查询
     *
     * @param pageResult
     * @return
     */
    @RequestMapping("/searchBrands")
    public PageResult searchBrands(LiveProgramVo liveProgramVo) {
        List<LiveSceneDealerBrand> data = liveProgramService.searchBrands(liveProgramVo);
        PageResult PageList = new PageResult<>();
        PageList.setData(data);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * 场次活动品牌下经销商
     *
     * @param pageResult
     * @return
     */
    @RequestMapping("/searchDealers")
    public PageResult searchDealers(LiveProgramVo liveProgramVo) {
        List<LiveSceneDealerBrand> data = liveProgramService.searchDealers(liveProgramVo);
        PageResult PageList = new PageResult<>();
        PageList.setData(data);
        PageList.setCode("0");
        return PageList;
    }

    /**
     * @param liveProgramVo
     * @return: com.tuanche.commons.util.ResultDto
     * @description: 返回直播场次
     * @author: czy
     * @create: 2020/5/25 13:34
     **/
    @RequestMapping("/queryScene")
    public ResultDto queryScene(LiveProgramVo liveProgramVo) {
        LiveScene liveScene = null;
        try {
            liveScene = liveSceneService.getLiveSceneById(liveProgramVo.getSceneId());
            return success(liveScene);
        } catch (Exception e) {
            return systemError();
        }
    }

    /**
     * 保存节目
     *
     * @param request
     * @param liveSceneVo
     * @return
     */
    @RequestMapping("/save")
    public ResultDto save(HttpServletRequest request, @RequestBody LiveProgramVo liveProgramVo) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (xxlUser == null) {
                return loginError();
            }
            if (liveProgramVo == null) {
                return paramBlank();
            }

            // 查询时间是否满足
            List<LiveProgramDto> data = liveProgramService.queryList(liveProgramVo);
            if (!CollectionUtils.isEmpty(data)) {
                return programTimeError();
            }
            liveProgramVo.setCreateUserId(xxlUser.getId());
            liveProgramVo.setCreateUserName(xxlUser.getEmpName());
            liveProgramVo.setCreateDt(new Date());
            liveProgramVo.setDeleteState(0);
            liveProgramService.save(liveProgramVo);
        } catch (Exception e) {
            return systemError();
        }
        return success();
    }

    /**
     * @param request
     * @param liveProgramVo
     * @return: com.tuanche.commons.util.ResultDto
     * @description: 更新节目经销商
     * @author: czy
     * @create: 2020/5/25 13:35
     **/
    @RequestMapping("/updateProgramDealer")
    public ResultDto updateProgramDealer(HttpServletRequest request, @RequestBody LiveProgramVo liveProgramVo) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (xxlUser == null) {
                return loginError();
            }
            if (liveProgramVo == null) {
                return paramBlank();
            }
            liveProgramVo.setCreateUserId(xxlUser.getId());
            liveProgramVo.setCreateUserName(xxlUser.getEmpName());
            liveProgramVo.setCreateDt(new Date());
            liveProgramVo.setDeleteState(0);
            liveProgramService.updateProgramDealer(liveProgramVo);
        } catch (Exception e) {
            return systemError();
        }
        return success();
    }

    /**
     * 更新节目
     *
     * @param request
     * @param liveProgramVo
     * @return
     */
    @RequestMapping("/update")
    public ResultDto updateProgram(HttpServletRequest request, @RequestBody LiveProgramVo liveProgramVo) {
        XxlUser xxlUser = getLoginUser(request);
        try {
            if (xxlUser == null) {
                return loginError();
            }
            if (liveProgramVo == null) {
                return paramBlank();
            }
            // 查询时间是否满足
            if (liveProgramVo.getBeginTime() != null) {
                List<LiveProgramDto> data = liveProgramService.queryList(liveProgramVo);
                if (!CollectionUtils.isEmpty(data)) {
                    return programTimeError();
                }
            }
            liveProgramVo.setUpdateUserId(xxlUser.getId());
            liveProgramVo.setUpdateUserName(xxlUser.getEmpName());
            liveProgramVo.setUpdateDt(new Date());
            liveProgramService.update(liveProgramVo);
        } catch (Exception e) {
            return systemError();
        }
        return success();
    }

    /**
     * @param request
     * @param response
     * @param programId
     * @return: org.springframework.http.ResponseEntity<byte[]>
     * @description: 单张海报下载
     * @author: czy
     * @create: 2020/5/25 13:35
     **/
    @RequestMapping("/poster")
    public ResponseEntity<byte[]> downloadFile(HttpServletRequest request, HttpServletResponse response,
                                               @RequestParam("programId") Integer programId) throws IOException {
        Integer count = liveProgramService.getPosterModelNumb(programId);
        CommonLogUtil.addInfo("海报下载","downloadFile==", "programId="+programId+" ;count="+count);
        if (count != null && count > 0) {
            if (count > 1){
                List<Integer> list = new ArrayList<>();
                list.add(programId);
                doDownloadZip(request , response , list);
            }else {
                List<PosterModel> posterModelList = liveProgramService.selectPosterByProgramId(programId);
               return doDownloadFile(posterModelList);
            }
        }
        return null;
    }

    /**
     * @param request
     * @param response
     * @param programIds
     * @return: void
     * @description: 批量海报下载 zip
     * @author: czy
     * @create: 2020/5/25 13:36
     **/
    @RequestMapping("/poster/zip")
    public void downloadZip(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("programIds") String programIds) throws Exception {
        List<Integer> programIdList = getProgramIdList(programIds);
        if (programIdList.size() > 0){
            doDownloadZip(request,response,programIdList);
        }
    }

    /**
     * @param httpUrl
     * @return: java.io.InputStream
     * @description: 返回输入流
     * @author: czy
     * @create: 2020/5/25 13:36
     **/
    public InputStream dowloadFile(String httpUrl) throws IOException {
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        return inStream;
    }

    /**
     * @param is
     * @return: byte[]
     * @description: 返回字节流
     * @author: czy
     * @create: 2020/5/25 13:37
     **/
    private byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len = 0;
        try {
            while ((len = is.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }

    /**
     * @param programIds
     * @return: java.util.List<java.lang.Integer>
     * @description: 返回节目id
     * @author: czy
     * @create: 2020/5/25 13:37
     **/
    private List<Integer> getProgramIdList(String programIds){
        List<Integer> integerList = new ArrayList<>();
        if (!programIds.isEmpty()){
            String[] split = programIds.split(",");
            if (split != null){
                for (String s : split){
                    integerList.add(Integer.valueOf(s));
                }
            }
        }
        return integerList;
    }

    /**
     * @param programIdList
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @description: 返回批量节目的URL
     * @author: czy
     * @create: 2020/5/25 13:38
     **/
    private Map<String,String> getFiles(List<Integer> programIdList){
        Map<String,String> map = new ConcurrentHashMap<>();
        for (Integer programId: programIdList){
            List<PosterModel> posterModelList = liveProgramService.selectPosterByProgramId(programId);
            if (posterModelList != null){
                for (PosterModel posterModel: posterModelList){
                    if (posterModel.getHaibaoUrl()!=null && !posterModel.getHaibaoUrl().isEmpty()){
                        map.put(posterModel.getDealerName()  + "_" + System.currentTimeMillis()
                                , posterPicUrl + posterModel.getHaibaoUrl());
                    }
                    if (posterModel.getHaibaoYureUrl()!=null && !posterModel.getHaibaoYureUrl().isEmpty()){
                        map.put(posterModel.getDealerName()  + "_" + System.currentTimeMillis()
                                , posterPicUrl + posterModel.getHaibaoYureUrl());
                    }
                }
            }
        }
        return map;
    }

    /**
     * @param posterModelList
     * @return: org.springframework.http.ResponseEntity<byte[]>
     * @description: 下载操作
     * @author: czy
     * @create: 2020/5/25 13:40
     **/
    private ResponseEntity<byte[]> doDownloadFile(List<PosterModel> posterModelList) throws IOException {
        String targetUrl = posterModelList.get(0).getType().equals(6)
                ? posterModelList.get(0).getHaibaoYureUrl() : posterModelList.get(0).getHaibaoUrl();
        CommonLogUtil.addInfo("doDownloadFile","doDownloadFile===",JSON.toJSONString(posterModelList));
        targetUrl = posterPicUrl + targetUrl;
        if (!targetUrl.isEmpty() && targetUrl!=null){
            String downName = posterModelList.get(0).getDealerName() + "_" + System.currentTimeMillis()+ ".png";
            HttpStatus status = HttpStatus.CREATED;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            try {
                headers.setContentDispositionFormData("attachment",   downName= URLEncoder.encode(downName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            InputStream is = null;
            try {
                is = dowloadFile(targetUrl);
                byte[] bytes = readInputStream(is);
                return new ResponseEntity<byte[]>(bytes, headers, status);
            } catch (IOException e) {
                CommonLogUtil.addError("海报下载异常","downloadFile===", e);
            }finally {
                is.close();
            }
        }
        return null;
    }

    /**
     * @param request
     * @param response
     * @param programIdList
     * @return: void
     * @description: 批量下载操作
     * @author: czy
     * @create: 2020/5/25 13:40
     **/
    private void  doDownloadZip(HttpServletRequest request, HttpServletResponse response,List<Integer> programIdList) throws IOException {
        ZipOutputStream zos =null;
        try {
            String downloadFilename = "海报_" + System.currentTimeMillis() + ".zip";//文件的名称
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            zos = new ZipOutputStream(response.getOutputStream());
            Map<String, String> files = getFiles(programIdList);
            if (files != null){
                for (Map.Entry<String, String> entry : files.entrySet()) {
                    URL url = new URL(entry.getValue());
                    zos.putNextEntry(new ZipEntry(entry.getKey() + ".png"));
                    //FileInputStream fis = new FileInputStream(new File(files[i]));
                    InputStream fis = url.openConnection().getInputStream();
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    fis.close();
                }
            }
        } catch (IOException e) {
            CommonLogUtil.addError("海报zip下载异常","downloadZip===", e);
        }
        finally {
            zos.flush();
            zos.close();
        }
    }
}
