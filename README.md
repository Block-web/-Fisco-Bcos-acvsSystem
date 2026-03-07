# 基于Fisco Bcos的学历证书认证系统

# acvsSystem 
以下内容为ai生成 
作者：zzw 三亚学院 软件工程（区块链方向）--本人毕业设计：未经允许，不能用来盈利
本人前端和数据库孱弱 ， 前端页面为ai生成加自己修改 脚本自己写。
    数据库为需求分析后由ai代为生成SQL简化开发时间 sql设计不好请不要见怪。
    ps:智能合约版本为V8.10,其他版本还没测试过
    公司管理页面还在开发中，业务繁忙后续会补上

基于 FISCO BCOS 区块链的学历证书认证系统，实现证书的安全存储、防篡改验证和查询功能。

## 🚀 技术栈

### 前端技术
- **Vue 3** - 渐进式JavaScript框架
- **Element Plus** - UI组件库
- **Vite** - 构建工具
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Axios** - HTTP客户端
- **Crypto-JS** - 加密库
- **html2canvas + jsPDF** - PDF生成

### 后端技术
- **Spring Boot 2.7.0** - Java应用框架
- **MyBatis Plus** - ORM框架
- **MySQL 8.0** - 数据库
- **Maven** - 依赖管理
- **Java 14** - 编程语言

### 区块链技术
- **FISCO BCOS 3.x** - 联盟链平台
- **WeBASE** - 区块链中间件
- **智能合约** - 证书哈希注册

## 📋 功能特性

### 核心功能
- ✅ 用户登录认证
- ✅ 证书上传与管理
- ✅ 证书区块链上链
- ✅ 证书验证查询
- ✅ 数据统计分析
- ✅ 文件存储（阿里云OSS集成）

### 待完善功能
- ⏳ 公司业务逻辑（部分未完成，无需关注）
- ⏳ 高级权限管理
- ⏳ 批量操作功能

## 🛠️ 快速开始

### 环境要求
- Node.js >= 20.19.0
- Java 14
- MySQL 8.0+
- Maven 3.6+
- FISCO BCOS 3.x（区块链环境）
- WeBASE管理平台（可选）

### 1. 前端运行

**安装依赖**
```bash
cd acvs
npm install
```

**配置环境变量**
```bash
# 复制环境变量模板
copy .env.example .env

# 编辑.env文件
VITE_API_BASE_URL=http://localhost:8080
VITE_OSS_ACCESS_KEY_ID=your_access_key
VITE_OSS_ACCESS_KEY_SECRET=your_secret
```


**启动开发服务器**
```bash
npm run dev
```
访问 http://localhost:5173

### 2. 后端运行

**数据库配置**
```sql
CREATE DATABASE acv_system CHARACTER SET utf8mb4;
```

**修改配置文件**
编辑 `acvsystem/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/acv_system?useSSL=false
    username: your_username
    password: your_password
```
**配置阿里云OSS**  （如果不使用环境变量）
1.你需要登录学习使用,配置阿里云OSS,有自己的bucket  如果没有可以参考B站教程
2.配置AliOssUtils 工具类
  字段如下 :
      ENDPOINT
      ACCESS_KEY_ID
      ACCESS_KEY_SECRET
      BUCKET_NAME
      region  写在了方法里面 我也不知道自己咋想的
3.请先测试一下能不能正常上传本地文件到自己的buckt     

**初始化区块链** 
所有区块链业务都在 CertificateController.java 中 你需要先拿到合约在fiscobcos上部署然后
拿到合约地址 将其配置到控制类中
需要修改的字段如下：
    //合约地址
    CONTRACTADDRESS = "0xc8ead4b26b2c6ac14c9fd90d9684c9bc2cc40085"

**启动后端服务**
```bash
cd acvsystem
mvn spring-boot:run
```
服务将在 http://localhost:8080 启动

### 3. 区块链环境部署

**方式一：使用FISCO BCOS一键部署**
```bash
# 下载部署脚本
curl -LO https://github.com/FISCO-BCOS/FISCO-BCOS/releases/download/v3.8.0/build_chain.sh
chmod +x build_chain.sh

# 构建4节点联盟链
./build_chain.sh -l "127.0.0.1:4" -p 30300,20200,8545

# 启动节点
bash nodes/127.0.0.1/start_all.sh
```

**方式二：使用Docker（推荐）**
```bash
# 启动FISCO BCOS节点
docker run -d --name fisco-bcos-node \
  -p 20200:20200 -p 8545:8545 \
  fiscoorg/fiscobcos:v3.8.0
```

**部署智能合约**
1. 访问WeBASE管理平台： http://虚拟机IP地址:5002/WeBASE-Front/#/home
2. 上传 `CertHashRegistry.sol` 合约
3. 部署合约并记录合约地址

**配置区块链连接**
修改 `config.toml` 区块链配置：
```
字段
peers=["192.168.1.3:20200", "192.168.1.3:20201", "192.168.1.3:20202", "192.168.1.3:20203"]
端口号不变的情况下 需要修改 ip地址为虚拟机地址 ps：虚拟机我只测试过桥接模式（要与主机在一个网关下）
```



## ⚠️ 项目缺点与限制

### 1. 技术债务
- **区块链依赖复杂**：需要单独部署FISCO BCOS区块链环境
- **配置繁琐**：WeBASE管理平台部署和智能合约配置较为复杂
- **安全性不足**：部分敏感信息硬编码在代码中
- **缺少测试**：缺乏单元测试和集成测试

### 2. 功能限制
- **性能瓶颈**：大文件上传和区块链交易可能存在性能问题
- **用户体验**：部分界面交互不够流畅
- **错误处理**：异常处理机制不够完善
- **文档缺失**：项目文档不够详细

### 3. 部署挑战
- **环境依赖多**：需要配置前端、后端、数据库、区块链等多个环境
- **运维复杂**：区块链节点需要定期维护和监控
- **扩展性有限**：系统架构在高并发场景下可能存在瓶颈

## 🔧 部署建议

### 开发环境
1. **使用Docker Compose**：一键部署完整开发环境
2. **配置本地区块链**：使用FISCO BCOS单节点开发网络
3. **使用WeBASE-Front**：方便管理区块链和部署合约

### 生产环境
1. **多节点区块链**：部署至少4个FISCO BCOS节点
2. **负载均衡**：前端和后端服务使用负载均衡
3. **监控系统**：集成区块链交易监控和应用性能监控
4. **灾备方案**：制定数据备份和恢复策略

### 安全建议
1. **环境变量管理**：使用专业的密钥管理服务
2. **HTTPS配置**：启用全站HTTPS
3. **访问控制**：完善API权限控制
4. **审计日志**：记录所有关键操作日志



## 📞 联系方式

- 项目主页: [GitHub Repository](https://github.com/Block-web/acvsSystem)
- 问题反馈: [Issues](https://github.com/Block-web/acvsSystem/issues)
- 我很菜 如果你有什么建议 可以联系我 我们可以一起探讨一下
- 如果需要虚拟机 也可也联系我
---

**注意**: 公司业务逻辑部分尚未完成，使用时请忽略相关功能。生产环境部署前请进行充分测试和安全评估。
