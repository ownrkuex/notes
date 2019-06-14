### 20190611
* tk通用mapper需要包括Mapper, IdsMapper, MySqlMapper。
* 成功创建返回id即可，成功更新返回成功状态码即可。若需要返回信息，用回填的id再查一遍即可。
* i18n是spring生态内的国际化框架。
* 在工程比较庞大复杂的情况下，service层也要有参数校验。
* 自定义异常的错误响应应尽可能和spring原生的错误响应保持一致，可以在其基础上扩展。
* 应该在http状态码基础上进一步定义业务错误码（500服务器内部错误，5001数据插入错误，5002数据更新错误，诸如此类）。
* dto类的两种命名方式XxxDTO和XxxCommand，前者包括实体的所有字段，后者包括实体的部分字段。比如UserDTO，UserRegisterCommand。
* PageHelper完成分页，Pageable接收分页参数信息（@SortDefault @PageableDefault）。
* 尽量用lombok简化代码。
* jwt主动失效方式：在jwt和服务端保存一个共同的票据，服务端删除票据表明jwt失效。
* BeanUtils.copyProperties有性能问题，尽量不要用。可以用静态工厂方法替代，比如User转UserDTO就在UserDTO类里定义UserDTO.create(User)。

### 20190614

* 对于编程性异常，比如IOException，SQLException这种，只要异常对象本身清楚说明了原因，直接用RuntimeException包装一下抛出即可。
* 数据库的增、改、删操作不需要在应用层判断是否出错。像这种基本的操作都出问题，说明整个系统已经出现严重问题，基本处于不可用状态。在这种状态下，应用层不做任何干预，前端也会收到500错误。