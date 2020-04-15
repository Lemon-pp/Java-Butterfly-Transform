## git命令

git添加文件步骤：

1. 克隆仓库：git clone 仓库地址
2. 仓库初始化：git init
3. 添加文件到缓存区：git add 文件名  或者上传全部文件 git add .
4. 将文件提交到归档区：git commit -m ‘注释’
5. 添加仓库：git remote add origin 仓库地址，用origin来代替仓库地址。
6. 通过push提交到远程仓库：git push -u origin master

本地文件修改后：

 	1. 使用git status 查看状态
 	2. 通过添加文件步骤，进行提交。

git init 初始化

git add .    添加当前目录下的所有文件到GitHub

git add  文件名   添加指定文件到GitHub

git commit -m ‘提交注释’

git push  上传

git pull  更新代码

git status 查看状态

git clone 链接地址  clone项目代码

git log 查看日志

git checkout -- 文件名 撤回修改内容

git rm 文件名  删除文件

git --help   # 帮助命令 

git pull origin master   # 将远程仓库里面的项目拉下来

dir             # 查看有哪些文件夹

git rm -r --cached target    # 删除target文件夹

