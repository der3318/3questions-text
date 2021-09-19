## 🔒 3 Questions Text

![ver](https://img.shields.io/badge/version-1.0-blue.svg)
![gradle](https://img.shields.io/badge/gradle-4.10.1-green.svg)
![java](https://img.shields.io/badge/java-JDK/JRE%208-brightgreen.svg)
![portable](https://img.shields.io/badge/portable-windows%20x64-yellow.svg)
![license](https://img.shields.io/badge/license-apache%20%28inherited%29-blueviolet.svg)

An encryptor based on [Shamir's secret sharing algo](https://en.wikipedia.org/wiki/Shamir%27s_Secret_Sharing) to protect your personal texts. By setting up 3 question-answer pairs, you will be able to see the plain text if and only if correctly providing any 2 of them.

<img src="/imgs/demo.gif?raw=true">


### 🧱 References

| Name | Version | Note |
| :- | :-: | :- |
| [com.codahale:shamir](https://github.com/codahale/shamir) | 6.2.10 | implementation of Shamir algorithm |
| [com.google.protobuf:protobuf-java](https://github.com/protocolbuffers/protobuf) | 3.17.3 | file serialization |
| [com.github.johnrengelman:shadow](https://github.com/johnrengelman/shadow) | 2.0.1 | fatjar packaging tool |
| [www.iconfinder.com](https://www.iconfinder.com/icons/299091/lock_open_icon) | - | app icon (<img src="/icon.png?raw=true" width="16px">) |


### 💭 How It Works

| Encrypt |
| :-: |
| <img src="/imgs/encrypt.png?raw=true"> |

| Decrypt |
| :-: |
| <img src="/imgs/decrypt.png?raw=true"> |


### 📗 How To Use

Using Portable Version (Windows 10/11 AMD64)

| Step | Description |
| :-: | :- |
| #1 | download and unzip [3qtxt-portable-1.0-x64.zip](https://github.com/der3318/3questions-text/releases/download/v1.0/3qtxt-portable-1.0-x64.zip) |
| #2 | double-click `launch.bat` to use the app |

Using JRE 8+

| Step | Description |
| :-: | :- |
| #1 | download [3qtxt-1.0-all.jar](https://github.com/der3318/3questions-text/releases/download/v1.0/3qtxt-1.0-all.jar) |
| #2 | (optional) download [icon.png](https://github.com/der3318/3questions-text/releases/download/v1.0/icon.png) |
| #3 | run `java -Duser.language=en -Dfile.encoding=UTF8 -jar 3qtxt-1.0-all.jar` in CLI |


### 📘 Build and Redistribution

| Step | Description |
| :-: | :- |
| #1 | clone the repository |
| #2 | run `gradew shadowJar` to compile and build the fatjar from `src/` |
| #3 | find the built binary: `build/libs/3qtxt-1.0-all.jar` |


### 📕 Update Protobuf Config & Template

| Step | Description |
| :-: | :- |
| #1 | clone the repository |
| #2 | setup protocol buffer compiler (or use `protobuf/protoc.exe`) |
| #3 | modify `protobuf/ThreeQuestionsTxtFileMsg.proto` to meet your requirement |
| #4 | run `protoc.exe --java_out=.\ ThreeQuestionsTxtFileMsg.proto` to generate java code |
| #5 | override `src/main/java/ThreeQuestionsTxtFileProtos.java` with the generated one |
| #6 | update the version of `protobuf-java` in `build.gradle` to algin with the protocol buffer compiler you use |
| #7 | rebuild the whole project under `src/` |


