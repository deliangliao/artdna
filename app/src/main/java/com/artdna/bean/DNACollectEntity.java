package com.artdna.bean;

import java.io.Serializable;
import java.util.List;

public class DNACollectEntity implements Serializable {
//    public String artId;//艺术品id
//    public String collector;//收藏人
//    public String startDate;//收藏开始时间
//    public String endDate;//收藏结束时间
//    public String remark;//收藏简介
//    public String pic;
//    public String sortNum;//排序

    public int RtnCode;
    public String RtnMsg;
    public List<CollectItem> data;

    public class CollectItem implements Serializable {
        public String id;//艺术品id
        public String artName;//艺术品id
        public String collector;//收藏人
        public String startDate;//收藏开始时间
        public String endDate;//收藏结束时间
        public String remark;//收藏简介
        public String pic;
        public String sortNum;//排序
    }
}
