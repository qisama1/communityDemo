package com.newcoder.communitydemo.util;

public interface CommunityConstant {
    /**
     * 激活成功
     */
    static final int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    static final int ACTIVATION_REPEAT = 1;

    /**
     * 重复激活
     */
    static final int ACTIVATION_FAILURE = 2;

    /**
     * 默认的登录凭证时间
     */
    static final int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 选择记住我以后
     */
    static final int REMEMBER_EXPIRED_SECONDS = 100 * 3600 * 24;

    /**
     * 实体类型、帖子
     */
    static final int ENTITY_TYPE_POST = 1;
    /**
     * 实体类型、常量
     */
    static final int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型：用户
     */
    static final int ENTITY_TYPE_USER = 3;

    /**
     * 主题：评论
     */
    static final String TOPIC_COMMENT = "comment";

    /**
     * 主题：点赞
     */
    static final String TOPIC_LIKE = "like";

    /**
     * 主题：关注
     */
    static final String TOPIC_FOLLOW = "follow";

    /**
     * 系统用户ID
     */
    static final int SYSTEM_USER_ID = 1;
}
