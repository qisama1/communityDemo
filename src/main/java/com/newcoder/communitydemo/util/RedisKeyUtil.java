package com.newcoder.communitydemo.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":"; // 拼接
    private static final String PREFIX_ENTITY_LIKE = "like:entity"; // 前缀

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
