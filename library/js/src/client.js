const axios = require('axios');

module.exports = class {
    constructor(url) {
        this.url = url;
    }

    async ping() {
        return axios.get(this.url + '/ping')
            .then(res => res.status == 200)
            .catch(() => false)
    }
}