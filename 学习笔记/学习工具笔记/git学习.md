## git命令

##### 1. git添加文件步骤：

1. 克隆仓库：git clone 仓库地址
2. 仓库初始化：git init
3. 添加文件到缓存区：git add 文件名  或者上传全部文件 git add .
4. 将文件提交到归档区：git commit -m ‘注释’
5. 添加仓库：git remote add origin 仓库地址，用origin来代替仓库地址。
6. 通过push提交到远程仓库：git push -u origin master

##### 2. 本地文件修改后：

 	1. 使用git status 查看状态
 	2. 通过添加文件步骤，进行提交。

##### 3.文件提交回滚：

1. 通过git log查看版本号。
2. git reset [opt] 版本号   进行回滚。默认opt参数是--mixed。
   1. --mixed  回滚缓冲区、归档区。
   2. --hard  回滚工作区、缓冲区、归档区。
   3. --soft  只回滚归档区
3. 再进行add、commit等操作。

##### 4.回滚错误还原：

1. 通过git reflog 查看版本号
2. 再通过git reset 版本号执行操作。

##### 5.创建分支

查看当前分支：git branch -v

创建分支并切换：git checkout -b 分支名

添加文件：步骤如上：

​				1.git add 文件名

​				2.git commit -m ‘注释’

​				3.git push origin 分支名

##### 6.合并分支：

1.先切换到master分支：git checkout master

2.查看当前分支：git branch -v

3.将b1分支合并到自己：git merge b1

4.同步到远程仓库：git push origin master

##### 7.合并分支产生冲突：

假设分支b1和b2都对4.txt进行了修改，然后add、commit了之后,都切到master分支，进行merge操作，bimerge完了，b2再去merge就产生了冲突。

解决办法：

1.新版本中，必须解决冲突了才能切换分支。

2.先git reset --hard 版本号回滚。

3.然后切换到b2分支，然后merge主干master分支。然后协商确当要改动的内容。然后删掉不要的，再次进行提交。

4.然后切回master分支，使用git merge b2将b2合并到master。

##### 8.本地仓库落后远程仓库不一致时：

更新本地仓库：git pull 。默认拉取origin。

git pull 是git fetch 和 git  merge操作的合并。





