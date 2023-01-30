const crypto = require('crypto')
const multer = require('multer')
const fs = require('fs')
const path = require('path')
function md5(s){
    //注意参数需要为ｓｔｒｉｎg类型，否则会报
    return crypto.createHash('md5')
.update(String(s)).digest('hex');
}

let  upload = multer({
    storage: multer.diskStorage({
        //设置文件存储路径
        destination: function(req,file,cb){
            let date = new Date()
            let year = date.getFullYear()
            let month = (date.getMonth() + 1).toString().padStart(2, '0')
            let day = date.getDate()
            let dir = path.join(__dirname, '../public/uploads/' + year + month + day)

            //判断目录是否存在，没有则创建
            if (!fs.existsSync(dir)) {
                fs.mkdirSync(dir,{recursive: true})
            }
            //ｄｉｒ就是上传文件存放目录
            cb(null,dir)
        },
        //设置文件名称
        filename: function(req,file,cb){
            let filename = Date.now() + path.extname(file.originalname)
            //ｆｉｌｅName就是上传的文件名
            cb(null,filename)
        }

    })
})
module.exports = {
    md5,
    upload
}