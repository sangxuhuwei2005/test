package com.guohang.zsu1.comment.center.domain.constant;

/**
 * @Author: 胡宝喜
 * @Date: 2019/1/16 13:44
 * @Description: 评价状态
 */
public enum CommentStatus {
    NO_CHECK(1,"未审核"),NEED_MANUAL_CHECK(2,"需人工审核"),PASS(3,"审核通过"),NO_PASS(4,"审核不通过");
    private CommentStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public Integer getStatus() {
        return status;
    }
    private Integer status;
    private String desc;
}
