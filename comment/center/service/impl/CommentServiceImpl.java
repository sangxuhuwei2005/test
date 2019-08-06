package com.guohang.zsu1.comment.center.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.guohang.zsu1.common.exception.exceptiontype.PreconditionException;
import com.guohang.zsu1.common.id.SnowflakeIdWorker;
import com.guohang.zsu1.common.utils.JacksonUtil;
import com.guohang.zsu1.comment.center.dao.mapper.CommentMapper;
import com.guohang.zsu1.comment.center.domain.Comment;
import com.guohang.zsu1.comment.center.domain.constant.CommentChoiceFlag;
import com.guohang.zsu1.comment.center.domain.constant.CommentObjType;
import com.guohang.zsu1.comment.center.domain.constant.CommentStatus;
import com.guohang.zsu1.comment.center.service.CommentService;
import com.guohang.zsu1.doctor.center.service.DoctorService;
import com.guohang.zsu1.message.center.domain.CommentEvent;
import com.guohang.zsu1.message.center.service.MsgProducerService;
import com.guohang.zsu1.security.center.content.check.domain.CheckEntity;
import com.guohang.zsu1.security.center.content.check.domain.Constant.CheckResultCode;
import com.guohang.zsu1.security.center.content.check.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author: 胡宝喜
 * @Date: 2019/1/17 16:35
 * @Description: TODO
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CheckService checkService;

    @Autowired
    DoctorService doctorService;


    @Autowired
    private MsgProducerService msgProducerService;

    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public void addComment(Comment comment) {
        comment.setId(SnowflakeIdWorker.getId("comment"));
        commentMapper.insertComment(comment);
        msgProducerService.send(CommentEvent.TOPIC,new CommentEvent(CommentEvent.ACTION_ADD,comment));
    }

    private  List<String> getPicUrlsFromJson(String data){
        List<String> pics = new ArrayList<>();
        JacksonUtil.toJSonNode(data).forEach((JsonNode node)->{
            pics.add(node.findValue("url").asText());
        });
        if(pics.size() > 6)
            throw new PreconditionException("评价最多有6张图片",null);
        return pics;
    }
    @Override
    public void checkComment(Comment comment){
        CheckEntity checkEntity = this.getCommentCheckEntity(comment);
        if(checkEntity.getPicUrls()== null && checkEntity.getTxt() == null && checkEntity.getVideoUrls() == null)
            comment.setStatus(CommentStatus.PASS.getStatus());
        else{
            Integer check = checkService.syncCheckContent(checkEntity);
            if(check == CheckResultCode.PASS.getRescode())
                comment.setStatus(CommentStatus.PASS.getStatus());
            else if(check == CheckResultCode.NEED_MANUAL.getRescode())
                comment.setStatus(CommentStatus.NEED_MANUAL_CHECK.getStatus());
            else
                throw new PreconditionException("评价内容有非法内容",null);
        }
    }

    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public void modifyComment(Comment comment) {
        Comment origin = commentMapper.selectCommentById(comment.getId());
        if(origin == null)
            throw new PreconditionException("修改的评价不存在",null);
        if(!origin.getUserId().equals(comment.getUserId()))
            throw new PreconditionException("无权修改该评价",null);
        commentMapper.updateComment(comment);
        msgProducerService.send(CommentEvent.TOPIC,new CommentEvent(CommentEvent.ACTION_MODIFY,comment,origin));
    }

    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public void delMyCommentById(Long id, Long userId) {
        Comment origin = commentMapper.selectCommentById(id);
        if(origin == null)
            throw new PreconditionException("评价不存在",null);
        if(origin.getUserId() != userId)
            throw new PreconditionException("无权删除该评价",null);
        commentMapper.deleteCommentByIdAndUserId(id, userId);
        msgProducerService.send(CommentEvent.TOPIC,new CommentEvent(CommentEvent.ACTION_DEL,origin));
    }

    @Override
    public void delCommentById(Long id) {
        Comment origin = commentMapper.selectCommentById(id);
        if(origin == null)
            throw new PreconditionException("评价不存在",null);
        commentMapper.deleteCommentById(id);
        msgProducerService.send(CommentEvent.TOPIC,new CommentEvent(CommentEvent.ACTION_DEL,origin));
    }

    @Override
    public void delCommentByDoctorId(Long doctorId) {//删除医生时，调用该接口删除评价，后续不再删除评价
        commentMapper.deleteCommentByCommentObj(CommentObjType.DOCTOR.getType(),doctorId);
    }

    @Override
    public Comment getCommentById(Long id) {
        commentMapper.updateCommentVisitCount(id,1);
        return commentMapper.selectCommentById(id);
    }


    @Override
    public List<Comment> getMyComments(Integer commentObjType,Long userId, Integer from, Integer pageSize){
        return commentMapper.selectCommentByUserId(commentObjType,userId,from,pageSize);
    }

    @Override
    public List<Comment> getCommentByDoctorId(Long doctorId, Integer from, Integer pageSize){
        return commentMapper.selectCommentByCommentObj(CommentObjType.DOCTOR.getType(),doctorId,from,pageSize);
    }

    @Override
    public List<Comment> getCommentByHospitalId(Long hospitalId, Integer from, Integer pageSize) {
        return commentMapper.selectCommentByCommentObj(CommentObjType.HOSPITAL.getType(),hospitalId,from,pageSize);
    }

    @Override
    public List<Comment> getPositiveCommentByDoctorId(Long doctorId, Integer from, Integer pageSize) {
        return commentMapper.selectPositiveCommentByCommentObj(CommentObjType.DOCTOR.getType(),doctorId, from, pageSize);
    }

    @Override
    public List<Comment> getPositiveCommentByHospitalId(Long doctorId, Integer from, Integer pageSize) {
        return commentMapper.selectPositiveCommentByCommentObj(CommentObjType.HOSPITAL.getType(),doctorId, from, pageSize);
    }

    @Override
    public List<Comment> getNegativeCommentByDoctorId(Long doctorId, Integer from, Integer pageSize) {
        return commentMapper.selectNegativeCommentByCommentObj(CommentObjType.DOCTOR.getType(),doctorId, from, pageSize);
    }

    @Override
    public List<Comment> getNegativeCommentByHospitalId(Long hospitalId, Integer from, Integer pageSize) {
        return commentMapper.selectNegativeCommentByCommentObj(CommentObjType.HOSPITAL.getType(),hospitalId, from, pageSize);
    }

    @Override
    public List<Comment> getCommentByCommentObjName(String commentObjName, Integer from, Integer pageSize) {
        return commentMapper.getCommentByCommentObjName(commentObjName, from, pageSize);
    }

    @Override
    public void setCommentChoiceFlag(Long id) {
        commentMapper.updateCommentChoiceFlag(id, CommentChoiceFlag.YES.getFlag());
    }

    @Override
    public void unsetCommentChoiceFlag(Long id) {
        commentMapper.updateCommentChoiceFlag(id, CommentChoiceFlag.NO.getFlag());
    }

    @Override
    public void updateCommentFirstPicture(Long id, String firstPicture) {
        Comment origin = commentMapper.selectCommentById(id);
        if(origin == null)
            throw new PreconditionException("评价不存在",null);
        Comment comment = new Comment();
        comment.setId(id);
        comment.setFirstPicture(firstPicture);
        commentMapper.updateCommentFirstPicture(id,firstPicture);
    }

    @Override
    public List<Comment> getCommentByChoice(Integer from, Integer pageSize) {
        return commentMapper.getCommentByChoiceFlag(CommentChoiceFlag.YES.getFlag(),from,pageSize);
    }

    @Override
    public List<Comment> getCommentByNoChoice(Integer from, Integer pageSize) {
        return commentMapper.getCommentByChoiceFlag(CommentChoiceFlag.NO.getFlag(),from,pageSize);
    }

    @Override
    public List<Comment> getComments(Integer from, Integer pageSize) {
        return commentMapper.getComments(from, pageSize);
    }

    @Override
    public void plusCommentLikeCount(Long commentId) {
        commentMapper.updateCommentLikeCount(commentId,1);
    }

    @Override
    public void minusCommentLikeCount(Long commentId) {
        commentMapper.updateCommentLikeCount(commentId,-1);
    }

    @Override
    public List<String> getCommentBuiltInFiles(Comment comment) {
        List<String> pics = new ArrayList<>();
        if(comment.getPictures() != null)
            JacksonUtil.toJSonNode(comment.getPictures()).forEach((JsonNode node)->{
                pics.add(node.findValue("url").asText());
            });
        if(comment.getFirstPicture() != null)
            pics.add(comment.getFirstPicture());
        return pics;
    }

    @Override
    public CheckEntity getCommentCheckEntity(Comment comment) {
        CheckEntity checkEntity = new CheckEntity();
        if(StringUtils.hasText(comment.getCommentContent()))
            checkEntity.addTxt(comment.getCommentContent());
        if(StringUtils.hasText(comment.getPictures()))
            checkEntity.setPicUrls(getPicUrlsFromJson(comment.getPictures()));
        if(StringUtils.hasText(comment.getExtAttributes()))
            checkEntity.addTxt(comment.getExtAttributes());
        return checkEntity;
    }


    @Override
    public List<Comment> getCommentByManualCheck(Long userId) {
        commentMapper.lockCheckComments(userId.longValue(),CommentStatus.NEED_MANUAL_CHECK.getStatus());
        return commentMapper.getLockCheckComments(userId.longValue(),CommentStatus.NEED_MANUAL_CHECK.getStatus());
    }

    @Transactional(propagation=Propagation.REQUIRED)
    @Override
    public void setCommentCheckPass(Long id) {
        Comment origin = commentMapper.selectCommentById(id);
        if(origin == null)
            throw new PreconditionException("评价不存在",null);
        commentMapper.updateCommentStatus(id,CommentStatus.PASS.getStatus());
        msgProducerService.send(CommentEvent.TOPIC,new CommentEvent(CommentEvent.ACTION_PASS,origin));
    }
}
