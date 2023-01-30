var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
const cors = require('cors')
const expressJWT = require('express-jwt')
const {PRIVATE_KEY} = require('./utils/constant')

var artRouter = require('./routes/article');
var usersRouter = require('./routes/users');
var commentRouter = require('./routes/comment')

var app = express();

console.log(1)
// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(cors())
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use(expressJWT({
  secret: PRIVATE_KEY   
}).unless({
  path: ['/api/user/register','/api/user/login','/api/user/upload','/api/article/allList','/api/article/detail','/api/comment/list']  //白名单,除了这里写的地址，其他的URL都需要验证
}));

app.use('/api/article', artRouter);
app.use('/api/user', usersRouter);
app.use('/api/comment',commentRouter)

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  console.log(err)
  if (err.name === 'UnauthorizedError') {   
    //  这个需要根据自己的业务逻辑来处理
    res.status(401).send({code:-1,msg:'token验证失败'});
  }else{
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};
    
    // render the error page
    res.status(err.status || 500);
    res.render('error');
  }
  
});

module.exports = app;
