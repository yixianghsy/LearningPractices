module.exports = {
    apps : [
        {
            name: 'app',
            script: './bin/www',
            instances: 1;
            autorestart: true,
            watch: true,
            ignore_watch: [                           // 不用监听的文件
                "node_modules",
                "logs"
            ],
        }
    ]
}