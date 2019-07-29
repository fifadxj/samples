#!/bin/bash
ps aux|grep "/alidata1/admin/eureka/"|grep -v grep|awk '{print $2}'|xargs kill -9
