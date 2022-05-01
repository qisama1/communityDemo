package com.newcoder.communitydemo.controller;

import com.newcoder.communitydemo.annotation.LoginRequired;
import com.newcoder.communitydemo.entity.Event;
import com.newcoder.communitydemo.entity.User;
import com.newcoder.communitydemo.event.EventProducer;
import com.newcoder.communitydemo.service.LikeService;
import com.newcoder.communitydemo.util.CommunityConstant;
import com.newcoder.communitydemo.util.CommunityUtil;
import com.newcoder.communitydemo.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User user = hostHolder.getUser();

        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 点赞数量和状态
        long entityLikeCount = likeService.findEntityLikeCount(entityType, entityId);
        int entityLikeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", entityLikeCount);
        map.put("likeStatus", entityLikeStatus);

        // 触发点赞事件
        if (entityLikeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fileEvent(event);
        }

        return CommunityUtil.getJSONString(0, null, map);
    }
}
