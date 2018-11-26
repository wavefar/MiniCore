package org.wavefar.lib.net;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * API基类
 * @author summer
 */
public interface BaseRestApi {


 /**
  * GET请求
  * @param url 请求地址
  */
 @GET
 Observable<ResponseBody> get(@Url String url);

 /**
  * GET请求
  * @param url 请求地址
  * @param maps 请求参数
  * @return
  */
 @GET
 Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> maps);


 /**
  * 下载
  * @param fileUrl 下载地址
  * @return
  */
 @Streaming
 @GET
 Observable<ResponseBody> downloadFile(@Url String fileUrl);

 /**
  * 普通post不带参数
  * @param url
  * @return
  */
 @POST
 Observable<ResponseBody> post(@Url String url);

 /**
  * POST请求
  * @param url 请求地址
  * @param requestBody 请求body参数
  * @return
  */
 @POST
 Observable<ResponseBody> post(@Url String url, @Body RequestBody requestBody);

 /**
  * POST请求
  * @param url 请求地址
  * @param body 请求字符串如json、raw之类的
  * @return
  */
 @POST
 Observable<ResponseBody> postBody(@Url String url, @Body String body);


 /**
  * POST请求
  * @param url 请求地址
  * @param map 请求参数
  * @return
  */
 @FormUrlEncoded
 @POST
 Observable<ResponseBody> postForm(@Url String url, @FieldMap Map<String, String> map);


 /**
  * 普通delete请求不带参数
  * @param url
  * @return
  */
 @DELETE
 Observable<ResponseBody> delete(@Url String url);

 /**
  * 普通delete请求带参数
  * @param url
  * @param maps
  * @return
  */
 @DELETE
 Observable<ResponseBody> delete(@Url String url,@QueryMap Map<String, String> maps);

 /**
  * 单图片上传
  * @param url url
  * @param file 文件类型
  * @return
  */
 @Multipart
 @POST
 Observable<ResponseBody> upLoadFile(
         @Url String url,
         @Part MultipartBody.Part file
 );

 /**
  * 多张图片上传
  * @param url
  * @param maps
  * @return
  */
 @Multipart
 @POST
 Observable<ResponseBody> uploadFiles(
         @Url String url,
         @PartMap() Map<String, RequestBody> maps);



}