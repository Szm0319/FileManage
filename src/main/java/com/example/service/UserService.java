package com.example.service;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    // 根据用户名查找用户
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    // 根据用户ID查找用户
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    // 保存用户
    public void save(User user) {
        userMapper.save(user);
    }

    // 更新用户
    public void updateById(Long id, User user) {
        user.setId(id);  // 设置 ID 以便在更新时使用
        userMapper.updateById(id);  // 调用 updateById 方法
    }
}
