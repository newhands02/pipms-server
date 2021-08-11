package com.pipms.controller;

import com.pipms.service.impl.ProjectInfoServiceImpl;
import com.pipms.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.net.URLEncoder;

/**
 * @ClassName VideoController
 * @Description 获取视频和附件资源
 * @Author 661595
 * @Date 2021/7/59:03
 * @Version 1.0
 **/
@RestController
public class FileController {
    @Autowired
    private ProjectInfoServiceImpl projectInfoService;

    /**
     *@Description 获取视频资源
     * **/
    @RequiresPermissions("project:find")
    @GetMapping("getVideo")
    public R getVideo(String projectNumber, String suffix, HttpServletRequest request, HttpServletResponse response){
        if ("undefined".equals(projectNumber) || projectNumber==null){
            return R.error("项目编号未定义");
        }
        String file = projectInfoService.getVideoUrlByProjectNumber(projectNumber, suffix);
        if ("".equals(file)){
            return R.error("该项目无视频");
        }
        String contentType="video/mp4";
        downLoadResource(file,contentType,request,response);
        return null;
    }
    /**
     *@Description 获取附件资源
     * **/
    @RequiresPermissions("project:find")
    @GetMapping("downLoadRelate/{suffix}")
    public R downRelateFile(@PathVariable("suffix") String suffix,String projectNumber, HttpServletRequest request,HttpServletResponse response){
        if (projectNumber==null){
            return R.error("项目编号未定义");
        }
        String url = projectInfoService.getFileUrlByProjectNumber(projectNumber,suffix);
        if ("".equals(url) || url==null){
            return R.error("该项目此附件为空");
        }
        String contentType="application/vnd.ms-excel";
        downLoadResource(url,contentType,request,response);
        return null;
    }
    private void downLoadResource(String filePath,String contentType,HttpServletRequest request,HttpServletResponse response){
        String absolutePath = request.getSession().getServletContext().getRealPath(filePath);
        if (absolutePath!=null){
            try{
                FileInputStream fin=new FileInputStream(absolutePath);
                byte[] data=new byte[fin.available()];
                fin.read(data);
                String diskFileName=filePath.split("/")[2];
                response.setContentType(contentType);
                response.setHeader("Content-Disposition","attachment;filename=\""+diskFileName+"\"");
                response.setContentLength(data.length);
                response.setHeader("Content-Ranges","bytes");
                response.setHeader("FileName", URLEncoder.encode(diskFileName,"utf-8"));
                response.setHeader("Access-Control-Expose-Headers","FileName");
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
                fin.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}

