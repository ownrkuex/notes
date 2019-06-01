### CSS选择器

* 根据标签名选择。比如下面的栗子，选择了所有的p标签。

  ```css
  p {
      color: red;
  }
  ```

* 根据元素id选择。比如下面的栗子，选择了id为paral的元素。

  ```css
  #paral {
      color: red;
  }
  ```

* 根据元素class选择。比如下面的栗子，选择了class为center的元素。

  ```css
  .center {
      color: red;
  }
  ```

* 根据标签名和class选择。比如下面的栗子，选择了所有p标签**且**class为center的元素。

  ```css
  p.center {
      color: red;
  }
  ```

* 一个标签可以同时具有多个class。比如下面的栗子，p元素同时具有center和large两个class。

  ```html
  <!-- 空格空开 -->
  <p class="center large">Hello, world!</p>
  ```

* 分组选择器。如果多个选择器对应的样式是一样的，可以用分组选择器合并代码。比如下面的栗子，选择所有h1**或**h2**或**p元素。

  ```css
  h1, h2, p {
      color: red;
  }
  ```

* CSS注释是`\*...*\`，可以跨行。

### 引入CSS

引入CSS有三种方式：

* 外部样式表（推荐）

  ```html
  <head>
    <!-- 注意rel不要写成ref -->
    <link rel="stylesheet" type="text/css" href="mystyle.css"/>
  </head>
  ```

* 内部样式表

  ```html
  <head>
    <style>
      p {
        color: red;
      }
    </style>
  </head>
  ```

* 行内style属性

  ```html
  <p style="color:blue;margin-left:30px;">Hello, world!</p>
  ```

#### CSS属性冲突

外部样式表中定义了如下样式：

```css
h1 {
    color: red;
}
```

内部样式表中定义了如下样式：

```css
h1 {
    color: blue;
}
```

那么h1的样式该使用哪一个呢？如果外部样式表在内部样式表之后引入，也就是说link标签在style标签之后，那颜色就是红色；反之就是蓝色。可以这么理解，应用后定义的样式的时候把先定义的样式覆盖了。

最佳实践：所有样式用外部样式表的方式引入，既能把html和css分开，又能避免冲突。

#### 样式优先级

样式优先级从高到低为：

1. 行内style属性
1. 内部或外部样式表，后定义的比先定义的优先级高
1. 浏览器默认样式

样式优先级高，就会优先应用，把优先级低的覆盖掉。在一些复杂的情况下，可以这么理解，**浏览器按样式定义的顺序逐个应用，后面的样式自然会覆盖掉前面的样式**。

### CSS颜色

CSS颜色有几种常见的表达方式：

* 直接用颜色名，比如red、blue等。
* #ff6347
* rgb(255, 99, 7)，提示一下，灰色的rgb三个值是一样的
* rgba(2555, 99, 7, 0.5)

CSS和颜色相关的常见属性：

* 字体颜色color
* 背景颜色background-color
* 边框颜色border-color