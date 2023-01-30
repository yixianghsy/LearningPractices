module.exports = {
    apps : [
        {
            name: 'app',
            script: './bin/www',
            instances: 1;
            autorestart: true,
            watch: true,
        }
    ]
}