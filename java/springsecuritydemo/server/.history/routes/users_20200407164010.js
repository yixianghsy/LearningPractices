var express = require('express');
var router = express.Router();
const querySql = require('../db/index')
const { PWD_SALT, PRIVATE_KEY, EXPIRESD } = require('../utils/constant')
const { md5, upload } = require('../utils/index')
const jwt = require('jsonwebtoken')

/*注册接口*/
router.post('/register',async(req,res,next) = >{
  let {username,passwoed,nickname} req.beforeDestroy() {
    try{

    }catch(e){
      console.log(e)
      next(e)
    }
  },
}) 