package com.example.administrator.cbwapplication.util.http;

import android.text.TextUtils;
import android.widget.Toast;

import com.example.administrator.cbwapplication.application.MyApplication;
import com.example.administrator.cbwapplication.util.JsonUtil;
import com.example.administrator.cbwapplication.util.SmartLog;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
/**
 * 网络访问工具类 get和post请求数据方法,上传文件方法<br>
 * Created by cbw on 2015/12/14.
 */
public
class HttpUtils {

    // log标签
    private static final String TAG = "HttpUtils";

    /*public HashMap<String, String> maps = new HashMap<>();
    private RequestParams requestParams;

    *//**
     * 构造
     * 加入自定义加密值
     *//*
    public HttpUtils() {
        requestParams = new RequestParams();
        maps.put("api_user", Constants.api_user);
        maps.put("from", Constants.from);
    }

    *//**
     * 添加请求参数
     *
     * @param name  请求参数名
     * @param value 请求参数Value值
     *//*
    public void addParams(String name, String value) {
        maps.put(name, value);
    }

    *//**
     * 添加请求参数
     *
     * @param name  请求参数名
     * @param value 请求参数Value值
     *//*
    public void addParams(String name, int value) {
        addParams(name, value + "");
    }

    *//**
     * 添加请求参数
     *
     * @param name  请求参数名
     * @param value 请求参数Value值
     *//*
    public void addParams(String name, long value) {
        addParams(name, value + "");
    }

    *//**
     * 添加请求参数
     *
     * @param name  请求参数名
     * @param value 请求参数Value值
     *//*
    public void addParams(String name, short value) {
        addParams(name, value + "");
    }*/

    /**
     * 发送HTTP POST请求
     *
     * @param url     发送的url
     * @param handler 消息处理
     * @throws Exception 抛异常
     */
    /*public void post(String url, final Handler handler) throws Exception {
        //判断是否有网络连接
        if (!MyApplication.isNetworkConnected()) {
            Toast.makeText(MyApplication.getContext(), "无可用的网络连接", Toast.LENGTH_SHORT).show();
            Message msg = Message.obtain();
            msg.what = Constants.NO_NETWORK;
            handler.sendMessage(msg);
            return;
        }
        //请求参数加入requestParams
        Set<String> keySet = maps.keySet();
        for (String key : keySet) {
            String value = maps.get(key);
            requestParams.addParameter(key, value);
        }

        //签名sign参数
        String sign = SignCreate.newCreateSignature(maps);
        requestParams.addParameter("sign", sign);

        //JSONArray jsonArray = new JSONArray(requestParams);
        //SmartLog.i(TAG, "请求服务器数据(post):url = " + url + "参数" + jsonArray.toString());
        SmartLog.i(TAG, "请求服务器数据(post):url = " + url);
        SmartLog.i(TAG, "HttpUtil签名参数" + sign);

        //执行请求
        requestParams.setCacheSize(1000 * 60);
        Callback.Cancelable cancelable
                = x.http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Message msg = Message.obtain();
                //获取状态码
                String code = JsonUtil.getJsonCode(result);
                SmartLog.i(TAG, "HttpUtil请求(post)状态码:" + code);
                SmartLog.i(TAG, "HttpUtil请求(post)结果: result = " + result);

                if (code != null && !"".equals(code)) {
                    //服务器内部错误
                    if (Integer.parseInt(code) == 400) {
                        msg.what = Constants.SERVER_ERROR;
                    } else if (Integer.parseInt(code) == 200) {//连接成功
                        msg.what = Constants.CONNECT_SUCCESS;
                    }
                }
                msg.obj = result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //吐司弹框 错误信息
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                SmartLog.i(TAG, "HttpUtil请求(post)错误信息:" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                SmartLog.i(TAG, "HttpUtil请求(post)完成");
            }
        });
        //取消
        cancelable.cancel();

    }*/

    /**
     * 发送 HTTP POST请求
     *
     * @param url    网址
     * @param params 参数
     * @throws Exception
     */
    public void post(String url, RequestParams params, final ResultListener mListener)
            throws Exception {
        //判断是否有网络连接
        if (!MyApplication.isNetworkConnected()) {
            Toast.makeText(MyApplication.getContext(), "无可用的网络连接", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(url)) {
            SmartLog.i(TAG, "请求服务器数据(post),url为null");
        }
        //请求值
        //JSONArray jsonArray = new JSONArray(params);
        //SmartLog.i(TAG, "请求服务器数据（post）：url = " + url + "，参数：" + jsonArray.toString());
        if (params != null) {
            //默认缓存存活时间, 单位:毫秒
            //params.setCacheSize(1000 * 60);
            //执行请求
            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    //获取状态码
                    String code = JsonUtil.getJsonCode(result);
                    SmartLog.i(TAG, "HttpUtil请求(post)状态码:" + code);

                    if (code != null && !"".equals(code)) {
                        //判断请求响应状态码，状态码为200便是服务端成功响应了客户端的请求
                        if (Integer.parseInt(code) == 200) {
                            SmartLog.i(TAG, "服务器返回信息(post): result = " + result);
                            //传递数据
                            mListener.getHttpResult(result);
                        } else {
                            SmartLog.i(TAG, "服务器返回异常!");
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    //吐司弹框 错误信息
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    SmartLog.i(TAG, "onError: HttpUtil请求(post)错误信息:" + ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    SmartLog.i(TAG, "onFinished: HttpUtil请求(post)完成");
                }
            });
        }
    }

    /**
     * g发送HTTP GET请求
     *
     * @param url    网址
     * @param params 参数
     * @return 返回值
     * @throws Exception
     */
    public void get(String url, RequestParams params, final ResultListener mListener) throws Exception {

        //判断是否有网络连接
        if (!MyApplication.isNetworkConnected()) {
            Toast.makeText(MyApplication.getContext(), "无可用的网络连接", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(url)) {
            SmartLog.i(TAG, "请求服务器数据(get),url为null");
        }
        //请求值
        //SmartLog.i(TAG, "请求服务器数据（get）：url = " + url);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //获取状态码
                String code = JsonUtil.getJsonCode(result);
                SmartLog.i(TAG, "HttpUtil请求(get)状态码:" + code);

                if (code != null && !"".equals(code)) {
                    //判断请求响应状态码，状态码为200便是服务端成功响应了客户端的请求
                    if (Integer.parseInt(code) == 200) {
                        SmartLog.i(TAG, "服务器返回信息(get): result = " + result);
                        //传递数据
                        mListener.getHttpResult(result);
                    } else {
                        SmartLog.i(TAG, "服务器返回异常!");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //吐司弹框 错误信息
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                SmartLog.i(TAG, "HttpUtil请求(get)错误信息:" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                SmartLog.i(TAG, "HttpUtil请求(get)完成");
            }
        });
    }


    /**
     * * 上传文件到服务器 httpurlconnection写起
     *
     * @param url      请求链接
     * @param filePath 文件路径
     * @return 服务器返回信息
     * @throws Exception
     */
    public static String uploadFile(String url, String filePath)
            throws Exception {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(filePath)) {
            // 参数为空
            LogUtil.i("uploadFile参数为null，取消上传");
            return null;
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            // 文件不存在
            LogUtil.i("uploadFile文件为null，取消上传");
            return null;
        }
        LogUtil.i("开始上传图片：" + file.getPath());
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--"; // 前缀
        String LINE_END = "\r\n"; // 换行
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        // 设置请求参数
        HttpURLConnection conn = (HttpURLConnection) new URL(url)
                .openConnection();
        conn.setReadTimeout(20 * 1000);// 设置获取获取读取输入流的超时时间
        conn.setConnectTimeout(20 * 1000);// 设置连接超时时间
        conn.setDoInput(true); // 允许输入流
        conn.setDoOutput(true); // 允许输出流
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST"); // 请求方式
        conn.setRequestProperty("Charset", "utf-8"); // 设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                + BOUNDARY);
        // 写入数据
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        // 写入前缀
        StringBuffer sb = new StringBuffer();
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        /**
         * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名
         */
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                + file.getName() + "\"" + LINE_END);
        sb.append("Content-Type: application/octet-stream; charset=" + "utf-8"
                + LINE_END);
        sb.append(LINE_END);
        dos.write(sb.toString().getBytes());
        // 写入文件数据
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[8192];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
            dos.write(bytes, 0, len);
        }
        is.close();
        dos.write(LINE_END.getBytes());
        // 写入后缀
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
        dos.write(end_data);
        dos.flush();
        /**
         * 获取响应码 200=成功 当响应成功，获取响应的流
         */
        int response = conn.getResponseCode();
        LogUtil.i("上传文件返回码：" + response);
        if (response == 200) {
            InputStream input = conn.getInputStream();
            StringBuffer sb1 = new StringBuffer();
            int ss;
            while ((ss = input.read()) != -1) {
                sb1.append((char) ss);
            }
            result = sb1.toString();
            LogUtil.i("上传文件成功：" + result);
        } else {
            LogUtil.i("上传文件失败！");
        }
        return result;
    }

}
