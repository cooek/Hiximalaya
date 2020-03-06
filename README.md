
# Hiximalaya
基于喜马拉雅SDK 构建的APP
 出于学习的目，基本涵盖了当前Android主流开源框架，是一个非常适合小白学习的项目。废话不多说，先上截图：     
  
 * [java版教程](https://www.bilibili.com/video/av69452769) 
 
 * [阳光沙滩社区](https://www.sunofbeach.net/) 
 
 ![](https://github.com/cooek/Hiximalaya/blob/master/app/album/1.jpg)&nbsp;&nbsp;&nbsp;
 ![](https://github.com/cooek/Hiximalaya/blob/master/app/album/1222.jpg)&nbsp;&nbsp;&nbsp;
 ![](https://github.com/cooek/Hiximalaya/blob/master/app/album/13333.jpg)&nbsp;&nbsp;&nbsp;
 ![](https://github.com/cooek/Hiximalaya/blob/master/app/album/2.jpg)&nbsp;&nbsp;&nbsp;
 ![](https://github.com/cooek/Hiximalaya/blob/master/app/album/2-1.jpg)&nbsp;&nbsp;&nbsp;
 ![](https://github.com/cooek/Hiximalaya/blob/master/app/album/2-2.jpg)&nbsp;&nbsp;&nbsp;
 ![](https://github.com/cooek/Hiximalaya/blob/master/app/album/2-3.jpg)

 
 
 #### tips
 * 项目还是测试阶段，如果大家有发现BUG或者什么好的建议，可直接提[issue](https://github.com/cooek/Hiximalaya/issues)
 * 遇到`please select SDK`的情况，是因为项目需要API28支持，请下载最新SDK kotlin version 1.3.61
 * 项目仅供学习使用，SDK数据属于原公司所有，请不要用于其他用途
 
 Point
 -----
 * 使用`MVP`对整个应用进行架构，分为`model`，`UI`，`presenter`三个部分。使用`contract`将`View`和`presenter`联系起来
 * 使用`rxjava3`其他操作符进行延时，轮转操作  
 * 使用`recyclerview`完成大部分界面，实现与`viewpager`结合，下拉刷新，上划加载更多
 * 使用`viewpager`与多个`fragment`结合完成主界面，MagicIndicator指示器
 * 使用glide完成图片加载，圆形头像加载，圆角矩形加载

 

Version
----
#### V 1.0.0
上传第一版

#### 下一步计划
 * 修复历史界面点击进入播放器界面不播放和不能反转排序的问题
 
遇到的问题
----
 * 在`recyclerview`中添加`item`时，会出现宽高失效的问题
>  因为是添加的自定义`view`，自定义`framelayout`中已经设置了宽高，然后让`TextView`显示在`center`。结果是`TextView`永远显示在开头。排除了布局问题之后，先查找资料。发现[RecyclerView Item布局宽高无效问题探究](https://www.jianshu.com/p/9a6db88b8ad3)这篇文章。感觉问题原因应该跟这个差不多。这篇文章讲的是`inflate`方法使用错误导致的问题。但是我这里是使用自定义`view`加载的布局，问题同样是在于，`RecyclerView`在加载子`View`的时候，使用了默认布局的缘故。解决办法是，在加载子`View`的时候，先给它设置`LayoutParams`为`MATCH_PARENT`。


Thanks
----
#### API
没有选择自己抓API，选择直接使用了 [喜马拉雅SDK](http://open.ximalaya.com/)


##### 主流框架
 * [Rxjava3](https://github.com/ReactiveX/RxJava)
 * [MVP](https://www.baidu.com/link?url=Ve8Weqmb1ix-v074Ntk4KNDJK8pImYcS5lSqZIpXYt2VKle9EkUF5dZhZYMnsT0e2XNNArIfu8qmToMoCboyYq&wd=&eqid=be255031000505f5000000065e60ac45)
##### 网络 
 * [Gson](https://github.com/google/gson)

##### UI
 * [Glide](https://github.com/bumptech/glide)
 * [MagicIndicator](https://github.com/hackware1993/MagicIndicator)
License
----
Copyright (c) 2020  喜马拉雅

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER ITWARE.
 
