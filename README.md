## 平时使用的代码封装

build.gradle
```
allprojects {
    repositories {
		***
		maven { url 'https://jitpack.io' }
    }
}
```

- support-v4
```
implementation 'com.github.jackyxq.common:util:1.0.9'
implementation 'com.github.jackyxq.common:annotations:1.0.9'
annotationProcessor 'com.github.jackyxq.common:compiler:1.0.9'
```

- androidx
```
implementation 'com.github.jackyxq.common:util:1.1.0'
implementation 'com.github.jackyxq.common:annotations:1.1.0'
annotationProcessor 'com.github.jackyxq.common:compiler:1.1.0'
```