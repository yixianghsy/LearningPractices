package com.mall.mapper.controller;
import com.mall.mansger.service.UploadService;
import com.mall.utils.JsonUtils;
import com.mall.utils.ResultUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传处理Controller
 * <p>Title: PictureController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class PictureController {
//
//    @Reference
//    private UploadService uploadService;
//    //produces= MediaType.TEXT_PLAIN_VALUE+";charset=utf-8"指定响应结果的content-type：
//    @RequestMapping(value="/pic/upload", produces= MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
//    @ResponseBody
//    @ApiOperation(value = "上传文件", notes = "上传文件")
//    public String uploadQiniu(MultipartFile uploadFile)  {
//        ResultUtil resultUtil = new ResultUtil();
//        try {
//            uploadFile.getBytes();
//            //文件名
//            String fileName = uploadFile.getOriginalFilename();
//            //取文件扩展名
//            String originalFilename = uploadFile.getOriginalFilename();
//            String extName  = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
//            resultUtil = uploadService.uploadFileQiniu(uploadFile.getBytes(),fileName);
//            String url = resultUtil.getData().getFileUrl();
//            System.out.println(url);
//            //封装到map中返回
//            Map result = new HashMap<>();
//            result.put("error", 0);
//            result.put("url", url);
//            return JsonUtils.objectToJson(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Map result = new HashMap<>();
//            result.put("error", 1);
//            result.put("message", "图片上传失败");
//            return JsonUtils.objectToJson(result);
//        }
//
//
//    }


}
