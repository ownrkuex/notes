## Elasticsearch 实用教程

### 版本问题

1.x -> 2.x -> 5.x
建议使用5.x

### 核心概念

1. 数据从被写入到可以被搜索到，有一个时间差，大概１秒，称为Nearly Real Time，简称**NRT**。
1. **文档**，相当于传统的关系型数据库中的一行记录。
1. **类型**，又称mapping，就是文档的schema。
1. **索引**，相当于一个database。一个索引中可以有多个类型。
1. **集群**和**节点**，这两个都是物理上的概念，节点对应一个物理进程，集群是多个节点的集合，使用Master-Slave模式。
1. **分片**，shard，逻辑概念，一个索引的数据量过于庞大，一台机子放不下，于是就把一个索引划分成若干分片，分散保存在不同的机子上。分片不仅解决了存储问题，还能提高查询性能，因为可以多个分片并行查询。
1. **副本**，replica，就是分片的冗余备份。shard又称为primary shard，replica又称为replica shard。副本不仅提供高可用性，还能分担primary shard的读压力。elasticsearch还规定，同一个primary shard和replica shard不能放在同一个节点上。

### 索引管理

#### 创建索引
```
PUT /{index_name}
{
    "settings": { ... any settings ... },
    "mappings": {
        "mapping1_name": { ... any mappings ... },
        "mapping2_name": { ... any mappings ... },
        ...
    }
}
```
es的默认行为是，在必要的时候自动创建索引（和类型）。比如写入一个文档，然后这个文档所属的索引不存在，就会自动创建一个。关闭索引的自动创建，可以在config/elasticsearch.yml中设置`action.auto_create_index: false`。

#### 删除索引
```
DELETE /{index_name}
```
可以使用通配符批量删除索引，不过这种行为很危险。建议在config/elasticsearch.yml中设置`action.destructive_requires_name: true`，禁止在删除索引的时候使用通配符。

#### 重要的索引设置
* **number_of_shards**: primary shards数量，索引创建后不可修改
* **number_of_replicas**: replica shards数量，索引创建后可以修改
* **analysis**: 在当前索引中创建自定义的text字段分析器，后面详细写

#### 修改索引设置
```
PUT /{index_name}/_settings
{
  "number_of_replicas": 1
}
```
不需要重启服务。

### 类型

#### 文档元数据
* _source: 文档的实际内容(有效负载)
* _type: 文档所属类型
* _index: 文档所属索引
* _id: 文档唯一标识，可手动指定，不指定将由Elasticsearch自动生成

#### 常用数据类型
* keyword: 普通的字符串类型，和java中的String差不多。
* **text**: 文本类型，是Elasticsearch中最特殊也是最重要的一个类型。Elasticsearch会对该类型的字段分词，并根据分词结果做倒排索引。后面详细写。
* 整数类型: byte, short, integer, long, 都是有符号的，跟java差不多。
* 逻辑类型: boolean，跟java一样
* 浮点类型: 常用的就是float和double，跟java中的一样
* 二进制类型: binary，用来保存图片、音频之类的
* date: 日期类型，Elasticsearch中其实并没有专门的时间类型，date类型的字段的值可以是日期格式化的字符串，也可以是表示毫秒时间戳的长整数，但最终都会存储为毫秒时间戳。

### 查询

#### 简单查询

* 根据id查询
* 查询所有文档：
```
{
	"query": {
		"match_all": {}
	},
	"from": 1, // optional
	"size": 1 // optional
}
```

#### 条件查询

* 倒排索引查询：
```
{
	"query": {
		"match": {
			"title": "xxx" // title字段必须是text类型
		}
	},
	"sort": { // 根据publish_date字段降序排序，可选
		"publish_date": {
			"order": "desc"
		}
	}
}
```
* 精确查询：
```
{
	"query": {
		"term": {
			"author": "xxx" // author字段必须是keyword类型
		}
	}
}
```

#### 聚合查询
// todo