package com.artdna.config;

public class Urls {
//	public static final String URL_SERVER_IP = "222.76.211.238"; // 正式
//	public static final String URL_SERVER_PORT = "8080"; // 正式

	/**
	 * 获取服务器根目录URL
	 */
	public static final String GET_SERVER_ROOT_URL() {
//        return "http://" + URL_SERVER_IP + ":" + URL_SERVER_PORT + "/";
        return "http://www.wealthartbank.com/";
	}

    /**
     * 获取dnaid（印刷编号）
     */
    public static String GET_DNA_ID_URL() {
        return GET_SERVER_ROOT_URL() + "art_manage/getDnaStockByUid.htm";
    }

    /**
     * 获取艺术品信息
     */
    public static String GET_DNA_INFO_URL() {
        return GET_SERVER_ROOT_URL() + "art_manage/getProductByDNATarget.do";
    }
    /**
     * 从通宝商城接口，获取艺术品信息
     */
    public static String GET_DNA_INFO_URL_BY_TONG_BAO() {
        return "http://tongb.cn/ECW-MallMgt/commodity_com/getProductByDNATarget.ihtml";
    }

//    /**
//     * 获取艺术品收藏信息
//     */
//    public static String GET_DNA_COLLECT_INFO_URL() {
//        return GET_SERVER_ROOT_URL() + "HomYGJ/art_manage/getArtCollectioByArtId.htm";
//    }

    /**
     * 获取艺术品收藏信息
     * http://222.76.211.238:8080/HomYGJ/art_manage/getArtCollectioByDnaId.htm?dnaId=T00000051
     */
    public static String GET_DNA_COLLECT_INFO_URL() {
        return GET_SERVER_ROOT_URL() + "art_manage/getArtCollectioByDnaId.htm";
    }
	
}
