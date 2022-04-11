package com.newcoder.communitydemo;

import com.newcoder.communitydemo.dao.DiscussPostMapper;
import com.newcoder.communitydemo.dao.UserMapper;
import com.newcoder.communitydemo.entity.DiscussPost;
import com.newcoder.communitydemo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityDemoApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void testSelectUser() {
        User user = mapper.selectById(101);
        System.out.println(user);
    }

    @Test
    public void testDiscuss() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(103, 0, 10);
        for (DiscussPost post:
             list) {
            System.out.println(post);
        };
        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }
}
