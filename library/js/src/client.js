const axios = require('axios');

const user = require('./user');
const group = require('./group');
const authedclient = require('./authedclient');

module.exports = class {
    constructor(url) {
        this.url = url;
    }

    async ping() {
        return axios.get(this.url + '/ping')
            .then(res => res.status == 200)
            .catch(() => false)
    }

    async login(params) {
        const token = await axios.post(this.url + '/token', params)
            .then(res => {
                if (res.status != 200) return null;
                return res.data['token'];
            })
            .catch(_ => null);

        return token ? new authedclient(this, token) : null;
    }

    async create_user(params) {
        const res = await axios.post(this.url + '/users', params)
            .then(res => {

                if (res.status != 200) return null;
                return res.data;
            })
            .catch(_ => null);

        return res ? new user(res) : null;
    }

    async search_user(params) {
        const res = await axios.get(this.url + '/search/users/' + params['sid'])
            .then(res => {
                if (res.status != 200) return null;
                return res.data;
            })
            .catch(_ => null);

        return res ? new user(res) : null;
    }

    async search_group(params) {
        const res = await axios.get(this.url + '/search/groups/' + params['sid'])
            .then(res => res.data)
            .catch(_ => null);

        return res ? new group(res) : null;
    }
}