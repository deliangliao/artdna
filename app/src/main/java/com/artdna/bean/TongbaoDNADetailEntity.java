package com.artdna.bean;

import java.io.Serializable;

//{
//        ImgUrl: "http://images-10006228.image.myqcloud.com/1447387596891.jpg",
//        Introduction: "犀牛角西施雕像",
//        ArtType: "雕像",
//        RtnCode: "1",
//        CreateTime: "2015-11-13 13:30:48",
//        RtnMsg: "OK",
//        ArtYear: "不清楚",
//        ArtName: "西施雕像"
//}

public class TongbaoDNADetailEntity implements Serializable{
    public String ImgUrl;
    public String ArtName;
    public String ArtYear;
    public String Introduction;
    public String ArtType;
    public String CreateTime;
    public String artPrice;//原来接口没有
    public int RtnCode;
    public String RtnMsg;
}




