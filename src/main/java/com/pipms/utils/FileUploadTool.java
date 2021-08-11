package com.pipms.utils;

import com.pipms.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class FileUploadTool {
    // 文件最大500M
    private static long upload_maxsize = 500 * 1024 * 1024;
    // 文件允许格式
    private static String[] allowFiles = { ".rar", ".doc", ".docx", ".zip",
            ".pdf", ".txt", ".swf", ".xlsx", ".gif", ".png", ".jpg", ".jpeg",
            ".bmp", ".xls", ".mp4", ".flv", ".ppt", ".avi", ".mpg", ".wmv",
            ".3gp", ".mov", ".asf", ".asx", ".vob", ".wmv9", ".rm", ".rmvb" };
    //
    private String fileAbsolutePath="";
    // 允许转码的视频格式
// private static String[] allowFLV = { ".avi", ".mpg", ".wmv", ".3gp",
// ".mov", ".asf", ".asx", ".vob" };
// 允许的视频转码格式
// private static String[] allowAVI = { ".wmv9", ".rm", ".rmvb" };
    public FileEntity createFile(MultipartFile multipartFile, HttpServletRequest request, String projectNumber, String suffix) {
        FileEntity entity = new FileEntity();
        entity.setProjectNumber(projectNumber);
        boolean bflag = false;
        String fileName = multipartFile.getOriginalFilename().toString();
// 判断文件不为空
        if (multipartFile.getSize() != 0 && !multipartFile.isEmpty()) {
            bflag = true;
// 判断文件大小
            if (multipartFile.getSize() <= upload_maxsize) {
                bflag = true;
// 文件类型判断
                if (this.checkFileType(fileName)) {
                    bflag = true;
                } else {
                    bflag = false;
                    System.out.println("文件类型不允许");
                }
            } else {
                bflag = false;
                System.out.println("文件大小超范围");
            }
        } else {
            bflag = false;
            System.out.println("文件为空");
        }
        if (bflag) {
            String logoPathDir = "/fujian/"+projectNumber+"/";
            String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir);
// 上传到本地磁盘
// String logoRealPathDir = "E:/upload";
            File logoSaveFile = new File(logoRealPathDir);
            if (!logoSaveFile.exists()) {
                logoSaveFile.mkdirs();
            }
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            System.out.println("文件名称：" + name);
// 新的文件名
            String newFileName = this.getName(fileName,projectNumber,suffix);
// 文件扩展名
            String fileEnd = this.getFileExt(fileName);
// 绝对路径
            String fileNamedirs = logoRealPathDir + File.separator + newFileName + fileEnd;
            this.fileAbsolutePath=fileNamedirs;
            System.out.println("保存的绝对路径：" + fileNamedirs);
            File filedirs = new File(fileNamedirs);
// 转入文件
            try {
                multipartFile.transferTo(filedirs);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
// 相对路径
            entity.setType(fileEnd);
            String fileDir = logoPathDir + newFileName + fileEnd;
            StringBuilder builder = new StringBuilder(fileDir);
            String finalFileDir = builder.substring(1);
// size存储为String
            String size = this.getSize(filedirs);
            entity.setSize(size);
            entity.setPath(finalFileDir);
            entity.setTitleOrig(name);
            entity.setTitleAlter(newFileName);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            entity.setUploadTime(timestamp);
            return entity;
        } else {
            return null;
        }
    }
    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    private boolean checkFileType(String fileName) {
        Iterator<String> type = Arrays.asList(allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
/**
 * 视频类型判断(flv)
 *
 * @param fileEnd
 * @return
 */
// private boolean checkMediaType(String fileEnd) {
// Iterator<String> type = Arrays.asList(allowFLV).iterator();
// while (type.hasNext()) {
// String ext = type.next();
// if (fileEnd.equals(ext)) {
// return true;
// }
// }
// return false;
// }
    /**
     * 获取文件扩展名
     *
     * @return string
     */
    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    /**
     * 依据原始文件名生成新文件名
     * @return
     */
    private String getName(String fileName,String projectNumber,String suffix) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmSS");
        String format = sdf.format(new Date());
        Iterator<String> type = Arrays.asList(allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.contains(ext)) {
// String newFileName = fileName.substring(0, fileName.lastIndexOf(ext));
                String newFileName = projectNumber+format+suffix;
                return newFileName;
            }
        }
        return "";
    }
    /**
     * 文件大小，返回kb.mb
     *
     * @return
     */
    private String getSize(File file) {
        String size = "";
        long fileLength = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024) {
            size = df.format((double) fileLength) + "BT";
        } else if (fileLength < 1048576) {
            size = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            size = df.format((double) fileLength / 1048576) + "MB";
        } else {
            size = df.format((double) fileLength / 1073741824) + "GB";
        }
        return size;
    }
    /**
     *删除文件
     *@Param [file]
     *@return boolean **/
    public boolean deleteFile(){
        File deleteFile=new File(this.fileAbsolutePath);
        boolean result=true;
        if (deleteFile.exists()){
            result = deleteFile.delete();
        }
        return result;
    }
}
