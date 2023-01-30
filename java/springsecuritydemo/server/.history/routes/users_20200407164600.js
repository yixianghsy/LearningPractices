await querySql('insert into user(username,password,nickname) value(?,?,?)', [username, password, nickname])
res.send({ code: 0, msg: '注册成功' })