### 用户登录(实现)

`user/login`：

接口功能：用于用户登录

请求头：

请求方式：POST

参数类型：JSON

请求参数说明：

| 字段名       | 字段说明   | 字段类型 | 是否必填 |
| ------------ | ---------- | -------- | -------- |
| username     | 账号名     | string   | 1        |
| passwd       | 用户密码   | string   | 1        |
| captcha      | 手机验证码 | string   | 1        |
| phone_number | 手机号     | string   | 1        |

响应示例：

```json
{
  "code": 1,
  "msg": null,
  "data": {
    "id": 1,
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNzE0NTUyMzQwfQ.k6s6tejIz7692ciBsiNHzLs8-npGHJ4vU-CJHt__iKU"
  }
}
{
  "code": 0,
  "msg": "密码错误",
  "data": null
}
```



### 用户注册(实现)

`user/register`：

接口功能：用于用户注册。可以选择邮箱或者手机号注册

请求头：
请求方式：POST

参数类型：JSON

请求参数说明：

| 字段名       | 字段说明       | 字段类型 | 是否必填 |
| ------------ | -------------- | -------- | -------- |
| phone_number | 手机号用于注册 | string   | 0        |
| email        | 邮箱           | string   | 0        |
| username     | 用户名         | string   | 1        |
| passwd       | 密码           | string   | 1        |
| captcha      | 验证码         | string   | 1        |

响应示例：

```json
{
  "code": 1,
  "msg": null,
  "data": true
}
{
  "code": 0,
  "msg": "手机号已被注册",
  "data": null
}
```

响应参数说明：

| 接口返回码 | 接口返回描述 |
| ---------- | ------------ |
| 200        | 成功         |
| 400        | 请求参数异常 |
| 401        | 授权失败     |
| 500        | 服务器异常   |

### 获取验证码——邮箱(实现)

`/user/getCaptchaByEmail`：

接口功能：用于通过邮箱获取验证码

请求头：
请求方式：GET

请求参数说明：

| 字段名 | 字段说明           | 字段类型 | 是否必填 |
| ------ | ------------------ | -------- | -------- |
| email  | 邮箱用于发送验证码 | string   | 1        |

响应示例：

```json
{
  "code": 1,
  "msg": null,
  "data": true
}
```

### 获取验证码——手机(实现)

`/user/getCaptchaByPhone`：

接口功能：用于通过手机获取验证码
请求方式：GET

请求参数说明：

| 字段名       | 字段说明           | 字段类型 | 是否必填 |
| ------------ | ------------------ | -------- | -------- |
| phone_number | 手机号用于发送短信 | string   | 1        |

响应示例：

```json
{
  "code": 1,
  "msg": null,
  "data": true
}
{  
    "code": 0,  
    "msg": "5min内发送过一次验证码",  
    "data": false
}
{  
    "code": 0,  
    "msg": "手机号为空",  
    "data": false
}
```

### 重置密码(实现)

`/user/resetPasswd`

接口功能：允许用于通过验证码验证后可以更新密码

请求方式：POST

请求参数说明：

| 字段名      | 字段说明                     | 字段类型 | 是否必填 |
| ----------- | ---------------------------- | -------- | -------- |
| phoneNumber | 手机号，即用户唯一标识       | string   | 1        |
| captcha     | 验证码，用于判定是否通过验证 | string   | 1        |
| passwd      | 新密码                       | string   | 1        |

```json
{  
    "code": 1,  
    "msg": null,  
    "data": true
}
{  
    "code": 0,  
    "msg": "账号不存在",  
    "data": false
}
{  
    "code": 0,  
    "msg": "验证码错误",  
    "data": false
}
```

### 获取当前所有用户的基本信息(实现)

`/admin/getAllUser`

接口功能：允许管理员用户获取系统中已注册的用户信息

请求方式：GET

请求参数说明：无参数

```json
{
  "code": 1,
  "msg": null,
  "data": [
    {
      "id": 1,
      "username": "root",
      "phoneNumber": "15881602569",
      "email": "aa@qq.com"
    },
    {
      "id": 2,
      "username": "xyh",
      "phoneNumber": "15881602568",
      "email": "bb@163.com"
    }
  ]
}

```

### 获取当前所有用户的手机号



```json
{
  "code": 1,
  "msg": null,
  "data": [
    "15881602569",
    "15881602568",
    "15881602560",
    "15881603278"
  ]
}
```



### 获取活动日程

`user/getSchedule`：

接口功能：用于获取当前用户的活动日程列表（面向所有用户）

请求头：
请求方式：GET

请求参数说明：

| 字段名 | 字段说明       | 字段类型 | 是否必填 |
| ------ | -------------- | -------- | -------- |
| id     | 用户名用于查询 | string   | 1        |

响应示例：

```json
{
    "code": "200",
    "message": "请求成功",
    "data": "schedule_list"
}
{
    "code": "200",
    "message": "请求失败",
    "data": null
}
```

### 检查活动冲突

`/user/CheckScheduleConflict`：

接口功能：用于检查活动日程、资源等等是否冲突（面向组织者）

请求头：
请求方式：POST

请求参数说明：

| 字段名   | 字段说明         | 字段类型 | 是否必填 |
| -------- | ---------------- | -------- | -------- |
| place    | 地点用于检测冲突 | string   | 1        |
| resource | 资源用于检测冲突 | string   | 1        |
| time     | 时间用于检测冲突 | date     | 1        |

响应示例：

```json
{
    "code": "200",
    "message": "不冲突",
    "data": true
}
{
    "code": "200",
    "message": "冲突",
    "data": false
}
```



### 创建活动——自定义

接口功能：创建一个新的活动，用户可以选择现有的活动模板或自定义活动细节

请求头：Content-Type: application/json

请求方式：POST

参数类型：JSON

URL：/api/activity/create

请求参数说明：

| 字段名        | 字段说明                             | 字段类型 | 是否必填 |
| :------------ | :----------------------------------- | :------- | :------- |
| template_id   | 活动模板的唯一标识符                 | int      | 0        |
| activity_name | 活动的名称                           | string   | 1        |
| agenda        | 活动的议程安排（会议主题、讨论内容） | string   | 1        |
| speakers      | 主讲嘉宾的姓名列表                   | string   | 1        |
| schedule      | 活动的时间安排                       | string   | 1        |
| attendees     | 参会人员的信息（姓名、角色和权限）   | string   | 1        |
| materials     | 活动相关的资料列表（会议文稿、PPT）  | string   | 1        |

响应示例：

```
{
    "status": "success",
    "activity_id": "string",
    "message": "Activity successfully created."
}
```

响应参数说明：

| 接口返回码 | 接口返回描述 |
| :--------- | :----------- |
| 200        | 成功         |
| 400        | 请求参数异常 |
| 500        | 服务器异常   |

### 创建活动——非自定义

接口功能：获取可用的活动模板列表，用户可以从中选择适合的模板来创建活动

请求方式：GET

URL：/api/activity/templates

```
{
    "status": "success",
    "templates": ["template1", "template2", ...]
}
```

响应参数说明：

| 接口返回码 | 接口返回描述 |
| :--------- | :----------- |
| 200        | 成功         |
| 401        | 授权失败     |
| 500        | 服务器异常   |

### 获取活动进度

接口功能：获取指定活动的当前进度状态，如筹备中、进行中、已完成等

请求方式：GET

URL：/api/activity/progress/&lcub;activity_id&rcub;

响应示例：

```
{
    "status": "success",
    "progress": "string"
}
```

响应参数说明：

| 接口返回码 | 接口返回描述 |
| :--------- | :----------- |
| 200        | 成功         |
| 401        | 授权失败     |
| 500        | 服务器异常   |

### 分享活动信息

接口功能：将指定活动的信息分享至外部平台，如微信、微博等

请求头:Content-Type: application/json

请求方式：POST

参数类型：JSON

URL：/api/activity/share/&lcub;activity_id&rcub;

请求参数说明：

| 字段名   | 字段说明     | 字段类型 | 是否必填 |
| :------- | :----------- | :------- | :------- |
| platform | 分享的平台名 | string   | 1        |

响应示例：

```
{
   "status": "success"
}
```



### 添加日程提醒

`add_Reminder`：

接口功能：用于添加日程提醒（面向所有用户）。可以选择发送短信或邮箱。

请求头：
请求方式：POST

请求参数说明：

| 字段名   | 字段说明         | 字段类型 | 是否必填 |
| -------- | ---------------- | -------- | -------- |
| act_name | 活动名           | string   | 1        |
| method   | 提醒方式         | int      | 1        |
| time     | 时间用于提醒时间 | date     | 1        |

响应示例：

```json
{
    "code": "200",
    "message": "添加成功",
    "data": true
}
{
    "code": "200",
    "message": "添加失败",
    "data": false
}
```



### 参与者分组

接口功能: 对某个活动的参与者进行分组管理, 只有活动创建者与管理员拥有该权限

请求接口: /participant_management

请求方式: POST

参数类型: JSON

参数说明:

| 字段名      | 字段类型                           | 字段类型 | 是否必填 |
| ----------- | ---------------------------------- | -------- | -------- |
| account     | 用户名(当前操作者)                 | string   | 是       |
| activity    | 活动名(当前管理的活动)             | string   | 是       |
| participant | 参与者(操作对象)                   | string   | 是       |
| group       | 分组结果(需要将参与者分配到的组别) | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "分组成功",
    "data": "Success"
}
```



### 参与者互动发送

接口功能: 某个活动的互动消息发送接口

请求接口: /participant_interactive_sending

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |
| content  | 发送消息的内容         | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "消息发送成功",
    "data": "Success"
}
```

响应参数说明:

### 参与者互动接收

接口功能: 某个活动的互动消息接收接口

请求接口: /participant_interactive_receiving

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "content": {
        "UID": "啦啦啦",
        "UID": "你好",
        ...
    }
    "data": "Success"
}
```

响应参数说明:

### 资源添加(实现)

接口功能: 管理员添加资源

请求接口: `admin/resourceAddition`

请求方式: POST

参数类型: JSON

参数说明:

| **字段名** | **字段类型** | **字段类型** | **是否必填** |
| ---------- | ------------ | ------------ | ------------ |
| resource   | 资源名       | string       | 是           |
| quantity   | 资源数量     | int          | 是           |
| type       | 资源类型     | int          | 是           |

```json
{
  "code": 1,
  "msg": null,
  "data": true
}
{
  "code": 0,
  "msg": "资源添加失败",
  "data": false
}
```

### 资源预约(实现)

接口功能: 某个用户的资源预约接口, 资源分配给用户, 再由用户分配给活动

请求接口:`user/resourceReservation`

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型                 | 字段类型 | 是否必填 |
| -------- | ------------------------ | -------- | -------- |
| account  | 用户名(当前操作者)       | string   | 是       |
| resource | 资源名(需要预约的资源名) | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "预约成功"
    "data": "Success"
}
{
    "code": 400,
    "message": "预约失败"
    "data": "Failed"
}
```

### 资源分配

接口功能: 某个用户的资源分配接口, 由用户将资源分配给指定活动

请求接口: `admin/resource_allocation`

请求方式: POST

参数类型: JSON

参数说明:

| 字段名      | 字段类型                 | 字段类型 | 是否必填 |
| ----------- | ------------------------ | -------- | -------- |
| phoneNumber | 用户名(当前操作者)       | string   | 是       |
| activity    | 活动名(当前管理的活动)   | string   | 是       |
| resource    | 资源名(需要预约的资源名) | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "分配成功"
    "data": "Success"
}
{
    "code": 400,
    "message": "分配失败"
    "data": "Failed"
}
```



### 预算设定

接口功能: 某个活动的预算设定接口, 仅管理员与创建者可以设置

请求接口: /budget_setting

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |
| budget   | 预算                   | money    | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "设定成功"
    "data": "Success"
}
{
    "code": 400,
    "message": "设定失败"
    "data": "Failed"
}
```

响应参数说明:

### 预算获取

接口功能: 某个活动的预算获取接口, 任何人可以查看当前活动的预算

请求接口: /budget_getting

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "budget": 100.2
    "data": "Success"
}
{
    "code": 400,
    "budget": -1
    "data": "Failed"
}
```

响应参数说明:

### 参与者费用报销提交

接口功能: 某个活动的预算报销提交接口, 任何人可以提交报销单; 当前仅提交, 等待审核结果

请求接口: /expense_reimbursement

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |
| expense  | 报销金额               | money    | 是       |
| rid      | 报销单ID               | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "提交成功"
    "data": "Success"
}
{
    "code": 400,
    "message": "提交失败"
    "data": "Failed"
}
```

响应参数说明:

### 参与者费用报销接收

接口功能: 某个活动的预算报销接收接口, 提交报销单后接收审核结果

请求接口: /expense_reimbursement_receive

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |
| rid      | 报销单ID               | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "提交成功"
    "data": "Success"
}
{
    "code": 400,
    "message": "提交失败"
    "data": "Failed"
}
```

响应参数说明:

### 管理员费用报销审核获取

接口功能: 某个活动的预算报销单获取接口, 管理员与组织者有权限获取全部报销单

请求接口: /obtaining_expense_reimbursement_forms

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |

相应示例:

``` json
{
    "code": 200,
    "expense": {
        "rid": "UID_expense_content", // 用户ID, 报销金额, 原因说明
        ...
    }
    "data": "Success"
}
```

响应参数说明:

### 管理员费用报销审核返回

接口功能: 某个活动的预算报销单获取接口, 管理员与组织者将审核结果发送给服务器, 由服务器经由参与者费用报销接收接口发送给提交报销单的参与者

请求接口: /expense_reimbursement_forms_response

请求方式: POST

参数类型: JSON

参数说明:

| 字段名   | 字段类型               | 字段类型 | 是否必填 |
| -------- | ---------------------- | -------- | -------- |
| account  | 用户名(当前操作者)     | string   | 是       |
| activity | 活动名(当前管理的活动) | string   | 是       |
| rid      | 报销单ID               | string   | 是       |
| result   | 审核结果               | bool     | 是       |

相应示例:

``` json
{
    "code": 200,
    "message": "接收成功",
    "data": "Success"
}
```



### 通知模板

请求头:Content-Type: application/json

接口功能: 提供多种通知模板，方便用户快速生成活动通知

请求方式: POST

参数类型: JSON

请求参数说明:

| 字段名      |        字段说明        | 字段类型 | 是否必填 |
| :---------- | :--------------------: | :------: | -------: |
| username    |         用户名         |  string  |        1 |
| template_id | 用户想要的模板的标识符 |   int    |        1 |

响应示例:

```
{
  "code":"200",
  "templates": [
    {
      "id": 1,
      "name": "会议提醒",
      "content": "您好，会议将于{{date}}在{{location}}举行，请准时参加。"
    },
    {
      "id": 2,
      "name": "活动通知",
      "content": "您好，我们将举办一场活动，详情请查看附件。"
    }
  ],
  "data":"Success"
}
```

templates: 包含多个通知模板的数组，内容如下：

id: 模板ID

name: 模板名称

content: 模板内容，支持变量替换，如{{date}}表示活动日期，{{location}}表示活动地点等。


### 通知发送统计

请求头：Content-Type: application/json。

接口功能: 记录通知的发送情况，包括发送数量、接收情况等。

请求方式: POST

参数类型: JSON

请求参数说明:

| 字段名    |    字段说明    | 字段类型 | 是否必填 |
| :-------- | :------------: | :------: | -------: |
| notice_id | 该通知的标识符 |   int    |        1 |

响应示例:

```
{
  "code":"200",
  "total_sent": 100,
  "total_delivered": 90,
  "total_failed": 10,
  "data":"Success"
}
```

total_sent: 总发送数量

total_delivered: 总送达数量

total_failed: 总发送失败数量

### 活动报告生成

请求头：Content-Type: application/json

接口功能: 根据活动数据自动生成活动报告，包括参与人数、活动效果等

请求方式: POST

参数类型: JSON

请求参数说明:

| 字段名      |    字段说明    | 字段类型 | 是否必填 |
| :---------- | :------------: | :------: | -------: |
| activity_id | 该活动的标识符 |   int    |        1 |

响应示例:

```
{
  "code":"200",
  "report_id": 12345,
  "report_url": "https://example.com/reports/12345",
  "data":"Success"
}
```

响应参数说明:

report_id: 生成的报告ID

report_url: 生成的报告URL，用户可通过此链接查看报告。

### 反馈收集与分析

请求头:Content-Type: application/json

接口功能: 收集参与者的反馈意见，并进行统计和分析，为未来的活动提供参考。

请求方式: POST

参数类型: JSON

请求参数说明:

| 字段名      |    字段说明    | 字段类型 | 是否必填 |
| :---------- | :------------: | :------: | -------: |
| activity_id | 该活动的标识符 |   int    |        1 |
| feed_back   |    反馈意见    |  string  |        1 |

响应示例:

```
{
  "code":"200",
  "message": "Feedback submitted successfully."
  "data":"Success"
}
```

message: 返回的消息提示