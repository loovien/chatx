## ChatX


<!-- vim-markdown-toc GFM -->

* [简介](#简介)
* [简单使用](#简单使用)
* [可以完善](#可以完善)

<!-- vim-markdown-toc -->

#### 简介

一个学习 netty 框架通信的项目。 (基于netty 实现的聊天DEMO, tcp 链接和 websocket实现互通)

```bash


tcp       ---> 
              |
              |-------> chatX(管理链接) --> 可以给tcp, websocket 同时发送消息。
              |
websocket --->

```

目录如下：

```bash
---chat
    |   ChatApplication.java  // springboot 启动
    |   ChatInitializr.java   // chatx 初始化的一些信息
    |   ChatTcpApplication.java // tcp 服务
    |   ChatWsApplication.java  // websocket 服务
    |
    +---clients
    |       TcpClient.java  // client test
    |
    +---comm
    |       Result.java // 通用返回结果
    |
    +---configs
    |       Json.java // jackson 配置
    |       Opts.java // application 配置
    |       TcpOps.java // tcp 配置
    |       WsOpts.java // websocket 配置
    |
    +---controller
    |   |   AbstractController.java 
    |   |   ChatController.java // 聊天controller
    |   |   UserController.java // 用户校验controller
    |   |
    |   \---handlers
    |           Handler.java
    |           Handlers.java
    |
    +---dto
    |       BizDTO.java // tcp 数据交换
    |       ChatDTO.java // 聊天数据 
    |       UserDTO.java // 用户数据 
    |
    +---exception
    |       BizException.java
    |       NotAuthException.java
    |
    +---tcp
    |       ChatTcpDecoder.java // 解码
    |       ChatTcpEncoder.java // 编码
    |       ChatTcpHandler.java // tcp 主handler
    |       Protocol.java // tcp 协议
    |
    \---ws
            Tcp2WsEncoder.java // tcp => websocket 编码
            WebSocketFrameHandler.java // websocket 主 handler
            
```
            
#### 简单使用

查看 clients.TcpClient.java, 连上去服务， 发送验证协议。 之后tcpclient会定时发消息。

websocket 通过连上去服务就能收到 tcp发的消息了。

以下代码可以放到 chrome 开发工具栏, 贴入， 可以看到效果。

```javascript

const ws = new WebSocket('ws://localhost:8001/ws');
ws.addEventListener("open", () => {console.log("socket open: send hi chatx"); ws.send('hi chatx');})
ws.addEventListener("message", (evt) => {console.log(evt.data)})
ws.addEventListener("close", () => { console.log('Connection closed');});

```

#### 可以完善

1. 完整的登录逻辑。
2. tcp, websocket 的数据交换。 目前就简单的处理了下。（Tcp2WsEncoder.java）。
            
            
