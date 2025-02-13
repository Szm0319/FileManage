package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        log.info("接收到登录用户信息："+user);
        // 验证用户名和密码
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }

        // 用户验证成功后，生成 JWT Token
        String token = jwtUtil.generateToken(user.getUsername());  // 使用 JwtUtil 生成 Token
        // 将 token 存入 Redis，设置过期时间为1小时
//        redisTemplate.opsForValue().set("TOKEN_" + user.getUsername(), token, 1, TimeUnit.HOURS);

        // 返回 token
        return ResponseEntity.ok().body(token);
    }

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        log.info("接收到注册用户信息："+user);
        // 检查用户名是否已经存在
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(400).body("用户名已存在");
        }

        // 设置创建时间和更新时间
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 保存用户到数据库
        userService.save(user);

        return ResponseEntity.status(201).body("注册成功");
    }
}
