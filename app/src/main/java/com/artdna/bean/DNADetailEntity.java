package com.artdna.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//        artistInfo: "作家简介",
//        artName: "青花瓷",
//        RtnCode: "1",
//        certificateName: "鉴定证书",
//        photo: "/HomYGJ/upload/2016/03/08/2016030816072357721.jpg",
//        imgUrl: "/HomYGJ/upload/2016/01/22/2016012215302023378.jpg",
//        photos: "",
//        artAge: "明朝万历年间",
//        ArtType: "陶瓷",
//        artModel: "青花瓷-001",
//        artInfo: "",
//        RtnMsg: "OK",
//        issAuthority: "中国艺术品鉴定中心",
//        artistName: "张三疯",
//        createDate: null
public class DNADetailEntity implements Serializable{
    public String imgUrl;
    public String artName;
    public String artAge;
    public String artInfo;
    public String artModel;
    @SerializedName("artistName")
    public String artAuthor;
    @SerializedName("artistInfo")
    public String artAuthorInfo;
    public String ArtType;
    public String createDate;
    public String artPrice;//原来接口没有
    public int RtnCode;
    public String RtnMsg;

    public String certificateName;//证书名称
    public String issAuthority;//颁发机构
    public String photo;//证件
    public String photos;//证件，多张，以逗号隔开
}




