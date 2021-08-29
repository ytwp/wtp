# WTP - A reliable thread pool management system

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/ytwp/wtp)
[![Gitee Release](https://img.shields.io/badge/release-v1.0.0-blue)](https://gitee.com/ytwp/wtp/releases)
[![Maven Central Repo](https://img.shields.io/badge/maven%20central-v1.0.0-blue)](https://search.maven.org/artifact/wang.yeting/wtp-core)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

### 介绍
WTP 是一个轻量级线程池管理系统，能够集中管理不同应用、不同集群的线程配置，修改配置后能够实时刷新，使用起来，简单易用。

支持JAVA客户端，可在Spring/Spring Boot环境下运行

支持JKD1.8，以及更高版本。

演示 :
- [demo.wtp.yeting.wang](http://demo.wtp.yeting.wang/)
- 账号：wtpwtp
- 密码：123456
- client连接地址：http://121.36.54.20:5000
- [演示环境部署方案](https://github.com/ytwp/wtp/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2%E6%96%B9%E6%A1%88#%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1-%E6%96%B9%E5%BC%8F)

> gitee：[https://gitee.com/ytwp/wtp](https://gitee.com/ytwp/wtp)
>
> github：[https://github.com/ytwp/wtp](https://github.com/ytwp/wtp)
>
> oschina：[https://www.oschina.net/p/wtp-admin](https://www.oschina.net/p/wtp-admin)

### 开源许可协议
本项目基于 [ApacheLicense-2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) 开源许可协议.

### 特点
+ **统一管理**
  + 在Web页面可轻松管理多个应用、多个集群、多个线程池参数配置，动态修改。
  
+ **实时生效**
  + 在Web页面修改完成配置后，客户端能实时（1秒）接收到配置，并更新线程池参数，达到动态修改的目的。

* **阈值报警**
  * 队列长度使用百分比、线程活跃度百分比，进行告警，已支持邮箱、企业微信、钉钉告警方式，并且支持自定义告警方式。
  
* **线程数运行日志**
  * 统计线程池运行状态，活跃线程数、最大创建线程数、队列长度、队列剩余长度、队列排队长度、任务完成数、任务完成平均时间、发生rejected次数、rejected处理方式等等... ，可快速了解线程池工作状况。
  
* **动态修改**
  * 不重启服务：支持修改核心线程数、最大线程数、队列长度（已提供两种可修改长度队列）、空闲线程回收时间、rejected处理方式。
  * 重启服务：支持修改核心线程数、最大线程数、队列长度、队列类型、空闲线程回收时间、rejected处理方式。
  
* **国际化**
  * Web端支持国际化设置，提供中文、英文两种语言，默认为中文。
    
* **分布式**
  * wtp-admin支持HA部署。
  
### 使用说明
  * [接入指南](https://gitee.com/ytwp/wtp/wikis/%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97?sort_id=2867497)
 
### 发布历史
  * [发布历史](https://gitee.com/ytwp/wtp/releases)

### 常见问题
  * [常见问题](https://gitee.com/ytwp/wtp/wikis/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98?sort_id=2867496)

### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

### 支持
<table>
  <thead>
    <th>微信：8364245</th>
  </thead>
</table>

### 截图
##### 线程池管理页面:
![线程池管理页面](https://img.yeting.wang/wtp/3.png)
##### 线程池运行报表:
![线程池运行报表](https://img.yeting.wang/wtp/5.png)
##### 线程池日志页面:
![线程池日志页面](https://img.yeting.wang/wtp/1.png)
![线程池日志页面](https://img.yeting.wang/wtp/2.png)
##### 告警通知页面（可扩展）
|Email|企业微信|钉钉|
|:--|:--|:--|
|![Email](https://img.yeting.wang/wtp/email.jpg)|![企业微信](https://img.yeting.wang/wtp/wecom.jpg)|![钉钉](https://img.yeting.wang/wtp/dingding.jpg)|



