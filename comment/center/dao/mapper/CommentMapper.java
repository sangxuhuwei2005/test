package com.guohang.zsu1.comment.center.dao.mapper;

import com.guohang.zsu1.comment.center.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: 胡宝喜
 * @Date: 2019/1/16 19:53
 * @Description: 评价
 */
@Mapper
public interface CommentMapper {

    @Insert("insert into comment(id,gmtCreate,gmtModified,commentObjType,commentObjId,commentObjName,userId,userNickName,userProfileUrl,score,status,commentContent,pictures,choiceFlag,gmtChoice,firstPicture,visitCount,tags,envScore,serviceScore,extAttributes,likeCount) " +
            "  values(#{id},now(),now(),#{commentObjType},#{commentObjId},#{commentObjName},#{userId},#{userNickName},#{userProfileUrl},#{score},#{status},#{commentContent},#{pictures},0,null,#{firstPicture},0,#{tags},#{envScore},#{serviceScore},#{extAttributes},0)")
    int insertComment(Comment comment);

    @Update("update comment set " +
            " gmtModified=now()," +
            " score=#{score}, " +
            " status=#{status}, " +
            " commentContent=#{commentContent}, " +
            " pictures=#{pictures}, " +
            " choiceFlag=0, " +
            " gmtChoice=null, " +
            " firstPicture=#{firstPicture}, " +
            " envScore=#{envScore}, " +
            " serviceScore=#{serviceScore}, " +
            " extAttributes=#{extAttributes} where id=#{id}")
    int updateComment(Comment comment);

    @Delete("delete from comment where id=#{id} and userId=#{userId}")
    int deleteCommentByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Delete("delete from comment where id=#{id}")
    int deleteCommentById(@Param("id") Long id);

    @Delete("delete from comment where commentObjType=#{commentObjType} and commentObjId=#{commentObjId}")
    int deleteCommentByCommentObj(@Param("commentObjType") Integer commentObjType,@Param("commentObjId") Long commentObjId);

    @Update("update comment set gmtModified=now(), status=#{status} where id=#{id}")
    int updateCommentStatus(@Param("id") Long id,@Param("status") Integer status);

    @Update("update comment set gmtModified=now(), firstPicture=#{firstPicture} where id=#{id}")
    int updateCommentFirstPicture(@Param("id") Long id, @Param("firstPicture") String firstPicture);

    @Select("select * from comment where commentObjType=#{commentObjType} and commentObjId=#{commentObjId} and status=3 order by id desc limit #{from},#{pageSize}")
    List<Comment> selectCommentByCommentObj(@Param("commentObjType") Integer commentObjType,@Param("commentObjId") Long commentObjId, @Param("from") Integer from, @Param("pageSize") Integer pageSize);

    @Select("select * from comment where commentObjType=#{commentObjType} and commentObjId=#{commentObjId} and status=3 and score >= 50 order by id desc limit #{from},#{pageSize}")
    List<Comment> selectPositiveCommentByCommentObj(@Param("commentObjType") Integer commentObjType,@Param("commentObjId") Long commentObjId, @Param("from") Integer from, @Param("pageSize") Integer pageSize);

    @Select("select * from comment where commentObjType=#{commentObjType} and commentObjId=#{commentObjId} and status=3 and score < 50 order by id desc limit #{from},#{pageSize}")
    List<Comment> selectNegativeCommentByCommentObj(@Param("commentObjType") Integer commentObjType,@Param("commentObjId") Long commentObjId, @Param("from") Integer from, @Param("pageSize") Integer pageSize);

    @Select("select * from comment where id=#{id}")
    Comment selectCommentById(@Param("id") Long id);

    @Select("select * from comment where  userId=#{userId} and commentObjType=#{commentObjType} order by id desc limit #{from},#{pageSize}")
    List<Comment>  selectCommentByUserId(@Param("commentObjType") Integer commentObjType,@Param("userId") Long userId, @Param("from") Integer from, @Param("pageSize") Integer pageSize);

    @Select("select *  from comment where status=#{status} and lockedBy=#{lockedBy} order by id desc")
    List<Comment> getLockCheckComments(@Param("lockedBy") Long lockedBy, @Param("status") Integer status);

    @Update("update comment set lockedBy=#{lockedBy},lockTime=now() where status=#{status} and (lockedBy=#{lockedBy} or lockTime is null or (unix_timestamp(now()) - unix_timestamp(lockTime)) > 600  ) order by id desc limit 20")
    int lockCheckComments(@Param("lockedBy") Long lockedBy,@Param("status") Integer status);

    @Select("select * from comment where commentObjName = #{commentObjName} order by id desc limit #{from},#{pageSize}")
    List<Comment> getCommentByCommentObjName(@Param("commentObjName") String commentObjName, @Param("from") Integer from, @Param("pageSize") Integer pageSize);


    @Update("update comment set gmtModified = now(),visitCount = visitCount + #{visitCount} where id=#{id}")
    int updateCommentVisitCount(@Param("id") Long id, @Param("visitCount") Integer visitCount);


    @Update("update comment set gmtModified=now(), choiceFlag=#{choiceFlag},gmtChoice=now() where id=#{id} and status=3")
    int updateCommentChoiceFlag(@Param("id") Long id,@Param("choiceFlag") Integer choiceFlag);

    @Select("select * from comment where choiceFlag = #{choiceFlag} order by gmtChoice desc limit #{from},#{pageSize}")
    List<Comment> getCommentByChoiceFlag(@Param("choiceFlag") Integer choiceFlag, @Param("from") Integer from, @Param("pageSize") Integer pageSize);

    @Select("select * from comment where status=3 order by id desc limit #{from},#{pageSize}")
    List<Comment> getComments(@Param("from") Integer from, @Param("pageSize") Integer pageSize);

    @Update("update comment set gmtModified=now(), likeCount=likeCount+#{likeCount} where id=#{id}")
    int updateCommentLikeCount(@Param("id") Long id,@Param("likeCount") Integer likeCount);

}
