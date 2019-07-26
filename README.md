# microservice-gateway

#### 介绍
统一进行API网关管理，所有的系统API接口通过网关进行暴露

#### 软件架构
软件架构说明


#### 安装教程

1. clone到本地
2. 修改microservice.yaml中credentials配置，替换自己信息，cse两种配置，上面的是连接华为提供的线上服务中心，需要自己去华为云注册获取秘钥，下面的是本地配置，需要去华为云下载本地服务中心。
3. mvn clean install下载相关依赖，然后启动即可

#### 使用说明

1. 项目安装后直接启动即可
2. 若使用本地配置启动，可以通过http://localhost:30103 访问服务中心UI，在services标签下可以看到对应微服务，可以点击进入对应微服务的swagger UI，直接点击测试对应接口。

#### 端口号
18080

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

