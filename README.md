**借还**
### 项目概述
记录每一笔借款与还款的详细数据，根据基本数据提供有利于管理与贷款的服务！
# 系统参数
- 使用spring-boot搭建基于spring的web框架。
- 使用mysql8.0.16版本数据库。
- 使用mybatis逆向工程生成代码。
- Java1.8
# 功能说明
## 管理员
* 管理员登录<br>
管理员登录后进入系统
* 管理员修改密码<br>
管理员通过数据库原密码进行验证后才能修改密码
## 用户管理
* 添加用户
* 用户信息修改
* 所有用户<br>
可以获取到所有用户的基本信息，包括头像url
* 用户预览<br>
获取到用户的余额，收益，收益率，最近一笔贷款的所有用户。
## 借款管理
* 创建借款<br>
用借款人id和出借人id创建一笔贷款。
* 查询借款<br>
通过借款id查询一笔借款
## 还款管理
* 创建还款
* 查询还款
## 积分管理
* 积分统计（支出与收入统计）
## 信用管理
* 信用评估（通过还款时间，积分值）
## 信贷额度
* 信贷额度
# 项目目录说明
## 文件划分
* dataobject:数据库对象映射类。
- dao:mapper助手类，声明sql语句调用方法。
- mapping：mapper映射配置文件
- controller：与前端间的所有交互，组装前端所需要的数据模型。
- model:存放数据模型。
- service:提供应用程序所有的服务和数据模型的组装。
