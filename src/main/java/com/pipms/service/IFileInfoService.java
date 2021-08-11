package com.pipms.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pipms.entity.FileEntity;


public interface IFileInfoService extends IService<FileEntity> {

    String getOriginNameByPath(String projectNumber,String filePath);
}
