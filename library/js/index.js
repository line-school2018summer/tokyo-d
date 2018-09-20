const client = require('./src/client');

module.exports = {
    'connect': url => new client(url)
}