#!/bin/sh
rc-update add postgresql && openrc boot
psql -U postgres -c 'create database expense_management;'

java -jar application.jar
