package com.guohang.zsu1.comment.center.service.impl;

import com.guohang.zsu1.comment.center.service.CommentService;
import com.guohang.zsu1.like.center.domain.Like;
import com.guohang.zsu1.like.center.domain.constant.LikeType;
import com.guohang.zsu1.message.center.domain.LikeEvent;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 胡宝喜
 * @Date: 2019/5/22 16:28
 * @Description: TODO
 */
@Service
public class CommentCenterEventListener {

    @Autowired
    CommentService commentService;

    @Service
    @RocketMQMessageListener(topic = LikeEvent.TOPIC , consumerGroup = LikeEvent.LIKE_CONSUMER_GROUP_COMMENT_CENTER,selectorExpression = LikeEvent.TAG_COMMENT)
    public class ConsumerLikeEvent implements RocketMQListener<LikeEvent> {
        public void onMessage(LikeEvent likeEvent) {
            if(LikeEvent.ACTION_ADD.equals(likeEvent.getAction()))
                commentService.plusCommentLikeCount(likeEvent.getLike().getLikeContentId());
            else if(LikeEvent.ACTION_DEL.equals(likeEvent.getAction()))
                commentService.minusCommentLikeCount(likeEvent.getLike().getLikeContentId());
            else return;
        }
    }
}
