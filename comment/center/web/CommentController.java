package com.guohang.zsu1.comment.center.web;

import com.guohang.zsu1.comment.center.domain.Comment;
import com.guohang.zsu1.comment.center.domain.constant.CommentObjType;
import com.guohang.zsu1.comment.center.service.CommentService;
import io.swagger.annotations.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 胡宝喜
 * @Date: 2019/1/18 09:23
 * @Description: TODO
 */
@Api(value="评价", tags="评价")
@RestController
@Validated
@RequestMapping("/v1")
public class CommentController {

    @Autowired
    CommentService commentService;


    @ApiOperation(value = "评价医生",notes = "评价医生")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", value = "评价", required = true, dataType = "Comment", paramType = "body"),
            @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "/doctor/comment",method = RequestMethod.POST)
    void addDoctorComment(@ApiIgnore @Min(value = 1,message = "大于0的整数") @RequestParam("sessionUserId") Long sessionUserId,
                          @Valid @RequestBody Comment comment){
        comment.setCommentObjType(CommentObjType.DOCTOR.getType());
        comment.setScore(comment.getScore() * 10);//扩大10倍
        comment.setUserId(sessionUserId);
        commentService.checkComment(comment);
        commentService.addComment(comment);
    }

    @ApiOperation(value = "评价医院",notes = "评价医院")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment", value = "评价", required = true, dataType = "Comment", paramType = "body"),
            @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "/hospital/comment",method = RequestMethod.POST)
    void addHospitalComment(@ApiIgnore @Min(value = 1,message = "大于0的整数") @RequestParam("sessionUserId") Long sessionUserId,
                          @Valid @RequestBody Comment comment){
        comment.setCommentObjType(CommentObjType.HOSPITAL.getType());
        comment.setScore(comment.getScore() * 10);//扩大10倍
        comment.setEnvScore(comment.getEnvScore()==null?0:comment.getEnvScore() * 10);//扩大10倍
        comment.setServiceScore(comment.getServiceScore()==null?0:comment.getServiceScore() * 10);//扩大10倍
        comment.setUserId(sessionUserId);
        commentService.checkComment(comment);
        commentService.addComment(comment);
    }



    @ApiOperation(value = "删除我的评价",notes = "删除我的评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "/my/comment",method = RequestMethod.DELETE)
    void delCommentById(@NotNull(message = "评价ID不能为空") @Min(value = 1,message = "大于0的整数") @RequestParam("id") Long id,
                              @ApiIgnore @Min(value = 1,message = "大于0的整数") @RequestParam("sessionUserId") Long sessionUserId){
        commentService.delMyCommentById(id,sessionUserId);
    }

    @ApiOperation(value = "删除评价",notes = "删除评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "/comment",method = RequestMethod.DELETE)
    void delCommentById(Long id){
        commentService.delCommentById(id);
    }

    @ApiOperation(value = "获取需要人工审核的评价",notes = "获取需要人工审核的评价")
    @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header")
    @RequestMapping(value = "/manual/comment",method = RequestMethod.GET)
    List<Comment> getCommentByManualCheck(@ApiIgnore @Min(value = 1,message = "大于0的整数") @RequestParam("sessionUserId") Long sessionUserId){
        return commentService.getCommentByManualCheck(sessionUserId);
    }

    @ApiOperation(value = "评价审核通过",notes = "评价审核通过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价id", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "/doctor/comment/status/pass",method = RequestMethod.PUT)
    void setCommentCheckPass(@NotNull(message = "评价id 不能为空") @Min(value = 1,message = "评价ID为正整数") @RequestParam("id") Long id,
                                   @ApiIgnore @Min(value = 1,message = "大于0的整数") @RequestParam("sessionUserId") Integer sessionUserId){
        commentService.setCommentCheckPass(id);
    }


    @ApiOperation(value = "获取我对医生的所有评价",notes = "获取我对医生的所有评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/doctor/my/comments",method = RequestMethod.GET)
    List<Comment> getMyDoctorComments(@ApiIgnore @Min(value = 1,message = "大于0的整数") @RequestParam("sessionUserId") Long sessionUserId,
                                @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getMyComments(CommentObjType.DOCTOR.getType(),sessionUserId,from,pageSize);
    }

    @ApiOperation(value = "获取我对医院的所有评价",notes = "获取我对医院的所有评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "TOKEN", value = "Authorization token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/hospital/my/comments",method = RequestMethod.GET)
    List<Comment> getMyHospitalComments(@ApiIgnore @Min(value = 1,message = "大于0的整数") @RequestParam("sessionUserId") Long sessionUserId,
                                      @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                      @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getMyComments(CommentObjType.HOSPITAL.getType(),sessionUserId,from,pageSize);
    }

    @ApiOperation(value = "获取医生评价",notes = "获取医生评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "doctorId", value = "医生id", required = true, paramType = "form", dataType = "long"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/doctor/comments",method = RequestMethod.GET)
    List<Comment> getCommentByDoctorId(@NotNull(message = "医生ID不能为空") @Min(value = 1,message = "医生ID 正整数") @RequestParam("doctorId") Long doctorId,
                                             @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                             @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getCommentByDoctorId(doctorId, from, pageSize);
    }

    @ApiOperation(value = "获取医院评价",notes = "获取医院评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hospitalId", value = "医院id", required = true, paramType = "form", dataType = "long"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/hospital/comments",method = RequestMethod.GET)
    List<Comment> getCommentByHospitalId(@NotNull(message = "医院ID不能为空") @Min(value = 1,message = "医院ID 正整数") @RequestParam("hospitalId") Long hospitalId,
                                       @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                       @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getCommentByHospitalId(hospitalId, from, pageSize);
    }



    @ApiOperation(value = "获取医生评价-好评",notes = "获取医生评价-好评")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "doctorId", value = "医生id", required = true, paramType = "form", dataType = "long"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/doctor/positive/comments",method = RequestMethod.GET)
    public List<Comment> getPositiveCommentByDoctorId(@NotNull(message = "医生ID不能为空") @Min(value = 1,message = "医生ID 正整数") @RequestParam("doctorId") Long doctorId,
                                                            @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                                            @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getPositiveCommentByDoctorId(doctorId, from, pageSize);
    }

    @ApiOperation(value = "获取医院评价-好评",notes = "获取医院评价-好评")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hospitalId", value = "医院id", required = true, paramType = "form", dataType = "long"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/hospital/positive/comments",method = RequestMethod.GET)
    public List<Comment> getPositiveCommentByHospitalId(@NotNull(message = "医院ID不能为空") @Min(value = 1,message = "医院ID 正整数") @RequestParam("hospitalId") Long hospitalId,
                                                      @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                                      @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getPositiveCommentByHospitalId(hospitalId, from, pageSize);
    }


    @ApiOperation(value = "获取医生评价-差评",notes = "获取医生评价-差评")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "doctorId", value = "医生id", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/doctor/negative/comments",method = RequestMethod.GET)
    public List<Comment> getNegativeCommentByDoctorId(@NotNull(message = "医生ID不能为空") @Min(value = 1,message = "医生ID 正整数") @RequestParam("doctorId") Long doctorId,
                                                            @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                                            @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getNegativeCommentByDoctorId(doctorId, from, pageSize);
    }

    @ApiOperation(value = "获取医院评价-差评",notes = "获取医院评价-差评")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hospitalId", value = "医院id", required = true, paramType = "form", dataType = "long"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/hospital/negative/comments",method = RequestMethod.GET)
    public List<Comment> getNegativeCommentByHospitalId(@NotNull(message = "医院ID不能为空") @Min(value = 1,message = "医院ID 正整数") @RequestParam("hospitalId") Long hospitalId,
                                                            @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                                            @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getNegativeCommentByHospitalId(hospitalId, from, pageSize);
    }

    @ApiOperation(value = "根据主体名称查评价",notes = "根据主体名称查评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentObjName", value = "主体名称", required = true, paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/comments/search/name",method = RequestMethod.GET)
    List<Comment> getCommentByDoctorName(@NotNull(message = "主体名称不能为空") @Size(min = 1,max = 200,message = "主体名称 1-200位字符") @RequestParam("commentObjName") String commentObjName,
                                               @NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                               @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getCommentByCommentObjName(commentObjName, from, pageSize);
    }

    @ApiOperation(value = "设置评价为精选内容",notes = "设置评价为精选内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价ID", required = true, paramType = "form", dataType = "long")
    })
    @RequestMapping(value = "/comments/choiceflag",method = RequestMethod.POST)
    void setCommentChoiceFlag(@NotNull(message = "评价ID不能为空") @Min(value = 1,message = "评价ID 正整数") @RequestParam("id")  Long id){
        commentService.setCommentChoiceFlag(id);
    }

    @ApiOperation(value = "取消精选评价",notes = "取消精选评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价ID", required = true, paramType = "form", dataType = "long")
    })
    @RequestMapping(value = "/comments/choiceflag",method = RequestMethod.DELETE)
    void unsetCommentChoiceFlag(@NotNull(message = "评价ID不能为空") @Min(value = 1,message = "评价ID 正整数") @RequestParam("id") Long id){
        commentService.unsetCommentChoiceFlag(id);
    }

    @ApiOperation(value = "获取精选评价",notes = "获取精选评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/comments/choice",method = RequestMethod.GET)
    List<Comment> getCommentByChoice(@NotNull(message = "起始行号不能为空") @Range(min = 0,max=520,message = "起始行号 范围[0,520]") @RequestParam("from") Integer from,
                                           @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        if(from > 500)
            return new ArrayList<>();
        return commentService.getCommentByChoice(from, pageSize);
    }

    @ApiOperation(value = "获取普通评价",notes = "获取普通评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/comments/nochoice",method = RequestMethod.GET)
    List<Comment> getCommentByNoChoice(@NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                             @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getCommentByNoChoice(from, pageSize);
    }

    @ApiOperation(value = "获取全部评价",notes = "获取全部评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "页开始行号，起始第一行行号为0，不是1", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页中记录数", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/comments/all",method = RequestMethod.GET)
    List<Comment> getComments(@NotNull(message = "起始行号不能为空") @Min(value = 0,message = "起始行号 非负整数") @RequestParam("from") Integer from,
                                    @NotNull(message = "页大小不能为空") @Range(min=0, max=20,message="页大小为0-20") @RequestParam("pageSize") Integer pageSize){
        return commentService.getComments(from, pageSize);
    }

    @ApiOperation(value = "修改评价首图",notes = "修改评价首图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价ID", required = true, paramType = "form", dataType = "long"),
            @ApiImplicitParam(name = "firstPicture", value = "评价首图", required = true, paramType = "form", dataType = "string")
    })
    @RequestMapping(value = "/comments/firstpicture",method = RequestMethod.PUT)
    void updateDoctorCommentFirstPicture(@NotNull(message = "评价ID不能为空") @Min(value = 1,message = "评价ID 为正整数") @RequestParam("id") Long id,
                                         @NotNull(message = "评价首图不能为空") @Size(min = 5,max = 200,message = "评价首图 长度范围[5,200]") @RequestParam("firstPicture") String firstPicture){
        commentService.updateCommentFirstPicture(id, firstPicture);
    }

    @ApiOperation(value = "获取评价详情",notes = "获取评价详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价ID", required = true, paramType = "query", dataType = "long")
    })
    @RequestMapping(value = "/comment/id",method = RequestMethod.GET)
    Comment getCommentDetail(@NotNull(message = "评价ID不能为空") @Min(value = 1,message = "评价ID 为正整数") @RequestParam("id") Long id){
        return commentService.getCommentById(id);
    }
}
