package com.kinnon.util;

import com.kinnon.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kinnon
 * @create 2022-08-06 12:49
 */
@Component
public class HostHolder {


    private ThreadLocal<User> local = new ThreadLocal();

    public User getUser(){
        return local.get();
    }

    public void setUser(User user){
        local.set(user);
    }

    public void clear(){
        local.remove();
    }
}
