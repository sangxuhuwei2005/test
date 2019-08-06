package com.guohang.zsu1.comment.center.service;

import com.guohang.zsu1.comment.center.domain.Comment;
import com.guohang.zsu1.security.center.content.check.domain.CheckEntity;

import java.util.List;

/**
 * @Author: 胡宝喜
 * @Date: 2019/1/17 11:12
 * @Description: TODO
 */
public interface CommentService {

    void checkComment(Comment comment);

    void addComment(Comment comment);

    void modifyComment(Comment comment);

    void delMyCommentById(Long id, Long userId);

    void delCommentById(Long id);


    void delCommentByDoctorId(Long doctorId);


    Comment getCommentById(Long id);

    List<Comment> getCommentByManualCheck(Long userId);

    void setCommentCheckPass(Long id);

    List<Comment> getMyComments(Integer commentObjType,Long userId, Integer from, Integer pageSize);

    List<Comment> getCommentByDoctorId(Long doctorId, Integer from, Integer pageSize);

    List<Comment> getCommentByHospitalId(Long hospitalId, Integer from, Integer pageSize);

    List<Comment> getPositiveCommentByDoctorId(Long doctorId, Integer from, Integer pageSize);

    List<Comment> getPositiveCommentByHospitalId(Long doctorId, Integer from, Integer pageSize);

    List<Comment> getNegativeCommentByDoctorId(Long doctorId, Integer from, Integer pageSize);

    List<Comment> getNegativeCommentByHospitalId(Long hospitalId, Integer from, Integer pageSize);

    List<Comment> getCommentByCommentObjName(String commentObjName, Integer from, Integer pageSize);

    void setCommentChoiceFlag(Long id);

    void unsetCommentChoiceFlag(Long id);

    void updateCommentFirstPicture(Long id, String firstPicture);

    List<Comment> getCommentByChoice(Integer from, Integer pageSize);

    List<Comment> getCommentByNoChoice(Integer from, Integer pageSize);

    List<Comment> getComments(Integer from, Integer pageSize);

    void plusCommentLikeCount(Long commentId);

    void minusCommentLikeCount(Long commentId);

    List<String> getCommentBuiltInFiles(Comment comment);

    CheckEntity getCommentCheckEntity(Comment comment);
}
