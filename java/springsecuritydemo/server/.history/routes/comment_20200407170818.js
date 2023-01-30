var express = require("express");
var router = express.Router();
const querySql = require("../db/index");
/**新增博客接口 */
router.post('/add',async(req,res,next) =>{
     let { title, content } = req.body;
     let { username } = req.user;

     try{
         let result = await querySql('select id from user where username = ?',[username])
         let user_id = result[0].id 
      await querySql(
        "insert into article(title,content,user_id,create_time) values(?,?,?,NOW())",
        [title, content, user_id]
        res.send({code:0,msg:'新增成功',data:null})
      }catch(e){
          console.log(e)
          next(e)
    } 
});