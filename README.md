![](https://github.com/fcannizzaro/oshare-java/blob/master/logo.png?raw=true)

Node Object Sharing (Socket) | **Remote Method Invocation** | Support for **callbacks**

[![npm](https://img.shields.io/npm/v/oshare.svg)](https://www.npmjs.com/package/oshare)

# Library
Import [Jar](https://github.com/fcannizzaro/oshare-java)

# Usage

## 0. Code Generation (Server)
```javascript
var io = share.server(3000, shared, callback, 'java');
```

- insert language param 'java' and run the server. (**only once**)
- a **Remote.java** file will be created in the root.

## 1. Share Methods/Fields

```java
@Share
private Integer java = 8;

@Share
private void alert(String str) {
  System.out.println("Alert: " + str);
}
```

## 2. Register Sharing Classes
```java
  Shared.register(this);
```

## 3. Connect Socket

#### init(String url, Class remote, [, ReadyListener])

```java
Oshare.init("http://localhost:3000", Remote.class, this);
```

## 4. Attach ReadyListener (Optional)

```java
public class Main implements ReadyListener {

    @Override
    public void onReady() {
      // this listener is called when data is updated.
    }

    @Override
    public void onConnected() {
      // this listener is called when socket is connected.
    }

}
```

# Details

## Annotations

### Share([, value])
- value optional name/path

### Callback
Mark method as callback

## Callback

```java
@Callback
void apiRun(String value, Integer number) {
  // do something
}

@Override
public void onConnected() {
  // add Callback Object as parameter
  Remote.api.run(Oshare.Callback(this));
}
```

Create a method containing method path in the name.

Ex. Remote.api.run -> **apiRun**

### Multiple Callbacks

```java
@Callback
void multi0(String value) {
  // do something with first callback
}

@Callback
void multi1(Integer value) {
  // do something with second callback
}

@Override
public void onConnected() {
  // add Callback Object as parameter
  // callbacks are assigned in order.
  // multi0 , multi1
  Remote.multi(Oshare.Cb(this), Oshare.Cb(this));
}
```
