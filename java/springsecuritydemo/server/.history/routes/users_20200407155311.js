var express = require('express');
var router = express.Router();
const querySql = require('../db/index')
const { PWD_SALT, PRIVATE_KEY, EXPIRESD } = require('../utils/constant')
const { md5, upload } = require('../utils/index')
const jwt = require('jsonwebtoken')