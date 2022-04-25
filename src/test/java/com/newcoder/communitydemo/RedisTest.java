package com.newcoder.communitydemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityDemoApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * String
     */
    @Test
    public void testStrings() {
        String redisKey = "test:count";
        // 存数据
        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }
    /**
     * 哈希
     */
    @Test
    public void testHash() {
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "username", "zhangsan");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    /**
     * 列表
     */
    @Test
    public void testList() {
        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey, 0));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0 , 2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    /**
     * 集合
     */
    @Test
    public void testSet() {
        String redisKey = "test:teacher";

        redisTemplate.opsForSet().add(redisKey, "刘备", "关羽", "张飞");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey)); // 这是随机弹出
        System.out.println(redisTemplate.opsForSet().members(redisKey));

    }

    /**
     * 有序集合
     */
    @Test
    public void testSortedSet() {
        String redisKey = "test:students";

        redisTemplate.opsForZSet().add(redisKey, "a", 100);
        redisTemplate.opsForZSet().add(redisKey, "b", 80);
        redisTemplate.opsForZSet().add(redisKey, "c", 60);
        redisTemplate.opsForZSet().add(redisKey, "d", 40);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey)); // 统计个数
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "a"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "b"));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));

    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));

        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);
    }

    // 多次访问同一个key
    @Test
    public void testBoundOperations() {
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
    }

    // 事务，但不严格满足acid
    @Test
    public void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";

                operations.multi(); // 启用事务
                operations.opsForSet().add(redisKey, "张三");
                operations.opsForSet().add(redisKey, "李四");
                operations.opsForSet().add(redisKey, "王五");
                System.out.println(operations.opsForSet().members(redisKey));
                // 事务执行前的查询无效
                return operations.exec(); // 提交事务
            }
        });
        System.out.println(obj);
    }

}
