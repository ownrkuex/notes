### JSX

考虑如下变量定义：

```jsx
const element = (<h1>hello, world!</h1>);
```

这种语法称为JSX，是javascript的一种扩展语法。标签部分跨行的时候括号不可省略，单行可以省略。JSX把html标签，也就是上面的h1标签看做一个React元素，或者说是一个React对象（可以当做普通的javascript对象用）。JSX把html和js放在一起表面上看起来好像不太好，但其实React认为UI的渲染逻辑和其他UI逻辑（数据绑定、事件处理）是紧密关联的。分离html和js这些不同的代码其实只是表面功夫，分离关注点（组件）才能从本质上松耦合。

#### JSX内嵌表达式

```jsx
const name = "Josh Perez";
const element = (<h1>hello, {name}</h1>);
```

任何合法的js表达式都可以通过这种方式内嵌到JSX中。

#### JSX视作表达式

JSX编译后其实就是普通的js方法调用，返回普通的js对象。所以JSX可以在if和for语句中使用、赋值给变量、作为方法参数传递、作为方法返回值返回。

```jsx
function getGreeting(user) {
  if (user) {
    return (<h1>Hello, {formatName(user)}!</h1>);
  }
  return (<h1>Hello, Stranger.</h1>);
}
```

#### JSX设置标签属性

```jsx
const element = <img src={user.avatarUrl}></img>;
```

通过这种方式可以用任意js表达式设置标签属性。注意花括号两边不要加引号。注意在JSX中标签属性的命名方式是camelCase，和html中不同。比如：html中的class在JSX中是className；tabindex在JSX中是tabIndex。

#### JSX防注入攻击

```jsx
let userMsg = "用户输入的评论，可能包含不安全的字符或脚本";
// 这样是安全的，该转义的字符都会转义，不会执行任何未知的东西
const element = (<span>{userMsg}</span>);
```

#### JSX编译结果

JSX编译后其实是调用了`React.createElement()`方法。下面两种写法等效：

```jsx
const element = (
  <h1 className="greeting">
    Hello, world!
  </h1>
);
```

```javascript
const element = React.createElement(
  'h1',
  {className: 'greeting'},
  'Hello, world!'
);
```

上面代码中`React.createElement()`方法的返回值（简化）：

```javascript
{
  type: 'h1',
  props: {
    className: 'greeting',
    children: 'Hello, world!'
  }
}
```

这个对象称为React元素，可以看做是html标签的js版本的描述。React根据这些对象构建DOM。

### React元素渲染