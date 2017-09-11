package com.sky.model.menu.almanac;

import java.io.Serializable;
import java.util.Map;

/**
 * 黄历
 *
 * @author Administrator
 */
public class Almanac implements Serializable{

    private String gongli;// 公历
    private String nongli;// 农历
    private String ganzhi;// 干支
    private String nayin;// 纳音
    private String wuxing;//五行
    private String shengxiao;// 生肖
    private String xingzuo;// 星座
    private String sheng12;// 十二神
    private String zhiri;// 值日
    private String chongsha;// 冲煞
    private String tszf;// 胎神吉方
    private String pzbj;// 彭祖百忌
    private String jrhh;// 今日合害
    private String yi;// 宜
    private String ji;// 忌
    private String jsyq;// 吉神宜趋
    private String jieqi24;// 24节气
    private String jieri;// 节日
    private String cai;
    private String xi;
    public static String[] scNames = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    public static String[] scJx = {"吉", "凶", "吉", "吉", "吉", "吉", "吉", "吉", "吉", "吉", "凶", "凶"};

    // 时辰宜忌
    Map<String, Map<String, String>> shichenMap;

    public String getGongli() {
        return gongli;
    }

    public void setGongli(String gongli) {
        this.gongli = gongli;
    }

    public String getNongli() {
        return nongli;
    }

    public void setNongli(String nongli) {
        this.nongli = nongli;
    }

    public String getGanzhi() {
        return ganzhi;
    }

    public void setGanzhi(String ganzhi) {
        this.ganzhi = ganzhi;
    }

    public String getNayin() {
        return nayin;
    }

    public void setNayin(String nayin) {
        this.nayin = nayin;
    }

    public String getShengxiao() {
        return shengxiao;
    }

    public void setShengxiao(String shengxiao) {
        this.shengxiao = shengxiao;
    }

    public String getXingzuo() {
        return xingzuo;
    }

    public void setXingzuo(String xingzuo) {
        this.xingzuo = xingzuo;
    }

    public String getSheng12() {
        return sheng12;
    }

    public void setSheng12(String sheng12) {
        this.sheng12 = sheng12;
    }

    public String getZhiri() {
        return zhiri;
    }

    public void setZhiri(String zhiri) {
        this.zhiri = zhiri;
    }

    public String getChongsha() {
        return chongsha;
    }

    public void setChongsha(String chongsha) {
        this.chongsha = chongsha;
    }

    public String getTszf() {
        return tszf;
    }

    public void setTszf(String tszf) {
        this.tszf = tszf;
    }

    public String getPzbj() {
        return pzbj;
    }

    public void setPzbj(String pzbj) {
        this.pzbj = pzbj;
    }

    public String getWuxing() {
        return wuxing;
    }

    public void setWuxing(String wuxing) {
        this.wuxing = wuxing;
    }

    public String getJrhh() {
        return jrhh;
    }

    public void setJrhh(String jrhh) {
        this.jrhh = jrhh;
    }

    public String getYi() {
        return yi;
    }

    public void setYi(String yi) {
        this.yi = yi;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getJsyq() {
        return jsyq;
    }

    public void setJsyq(String jsyq) {
        this.jsyq = jsyq;
    }

    public String getJieqi24() {
        return jieqi24;
    }

    public void setJieqi24(String jieqi24) {
        this.jieqi24 = jieqi24;
    }

    public String getJieri() {
        return jieri;
    }

    public void setJieri(String jieri) {
        this.jieri = jieri;
    }

    public String getCai() {
        return cai;
    }

    public void setCai(String cai) {
        this.cai = cai;
    }

    public String getXi() {
        return xi;
    }

    public void setXi(String xi) {
        this.xi = xi;
    }

    public void setShichenMap(Map<String, Map<String, String>> shichenMap) {
        this.shichenMap = shichenMap;
    }

    public Map<String, Map<String, String>> getShichenMap() {
        return shichenMap;
    }

    @Override
    public String toString() {
        return "Almanac{" +
                "gongli='" + gongli + '\'' +
                ", nongli='" + nongli + '\'' +
                ", ganzhi='" + ganzhi + '\'' +
                ", nayin='" + nayin + '\'' +
                ", wuxing='" + wuxing + '\'' +
                ", shengxiao='" + shengxiao + '\'' +
                ", xingzuo='" + xingzuo + '\'' +
                ", sheng12='" + sheng12 + '\'' +
                ", zhiri='" + zhiri + '\'' +
                ", chongsha='" + chongsha + '\'' +
                ", tszf='" + tszf + '\'' +
                ", pzbj='" + pzbj + '\'' +
                ", jrhh='" + jrhh + '\'' +
                ", yi='" + yi + '\'' +
                ", ji='" + ji + '\'' +
                ", jsyq='" + jsyq + '\'' +
                ", jieqi24='" + jieqi24 + '\'' +
                ", jieri='" + jieri + '\'' +
                ", cai='" + cai + '\'' +
                ", xi='" + xi + '\'' +
                ", shichenMap=" + shichenMap +
                '}';
    }
}
