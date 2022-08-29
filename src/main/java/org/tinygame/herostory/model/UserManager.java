package org.tinygame.herostory.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//用户管理器
public final class UserManager {

    //不需要实例化
    private UserManager(){}

    private static final Map<Integer,User> _userMap = new HashMap<>();

    /**
     * 添加用户
     * @param newUser
     */
    public static void addUser(User newUser){
        if (null != newUser){
            _userMap.put(newUser.userId, newUser);
        }
    }

    /**
     * 根据用户id移除用户
     * @param userId
     */
    public static void removeUserById(int userId){
        _userMap.remove(userId);
    }

    /**
     * 返回用户列表，类型为Collection
     * @return
     */
    public static Collection<User> listUser(){
        return _userMap.values();
    }



}
