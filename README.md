## 项目简介
该项目是基于提升[MindMap](https://github.com/Erym5/mindMap)上线体验，集成站内聊天系统，基于(KT-Chat)开发

## 项目改善
1. 增加用户认证子模块，集成security oauth、jwt、jcasbin，为了满足 IM 系统中访问控制模型的动态性，选取 jcasbin 为鉴权框架，使用 PBAC 模型进
行集成，较 security 更为快速的开发与解耦；与 gateway 整合，进行路由、鉴权。
2. 完善Netty网络编程开发，增加线程池、建立路由表等

# 项目进度
未完待续

## 参考项目
[KT-Chat](https://github1s.com/KimTou/KT-Chat)
[Jcasbin-Demo](https://github.com/VINO42/jcasbin-springboot-demo)
[SpringCloud-Study](https://github1s.com/macrozheng/springcloud-learning)



