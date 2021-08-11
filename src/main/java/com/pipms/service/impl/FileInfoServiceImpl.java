package com.pipms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pipms.entity.FileEntity;
import com.pipms.mapper.FileInfoMapper;
import com.pipms.service.IFileInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileInfoServiceImpl
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/2910:13
 * @Version 1.0
 **/
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileEntity> implements IFileInfoService {
    @Override
    public String getOriginNameByPath(String projectNumber, String filePath) {
        QueryWrapper<FileEntity> wrapper=new QueryWrapper<>();
        wrapper.eq("project_number",projectNumber).eq("path",filePath);
        FileEntity one = this.getOne(wrapper, false);
        String fileName="";
        if (one!=null){
            fileName=one.getTitleOrig()+one.getType();
        }
        return fileName;
    }
    public List<Map> getVideoList(String projectNumber, String path) {
        List<Map> videoList=new LinkedList<>();
        if (path!=null){
            String originName = this.getOriginNameByPath(projectNumber, path);
            videoList.add(this.getFileInfoMap(originName,path));
        }
        return videoList;
    }
    public List<Map> getRelateFileList(String projectNumber, String path,String path2) {
        List<Map> fileList=new LinkedList<>();
        if (path!=null){
            String fileName1 = this.getOriginNameByPath(projectNumber, path);
            fileList.add(this.getFileInfoMap(fileName1,path));
        }
        if (path2!=null){
            String fileName2 = this.getOriginNameByPath(projectNumber, path2);
            fileList.add(this.getFileInfoMap(fileName2,path2));
        }
        return fileList;
    }
    private Map<String,String> getFileInfoMap(String name, String url){
        Map<String,String> map=new HashMap<>();
        map.put("name",name);
        map.put("url",url);
        return map;
    }
}

