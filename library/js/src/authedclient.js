const axios = require('axios');

const group = require('./group');

module.exports = class {
    constructor(client, token) {
        this.client = client;
        this.url = this.client.url;
        this.token = token;
    }

    async ping() {
        return this.client.ping();
    }

    async login(params) {
        return this.client.login(params);
    }

    // =========================================================================

    async create_user(params) {
        return this.client.create_user(params);
    }

    async search_user(params) {
        return this.client.search_user(params);
    }

    async delete_user(params) {
        const res = await axios.delete(this.url + '/users/' + params['id'], {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(_ => true)
            .catch(_ => false);

        return res;
    }

    // =========================================================================

    async create_group(params) {
        const res = await axios.post(this.url + '/groups', {
                'sid': params['sid'],
                'name': params['name']
            }, {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(res => res.data)
            .catch(_ => null);

        return res ? new group(res) : null;
    }

    async search_group(params) {
        return this.client.search_group(params);
    }

    async delete_group(params) {
        const res = await axios.delete(this.url + '/groups/' + params['id'], {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(_ => true)
            .catch(_ => false);

        return res;
    }
}