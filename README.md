# MiniCore
#### 项目介绍
android快速开发项目介绍，常用公共库（mini-core）整合okHttp、dataBinding、retrofit、glide、eventbus、
bindingcollectionadapter、agentweb等常用三方工具库；

#### 目录介绍
- base目录，封装常用基类；
- binding目录，封装常用的BindingAdapter；
- exception目录，封装常用的http异常处理；
- net目录，okHttp+retrofit+rxjava2封装，常用rest(get\post\delete\postFile\download)的封装,统一处理了的日志、缓存、Cookie、头信息；
- ui 目录，封装了常用的功能activity/fragment（引导页、WebView封装、Tab菜单封装）
   - 引导页GuideActivity,如项目中需要引导操作的继承该类做业务处理；
   - Tab菜单页封装 TabFragmentActivity；
   - widget 封装常用的小控件（轮播BannerView、带清除按钮的EditText、购物车加减输入控件CounterView、流式布局FlowLayout）；
- utils目录，封装常用的工具类，如文件、字符串正则、加密解密、弹窗、日志、时间、权限、页面跳转、获取设备参数、获取屏幕参数等工具类；

更多内容敬请期待，文档更新中...

