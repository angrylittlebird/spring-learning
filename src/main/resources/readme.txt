1.elasticsearch
需要在安装es目录下的config/elasticsearch.yml文件中配置如下es服务器ip，以及集群名称：
cluster.name: my-application
才能正确连接，否则会报org.elasticsearch.client.transport.NoNodeAvailableException: None of the configured nodes are available错误提示