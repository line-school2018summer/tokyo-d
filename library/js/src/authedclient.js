const axios = require('axios');

const user = require('./user');
const group = require('./group');
const user_relation = require('./user_relation');
const group_relation = require('./group_relation');

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

    async get_user(params) {
        const res = await axios.get(this.url + '/users/' + params['id'])
            .then(res => res.data)
            .catch(_ => null);

        return res ? new user(res) : null;
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

    async get_group(params) {
        const res = await axios.get(this.url + '/groups/' + params['id'])
            .then(res => res.data)
            .catch(_ => null);

        return res ? new group(res) : null;
    }

    async delete_group(params) {
        const res = await axios.delete(this.url + '/groups/' + params['id'], {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(_ => true)
            .catch(_ => false);

        return res;
    }

    // =========================================================================

    async create_user_relation(from, to) {
        const res = await axios.post(this.url + '/relations/users', {
                'from': from['id'],
                'to': to['id']
            }, {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(res => res.data)
            .catch(_ => null);

        return res ? new user_relation(res) : null;
    }

    async get_user_relation(from, to) {
        const res = await axios.get(this.url + '/relations/users/' + to['id'], {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(res => res.data)
            .catch(_ => null);

        return res ? new user_relation(res) : null;
    }

    async delete_user_relation(from, to) {
        const res = await axios.delete(this.url + '/relations/users/' + to['id'], {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(_ => true)
            .catch(_ => false);

        return res;
    }

    // =========================================================================

    async create_group_relation(from, to) {
        const res = await axios.post(this.url + '/relations/groups', {
                'from': from['id'],
                'to': to['id']
            }, {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(res => res.data)
            .catch(_ => null);

        return res ? new group_relation(res) : null;
    }

    async get_group_relation(from, to) {
        const res = await axios.get(this.url + '/relations/groups/' + to['id'], {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(res => res.data)
            .catch(_ => null);

        return res ? new group_relation(res) : null;
    }

    async delete_group_relation(from, to) {
        const res = await axios.delete(this.url + '/relations/groups/' + to['id'], {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(_ => true)
            .catch(_ => false);

        return res;
    }

    // =========================================================================

    async send_message_to_user(msg, from, to) {
        const res = await axios.post(this.url + '/messages/users', {
                'from': from['id'],
                'to': to['id'],
                'content': msg
            }, {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(_ => true)
            .catch(_ => false)

        return res;
    }

    async send_message_to_group(msg, from, to) {
        const res = await axios.post(this.url + '/messages/groups', {
                'from': from['id'],
                'to': to['id'],
                'content': msg
            }, {
                'headers': { 'Authorization': 'Bearer ' + this.token }
            })
            .then(_ => true)
            .catch(_ => false)

        return res;
    }
}