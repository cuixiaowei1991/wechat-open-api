package com.cn.weixin;

import com.cn.struts2.BaseAction;
import com.cn.weixin.protocol.commenUtil;
import com.cn.weixin.protocol.httpsPostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scala.annotation.meta.param;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuixiaowei on 2016/6/21.
 */
public class ForWeiXin extends BaseAction {
    private static Logger logger = Logger
            .getLogger(ForWeiXin.class);
    private thirdPartyPlatformFunction tppfFunction;
    /**
     * 门店创建接口
     */
    public JSONObject createTheStore(JSONObject storeInfo, JSONArray photo_url_array) {
        JSONObject requestJSON = new JSONObject();
        JSONObject business = new JSONObject();
        JSONObject base_info = new JSONObject();
        // logger.debug("上传门店信息开始： " + storeInfo.getBusinessName());
        String sid = "";
        String appid="";
        JSONObject rjson = new JSONObject();
        try {
            sid = storeInfo.getString("shopId");
            appid=storeInfo.getString("appid");
            // ------------------------------------------图片----------------------------------------
            if (!"error".equals(photo_url_array)) {
                //JSONArray photo_list = new JSONArray();
                //JSONObject photo_urlJSON = new JSONObject();
                // String a
                // ="\/\/mmbiz.qpic.cn\/mmbiz\/Geghiaib1vF2jJW5aEkHic9po3ib2yPWKiahThzbJCehf1hmearOJu8bnh8w2b7mBg71HuTLM9qHEVJbhXIequmibRSQ\/0";
                //photo_urlJSON.put("photo_url", photo_url);
                //photo_list.put(photo_url_array);
                // 图片列表，url 形式，可以有多张图片，尺寸为640*340px。必须为上一接口生成的url
                // 是
                base_info.put("photo_list", photo_url_array);
            }
            // ------------------------------------------图片----------------------------------------
            // ----------------------------------------必填字段---------------------------------------
            Map<String, String> notNullPartKey = new HashMap<String, String>();
            notNullPartKey.put("businessName", "business_name");// 门店名称（仅为商户名，如：国美、麦当劳，不应包含地区、店号等信息，错误示例：北京国美）
            notNullPartKey.put("province", "province");// 门店所在的省份（直辖市填城市名,如：北京市）
            notNullPartKey.put("city", "city");// 门店所在城市
            notNullPartKey.put("address", "address");// 门店所在的详细街道地址（不要填写省市信息）
            notNullPartKey.put("telephone", "telephone");// 门店的电话（纯数字，区号、分机号均由“-”隔开）
            notNullPartKey.put("offsetType", "offset_type");// 坐标类型，1
            // 为火星坐标（目前只能选1）
            notNullPartKey.put("longitude", "longitude");// 门店所在地理位置的经度
            notNullPartKey.put("latitude", "latitude");// 门店所在地理位置的纬度（经纬度均为火星坐标，最好选用腾讯地图标记的坐标）
            notNullPartKey.put("special", "special");// 特色服务，如免费wifi，免费停车，送货上门等商户能提供的特色功能或服务
            notNullPartKey.put("openTime", "open_time");// 营业时间，24
            // 小时制表示，用“-”连接，如8:00-20:00
            for (String key : notNullPartKey.keySet()) {
                if (!storeInfo.isNull(key)) {
                    base_info.put(notNullPartKey.get(key), storeInfo.get(key));
                } else {
                    rjson.put("errcode", 003);
                    rjson.put("errmsg", storeInfo.get(key) + " is NULL");
                    break;
                }

            }
            // ----------------------------------------必填字段---------------------------------------
            // ----------------------------------------非必填字段---------------------------------------
            Map<String, String> canNullPartKey = new HashMap<String, String>();
            canNullPartKey.put("branchName", "branch_name");// 分店名称（不应包含地区信息、不应与门店名重复，错误示例：北京王府井店）
            canNullPartKey.put("district", "district");// 门店所在地区
            canNullPartKey.put("categories", "categories");// 门店的类型（详细分类参见分类附表，不同级分类用“,”隔开，如：美食，川菜，火锅）
            canNullPartKey.put("recommend", "recommend");// 推荐品，餐厅可为推荐菜；酒店为推荐套房；景点为推荐游玩景点等，针对自己行业的推荐内容
            canNullPartKey.put("introduction", "introduction");// 商户简介，主要介绍商户信息等
            canNullPartKey.put("avgPrice", "avg_price");// 人均价格，大于0 的整数
            canNullPartKey.put("shopId", "sid");// 商户自己的id，用于后续审核通过收到poi_id
            // 的通知时，做对应关系。请商户自己保证唯一识别性
            for (String key : canNullPartKey.keySet()) {
                if (!storeInfo.isNull(key)) {
                    base_info.put(canNullPartKey.get(key), storeInfo.get(key));
                }
            }
            // ----------------------------------------非必填字段---------------------------------------

            business.put("base_info", base_info);
            requestJSON.put("business", business);
        } catch (JSONException e) {
            logger.debug("拼接请求微信创建门店时的数据" + e.getMessage());
            e.printStackTrace();
        }
        if (rjson.length() == 0) {
            logger.debug(requestJSON.toString());
            String rStr = httpsPostMethod.doPostJson(
                    "http://api.weixin.qq.com/cgi-bin/poi/addpoi?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), requestJSON.toString()
            );
            logger.debug("门店创建结果：" + rStr);

            // JSONObject returnJSON = new JSONObject();
            try {
                rjson = new JSONObject(rStr);
            } catch (JSONException e) {
                logger.debug("解析请求微信创建门店结果" + e.getMessage());
                e.printStackTrace();
            }
        }

        return rjson;
    }


    //上传门店图片
    /**
     * 上传门店图片文件
     *

     *            请求的地址
     * @param imgFilePath
     * @param explanatory
     * @param mark
     *            标记：forever永久 temp临时
     * @return
     */
    public String uploadMedia(String appid,String storeId, String imgFilePath,
                              String explanatory, String mark) {

        String str_return = null;
        HttpsURLConnection conn = null;
        logger.debug(explanatory + "  请求微信https上传文件开始:");
        String access_token = tppfFunction.getAuthAccesstoken(appid);//商户appid
        String weixinMediaUrl = "";

        if (null == access_token) {
            str_return = "{\"errcode\":\"001\",\"errmsg\":\"获取access_token失败\"}";
        } else {
            if ("forever".equals(mark)) {
                weixinMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token="
                        + access_token;
            }
            else if("foreverImage".equals(mark))
            {
                weixinMediaUrl = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token="
                        + access_token + "&type=image";
            }
            else {
                weixinMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="
                        + access_token + "&type=image";
            }

            SSLContext sc;
            // 定义数据分隔符
            String boundary = "------------7da2miteno4c8";
            // 处理错误: javax.net.ssl.SSLProtocolException: handshake
            // alert:unrecognized_name
            System.setProperty("jsse.enableSNIExtension", "false");
            BufferedInputStream bufferedInput = null;
            try {
                sc = SSLContext.getInstance("SSL", "SunJSSE");
                sc.init(null,
                        new TrustManager[] { new TrustAnyTrustManager() },
                        new java.security.SecureRandom());
                URL console = new URL(weixinMediaUrl);
                conn = (HttpsURLConnection) console.openConnection();
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                DataOutputStream outputStream = new DataOutputStream(
                        conn.getOutputStream());
                // 读取图片的connection
                URL mediaUrl = new URL(imgFilePath);
                HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl
                        .openConnection();
                meidaConn.setDoOutput(true);
                meidaConn.setRequestMethod("GET");
                // 根据内容类型判断文件扩展名
                String fileExt = imgFilePath.substring(
                        imgFilePath.lastIndexOf(".") + 1, imgFilePath.length());// WeixinUtil.getFileEndWitsh(contentType);
                String contentType = meidaConn.getHeaderField("Content-Type");
                System.out.println(String.format("fileExt：%s", fileExt));
                // 请求体开始
                outputStream.write(("--" + boundary + "\r\n").getBytes());
               /* outputStream
                        .write(String
                                .format("Content-Disposition: form-data; name=\"media\"; filename=\"file1.%s\"\r\n",
                                        fileExt).getBytes());*/
                outputStream
                        .write(String
                                .format("Content-Disposition: form-data; name=\"media\"; filename=\""+commenUtil.getRandomString(5)+".%s\"\r\n",
                                        fileExt).getBytes());
                outputStream.write(String.format("Content-Type: %s\r\n\r\n",
                        contentType).getBytes());

                byte[] bytebuffer = new byte[8096];

                // 创建BufferedInputStream 对象
                bufferedInput = new BufferedInputStream(
                        meidaConn.getInputStream());
                int bytesRead = 0;

                // 从文件中按字节读取内容，到文件尾部时read方法将返回-1
                while ((bytesRead = bufferedInput.read(bytebuffer)) != -1) {
                    // 将读取的字节转为字符串对象
                    outputStream.write(bytebuffer, 0, bytesRead);
                }
                // 请求体结束
                outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
                outputStream.close();
                bufferedInput.close();

                //
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
                int code = conn.getResponseCode();
                if (HttpsURLConnection.HTTP_OK == code) {
                    String temp = in.readLine();

                    while (temp != null) {
                        if (str_return != null)
                            str_return += temp;
                        else
                            str_return = temp;
                        temp = in.readLine();
                    }
                }
                conn.disconnect();

            } catch (NoSuchAlgorithmException e) {
                str_return = "{\"errcode\":\"002\",\"errmsg\":\"图片上传失败\"}";
                logger.debug(explanatory + "  出现异常");
                e.printStackTrace();
            } catch (KeyManagementException e) {
                str_return = "{\"errcode\":\"002\",\"errmsg\":\"图片上传失败\"}";
                logger.debug(explanatory + "  出现异常");
                e.printStackTrace();
            } catch (MalformedURLException e) {
                str_return = "{\"errcode\":\"002\",\"errmsg\":\"图片上传失败\"}";
                logger.debug(explanatory + "  出现异常");
                e.printStackTrace();
            } catch (IOException e) {
                str_return = "{\"errcode\":\"002\",\"errmsg\":\"图片上传失败\"}";
                logger.debug(explanatory + "  出现异常");
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                str_return = "{\"errcode\":\"002\",\"errmsg\":\"图片上传失败\"}";
                logger.debug(explanatory + "  出现异常");
                e.printStackTrace();
            } finally {
                // 关闭 BufferedInputStream
                try {
                    if (bufferedInput != null)
                        bufferedInput.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }

        logger.debug(explanatory + "  的返回为:" + str_return + "@@");
        return str_return;
    }

    /**
     * 根据poid查询门店信息
     * @param jsonStr
     */
    public String getpoi(String jsonStr) {
        logger.debug("forWeixin根据poi_id查询门店信息  接收的请求参数：" + jsonStr);
        String rp="";
        try {
            JSONObject requestJSON = new JSONObject(jsonStr);
            String appid = requestJSON.getString("appid");//商户appid
            String poi_id = requestJSON.getString("poi_id");
            rp = httpsPostMethod.doPostJson(
                    "http://api.weixin.qq.com/cgi-bin/poi/getpoi?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), "{\"poi_id\":" + poi_id
                            + "}");
            logger.debug("查询门店信息返回结果：" + rp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * 查询门店列表
     * @param jsonStr
     */
    public String getpoilist(String jsonStr) {
        logger.debug("forWeixin查询门店列表 接收的请求参数：" + jsonStr);
        String rp="";
        try {
            JSONObject requestJSON = new JSONObject(jsonStr);
            String appid = requestJSON.getString("appid");
            String begin = requestJSON.getString("begin");
            String limit = requestJSON.getString("limit");

            rp = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/poi/getpoilist?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), "{\"begin\":" + begin
                            + ",\"limit\":" + limit + "}", "查询门店列表");
            logger.debug("查询门店列表返回结果：" + rp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * 创建卡券
     * @param couponInfo
     * @param logo_url
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public  String CreateCard(JSONObject couponInfo, String logo_url)
            throws JSONException, UnsupportedEncodingException {
        JSONObject requestJSON = new JSONObject();
        String sid = couponInfo.getString("sid");
        String appid = couponInfo.getString("appid");
        String card_type = couponInfo.getString("card_type");

        JSONObject card = new JSONObject();
        // 卡券类型。 卡券类型不同,票券数据类型不同
        card.put("card_type", card_type);

        JSONObject base_info = new JSONObject();
        // 卡券的商户logo，尺寸为300*300。
        // 是
        base_info.put("logo_url", logo_url);
        // ------------------------------必填字段----------------------------------
        String[] notNullPartKey = { "brand_name",// 商户名字,字数上限为12
                // 个汉字。（填写直接提供服务的商户名，第三方商户名填写在source
                // 字段)
                "code_type",// 显示的扫码类型,CODE_TYPE_BARCODE是一维码(条码)
                "title",// 券名，字数上限为9 个汉字。(建议涵盖卡券属性、服务及金额)
                "sub_title",// 券名的副标题，字数上限为18 个汉字。
                "color",// 券颜色。按色彩规范标注填写Color010-Color100
                "notice",// 使用提醒，字数上限为12 个汉字。（一句话描述，展示在首页，示例：请出示二维码核销卡券）
                "description",// 使用说明。长文本描述，可以分行，上限为1000 个汉字。
                "service_phone",// 客服电话

        };
        for (int i = 0; i < notNullPartKey.length; i++) {
            if (!couponInfo.isNull(notNullPartKey[i])) {
                base_info.put(notNullPartKey[i],
                        couponInfo.get(notNullPartKey[i]));
            } else {
                logger.debug("创建微信卡券接口必要参数\"" + notNullPartKey[i] + "\"为空.");
            }
        }
        // ------------------------------必填字段----------------------------------
        // -------------------------------特殊结构必填字段---------------------------
        if (!couponInfo.isNull("quantity")) {
            JSONObject sku = new JSONObject();
            // 卡券库存的数量。（不支持填写0或无限大）
            sku.put("quantity", couponInfo.getLong("quantity"));
            // 商品信息
            base_info.put("sku", sku);
        } else {
            logger.debug("创建微信卡券接口必要参数\"quantity\"为空.");
        }

        if (!couponInfo.isNull("type")) {
            JSONObject date_info = new JSONObject();
            // 使用时间的类型，仅支持选择一种时间类型的字段填入。
            // 1：固定日期区间，2：固定时长（自领取后按天算）
            // 是
            String type = couponInfo.getString("type");
            date_info.put("type", type);
            if ("DATE_TYPE_FIX_TIME_RANGE".equals(type)) {
                long begin_timestamp = couponInfo.getLong("begin_timestamp");
                long end_timestamp = couponInfo.getLong("end_timestamp");
                // type 为1 时专用，表示起用时间。
                // 是
                date_info.put("begin_timestamp", begin_timestamp);
                // type 为1 时专用，表示结束时间。
                // 是
                date_info.put("end_timestamp", end_timestamp);
            } else if ("DATE_TYPE_FIX_TERM".equals(type)) {
                int fixed_term = couponInfo.getInt("fixed_term");
                int fixed_begin_term = couponInfo.getInt("fixed_begin_term");
                // type 为2 时专用，表示自领取后多少天内有效。（单位为天）领取后当天有效填写0。
                date_info.put("fixed_term", fixed_term);
                // type 为2 时专用，表示自领取后多少天开始生效。（单位为天）
                date_info.put("fixed_begin_term", fixed_begin_term);
                if (!couponInfo.isNull("end_timestamp")) {
                    // 可用于DATE_TYPE_FIX_TERM时间类型，表示卡券统一过期时间，建议设置为截止日期的23:59:59过期。（东八区时间，单位为秒），设置了fixed_term卡券，当时间达到end_timestamp时卡券统一过期
                    date_info.put("end_timestamp",
                            couponInfo.getLong("end_timestamp"));
                }
            }
            // 使用日期，有效期的信息
            // 是
            base_info.put("date_info", date_info);
        } else {
            logger.debug("创建微信卡券接口必要参数\"type\"为空.");
        }
        // -------------------------------特殊结构必填字段---------------------------
        // --------------------------------非必填字段---------------------------------
        String[] canNullPartKey = { "use_custom_code",// 是否自定义code
                // 码。填写true或false，不填代表默认为false。该字段需单独申请权限，详情见三、开发者必读。
                "bind_openid",// 是否指定用户领取，填写true 或false。不填代表默认为否。
                "service_phone",// 客服电话
                "location_id_list",// 门店位置poiid。调用POI门店管理接口获取门店位置poiid。具备线下门店的商户为必填。
                "source",// 第三方来源名，例如同程旅游、大众点评。
                "custom_url_name",// 自定义跳转外链的入口名字。详情见活用自定义入口
                "center_title",// 卡券顶部居中的按钮，仅在卡券状态正常(可以核销)时显示，建议开发者设置此按钮时code_type选择CODE_TYPE_NONE类型。
                "center_sub_title",// 显示在入口下方的提示语，仅在卡券状态正常(可以核销)时显示
                "center_url",// 顶部居中的url，仅在卡券状态正常(可以核销)时显示。
                "custom_url",// 自定义跳转的URL。
                "custom_url_sub_title",// 显示在入口右侧的提示语。
                "promotion_url_name",// 营销场景的自定义入口名称。
                "promotion_url",// 入口跳转外链的地址链接。
                "promotion_url_sub_title",// 显示在营销入口右侧的提示语。
                "get_limit",// 每人可领券的数量限制,不填写默认为50。
                "can_share",// 卡券领取页面是否可分享。
                "can_give_friend",// 卡券是否可转赠。

        };
        for (int i = 0; i < canNullPartKey.length; i++) {
            if (!couponInfo.isNull(canNullPartKey[i])) {
                base_info.put(canNullPartKey[i],
                        couponInfo.get(canNullPartKey[i]));
            }
        }
        // --------------------------------非必填字段---------------------------------

        // --------------------------------创建子商户卡券专用字段---------------------
        if (!couponInfo.isNull("merchant_id")) {
            long merchant_id = couponInfo.getLong("merchant_id");
            JSONObject merchan = new JSONObject();
            merchan.put("merchant_id", merchant_id);
            base_info.put("sub_merchant_info", merchan);
        }
        // --------------------------------创建子商户卡券专用字段---------------------
        if ("GROUPON".equals(card_type)) {
            // 团购券
            // 这层的名称,根据卡券类型变化,子层数据也不同
            JSONObject groupon = new JSONObject();
            // String deal_detail = couponInfo.getString("deal_detail");
            // 团购券专用，团购详情
            groupon.put("deal_detail", couponInfo.get("deal_detail"));
            groupon.put("base_info", base_info);
            card.put("groupon", groupon);
        } else if ("DISCOUNT".equals(card_type)) {
            // 折扣券
            // 这层的名称,根据卡券类型变化,子层数据也不同
            JSONObject discountJSON = new JSONObject();
            // String discount = couponInfo.getString("discount");
            // 折扣券专用，表示打折额度（百分比）。填30 就是七折。
            discountJSON.put("discount", couponInfo.get("discount"));
            discountJSON.put("base_info", base_info);
            card.put("discount", discountJSON);
        } else if ("CASH".equals(card_type)) {
            // 代金券
            // 这层的名称,根据卡券类型变化,子层数据也不同
            JSONObject cash = new JSONObject();
            // String least_cost = couponInfo.getString("least_cost");
            // String reduce_cost = couponInfo.getString("reduce_cost");
            // 代金券专用，表示起用金额（单位为分）
            cash.put("least_cost", couponInfo.get("least_cost"));
            // 代金券专用，表示减免金额（单位为分）
            cash.put("reduce_cost", couponInfo.get("reduce_cost"));
            cash.put("base_info", base_info);
            card.put("cash", cash);
        } else if ("GENERAL_COUPON".equals(card_type)) {
            // 优惠券
            // 这层的名称,根据卡券类型变化,子层数据也不同
            JSONObject general_coupon = new JSONObject();
            // String default_detail = couponInfo.getString("default_detail");
            // 优惠券专用，填写优惠详情。
            general_coupon.put("default_detail",
                    couponInfo.get("default_detail"));
            general_coupon.put("base_info", base_info);
            card.put("general_coupon", general_coupon);
        }

        requestJSON.put("card", card);

        logger.debug("创建卡券接口最后拼接的数据" + requestJSON.toString());
        String return_info=httpsPostMethod.sendHttpsPost("https://api.weixin.qq.com/card/create?access_token="
                + tppfFunction.getAuthAccesstoken(appid), requestJSON.toString(), "创建微信卡券");
        logger.debug("创建微信卡券返回的数据@@@@@@@@@@@@@@@@@@@@@@"+return_info);
        return return_info;

    }

    /**
     * 微信发红包
     * @param url  微信红包api地址
     * @param data
     * @return
     * @throws Exception
     */
    public static String doSendMoney(String url, String data) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        //  FileInputStream instream = new FileInputStream(new File(KEYSTORE_FILE));//P12文件目录
        //测试路径-----------------------------------------------------
        InputStream instream = commenUtil.class.getResourceAsStream("");
        //测试路径结束---------------------------------------------------
        try {
            keyStore.load(instream, "".toCharArray());//这里写密码..默认是你的MCHID
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "".toCharArray())//这里也是写密码的(商户号)
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = commenUtil.toStringInfo(response.getEntity(), "UTF-8");

                //微信返回的报文时GBK，直接使用httpcore解析乱码
                //  String jsonStr = EntityUtils.toString(response.getEntity(),"UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    /**
     * 查询自定义菜单
     * @param json
     * @return
     */
    public String getMenu(String json) {
        String rp="";
        try {
            logger.debug("接收查询自定义菜单参数：" + json);
            JSONObject returnJSON = new JSONObject(json);
            String appid = returnJSON.getString("appid");

            rp = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), "", "查询自定义菜单");
            logger.debug("查询自定义菜单结果为：" + rp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * 获取自定义菜单配置接口（如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置）
     * @param json
     * @return
     */
    public String getPeiZhiMenu(String json) {
        String rp="";
        try {
            logger.debug("接收查询自定义菜单配置接口参数：" + json);
            JSONObject returnJSON = new JSONObject(json);
            String appid = returnJSON.getString("appid");

            rp = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), "", "接收查询自定义菜单配置接口参数");
            logger.debug("接收查询自定义菜单配置接口参数：" + rp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rp;
    }
    /**
     * 创建自定义菜单
     *
     */
    public String createMenu(String appid, String menuStr) {
        return httpsPostMethod.sendHttpsPost(
                "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="
                        + tppfFunction.getAuthAccesstoken(appid), menuStr, "创建自定义菜单");
    }

    /**
     * 删除自定义菜单
     * @param
     * @return
     */
    public String deleteMenu(String appid)
    {
        String rp="";
        try {
            logger.debug("接收查询自定义菜单配置接口参数：" + appid);

            rp = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), "", "删除自定义菜单");
            logger.debug("删除自定义菜单微信回应：" + rp);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rp;

    }

    /**
     * Apay向微信查看卡券详情
     *
     * @param json
     */
    public String cardGet(String json) {
        logger.debug("接收Apay向微信查看卡券详情参数：" + json);
        JSONObject returnJSON;
        String rp="";
        try {
            returnJSON = new JSONObject(json);
            String appid = returnJSON.getString("appid");
            String CardId = returnJSON.getString("CardId");
            rp = httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/card/get?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), "{\"card_id\":\"" + CardId
                            + "\"}", "卡券系统向微信查看卡券详情");
            logger.debug("卡券系统向微信查看卡券详情结果为：" + rp);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * 创建会员卡
     * @param couponInfo
     * @param logo_url
     * @return
     */
    public String CreateMemberCard(JSONObject couponInfo, String logo_url) throws JSONException {
        JSONObject card = new JSONObject();//根目录
        JSONObject message=new JSONObject();//卡券信息
        String sid = couponInfo.getString("sid");
        String appid = couponInfo.getString("appid");
        String card_type = couponInfo.getString("card_type");
        String prerogative=couponInfo.getString("prerogative");//会员卡特权说明
        message.put("card_type",card_type);//会员卡类型
        JSONObject member_card=new JSONObject();//卡券信息
        member_card.put("supply_bonus",true);
        member_card.put("supply_balance",false);
        member_card.put("prerogative",prerogative);
        member_card.put("auto_activate",true);
        member_card.put("activate_url","https://www.hao123.com/");

        message.put("member_card","");//h会员卡信息
        card.put("card","");
        return "";
    }

    /**
     * 获取永久素材列表（紧图片）
     * @param json
     * @return
     */
    public String batchget_material(String json){
        String result="";
        try {
            logger.debug("接收获取素材列表（仅图文）参数：" + json);
            JSONObject returnJSON = new JSONObject(json);
            String appid = returnJSON.getString("appid");
            String count = returnJSON.getString("count");
            String offset = returnJSON.getString("offset");

            JSONObject param=new JSONObject();
            param.put("type", "image");
            param.put("offset", offset);
            param.put("count", count);
            String rp= httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), param.toString(), "获取素材列表");
            logger.debug("获取素材列表返回结果：" + rp);
            result=rp;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public String getMediaListCount(String json)
    {
        logger.debug("接收获取素材列表（仅图文）参数：" + json);

        String appid = null;
        String result="";
        try {
            JSONObject returnJSON = new JSONObject(json);
            appid = returnJSON.getString("appid");
            String rp= httpsPostMethod.sendHttpsPost(
                    "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token="
                            + tppfFunction.getAuthAccesstoken(appid), "", "获取素材列表");
            logger.debug("获取素材总数返回结果：" + rp);
            result=rp;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 主动发送微信消息给用户
     * @param json
     * @return
     */
    public String sendWeiXinMsg(String json)
    {
        logger.debug("主动发送微信消息给用户：" + json);


        String result="";
        try {
            JSONObject returnJSON = new JSONObject(json);
            String appid = returnJSON.getString("appid");
            String openid = returnJSON.getString("openid");
            String content = returnJSON.getString("content");
            String msgType = returnJSON.getString("msgType");

            if("text".equals(msgType))
            {
                result = httpsPostMethod.sendHttpsPost(
                        "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
                                + tppfFunction.getAuthAccesstoken(appid), "{\"touser\":\"" + openid
                                + "\",\"msgtype\":\"text\",\"text\":{\"content\":\""+content+"\"}}", "发送文本消息");
            }
            else if("image".equals(msgType))
            {
                result = httpsPostMethod.sendHttpsPost(
                        "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
                                + tppfFunction.getAuthAccesstoken(appid), "{\"touser\":\"" + openid
                                + "\",\"msgtype\":\"image\",\"image\":{\"media_id\":\""+content+"\"}}", "发送图片消息");
            }
            else if("voice".equals(msgType))
            {
                result = httpsPostMethod.sendHttpsPost(
                        "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
                                + tppfFunction.getAuthAccesstoken(appid), "{\"touser\":\"" + openid
                                + "\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\""+content+"\"}}", "发送语音消息");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     *威富通推送广告
     * openid 微信用户openid
     *  appid 微信appid
     * attach 支付备注
     * total_fee 支付金额
     * transaction_id 交易单号
     *  dateStr_new 交易时间（yyyyMMddHHmmss）
     * @return
     * @throws JSONException
     */
    public String sendWeiFuTong(String json) {
        String rp="";
        try {
            logger.debug("威富通推送广告参数：" + json);
            JSONObject returnJSON = new JSONObject(json);
            String appid = returnJSON.getString("appid");
            String openid = returnJSON.getString("openid");
            String attach = returnJSON.getString("attach");
            String total_fee = returnJSON.getString("total_fee");
            String transaction_id = returnJSON.getString("transaction_id");
            String dateStr_new = returnJSON.getString("dateStr_new");

            rp=tppfFunction.sendWeiFuTong(openid,appid,attach,total_fee,transaction_id,dateStr_new);

            logger.debug("威富通推送广告：" + rp);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * 获取威富通广告返回前台
     * @return
     */
    public String returnWeiFuTongAder(String json)
    {
        String rp="";
        try {
            logger.debug("威富通推送广告参数：" + json);
            JSONObject returnJSON = new JSONObject(json);
            String appid = returnJSON.getString("appid");
            String openid = returnJSON.getString("openid");
            String mark = returnJSON.getString("mark");
            if("1".equals(mark))
            {//威富通广告
                rp= tppfFunction.getAdvert(openid,appid,"");
            }

        }catch (Exception e)
        {

        }
        return rp;
    }
    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }



    public thirdPartyPlatformFunction getTppfFunction() {
        return tppfFunction;
    }

    public void setTppfFunction(thirdPartyPlatformFunction tppfFunction) {
        this.tppfFunction = tppfFunction;
    }
}
