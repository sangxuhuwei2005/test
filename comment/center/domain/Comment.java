package com.guohang.zsu1.comment.center.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * @Author: 胡宝喜
 * @Date: 2019/1/16 17:14
 * @Description: 评价表
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class Comment {

    @ApiModelProperty(value = "评价ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    @ApiModelProperty(dataType="java.lang.String",value = "评价时间，格式 yyyy-MM-dd HH:mm:ss")
    private Timestamp gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    @ApiModelProperty(dataType="java.lang.String",value = "修改时间，格式 yyyy-MM-dd HH:mm:ss")
    private Timestamp gmtModified;


    @Range(min = 1,max=2,message = "评价主体类型取值范围[1,2]")
    @ApiModelProperty(value = "评价主体类型  ,1：医生  2：医院")
    private Integer commentObjType;

    @NotNull(message = "评价主体ID 不能为空")
    @Min(value = 1,message = "评价主体ID 为正整数")
    @ApiModelProperty(value = "评价主体ID 正整数",required = true)
    private Long commentObjId;

    @NotNull(message = "评价主体名称 不能为空")
    @Size(min = 1,max = 200,message = "评价主体名称 长度为[1,200]")
    @ApiModelProperty(value = "评价主体名称 长度为[1,200]",required = true)
    private String commentObjName;

    @NotNull(message = "患者ID不能为空")
    @Min(value = 1,message = "患者ID为正整数")
    @ApiModelProperty(value = "患者ID 正整数",required = true)
    private Long userId;

    @Size(min = 1,max = 48,message = "昵称长度为[1,48]")
    @ApiModelProperty(value = "患者昵称")
    private String userNickName;

    @Size(min = 5,max = 200,message = "头像长度为[1,200]")
    @ApiModelProperty(value = "患者头像")
    private String userProfileUrl;

    @ApiModelProperty(value = "状态, 1：未审核   2：待人工审核  3：审核通过  4：未通过审核")
    private Integer status;

    @NotNull(message = "评分不能为空")
    @Range(min = 1, max=10, message = "评分范围[1,10]")
    @ApiModelProperty(value = "评分, 取值范围[1,10]",required = true)
    private Integer score;


    @Size(min = 5,max = 500,message = "评价内容 长度范围为[5,500]")
    @ApiModelProperty(value = "评价内容，长度为[5,500]")
    private String commentContent;


    @Size(min = 5,max = 1200,message = "评价图片，长度范围为[5,1200]")
    @ApiModelProperty(value = "评价图片，长度范围为[5,1200]")
    private String pictures;


    @Range(min = 0,max = 1,message = "精选标志，[0,1]")
    @ApiModelProperty(value = "精选标志，0：普通 1：精选")
    private Integer choiceFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh" , timezone="GMT+8")
    @ApiModelProperty(dataType="java.lang.String",value = "评价被选择为精选的时间，格式 yyyy-MM-dd HH:mm:ss")
    private Timestamp gmtChoice;

    @Size(min = 5,max = 200,message = "评价首图，长度范围为[5,200]")
    @ApiModelProperty(value = "评价首图，长度范围为[5,200]")
    private String firstPicture;


    @ApiModelProperty(value = "被浏览次数")
    private Integer visitCount;

    @Size(min = 1,max = 50,message = "评价标签，长度范围为[1,50]")
    @ApiModelProperty(value = "评价标签，多个标签用逗号分隔 长度范围[1,50]")
    private String tags;

    @Range(min = 1, max=10, message = "环境评分范围[1,10]")
    @ApiModelProperty(value = "环境评分, 取值范围[1,10]",required = true)
    private Integer envScore;

    @Range(min = 1, max=10, message = "服务评分范围[1,10]")
    @ApiModelProperty(value = "服务评分, 取值范围[1,10]",required = true)
    private Integer serviceScore;

    /**医生评价里的所患疾病、治疗方式、疗效等不参与查询的属性都放到这个扩展字段**/
    @Size(min = 3,max = 200,message = "扩展属性，长度范围为[3,200]")
    @ApiModelProperty(value = "扩展属性，JSON字符串 长度范围[3,200]  示例：" +
            "{\"diseaseName\":\"所患疾病\",\n " +
            "\"curativeEffect\":\"治愈状态\",\n " +
            "\"treatment\":\"治疗方式\"}")
    private String extAttributes;

    @ApiModelProperty(value = "被点赞次数")
    private Integer likeCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Integer getChoiceFlag() {
        return choiceFlag;
    }

    public void setChoiceFlag(Integer choiceFlag) {
        this.choiceFlag = choiceFlag;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public Integer getCommentObjType() {
        return commentObjType;
    }

    public void setCommentObjType(Integer commentObjType) {
        this.commentObjType = commentObjType;
    }

    public Long getCommentObjId() {
        return commentObjId;
    }

    public void setCommentObjId(Long commentObjId) {
        this.commentObjId = commentObjId;
    }

    public String getCommentObjName() {
        return commentObjName;
    }

    public void setCommentObjName(String commentObjName) {
        this.commentObjName = commentObjName;
    }

    public Timestamp getGmtChoice() {
        return gmtChoice;
    }

    public void setGmtChoice(Timestamp gmtChoice) {
        this.gmtChoice = gmtChoice;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getEnvScore() {
        return envScore;
    }

    public void setEnvScore(Integer envScore) {
        this.envScore = envScore;
    }

    public Integer getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Integer serviceScore) {
        this.serviceScore = serviceScore;
    }

    public String getExtAttributes() {
        return extAttributes;
    }

    public void setExtAttributes(String extAttributes) {
        this.extAttributes = extAttributes;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
