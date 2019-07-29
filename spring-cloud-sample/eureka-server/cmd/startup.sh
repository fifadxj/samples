#!/bin/bash
nohup java -jar -Xms64m -Xmx256m /alidata1/admin/eureka/eureka-server.jar > /alidata1/admin/eureka/logs/eureka-server-nohup.out 2>&1 &
nohup java -jar -Xms64m -Xmx256m /alidata1/admin/eureka/eureka-server2.jar > /alidata1/admin/eureka/logs/eureka-server2-nohup.out 2>&1 &
