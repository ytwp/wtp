# WTP - A reliable thread pool management system

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/ytwp/wtp)
[![GitHub Release](https://img.shields.io/badge/release-v1.0.0-blue)](https://github.com/ytwp/wtp/releases)
[![Maven Central Repo](https://img.shields.io/badge/maven%20central-v1.0.0-blue)](https://mvnrepository.com/artifact/wang.yeting/wtp-core)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

WTP 是一个轻量级线程池管理系统，能够集中管理不同应用、不同集群的线程配置，修改配置后能够实时刷新，使用起来，简单易用。

暂支持JAVA客户端，可在Spring/Spring Boot环境下运行

支持JKD1.8，以及更高版本。

演示（Demo）:
- [demo.wtp.yeting.wang](http://demo.wtp.yeting.wang/)
- 账号：wtpwtp
- 密码：123456
- client链接地址：http://121.36.54.20:5000
- [演示环境部署方案](https://github.com/ytwp/wtp/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2%E6%96%B9%E6%A1%88#%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1-%E6%96%B9%E5%BC%8F)

> 国内镜像：[gitee镜像](https://gitee.com/ytwp/wtp)

# License
The project is based on [ApacheLicense-2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

## Features 
+ **统一管理**
  + 在Web页面可轻松管理不同应用、不同集群、不用池的参数配置。
  
+ **实时生效**
  + 在Web页面修改完配置后，客户端能实时（1秒）接收到配置，并更新线程池配置。

+ **客户端运行监控**
  + 监控线程池运行状况，进行统计分析，可快速了解线程池当前的工作状况。

* **阈值报警**
  * 支持队列长度、线程活跃度阈值，进行告警。
  
* **线程数允许日志**
  * 统计线程池运行状态，线程数，队列数，rejected次数等等...。
  
* **动态修改**
  * 支持修改核心线程数，最大线程数，队列长度，rejected处理方式等等...。
  
* **国际化**
  * Web端支持国际化设置，提供中文、英文两种语言，默认为中文。
    
* **分布式**
  * wtp-admin支持HA部署。
  
# Usage
  * [接入指南](https://github.com/ytwp/wtp/wiki/%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97)
 
# Release Notes
  * [发布历史](https://github.com/ytwp/wtp/releases)

# FAQ
  * [常见问题](https://github.com/ytwp/wtp/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

# Support
<table>
  <thead>
    <th>群号：1041653365</th>
  </thead>
</table>

# Screenshots
![线程池管理页面](https://img.yeting.wang/wtp/3.png)
![线程池运行报表](https://img.yeting.wang/wtp/5.png)
![线程池日志页面](https://img.yeting.wang/wtp/1.png)
