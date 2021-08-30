<p align="center">
  <a href="https://github.com/conifercone/uaa">
   <img alt="uaa-Logo" src="https://raw.githubusercontent.com/conifercone/images/main/20210818112933.png">
  </a>
</p>
<p align="center">
  <a href="https://mit-license.org/">
    <img alt="project License" src="https://img.shields.io/github/license/conifercone/uaa">
  </a>
  <a href="https://github.com/conifercone/uaa">
    <img alt="project License" src="https://img.shields.io/github/languages/count/conifercone/uaa">
  </a>
  <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
    <img alt="jdk version" src="https://img.shields.io/badge/JDK-8+-green.svg" />
  </a>
</p>

# 🍈 账户管理统一认证服务

## 内容列表

- [背景](#背景)
- [构建](#构建)
- [依赖说明](#依赖说明)
- [使用说明](#使用说明)
- [维护者](#维护者)
- [如何贡献](#如何贡献)
- [使用许可](#使用许可)

## 背景

> 工作了这么长时间，自己也参与了很多个项目，但是一直没有一个能让自己满意的或者说让自己心安的
> 账户管理统一认证服务，所以准备自己动手实现一个完整的账户管理统一认证服务。

## 构建

## 依赖说明

<p>
  <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/">
    <img alt="SpringBoot Version" src="https://img.shields.io/badge/SpringBoot-2.5.3-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies/2020.0.3">
    <img alt="SpringCloud Version" src="https://img.shields.io/badge/SpringCloud-2020.0.3-brightgreen">
  </a>
  <a href="https://spring.io/projects/spring-cloud-alibaba#learn">
    <img alt="SpringCloud Alibaba Version" src="https://img.shields.io/badge/SpringCloud%20Alibaba-2021.1-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web/2.5.3">
    <img alt="spring-boot-starter-web Version" src="https://img.shields.io/badge/spring--boot--starter--web-2.5.3-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation/2.5.3">
    <img alt="spring-boot-starter-validation Version" src="https://img.shields.io/badge/spring--boot--starter--validation-2.5.3-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.25">
    <img alt="mysql-connector-java Version" src="https://img.shields.io/badge/mysql--connector--java-8.0.25-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter/2.2.0">
    <img alt="mybatis-spring-boot-starter Version" src="https://img.shields.io/badge/mybatis--spring--boot--starter-2.2.0-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-boot-starter/3.4.3.1">
    <img alt="mybatis-plus-boot-starter Version" src="https://img.shields.io/badge/mybatis--plus--boot--starter-3.4.3.1-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter/1.2.6">
    <img alt="druid-spring-boot-starter Version" src="https://img.shields.io/badge/druid--spring--boot--starter-1.2.6-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-config/2021.1">
    <img alt="spring-cloud-starter-alibaba-nacos-config Version" src="https://img.shields.io/badge/spring--cloud--starter--alibaba--nacos--config-2021.1-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-alibaba-nacos-discovery/2021.1">
    <img alt="spring-cloud-starter-alibaba-nacos-discovery Version" src="https://img.shields.io/badge/spring--cloud--starter--alibaba--nacos--discovery-2021.1-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap/3.0.3">
    <img alt="spring-cloud-starter-bootstrap Version" src="https://img.shields.io/badge/spring--cloud--starter--bootstrap-2020.0.3-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/com.alibaba.cloud/spring-cloud-starter-dubbo">
    <img alt="spring-cloud-starter-dubbo Version" src="https://img.shields.io/badge/spring--cloud--starter--dubbo-2021.1-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/cn.dev33/sa-token-spring-boot-starter/1.25.0">
    <img alt="sa-token-spring-boot-starter Version" src="https://img.shields.io/badge/sa--token--spring--boot--starter-1.25.0-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/cn.hutool/hutool-all/5.7.7">
    <img alt="hutool-all Version" src="https://img.shields.io/badge/hutool--all-5.7.7-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/cn.dev33/sa-token-dao-redis-jackson/1.25.0">
    <img alt="sa-token-dao-redis-jackson Version" src="https://img.shields.io/badge/sa--token--dao--redis--jackson-1.25.0-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/cn.dev33/sa-token-oauth2/1.25.0">
    <img alt="sa-token-oauth2 Version" src="https://img.shields.io/badge/sa--token--oauth2-1.25.0-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.apache.commons/commons-pool2/2.10.0">
    <img alt="commons-pool2 Version" src="https://img.shields.io/badge/commons--pool2-2.10.0-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/io.springfox/springfox-boot-starter/3.0.0">
    <img alt="springfox-boot-starter Version" src="https://img.shields.io/badge/springfox--boot--starter-3.0.0-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.projectlombok/lombok/1.18.20">
    <img alt="lombok Version" src="https://img.shields.io/badge/lombok-1.18.20-brightgreen">
  </a>
  <a href="https://github.com/alibaba/jetcache">
    <img alt="jetcache Version" src="https://img.shields.io/badge/jetcache--starter--redis-2.6.0-brightgreen">
  </a>
  <a href="https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test/2.5.3">
    <img alt="spring-boot-starter-test Version" src="https://img.shields.io/badge/spring--boot--starter--test-2.5.3-brightgreen">
  </a>
  <a href="https://package-search.jetbrains.com/package?id=org.apache.commons%3Acommons-lang3">
    <img alt="commons-lang3 Version" src="https://img.shields.io/badge/commons--lang3-3.12.0-brightgreen">
  </a>
</p>

## 使用说明

## 维护者

[@conifercone](https://github.com/conifercone)。

## 如何贡献

非常欢迎你的加入！[提一个 Issue](https://github.com/conifercone/uaa/issues/new) 或者提交一个 Pull Request。

标准 Readme 遵循 [Contributor Covenant](http://contributor-covenant.org/version/1/3/0/) 行为规范。

## 贡献者

## 使用许可

[MIT](LICENSE) © sky5486560@gmail.com
