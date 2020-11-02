# WTP - A reliable thread pool management system

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/ytwp/wtp)
[![Gitee Release](https://img.shields.io/badge/release-v1.0.0-blue)](https://gitee.com/ytwp/wtp/releases)
[![Maven Central Repo](https://img.shields.io/badge/maven%20central-v1.0.0-blue)](https://search.maven.org/artifact/wang.yeting/wtp-core)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

### Introduce
WTP is a lightweight thread pool management system, can centrally manage different applications, different clusters of thread configuration, modify the configuration can be real-time refresh, use, easy to use.

JAVA client support, can be run in the Spring/Spring Boot environment

Support for JKD1.8 and later.

Demo :
- [demo.wtp.yeting.wang](http://demo.wtp.yeting.wang/)
- account：wtpwtp
- password：123456
- client connection address：http://121.36.54.20:5000
- [Demonstrate the environment deployment scenario](https://github.com/ytwp/wtp/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2%E6%96%B9%E6%A1%88#%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1-%E6%96%B9%E5%BC%8F)

> gitee：[https://gitee.com/ytwp/wtp](https://gitee.com/ytwp/wtp)
>
> github：[https://github.com/ytwp/wtp](https://github.com/ytwp/wtp)
>
> oschina：[https://www.oschina.net/p/wtp-admin](https://www.oschina.net/p/wtp-admin)

### License
The project is based on [ApacheLicense-2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

### Features
+ **Unified management**
	+ In the Web page can easily manage multiple applications, multiple clusters, multiple thread pool parameter configuration, dynamic modification.

+ **effective on time**
	+ After the configuration of the Web page is modified, the client can receive the configuration in real time (1 second) and update the thread pool parameters to achieve the purpose of dynamic modification.

+ **hreshold alarm**
	* the queue length USES percentage and thread activity percentage to alarm. It has supported Email, WeCom, DingTalk, and supports custom alarm mode.

+ **Number of threads running log**
	* Count the running state of the thread pool, the number of active threads, the maximum number of created threads, the length of the queue, the remaining length of the queue, the length of the queue, the number of tasks completed, the average time to complete the task, the number of occurrences of rejected, the rejected handling and so on... , to quickly understand how the thread pool is working.

+ **Modify  dynamically**
	* Do not restart service: Support for modifying core thread count, maximum thread count, queue length (two modifiable length queues are available), idle thread recovery time, and Rejected handling.
	* Restart service: Support for changing core thread count, maximum thread count, queue length, queue type, idle thread recovery time, and rejected handling.

+ **Internationalization**
	* Web side supports internationalization Settings, providing Chinese and English languages, default is Chinese.

+ **distributed**
	* WTP-Admin supports HA deployment.
  
### Usage
  * [Usage](https://gitee.com/ytwp/wtp/wikis/%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97?sort_id=2867497)
 
### Release Notes
  * [Release Notes](https://gitee.com/ytwp/wtp/releases)

### FAQ
  * [FAQ](https://gitee.com/ytwp/wtp/wikis/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98?sort_id=2867496)

### Contributing

1. Fork the warehouse
2. Create a new Feat_xxx branch
3. Commit the code
4. Open a Pull Request

### Screenshots
##### Thread pool management page:
![Thread pool management page](https://img.yeting.wang/wtp/3.png)
##### Thread pool runs the report:
![Thread pool runs the report](https://img.yeting.wang/wtp/5.png)
##### Thread pool log page:
![Thread pool log page](https://img.yeting.wang/wtp/1.png)
![Thread pool log page](https://img.yeting.wang/wtp/2.png)
##### Alarm notification page (extensible)）
|Email|WeCom|DingTalk|
|:--|:--|:--|
|![Email](https://img.yeting.wang/wtp/email.jpg)|![WeCom](https://img.yeting.wang/wtp/wecom.jpg)|![DingTalk](https://img.yeting.wang/wtp/dingding.jpg)|

