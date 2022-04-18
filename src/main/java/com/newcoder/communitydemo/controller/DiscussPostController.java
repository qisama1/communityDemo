package com.newcoder.communitydemo.controller;

import com.newcoder.communitydemo.entity.Comment;
import com.newcoder.communitydemo.entity.DiscussPost;
import com.newcoder.communitydemo.entity.Page;
import com.newcoder.communitydemo.entity.User;
import com.newcoder.communitydemo.service.CommentService;
import com.newcoder.communitydemo.service.DiscussPostService;
import com.newcoder.communitydemo.service.UserService;
import com.newcoder.communitydemo.util.CommunityUtil;
import com.newcoder.communitydemo.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.newcoder.communitydemo.util.CommunityConstant.ENTITY_TYPE_COMMENT;
import static com.newcoder.communitydemo.util.CommunityConstant.ENTITY_TYPE_POST;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;
    /**
     * 添加帖子
     * @param title
     * @param content
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        // 报错之后统一处理
        return CommunityUtil.getJSONString(0, "发布成功");
    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 查询帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 评论信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        // 给帖子的叫评论
        // 给评论的叫回
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST,
                post.getId(), page.getOffset(), page.getLimit());
        // 评论的Vo列表
        List<Map<String, Object>> commentVoList =  new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                // 回复
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,
                        comment.getId(), 0, Integer.MAX_VALUE);
                // 回复的vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replies", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }

}
