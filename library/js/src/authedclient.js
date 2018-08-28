module.exports = class {
    constructor(client, token) {
        this.client = client;
        this.url = this.client.url;
        this.token = token;
    }

    async delete_user(params) {
        const res = await axios.delete(this.url + '/users/' + params['id'])
            .then(res => res.status === 200)
            .catch(_ => false);

        return res;
    }

    async delete_group(params) {
        const res = await axios.delete(this.url + '/groups/' + params['id'])
            .then(res => res.status === 200)
            .catch(_ => false);

        return res;
    }
}