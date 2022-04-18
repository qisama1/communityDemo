package com.newcoder.communitydemo;

import com.mysql.cj.log.Log;
import com.newcoder.communitydemo.dao.DiscussPostMapper;
import com.newcoder.communitydemo.dao.LoginTicketMapper;
import com.newcoder.communitydemo.dao.UserMapper;
import com.newcoder.communitydemo.entity.DiscussPost;
import com.newcoder.communitydemo.entity.LoginTicket;
import com.newcoder.communitydemo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityDemoApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser() {
        System.out.println(mapper.selectByName("niuke"));

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

    @Test
    public void testInsertTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    @Test
    public void testSelectTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);


    }
}
