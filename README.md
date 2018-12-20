# JavaFastApp


JavaFastApp是一款用来进行app快速开发的工具集合框架

### 架构层级
JavaFastApp

为了方便用户直接使用，对于更新流程中非用户必须定制的接口。框架也对各个节点实现了一套自身默认的定制接口。做到拿来即用的。

### 功能特点

- 网络请求封装
- 图片框架封装
- 标题栏Activity封装
- Adapter适配器引入三方框架
- 更新功能封装
- webview封装
- 常用工具类

### 引入方式：

请下载源码自行修改

### 使用方式：

#### 网络请求：

```
UpdateConfig.getConfig()
		.setUrl(url)// 配置检查更新的API接口
		.setUpdateParser(new UpdateParser() {
			@Override
			public void Update parse(String response) throws Exception {
				// TODO 此处的response数据为上方检查更新接口所返回回来的数据。
				// 需要在此对response数据进行解析，并创建出对应的update实体类数据
				// 提供给框架内部进行使用
				return update;
			}
		});
```

#### 图片框架

框架提供两种更新任务启动方式，分别对应于不同的场景下进行使用：

##### 1. 普通更新任务

```
UpdateBuilder.create()
	.check();// 启动更新任务
```
普通更新任务主要用于设置页中，由用户点击检查更新时所主动触发的更新任务。
##### 2. 后台更新任务

后台更新任务主要是提供出来，采用后台轮询更新的机制，便于及时检查到新发布的APK进行版本更新

```
UpdateBuilder task = UpdateBuilder.create()

// 启动后台更新任务，retryTime为重启时间间隔，单位为秒。
// 即通过此方法所启动的更新任务。将会在'无更新'，'更新失败'等条件下：
// 延迟指定的时间间隔后，自动重新启动。
task.checkForDaemon(retryTime);
...
// 可使用此方法，停止后台更新任务的重启机制。
task.stopDaemon();
```

[更多使用方法请参考此处WIKI文档](https://github.com/yjfnypeu/UpdatePlugin/wiki)

### 联系作者
email: 1049274119@qq.com
