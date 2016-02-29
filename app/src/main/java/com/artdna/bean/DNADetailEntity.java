package com.artdna.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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
    public List<String> photos;//证件
}
