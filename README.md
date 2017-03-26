![](https://github.com/fcannizzaro/oshare-java/blob/master/logo.png?raw=true)

Node Object Sharing (Socket) | **Remote Method Invocation** | Support for **callbacks**

# Library
Import [Jar](https://github.com/fcannizzaro/oshare-java/releases/tag/1.0.2)

## Before Start

```javascript
var io = share.server(3000, shared, callback, 'java');
```

- insert language param 'java' and run the server. (**only once**)
- a **Remote.java** file will be created in the root.

# Usage

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
- **value** optional name/path (*default = field name*).

### Callback
Mark method as callback.

## Callback

```java
@Callback
void apiRun(String value, Integer number) {
  // do something
}

@Override
public void onConnected() {
  // add callback argument
  Remote.api.run(Oshare.Callback(this));
}
```

Callback names **should** at least contain method path.

Samples:

- Remote.api.run() -> **apiRun()**
- Remote.submodule.hello() -> **submoduleHello()**

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
  // add callbacks argument
  // callbacks are assigned in order.
  // multi0 , multi1
  Remote.multi(Oshare.Cb(this), Oshare.Cb(this));
}
```

## Sample

See [Main.java](https://github.com/fcannizzaro/oshare-java/blob/master/sample/src/main/java/Main.java)]


