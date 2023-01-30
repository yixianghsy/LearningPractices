const mysql = require('mysql')
const dbOption =require('./config')

//创建连接池
const pool = mysql.createPool(dbOption)
