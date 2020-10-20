# 学习笔记

## 作业备忘

java基础代码还是需要多练，框架用习惯后连居然最基础的读文件都不会写了。

**文件流式读取：**

```
File f = new File(path);
int length = (int) f.length();
byte[] bytes = new byte[length];
try{
	new FileInputStream(f).read(bytes);
}catch (IOException e) {
	// TODO
}
```

**内存参数：**

```
-Xmx：指定最大堆内存
-Xms：指定堆内存空间的初始大小
-Xmn：指定年轻代大小
-Meta：元空间
-DirectMemory：堆外内存
-Xss：设置每个线程栈的字节数
```

