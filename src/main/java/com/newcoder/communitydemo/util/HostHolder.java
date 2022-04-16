package com.newcoder.communitydemo.util;

import com.newcoder.communitydemo.entity.User;
import org.springframework.stereotype.Component;

/**
 * 起到一个容器的作用，持有用户的信息，用户代替session对象
 * 把users信息存在ThreadLocal当中去
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<User>();

    public void setUsers(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
