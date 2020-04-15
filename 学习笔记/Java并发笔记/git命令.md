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

文件提交回滚：

1. 通过git log查看哈希码
2. git reset 哈希码回滚。
3. 再进行add、commit等操作。

回滚错误再回滚：

1. 通过git reflog 查看哈希码
2. 再通过git reset 哈希码执行操作。



