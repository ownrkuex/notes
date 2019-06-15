## 语句

多条语句写一行必须用分号隔开，一行一条语句可以不需要，但强烈建议加上。

## let

let和var类似，用于声明变量。和var的区别在于：

* let变量的作用域不会超过当前代码块（块级作用域）。
* var变量存在作用域提升现象，也就是说可以先使用后声明（作用域提升到全局），但let没有这种怪异现象，必须先声明再使用。
* 在for循环中用let声明循环变量，该变量只在**本次循环内有效**，也就是说，循环几次，就有几个i，这些i是不同的变量，互相独立互不影响。用var，不管循环几次，都只有一个i。

## 块级作用域

* 块级作用域内优先使用函数表达式，不使用函数声明语句：

  ```js
  {
      // 这是函数声明语句，块级作用域内不推荐使用
      function f() {
      }
  }
  {
      // 这是函数表达式，优先使用这种方式替代函数声明语句
      let f = function() {
      }
  }
  ```

* 块级作用域必须要有花括号，没有花括号就没有块级作用域。比如：

  ```js
  if (true) {
      console.log('这是一个块级作用域');
  }
  if (true)
      console.log('这里没有块级作用域！');
  ```

## const

const用于声明常量，声明后必须立刻初始化，不能留到以后赋值。const常量的作用域不会超过当前代码块（块级作用域）。const常量也必须先声明和初始化，才能使用。

## 顶层对象的属性

顶层对象是指window对象（它的另一个名字是self），用var声明一个全局变量，该变量将自动成为window对象的属性，但let变量和const常量不属于window对象。比如：

```js
var a = 1;
console.log(window.a); // 输出1
let b = 1;
console.log(window.b); // 输出undefined
const PI = 3.14;
console.log(window.PI); // 输出undefined
```

## 数组的解构赋值

ES6允许从数组和对象中批量提取和分发值，这一过程称为解构。比如：

```js
let nums = [1, 2];
let a = nums[0];
let b = nums[1];

// 解构做法
let [c, d] = nums; // c的值为1，d的值为2
```

上面的写法属于模式匹配，只要左右两边的结构相同，变量就会得到相同位置上的值。更多栗子：

```js
let [, a] = [1, 2];
a // 2
let [head, ...tail] = [1, 2, 3];
head // 1
tail // [2, 3]
```

如果两边结构不同，左边的变量在右边没有相同的位置，那么该变量就是undefined或[]。比如：

```js
let [a, b, ...c] = [1];
a // 1
b // undefined
c // []
```

两边结构不同的另一种情况是不完全解构：

```js
let [a, b] = [1, 2, 3];
a // 1
b // 2
let [a, [b], c] = [1, [2, 3], 4];
a // 1
b // 2
c // 4
```

如果右边是不可遍历的值（不具备Iterator接口），就会报错。注意用花括号括起的普通对象是不可遍历的，也就是说数组的解构赋值不可以用于对象。

### 默认值

解构赋值允许指定默认值：

```js
let [a = 1] = [];
a // 1
```

当右边相应位置的值`=== undefined`（严格等于undefined）时，默认值才会生效。如果默认值是个表达式，则只会在默认值生效的情况下计算。默认值可以引用解构赋值的其他变量，比如：

```js
let [a = 1, b = a] = [];
a // 1
b // 1
let [c = d, d = 1] = []; // ReferenceError：d未定义
```

## 对象的解构赋值

解构不仅可以用于数组，还可以用于对象：

```js
let { b, a } = { a: "foo", b: "bar" };
a // "foo"
b // "bar"
let { c } = { a: "foo", b: "bar" };
c // undefined，解构失败
```

对象的解构赋值除了可以取得对象的成员变量，还可以取得成员方法：

```js
let { log } = console; // 获取console的log方法，赋值给log
log('hello');
```

对象的解构赋值可以取到继承的属性：

```js
const obj1 = {};
const obj2 = { foo: 'bar' };
Object.setPrototypeOf(obj1, obj2);

const { foo } = obj1;
foo // "bar"
```

和数组解构类似，对象解构也可以设置默认值。默认值生效的条件是对象中相应的属性或方法`=== undefined`。

将一个已声明的变量用于解构赋值需要注意一个坑：

```js
let x;
{ x } = { x: 1 }; // 语法错误，将{ x }解释成了代码块
({ x } = { x: 1 }); // 正确的写法
```

数组在本质上是特殊的对象，所以对象解构也可以用于数组：

```js
let arr = [1, 2, 3];
let { [0]: first, [arr.length - 1]: last } = arr;
first // 1
last // 3
```

## 字符串的解构赋值

字符串可以看作是一个类似数组的对象，因此字符串也可以解构赋值：

```js
const [a, b, c, d, e] = 'hello';
a // h
b // e
c // l
d // l
e // o
const { length } = 'hello';
length // 5
```

## 函数参数的赋值解构

函数的参数也可以赋值解构：

```js
function add([x, y]){
  return x + y;
}

add([1, 2]); // 3
```

add函数的参数类型是一个数组，但在传入参数的那一刻，参数被解构成变量x和y，函数中的代码只能使用x和y。

同样，函数参数的解构赋值也可以使用默认值：

```js
function move({x = 0, y = 0} = {}) {
  return [x, y];
}

move({x: 3, y: 8}); // [3, 8]
move({x: 3}); // [3, 0]
move({}); // [0, 0]
move(); // [0, 0]
```

同样，默认值触发的条件是相应的对象属性或方法`=== undefined`。

## 解构赋值的用途

* 交换变量的值：
  
  ```js
  let x = 1;
  let y = 2;
  ([y, x] = [x, y]);
  x // 2
  y // 1
  ```

* 当函数返回多个值时，这些值会包装在数组或对象中返回。通过解构赋值，可以方便地取出多个返回值。
* 方便地将一组参数与变量名对应起来并指定默认值，提高可读性。
* 快速提取JSON对象中的值。
* 遍历map对象：

  ```js
  const map = new Map();
  map.set('first', 'hello');
  map.set('second', 'world');

  for (let [key, value] of map) {
    console.log(key + " is " + value);
  }
  // first is hello
  // second is world
  ```

## 二进制、八进制与十六进制表示法

二进制用前缀`0b`，八进制用前缀`0o`，十六进制用前缀`0x`。

其他进制转十进制：

```js
Number('0xff') // 255
Number('0o77') // 63
Number('0b11') // 3
```

## Number.EPSILON