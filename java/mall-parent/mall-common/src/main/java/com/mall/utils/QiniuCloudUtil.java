package com.mall.utils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
public class QiniuCloudUtil {
    /**
     *
     */
    private  static  final  String ACCESS_KEY = "KplNYyPVgFZwE4SlHJ8nlFVuIUivHOL39-X0CNrc";

    private  static  final  String SECRET_KEY = "uwmdSM7uyYc64TIcOk-pmS-DymSXn6DzkkKFWCNW";

    //要上传的空间bucket
    private  static final  String BUCKET = "hsymall";
    //http://q9hoxeklo.bkt.clouddn.com
    private  static final  String  DOMAIN = "http://qinniu.sylianxizhuanyong.cn/";

    /**
     * 获取其牛云Token
     * @return
     */
    public  static String getupToken(){
        Auth auth = Auth.create(ACCESS_KEY,SECRET_KEY);
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBoby","{\"fileUrl\": \"" + DOMAIN+"$(key)\"}");
        long exporeSecomnds = 3600;
        String upToken = auth.uploadToken(BUCKET,null,exporeSecomnds,putPolicy);
        System.out.println(upToken);
        return  upToken;
    }
    public static String uploadFile() {
        StringBuffer fileurl = new StringBuffer(DOMAIN);
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String LocalFilePath = "/home/hsy/图片/wheat-850328_960_720.webp";
        String key = null;
        try {
            Response response = uploadManager.put(LocalFilePath, key, getupToken());
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            fileurl.append(putRet.key);
        }catch(QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignor
            }
        }
        return fileurl.toString();
    }
    //base64
    public static String uploadFileBase64(byte[]base64,String key)throws Exception {
        String file64 = Base64.encodeToString(base64, 0);
        Integer l = base64.length;
        String uploadUrl = "http://up-z2.qiniup.com/putb64/" + l + "/key/" + UrlSafeBase64.encodeToString(key);

        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(uploadUrl)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken" + getupToken())
                .post(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
        return DOMAIN + key;
    }
    public static String uploadFileBytes(byte[]uploadBytes,String fileName) throws Exception{
        StringBuffer fileUrl=new StringBuffer(DOMAIN);
        Configuration cfg= new Configuration(Zone.zone0());
        UploadManager uploadManager= new UploadManager(cfg);
        String key = fileName;
        Auth auth=Auth.create(ACCESS_KEY,SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        try {
            Response response = uploadManager.put(uploadBytes, key, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            fileUrl.append(putRet.key);
        }catch(QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignor
            }
        }
        return fileUrl.toString();
    }
    public static void main(String[] args) {


    }
}