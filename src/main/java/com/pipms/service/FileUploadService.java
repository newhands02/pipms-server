package com.pipms.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pipms.entity.FileEntity;
import com.pipms.entity.ProjectInfo;
import com.pipms.service.impl.FileInfoServiceImpl;
import com.pipms.service.impl.ProjectInfoServiceImpl;
import com.pipms.utils.FileUploadTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileUploadService
 * @Description 文件上传业务层
 * @Author 661595
 * @Date 2021/6/2910:15
 * @Version 1.0
 **/
@Service
public class FileUploadService {
    @Autowired
    FileInfoServiceImpl fileInfoService;
    @Autowired
    ProjectInfoServiceImpl projectInfoService;
    @Autowired
    FileUploadTool uploadTool;
    private List<String> messageList=new ArrayList<>();

    //上传改善前视频
    public boolean uploadBeforeVideo(MultipartFile file, HttpServletRequest request,String projectNumber,String account){
        if (file==null || projectNumber==null || account==null){
            return false;
        }
        FileEntity beforeFile = uploadTool.createFile(file, request, projectNumber, "before");
        beforeFile.setUploadAccount(account);
        boolean success = this.uploadVideo(beforeFile, projectNumber,"improvement_before_video", "改善前");
        return success;
    }
    //上传改善后视频
    public boolean uploadAfterVideo(MultipartFile file, HttpServletRequest request,String projectNumber,String account){
        if (file==null || projectNumber==null || account==null){
            return false;
        }
        FileEntity afterFile = uploadTool.createFile(file, request, projectNumber, "after");
        afterFile.setUploadAccount(account);
        boolean success = this.uploadVideo(afterFile, projectNumber,"improvement_after_video", "改善后");
        return success;
    }
    //上传相关附件
    public boolean uploadRelateFile(MultipartFile file, HttpServletRequest request,String projectNumber,String fileKey,String account){
        if (file==null || projectNumber==null || account==null){
            return false;
        }
        FileEntity relateFile = uploadTool.createFile(file, request, projectNumber, fileKey);
        relateFile.setUploadAccount(account);
        boolean success = this.uploadVideo(relateFile, projectNumber,"file_relate_"+fileKey, "相关附件"+fileKey);
        return success;
    }

    private boolean uploadVideo(FileEntity videoFile,String projectNumber,String videoKey,String videoPrefix){
        boolean answer=true;
        if (videoFile==null){
            answer=false;
            this.setUploadMessage("存储"+videoPrefix+"视频失败");
        }else {
//插入文件信息
            UpdateWrapper<FileEntity> fileInfoWrapper=new UpdateWrapper<>();
            fileInfoWrapper.eq("project_number",videoFile.getProjectNumber())
                    .eq("title_orig",videoFile.getTitleOrig());
            boolean saveFile = fileInfoService.saveOrUpdate(videoFile, fileInfoWrapper);
            if (saveFile){
                this.setUploadMessage("插入"+videoPrefix+"视频信息成功");
            }else {
                this.setUploadMessage("插入"+videoPrefix+"视频信息失败");
            }
//更新文件地址
            UpdateWrapper<ProjectInfo> projectInfoUpdateWrapper=new UpdateWrapper<>();
            projectInfoUpdateWrapper.eq("project_number",projectNumber)
                    .set(videoKey,videoFile.getPath());
            boolean update = projectInfoService.update(projectInfoUpdateWrapper);
            if (update){
                this.setUploadMessage("更新"+videoPrefix+"视频地址成功");
            }else {
                this.setUploadMessage("更新"+videoPrefix+"视频地址失败");
            }
            answer=saveFile&&update;
            if (!answer){
                uploadTool.deleteFile();
            }
        }
        return answer;
    }
    private void setUploadMessage(String message){
        this.messageList.add(message);
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }
}
