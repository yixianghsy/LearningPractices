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
            "error_file": "./logs/app-err.log",         // 错误日志文件
            "out_file": "./logs/app-out.log",
            "log_date_format": "YYYY-MM-DD HH:mm:ss", // 给每行日志标记一个时间
            max_memory_restart: '1G',
            env: {
                
            }

        }
    ]
}