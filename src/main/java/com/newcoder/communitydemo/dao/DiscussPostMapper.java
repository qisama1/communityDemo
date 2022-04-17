package com.newcoder.communitydemo.dao;

import com.newcoder.communitydemo.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 支持了后期分页
     * @param userId
     * @param offset 数据偏移量
     * @param limit 每页的数据
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * @Param 是取别名，如果只有一个参数还要动态使用这个参数那么就要取别名
     * 在<if>里面使用
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);
}
