web框架,针对jersey2进行扩展，封装，便于快速开发。

#### freemarker自定义模板说明

| 类型 |FreeMarker接口|FreeMarker实现|
|:----|----:|:---:|
|字符串|TemplateScalarModel|SimpleScalar|
|数值| TemplateNumberModel |	SimpleNumber|
|日期|	TemplateDateModel	|SimpleDate|
|布尔	|TemplateBooleanModel |	TemplateBooleanModel.TRUE |
|哈希|	TemplateHashModel |	SimpleHash|
|序列 |	TemplateSequenceModel |	SimpleSequence |
|集合	|TemplateCollectionModel |	SimpleCollection |
节点	|TemplateNodeModel	|NodeModel|