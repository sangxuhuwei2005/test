package com.guohang.zsu1.comment.center.domain.constant;

/**
 * @Author: 胡宝喜
 * @Date: 2019/5/15 15:50
 * @Description: 评价对象类型
 */
public enum CommentObjType {
    DOCTOR(1,"医生"),HOSPITAL(2,"医院");
    private CommentObjType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    private Integer type;
    private String desc;
}
