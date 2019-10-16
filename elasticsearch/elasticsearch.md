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
* **analysis**: 在当前索引中创建自定义的全文字段分析器，后面详细写

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

#### 基本数据类型