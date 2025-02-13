# FileManage
基于springboot的文件上传管理系统

本项目是2025年实习工作中领导要求开发的内部系统

技术栈：springboot+mysql+mybatis+vue3+element-plus

主要完成功能如下：

1.用户的登录注册功能

2.文件的上传下载功能

3.文件列表查询和删除功能

开发过程中解决的问题：

1.使用JWT编码Token替换了之前开发中使用Redis来设置过期时间，完成用户的身份认证

3.解决了频繁遇到的各种浏览器的跨域请求问题

4.解决了大文件的下载BLOB类型产生NETWORK ERROR问题

5.使用后端拦截器来拦截每一次非登录注册请求来验证用户身份

6.前端设置request拦截器携带username和token信息

7.解决了刷新页面后保持用户登录状态的问题

![image](https://github.com/user-attachments/assets/ff2a4ef6-e813-45fa-a505-66ab666e9a49)
![image](https://github.com/user-attachments/assets/123678ac-94d2-4a98-941a-f13b771ce395)

