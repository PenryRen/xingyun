# WebBE部署指南

## 一、数据库依赖

WebBE项目部署依赖以下数据库服务：

### 1. MySQL数据库
- **版本要求**：8.0.33及以上
- **用途**：存储所有业务数据，包括用户信息、考试数据、培训记录等
- **配置信息**：
  - 数据库名：默认使用`jxsx`（开发环境）或`wdd`（生产环境）
  - 字符集：utf8
  - 时区：Asia/Shanghai

### 2. Redis
- **版本要求**：5.0及以上
- **用途**：
  - 缓存管理
  - 会话存储
  - 分布式锁
- **配置信息**：
  - 默认数据库：1
  - 超时时间：10000ms

## 二、部署环境准备

### 1. 系统要求
- **操作系统**：Linux/Unix或Windows Server
- **Java环境**：JDK 1.8
- **内存**：建议8GB及以上
- **磁盘**：建议50GB及以上可用空间

### 2. 依赖软件安装

#### MySQL安装
1. **下载并安装MySQL 8.0.33**
   - Linux: 使用包管理器或官方二进制包
   - Windows: 使用官方安装程序

2. **配置MySQL**
   - 启动MySQL服务
   - 创建数据库：`CREATE DATABASE wdd CHARACTER SET utf8 COLLATE utf8_general_ci;`
   - 创建用户并授权：`GRANT ALL PRIVILEGES ON wdd.* TO 'wdd'@'%' IDENTIFIED BY 'your_password';`
   - 刷新权限：`FLUSH PRIVILEGES;`

#### Redis安装
1. **下载并安装Redis 5.0+**
   - Linux: 使用包管理器或编译安装
   - Windows: 使用Windows版本或WSL

2. **配置Redis**
   - 启动Redis服务
   - 如需远程访问，修改`redis.conf`中的`bind`配置

#### Nginx安装（可选，用于反向代理和静态资源）
1. **下载并安装Nginx**
2. **配置Nginx**：参考项目中的`Nginx/nginx.conf`文件

## 三、WebBE项目部署

### 1. 项目构建

1. **克隆代码**
   ```bash
   git clone <repository-url>
   cd xingyun/WebBE
   ```

2. **构建项目**
   ```bash
   mvn clean package -DskipTests
   ```
   构建完成后，在`target`目录会生成三个jar包：
   - wdd-admin-9.5.0.jar
   - wdd-user-web-9.5.0.jar
   - wdd-user-mobile-9.5.0.jar

### 2. 配置文件修改

1. **修改配置文件**
   - 进入对应模块的配置目录
   - 修改`application-prod.yml`文件中的数据库连接信息：
   
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://<mysql-host>:3306/wdd?useSSL=false&useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowPublicKeyRetrieval=true&allowMultiQueries=true
       username: <your-username>
       password: <your-password>
       driver-class-name: com.mysql.cj.jdbc.Driver
     redis:
       host: <redis-host>
       port: 6379
       database: 1
   ```

2. **修改资源存储配置**
   ```yaml
   system:
     resource:
       local-store: true
       file:
         location: <your-file-storage-path>
         url: <your-file-access-url>
   ```

### 3. 应用部署

#### 方式一：直接运行jar包

1. **启动管理后台**
   ```bash
   java -jar wdd-admin-9.5.0.jar --spring.profiles.active=prod
   ```

2. **启动用户Web端**
   ```bash
   java -jar wdd-user-web-9.5.0.jar --spring.profiles.active=prod
   ```

3. **启动用户移动端**
   ```bash
   java -jar wdd-user-mobile-9.5.0.jar --spring.profiles.active=prod
   ```

#### 方式二：使用Docker部署

1. **构建Docker镜像**
   - 参考项目中的`docker.yml`文件

2. **启动容器**
   ```bash
   docker-compose up -d
   ```

## 四、服务端口

| 服务名称 | 默认端口 | 说明 |
|---------|---------|------|
| wdd-admin | 16002 | 管理后台服务 |
| wdd-user-web | 16001 | 用户Web端服务 |
| wdd-user-mobile | 16003 | 用户移动端服务 |
| MySQL | 3306 | 数据库服务 |
| Redis | 6379 | 缓存服务 |
| Nginx | 80/443 | 反向代理服务 |

## 五、部署验证

1. **服务启动验证**
   - 检查各服务是否正常启动
   - 访问管理后台：`http://<server-ip>:16002/api/user/login`
   - 访问用户Web端：`http://<server-ip>:16001/api/user/login`

2. **数据库连接验证**
   - 检查应用日志中是否有数据库连接错误
   - 尝试登录系统，验证数据读写是否正常

3. **缓存验证**
   - 检查Redis连接是否正常
   - 验证系统响应速度是否符合预期

## 六、常见问题处理

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 验证数据库连接字符串是否正确
- 确认数据库用户权限是否正确

### 2. Redis连接失败
- 检查Redis服务是否启动
- 验证Redis配置是否正确
- 确认网络连接是否畅通

### 3. 服务启动失败
- 检查端口是否被占用
- 查看应用日志，定位具体错误信息
- 验证依赖服务是否正常

### 4. 静态资源访问失败
- 检查Nginx配置是否正确
- 验证静态资源路径是否存在
- 确认文件权限是否正确

## 七、监控与维护

1. **日志管理**
   - 应用日志默认存储在`./log`目录
   - 定期清理日志文件，避免磁盘空间不足

2. **数据库维护**
   - 定期备份MySQL数据库
   - 监控数据库性能，及时优化

3. **Redis维护**
   - 监控Redis内存使用情况
   - 配置合适的持久化策略

4. **应用更新**
   - 停止旧版本服务
   - 部署新版本
   - 启动服务并验证

## 八、安全配置

1. **数据库安全**
   - 使用强密码
   - 限制数据库访问IP
   - 定期更新数据库密码

2. **Redis安全**
   - 设置密码认证
   - 限制Redis访问IP
   - 禁用危险命令

3. **应用安全**
   - 配置HTTPS
   - 定期更新依赖包
   - 实施防火墙策略

---

**部署完成后**，系统即可正常运行，提供实训教学管理平台的全部功能。