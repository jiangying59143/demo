package com.mall.demo.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.common.sms.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SmsUtils {

    public static final String VERIFICTION_CODE_TEMPLATE = "TP18091728";

    public static final String NOTE_PASSWORD_TEMPLATE = "TP18091730";

    public static void main(String[] args) throws ClientException {
        sendSMS("13814959143", "1234", VERIFICTION_CODE_TEMPLATE);
        sendSMS("13814959143", "JIANGYING", NOTE_PASSWORD_TEMPLATE);
    }

     //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "25074152";
    static final String accessKeySecret = "a1cc0d482e97c1953f01f9ebbd900b07";
    static final String appCode="08f9bf411a9244898de51e8dc76e4847";

    /**
     * 10000	参数异常	必传参数有空值()
     * 10001	手机号格式不正确	手机号应为11位手机号
     * 10002	模板不存在	没有申请模板,或模板未通过审核
     * 10003	模板变量不正确	模板中含有变量,但未将变量传入,变量传值格式错误
     * 10004	变量中含有敏感词	变量中含有违法敏感词
     * 10005	变量名称不匹配	申请的模板中含有变量名称,变量的名称与所传变量名称不匹配
     * 10006	短信长度过长	签名+模板+变量长度超过70字,超过一条短信长度,如果有超长短信需求请联系客服
     * 10007	手机号查询不到归属地	所传手机号查询不到归属地
     * 10008	产品错误	系统错误,详情请联系客服
     * 10009	价格错误	系统错误,详情请联系客服
     * 10010	重复调用	由于网络原因重复调用接口
     * 99999	系统错误	详情请联系客服
     * 00000	调用成功
     * @param phoneNumber
     * @param code
     * @return
     */
    public static Result sendSMS(String phoneNumber, String code, String templetaeId){
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String appcode = appCode;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNumber);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", templetaeId);
        Map<String, String> bodys = new HashMap<String, String>();
        String returnCode;
        try {
            Response response = com.mall.demo.common.sms.util.HttpUtil.httpPost(host, path, 10000,
                    headers,querys, bodys, null, accessKeyId, accessKeySecret);
            returnCode = response.getBody();
            System.out.println(returnCode);
            returnCode = JSON.parseObject(returnCode).get("return_code").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(ResultCode.ERROR);
        }
        if ("00000".equals(returnCode)){
            System.out.println("OK");
            return Result.success();
        }
        return Result.error(ResultCode.ERROR);
    }

    public static SendSmsResponse sendSms(String phoneNumber) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("08f9bf411a9244898de51e8dc76e4847");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("TP1711063");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"1234\"}");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("123");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId, String phoneNumber) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }
}