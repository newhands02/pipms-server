package com.pipms.controller;

import com.pipms.entity.UserRole;
import com.pipms.service.FileUploadService;
import com.pipms.shiro.utils.ShiroUtils;
import com.pipms.utils.R;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName UploadController
 * @Description 文件上传控制层
 * @Author 661595
 * @Date 2021/6/2916:19
 * @Version 1.0
 **/
@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    FileUploadService fileUploadService;

    /**
     *@Description 上传改善前视频
     * **/
    @RequiresRoles(value = {"sponsor","super_admin"},logical = Logical.OR)
    @PutMapping("beforeVideo")
    public R uploadBeforeVideo(MultipartFile file, String projectNumber, HttpServletRequest request){
        UserRole sysUser = ShiroUtils.getSysUser();
        boolean b = fileUploadService.uploadBeforeVideo(file, request, projectNumber,sysUser.getUserAccount());
        return b?R.ok():R.error();
    }

    /**
     *@Description 上传改善后视频
     * **/
    @RequiresRoles(value = {"sponsor","super_admin"},logical = Logical.OR)
    @PutMapping("afterVideo")
    public R uploadAfterVideo(MultipartFile file,String projectNumber,HttpServletRequest request){
        UserRole sysUser = ShiroUtils.getSysUser();
        boolean b = fileUploadService.uploadAfterVideo(file, request, projectNumber,sysUser.getUserAccount());
        return b?R.ok():R.error();
    }
    /**
     *@Description 上传相关附件，最多两个附件
     * **/
    @PutMapping("relateFile")
    @RequiresRoles(value = {"sponsor","super_admin"},logical = Logical.OR)
    public R uploadRelateFile(MultipartFile file,String projectNumber,HttpServletRequest request,int num){
        String countKey=num==0?"one":"two";
        UserRole sysUser = ShiroUtils.getSysUser();
        boolean one = fileUploadService.uploadRelateFile(file, request, projectNumber,countKey,sysUser.getUserAccount());
        return one?R.ok():R.error();
    }
    @GetMapping("infoList")
    public List<String> getInfo(){
        return fileUploadService.getMessageList();
    }
}
