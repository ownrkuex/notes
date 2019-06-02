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

React中的事件处理和常规的html中的事件处理基本上是一样的，主要有几点不同：

* React中的事件是用camelCase方式命名的，不是lowercase方式。
* React中只能传入一个函数或lambda表达式作为事件处理器，不能是字符串。

举个栗子，在html中：

```html
<button onclick="activateLasers()">
  Activate Lasers
</button>
```

在React中就可以：

```jsx
<button onClick={activateLasers}>
  Activate Lasers
</button>
```

另一个不同点是，在html中可以让事件处理器返回false来阻止默认事件。但React不行，只能调用事件对象的`preventDefault()`方法阻止默认事件。举个栗子，在html中要阻止a标签的默认点击事件（打开新的标签页），可以这样：

```html
<a href="#" onclick="console.log('The link was clicked.'); return false">
  Click me
</a>
```

但在React中，只能这样：

```jsx
function ActionLink() {
  function handleClick(e) { // 只要参数名是e，就默认是事件对象
    e.preventDefault();
    console.log('The link was clicked.');
  }

  return (
    <a href="#" onClick={handleClick}>
      Click me
    </a>
  );
}
```

如果使用class定义组件，最佳实践是把组件类的一个成员方法作为事件处理器。举个栗子，下面的Toggle组件表示一个开关，每次点击都会在ON和OFF之间切换：

```jsx
class Toggle extends React.Component {
  constructor(props) {
    super(props);
    this.state = {isToggleOn: true};

    // 必须要绑定，否则点击进入handleClick方法时，this是undefined
    // 更广泛来说，只要引用了成员方法（后面不带括号），就应该绑定
    this.handleClick = this.handleClick.bind(this);
  }

  handleClick() {
    this.setState(state => ({
      isToggleOn: !state.isToggleOn
    }));
  }

  render() {
    return (
      <!-- 引用了this.handleClick方法，没有括号，不是调用，所以必须绑定 -->
      <button onClick={this.handleClick}>
        {this.state.isToggleOn ? 'ON' : 'OFF'}
      </button>
    );
  }
}
```

从原理上来说，只要把类的成员方法作为函数看待，就需要绑定，否则在函数（其实是方法）执行过程中，this就是undefined。

这种绑定操作有语法糖，把上面的handleClick方法修改如下：

```jsx
// 很新的特性，慎用
handleClick = () => {
  this.setState(state => ({
    isToggleOn: !state.isToggleOn
  }));
}
```

然后构造器里面的绑定方法就不需要再调用了。

当然也可以把handleClick方法外再包一层lambda表达式作为事件处理器，这样就不必把handleClick看做函数了，也就不需要绑定操作了。但这种方式有性能问题，每次渲染都要重新创建一个lambda表达式对象。如果仅仅是这样其实是完全可以接受的，但lambda表达式作为参数传递给子组件，那么这个子组件也要重新渲染一遍。所以，这种方式不推荐。

#### 事件处理器参数

向事件处理器传递参数有两种方式：

```jsx
<button onClick={(e) => this.deleteRow(id, e)}>Delete Row</button>
<button onClick={this.deleteRow.bind(this, id)}>Delete Row</button>
```

这两种方式是等价的。第一种方式不需要绑定操作，但事件对象要手动传递；第二种方式要额外绑定this，但事件对象会自动传递。其实从第二种方式可以看出，所谓的this绑定，其实是用一种曲折的方式把this对象作为参数传递给函数。传递任意参数（绑定任意对象）都可以用这种方式，比如上面的id。

### 条件渲染

最简单直接的条件渲染：

```jsx
function Greeting(props) {
  const isLoggedIn = props.isLoggedIn;
  if (isLoggedIn) {
    return (<UserGreeting />);
  }
  return (<GuestGreeting />);
}
```

使用局部变量实现条件渲染：

```jsx
render() {
  // 防止竞态条件
  const isLoggedIn = this.state.isLoggedIn;
  let button;

  if (isLoggedIn) {
    button = (<LogoutButton />);
  } else {
    button = (<LoginButton />);
  }

  return (
    <div>
      <Greeting isLoggedIn={isLoggedIn} />
      {button}
    </div>
  );
}
```

其实上面两种方式都差不多，都很繁琐。下面使用&&操作符实现单分支条件渲染：

```jsx
function Mailbox(props) {
  const unreadMessages = props.unreadMessages;
  return (
    <div>
      <h1>Hello!</h1>
      {
        unreadMessages.length > 0 && (
          <h2>
            You have {unreadMessages.length} unread messages.
          </h2>
        )
      }
    </div>
  );
}
```

三目运算符实现条件渲染：

```jsx
render() {
  const isLoggedIn = this.state.isLoggedIn;
  return (
    <div>
      {isLoggedIn ? (
        <LogoutButton onClick={this.handleLogoutClick} />
      ) : (
        <LoginButton onClick={this.handleLoginClick} />
      )}
    </div>
  );
}
```

如果三目运算符操作的表达式太长，可读性会变差。这个时候应该考虑提取组件。

#### 中止渲染

组件的`render()`方法如果返回null，可以阻止该组件被渲染，起到隐藏组件的效果。返回null不影响生命周期方法，该调用的生命周期方法还是会调用。

### 列表和Keys

#### 循环渲染

循环渲染有两种方式，一种是直接用for循环或while循环准备好React元素数组，再将该数组嵌入到JSX中；另一种是用数组的`map()`方法将普通数组映射为React元素数组，再将该数组嵌入到JSX中。

注意如果传给`map()`方法的映射函数过于复杂，应考虑提取组件。

#### 基本列表组件

看下面的栗子，组件接收一个整数数组作为参数，返回整数的列表。

```jsx
function NumberList(props) {
  const numbers = props.numbers;
  const listItems = numbers.map(number => (
    <li>{number}</li>
  ));
  return (
    <ul>{listItems}</ul>
  );
}
```

这段代码会引发一个警告：列表项应该要有一个key。消除警告要这么做：

```jsx
function NumberList(props) {
  const numbers = props.numbers;
  // key是字符串属性，key={number}这里有一个自动类型转换
  const listItems = numbers.map(number => (
      <li key={number}>
        {number}
      </li>
  ));
  return (
    <ul>{listItems}</ul>
  );
}
```

#### Keys

如果一个组件以列表的形式包含若干子组件或标签，那么这些子组件或标签就必须要有key属性。key用于唯一标识一个列表项（在同一级中）。在更新组件时，通过这个唯一标识才可以判断某个列表项是否发生变化、是否是新增的以及哪些列表项被删除了。如果找不到合适的方式设置key，可以把列表项索引当做key，这也是默认行为，但不推荐这么做。

注意设置key的方式和组件参数完全一样，但key不是组件参数。

### 表单

React中的表单和普通的html表单几乎完全一样。下面这段代码：

```html
<form>
  <label>
    Name:
    <input type="text" name="name" />
  </label>
  <input type="submit" value="Submit" />
</form>
```

作为普通的html表单和作为React组件，其行为是完全一样的。但React可以在此基础上加点东西，比如增加表单提交事件和用户输入事件：

```jsx
class NameForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {value: ''};

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    // 除了记录最新的用户输入，还可以对用户输入进行实时检测，动态提示错误
    // 甚至还可以直接修正输入
    this.setState({value: event.target.value});
  }

  handleSubmit(event) {
    // 除了日志记录，还可以在提交表单时检测用户输入，根据检测结果中止提交和提示错误
    alert('A name was submitted: ' + this.state.value);
    event.preventDefault();　// 中止提交
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          Name:
          <input 
            type="text" 
            value={this.state.value} 
            onChange={this.handleChange} 
          />
        </label>
        <input type="submit" value="Submit" />
      </form>
    );
  }
}
```

上面这个组件称为受控组件。

#### textarea

在普通html表单中，textarea的内容定义在子节点中，比如：

```html
<textarea>Hello, world!</textarea>
```

但是在React表单中得这么写：

```jsx
// 和单行input文本框一致
(<textarea value="Hello, world!" />)
```

#### select

在普通的html表单中是这么使用select构造下拉菜单的：

```html
<select>
  <!-- 选中了第二项 -->
  <option value="grapefruit">Grapefruit</option>
  <option selected value="coconut">Coconut</option>
</select>
```

在React表单中得这么写：

```jsx
// 单选，选中了第二项
(
  <select value="coconut">
    <option value="grapefruit">Grapefruit</option>
    <option value="coconut">Coconut</option>
  </select>
)
```

多选：

```jsx
// 全选
(
  <select multiple={true} value={["grapefruit", "coconut"]}>
    <option value="grapefruit">Grapefruit</option>
    <option value="coconut">Coconut</option>
  </select>
)
```

#### checkbox

在普通html表单中构造复选框：

```html
<input checked type="checkbox" />
```

在React表单中：

```jsx
(<input type="checkbox" checked={true} />)
```

#### 同时处理多个input

如果多个input上的事件交给一个事件处理器处理，那么这些input上要有name属性，这样在事件处理器中就可以通过event.target.name区分当前事件发生在哪个input上。

#### input空值

如果把input的value值设置为常量，那么该input是不可编辑的。举个栗子：

```jsx
/* 随便组件的状态怎么变，输入框里都只有一个hi，因为每次渲染都只会渲染这个常量
   出来的效果就是不可编辑
   之前的栗子可以编辑是因为把value设置成组件状态，且有onChange配合
   达到可编辑的效果除了之前栗子的做法，还可以把value设置成null或undefined，
   这样不需要onChange也可以编辑，不过这样意义不大
*/
ReactDOM.render(<input value="hi" />, mountNode);
```

### ref转发

之前我们说过在React组件树中数据流是单向的，只能从父组件到子组件，但其实通过ref转发我们可以做到信息从子组件传递给父组件。这种逆向流动一般发生在叶子组件（节点）中，这些组件通常都是一些小巧简单的高复用组件。举个栗子：

```jsx
function FancyButton(props) {
  return (
    <button className="fancy-button">
      {props.children}
    </button>
  );
}
```

使用FancyButton组件的组件可能需要直接操作FancyButton组件内部的button，为了操作上的简便可以直接把button暴露给外部：

```jsx
const FancyButton = React.forwardRef((props, ref) => (
  <button ref={ref} className="fancy-button">
    {props.children}
  </button>
));
```

通过这种方式定义的组件，把原本私有的button公开。外部可以这么访问：

```jsx
const ref = React.createRef();
<FancyButton ref={ref}>Click me!</FancyButton>;
// ref.current就是FancyButton内部的button
```

注意当且仅当`React.forwardRef()`方式定义组件时，组件才会接收ref参数。ref参数无法通过props获取。另外，不仅基本标签（button、p、h1这种）可以公开，React组件也可以用ref转发的方式公开。