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

这个对象称为React元素，可以看做是html标签的js版本的描述。从这个对象的结构可以看出JSX中的html必须要有且仅有一个根标签。React根据这些对象构建DOM。

### React元素渲染

假设html文件中有一个`<div>`标签（作为React根DOM节点）：

```html
<div id="root"></div>
```

把一棵由React管理的DOM树放入该节点下（或者说把一个React元素渲染到该节点下），可以这么做：

```jsx
const element = (<h1>Hello, world</h1>);
ReactDOM.render(element, document.getElementById('root'));
```

最终出来的效果就是：

```html
<div id="root">
  <h1>Hello, world</h1>
</div>
```

一个React应用中可以有任意数量的根DOM节点，一般是只有一个。

#### 更新已渲染的React元素

React元素是不可变的，可以看做是UI的一个帧。要更新已渲染的React元素就只能创建一个新的然后替换，举个栗子：

```jsx
function tick() {
  const element = (
    <div>
      <h1>Hello, world!</h1>
      <h2>It is {new Date().toLocaleTimeString()}.</h2>
    </div>
  );
  ReactDOM.render(element, document.getElementById('root'));
}
// 每过一秒就创建一个新的元素并替换掉原来的，出来的效果就是显示实时时间，每秒更新
setInterval(tick, 1000);
```

最佳实践：一个React应用只有一个根DOM节点，对于一个React根DOM节点只显式调用一次`ReactDOM.render()`方法，就是把根组件（对应的元素）渲染进去，其他组件都封装在根组件中。

#### 局部渲染

替换渲染React元素的时候，会比较旧的和新的元素并只渲染不同的部分。对于上一个栗子，虽然每过一秒就替换了整个元素，但频繁的重新渲染整个元素显然在性能上不划算。所以在上一个栗子中，刷新的只有时间部分，其他部分比如hello world那块不会去反复渲染，因为一直没变。

### 组件和组件参数

通过组件可以把UI划分成一些独立的模块。组件和js函数类似，接受组件参数并返回React元素（描述该组件渲染后的效果）。

#### 定义组件

最简单的方式是：

```jsx
function Welcome(props) {
  return (<h1>Hello, {props.name}</h1>);
}
```

这个函数是一个合法的React组件，接收组件参数props返回React元素。用这种函数的方式定义的组件称为“函数组件”。

还有一种方式：

```jsx
class Welcome extends React.Component {
  render() { // 渲染方法
    return (<h1>Hello, {this.props.name}</h1>);
  }
}
```

这两种方式是等价的。

#### 渲染组件

目前为止，React元素中只包含了常规的html标签：

```jsx
const element = (<div />);
```

除了常规的html标签，还可以包含用户自定义组件：

```jsx
function Welcome(props) {
  return (<h1>Hello, {props.name}</h1>);
}

const element = (<Welcome name="Sara" />);
ReactDOM.render(
  element,
  document.getElementById('root')
);
```

在上面的栗子中：

1. 调用`ReactDOM.render()`方法渲染`<Welcome name="Sara" />`。
1. 调用Welcome组件并传入参数`{ name: "Sara" }`。
1. Welcome组件返回React元素`<h1>Hello, Sara</h1>`。
1. React渲染`<h1>Hello, Sara</h1>`到根节点下。

注意自定义组件名必须用大写字母开头。

#### 组合组件

一个组件中可以包含任意数量的其他组件，比如：

```jsx
function Welcome(props) {
  return <h1>Hello, {props.name}</h1>;
}

function App() {
  return (
    <div>
      <Welcome name="Sara" />
      <Welcome name="Cahal" />
      <Welcome name="Edite" />
    </div>
  );
}

ReactDOM.render(
  <App />,
  document.getElementById('root')
);
```

最佳实践：一个React应用只包含一个名为App根组件，其他所有组件都包含在该组件中。

#### 提取组件

组件不应该过于庞大，过于庞大的组件将难以维护，其中的一些公共的部分也将难以复用。对过于庞大的组件可以把其中的一些部分提取出来做成另一个单独的组件。举个栗子：

```jsx
// 用户评论组件，展示用户信息、用户头像、评论正文和评论时间
function Comment(props) {
  return (
    <div className="Comment">
      <div className="UserInfo">
        <img className="Avatar"
          src={props.author.avatarUrl}
          alt={props.author.name}
        />
        <div className="UserInfo-name">
          {props.author.name}
        </div>
      </div>
      <div className="Comment-text">
        {props.text}
      </div>
      <div className="Comment-date">
        {formatDate(props.date)}
      </div>
    </div>
  );
}
```

可以把用户头像部分提取出来做成一个单独的Avatar组件：

```jsx
function Avatar(props) {
  return (
    <img className="Avatar"
      src={props.user.avatarUrl}
      alt={props.user.name}
    />

  );
}
```

然后再把Avatar组件和用户信息部分进一步提取出来作为一个UserInfo组件：

```jsx
function UserInfo(props) {
  return (
    <div className="UserInfo">
      <Avatar user={props.user} />
      <div className="UserInfo-name">
        {props.user.name}
      </div>
    </div>
  );
}
```

Comment组件简化后成了这样：

```jsx
function Comment(props) {
  return (
    <div className="Comment">
      <UserInfo user={props.author} />
      <div className="Comment-text">
        {props.text}
      </div>
      <div className="Comment-date">
        {formatDate(props.date)}
      </div>
    </div>
  );
}
```

最佳实践：适当提取组件可以提高组件的可维护性和复用性。一个应用中应该只有两种组件，一种是小巧的高复用的组件，另一种是起骨架作用的组件。

#### 组件参数是只读的

下面这个函数：

```javascript
function sum(a, b) {
  return a + b;
}
```

称为“纯函数”。纯函数是指不修改参数且在参数相同的情况下返回值也一定相同的函数。举个反例，下面这个函数就不是纯函数：

```javascript
function withdraw(account, amount) {
  account.total -= amount;
}
```

React强制要求所有组件必须具有纯函数的行为，也就是说组件参数是只读的。

### 组件内部状态和生命周期

考虑之前那个计时器的例子：

```jsx
function tick() {
  const element = (
    <div>
      <h1>Hello, world!</h1>
      <h2>It is {new Date().toLocaleTimeString()}.</h2>
    </div>
  );
  ReactDOM.render(element, document.getElementById('root'));
}

setInterval(tick, 1000);
```

把计时器组件化：

```jsx
function Clock(props) {
  return (
    <div>
      <h1>Hello, world!</h1>
      <h2>It is {props.date.toLocaleTimeString()}.</h2>
    </div>
  );
}

function tick() {
  ReactDOM.render(
    <Clock date={new Date()} />,
    document.getElementById('root')
  );
}

setInterval(tick, 1000);
```

但是这样还不够，我们希望能把计时器相关的代码全部封装到Clock组件中，外部不需要做额外的工作，说白了就是这种效果：

```jsx
// 其他代码全在Clock组件里
ReactDOM.render(<Clock />, document.getElementById('root'));
```

首先我们需要把Clock组件改写为class组件并增加一个表示当前时间的内部状态：

```jsx
class Clock extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      time: new Date()
    };
  }
  render() {
    return (
      <div>
        <h1>Hello, world!</h1>
        <h2>It is {this.state.time.toLocaleTimeString()}</h2>
      </div>
    );
  }
}
```

组件的内部状态是私有的，且完全由当前组件控制，对父组件和子组件都不可见。可以通过`this.setState()`方法更新状态，更新状态后将自动调用`render()`方法重新渲染组件。

然后我们需要在组件中的某个地方设置定时器，每过一秒就更新内部状态。为此我们引出两个组件的生命周期方法，在这两个方法中处理定时器相关的逻辑：

```jsx
class Clock extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      time: new Date()
    };
  }

  componentDidMount() {
    // timer只是一个普通字段，不是状态也不是参数
    this.timer = setInterval(() => this.setState({
      time: new Date()
    }), 1000);
  }

  componentWillUnmount() {
    clearInterval(this.timer);
  }

  render() {
    return (
      <div>
        <h1>Hello, world!</h1>
        <h2>It is {this.state.time.toLocaleTimeString()}</h2>
      </div>
    );
  }
}
```

`componentDidMount()`和`componentWillUnmount()`两个方法都是组件的生命周期方法。组件挂载（第一次渲染）到DOM树后调用`componentDidMount()`；组件从DOM树卸载（删除）前调用`componentWillUnmount()`。

快速梳理一下整个过程：

1. 当`<Clock />`作为参数传递给`ReactDOM.render()`方法时，React首先调用Clock组件的构造器得到Clock组件对象，且该组件的内部状态初始化为当前时间。
1. React调用组件的`render()`方法，将Clock组件挂载到DOM树。
1. React调用组件的`componentDidMount()`方法。在方法内部，Clock组件设置定时器，每隔一秒重设内部状态并自动重新渲染。
1. 如果Clock组件从DOM树卸载，React将先调用`componentWillUnmount()`停止计时器。

#### 关于`this.setState()`方法

* 不要直接修改state对象，把state对象当做不可变的。只能在构造器中赋值state，只能用`this.setState()`方法更新state对象。
  ```jsx
  // 错了，这样不会重新渲染
  this.state.comment = 'Hello';
  ```
  ```jsx
  // 正确姿势
  this.setState({comment: 'Hello'});
  ```
* 组件参数和状态的更新是异步的，如果组件的下一个状态依赖于组件参数和当前状态，应当特别注意：
  ```jsx
  // 可能会错，this.state和this.props可能不是最新的
  this.setState({
    counter: this.state.counter + this.props.increment,
  });
  ```
  ```jsx
  // 正确，传入一个函数，指示组件如何根据当前状态和组件参数更新到下一状态
  this.setState((state, props) => ({
    counter: state.counter + props.increment
  }));
  ```
* 可以只更新部分状态，举个栗子：
  ```jsx
  constructor(props) {
    super(props);
    this.state = {
      posts: [],
      comments: ["hello, world"]
    };
  }
  ```
  ```jsx
  // 只更新posts，comments原封不动，部分更新也会自动渲染
  this.setState({
    posts: [8080, 8081, 8082]
  });
  ```

#### 瀑布数据流

组件的内部状态无法直接暴露，但可以把自己的内部状态作为组件参数传递给子组件（也只能是子组件，不能是其他）。子组件不会知道自己接收的参数的来源。这种模式称为“瀑布数据流”，组件的状态只能影响自己以及下游的子组件。

组件是有状态的还是无状态的这属于组件内部的实现细节，和其他组件没有任何关系。

### 事件处理

