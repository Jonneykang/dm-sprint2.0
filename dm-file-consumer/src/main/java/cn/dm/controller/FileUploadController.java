package cn.dm.controller;
import cn.dm.common.*;
import cn.dm.exception.BaseErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * <p></p>
 * <p/>
 * Created by zzshang on 2015/10/15.
 */
@Controller
@RequestMapping("file/api/p/")
public class FileUploadController {

    static Logger logger = Logger.getLogger(FileUploadController.class);

    @Value("${serverSavePath}")
    private String serverSavePath;

    @RequestMapping("/upload")
    @ResponseBody
    public Dto upload(HttpServletRequest request) throws Exception{
        List<String> fileList=new ArrayList<String>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        //判断 request 是否有文件上传,即多部分请求
        if (!multipartResolver.isMultipart(request)) {
            throw new BaseException(BaseErrorCode.Illegal_Upload);
        }
        //取得request中的所有文件名
        Iterator<String> iter = multiRequest.getFileNames();
        while (iter.hasNext()) {
            //取得上传文件
            MultipartFile file = multiRequest.getFile(iter.next());
            //判断文件是否为空文件
            if (EmptyUtils.isEmpty(file)) {
                throw new BaseException(BaseErrorCode.File_isEmpty);
            }
            //判断文件类型
            String fileType = FileType.getFileByInputStream(file.getInputStream());
            //生成新的文件名
            String fileName = IdWorker.getId().toString();
            logger.info("newFileName:>>>>>>>>>>>>>>>>>>>>>>>" + fileName);
            //重命名上传后的文件名
            String savePath=serverSavePath + File.separator;
            File tempFile=new File(savePath);
            if(!tempFile.exists()){
                tempFile.mkdirs();
            }
            String path = savePath+File.separator+fileName + "." + fileType;
            File localFile = new File(path);
            file.transferTo(localFile);
            //上传完成开始保存数据库
            fileList.add(Constants.FILE_PRE+fileName+"."+fileType);
        }
        return DtoUtil.returnDataSuccess(fileList);
    }
}
