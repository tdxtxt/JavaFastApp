# JavaFastApp


JavaFastApp是一款用来进行app快速开发的工具集合框架

### 架构层级
JavaFastApp

为了方便用户直接使用，对于更新流程中非用户必须定制的接口。框架也对各个节点实现了一套自身默认的定制接口。做到拿来即用的。

### 功能特点

- 网络请求封装
- 图片框架封装
- 存储框架封装
- Activity封装
- Dialog封装
- Adapter适配器引入三方框架
- 更新功能封装
- 分享功能封装
- webview封装
- 常用工具类

### 引入方式：

请下载源码自行修改

### 使用方式：

对各个功能逐一讲解如下

#### 网络请求：

框架最大的优点在于动态改变网络请求根域名，减少测试过程中平凡打包的问题。

```
 ApiClient.getService().testapi(parameter)
                                .compose(this.<ResponseBody>bindToLifeAndReqApi())
                                .subscribe(new Consumer<ResponseBody>() {
                                    @Override
                                    public void accept(ResponseBody body) throws Exception {
                                       //处理数据内容
                                    }
                                });
```
#### 图片框架

#### 存储框架

#### Activity的封装
##### 1. ButterKnife的统一封装
##### 2. Android 4.4版本以上沉浸式的封装
##### 3. 状态页统一封装
##### 4. 进度条封装
##### 5. EventBus封装集成
##### 6. 动态权限请求封装
##### 7. 对顶部标题栏的封装，请参考CommToolBarActivity，继承BaseActivity
##### 8. mvp架构封装，请参考CommToolBarMvpActivity，继承CommToolBarActivity


#### Dialog使用

封装工具类提供两种使用dialog的方式

##### 1. 静态调用方法

##### 2. 继承BaseDialog类

### 联系作者
email: 1049274119@qq.com
