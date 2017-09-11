package com.sky.util;

import com.show.api.ShowApiRequest;
import com.sky.model.menu.almanac.Almanac;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 17-8-31.
 */
public class APIUtil {

    private static final String APPID = "45212";
    private static final String SECRET = "3356fa8c10e94b60af1bdd63e75d15a8";

    //黄历
    private static final String ALMANAC_URL = "http://route.showapi.com/856-1";

    public static Almanac getAlmanac(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String res = new ShowApiRequest(ALMANAC_URL, APPID, SECRET)
                .addTextPara("date", sdf.format(date))
                .post();
        Almanac am = new Almanac();
        try {
            JSONObject obj = new JSONObject(res);
            if (obj.has("showapi_res_code") && obj.has("showapi_res_body") && obj.getString("showapi_res_code").equals("0")) {
                obj = (JSONObject) obj.get("showapi_res_body");
                if (obj.has("gongli")) {//公历日期
                    am.setGongli(obj.getString("gongli"));
                }
                if (obj.has("nongli")) {//农历日期
                    am.setNongli(obj.getString("nongli"));
                }
                if (obj.has("ganzhi")) {//干支
                    am.setGanzhi(obj.getString("ganzhi"));
                }
                if (obj.has("nayin")) {//纳音
                    String nayin = obj.getString("nayin");
                    am.setNayin(nayin);
                    if(nayin.contains("[日]")){
                        if(nayin.split("\\[日\\]").length==2){
                            am.setWuxing(nayin.split("\\[日\\]")[1]);
                        }
                    }
                }
                if (obj.has("shengxiao")) {//生肖
                    am.setShengxiao(obj.getString("shengxiao"));
                }
                if (obj.has("xingzuo")) {//星座
                    am.setXingzuo(obj.getString("xingzuo"));
                }
                if (obj.has("sheng12")) {//十二神
                    am.setSheng12(obj.getString("sheng12"));
                }
                if (obj.has("zhiri")) {//值日
                    am.setZhiri(obj.getString("zhiri"));
                }
                if (obj.has("chongsha")) {//冲煞
                    am.setChongsha(obj.getString("chongsha"));
                }
                if (obj.has("tszf")) {//胎神吉方
                    am.setTszf(obj.getString("tszf"));
                }
                if (obj.has("pzbj")) {//彭祖百忌
                    am.setPzbj(obj.getString("pzbj"));
                }
                if (obj.has("jrhh")) {//今日合害
                    am.setJrhh(obj.getString("jrhh"));
                }
                if (obj.has("yi")) {//宜
                    am.setYi(obj.getString("yi"));
                }
                if (obj.has("ji")) {//忌
                    am.setJi(obj.getString("ji"));
                }
                if (obj.has("jsyq")) {//吉神宜趋
                    am.setJsyq(obj.getString("jsyq"));
                }
                if (obj.has("jieqi24")) {//节气
                    am.setJieqi24(obj.getString("jieqi24"));
                }
                if (obj.has("jieri")) {//节日
                    am.setJieri(obj.getString("jieri"));
                }
                Map<String, Map<String, String>> scMap = new HashMap<>();
                if (obj.has("t3")) {//寅时 3-5
                    Map<String, String> map = parseShichen(obj.getString("t3"));
                    scMap.put("yinshi", map);
                }
                if (obj.has("t21")) {//亥时 21-23
                    Map<String, String> map = parseShichen(obj.getString("t21"));
                    scMap.put("haishi", map);
                }
                if (obj.has("t1")) {//丑时 1-3
                    Map<String, String> map = parseShichen(obj.getString("t1"));
                    scMap.put("choushi", map);
                }
                if (obj.has("t23")) {//子时 23-1
                    Map<String, String> map = parseShichen(obj.getString("t23"));
                    scMap.put("zishi", map);
                }
                if (obj.has("t11")) {//午时 11-13
                    Map<String, String> map = parseShichen(obj.getString("t11"));
                    scMap.put("wushi", map);
                }
                if (obj.has("t13")) {//未时 13-15
                    Map<String, String> map = parseShichen(obj.getString("t13"));
                    scMap.put("weishi", map);
                }
                if (obj.has("t15")) {//申时 15-17
                    Map<String, String> map = parseShichen(obj.getString("t15"));
                    scMap.put("shenshi", map);
                }
                if (obj.has("t17")) {//酉时 17-19
                    Map<String, String> map = parseShichen(obj.getString("t17"));
                    scMap.put("youshi", map);
                }
                if (obj.has("t19")) {//戌时 19-21
                    Map<String, String> map = parseShichen(obj.getString("t19"));
                    scMap.put("xushi", map);
                }
                if (obj.has("t5")) {//卯时 5-7
                    Map<String, String> map = parseShichen(obj.getString("t5"));
                    scMap.put("maoshi", map);
                }
                if (obj.has("t7")) {//辰时 7-9
                    Map<String, String> map = parseShichen(obj.getString("t7"));
                    scMap.put("chenshi", map);
                }
                if (obj.has("t9")) {//巳时 9-11
                    Map<String, String> map = parseShichen(obj.getString("t9"));
                    scMap.put("sishi", map);
                }
                am.setShichenMap(scMap);

                String shichen = getCurrentShichen();
                if(scMap.containsKey(shichen)){
                    Map<String, String> map = scMap.get(shichen);
                    if(null!=map){
                        String wuxing = map.get("wuxing");
                        if(null!=wuxing && !wuxing.equals("")){
                            am.setWuxing(wuxing);
                        }
                        String chongsha = map.get("chongsha");
                        if(null!=chongsha && !chongsha.equals("")){
                            am.setChongsha(chongsha);
                        }
                        String cai = map.get("cai");
                        if(null!=cai && !cai.equals("")){
                            am.setCai(cai);
                        }
                        String xi = map.get("xi");
                        if(null!=xi && !xi.equals("")){
                            am.setXi(xi);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return am;
    }

    private static Map<String, String> parseShichen(String content) {
        Map<String, String> map = new HashMap<>();
        String[] ary = content.split("\\r\\n");
        String colon = "：";
        for (String str : ary) {
            if (str.contains("吉凶：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    map.put("jixiong", str.split(colon)[1].substring(0,1));
                } else {
                    map.put("jixiong", "");
                }
                continue;
            }
            if (str.contains("时柱：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    String sz = str.split(colon)[1];
                    if(sz.contains("(") && sz.contains(")")){
                        map.put("wuxing", sz.substring(sz.indexOf("(")+1, sz.lastIndexOf(")")));
                    }else{
                        map.put("wuxing", sz);
                    }
                } else {
                    map.put("wuxing", "");
                }
                continue;
            }
            if (str.contains("冲煞：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    map.put("chongsha", str.split(colon)[1]);
                } else {
                    map.put("chongsha", "");
                }
                continue;
            }
            if (str.contains("正冲：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    map.put("zhengchong", str.split(colon)[1]);
                } else {
                    map.put("zhengchong", "");
                }
                continue;
            }
            if (str.contains("时宜：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    map.put("shiyi", str.split(colon)[1]);
                } else {
                    map.put("shiyi", "");
                }
                continue;
            }
            if (str.contains("时忌：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    map.put("shiji", str.split(colon)[1]);
                } else {
                    map.put("shiji", "");
                }
                continue;
            }
            if (str.contains("吉神：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    map.put("jishen", str.split(colon)[1]);
                } else {
                    map.put("jishen", "");
                }
                continue;
            }
            if (str.contains("凶煞：")) {
                String strAry[] = str.split(colon);
                if (strAry.length == 2) {
                    map.put("xiongsha", str.split(colon)[1]);
                } else {
                    map.put("xiongsha", "");
                }
                continue;
            }
            if (str.contains("财喜：")) {
                String[] caixi = str.split(colon);
                String cai = "", xi = "";
                if (caixi.length == 2) {
                    String cxAry[] = caixi[1].split(" ");
                    if (cxAry.length == 2) {
                        if (cxAry[0].length() == 4) {
                            cai = cxAry[0].substring(2);
                        }
                        if (cxAry[1].length() == 4) {
                            xi = cxAry[1].substring(2);
                        }
                    }
                }
                map.put("cai", cai);
                map.put("xi", xi);
                continue;
            }
        }
        return map;
    }

    public static String getCurrentShichen(){
        String shichen = "";
        SimpleDateFormat hhFormat = new SimpleDateFormat("HH");
        String hh = hhFormat.format(new Date());
        switch(hh){
            case "00": case "23":
                shichen = "zishi";//子时、壬子时
                break;
            case "01": case "02":
                shichen = "choushi";//丑时、癸丑时
                break;
            case "03": case "04":
                shichen = "yinshi";//寅时、甲寅时
                break;
            case "05": case "06":
                shichen = "maoshi";//卯时、乙卯时
                break;
            case "07": case "08":
                shichen = "chenshi";//辰时、丙辰时
                break;
            case "09": case "10":
                shichen = "sishi";//巳时、丁巳时
                break;
            case "11": case "12":
                shichen = "wushi";//午时、戊午时
                break;
            case "13": case "14":
                shichen = "weishi";//未时、己未时
                break;
            case "15": case "16":
                shichen = "shenshi";//申时、庚申时
                break;
            case "17": case "18":
                shichen = "youshi";//酉时、辛酉时
                break;
            case "19": case "20":
                shichen = "xushi";//戌时、壬戌时
                break;
            case "21": case "22":
                shichen = "haishi";//亥时、癸亥时
                break;
            default:
                break;
        }
        return shichen;
    }
}