module.exports = class {
    constructor(params) {
        this.id = params['id'];
        this.sid = params['sid'];
        this.name = params['name'];
        this.pass = params['pass'];
        this.created_at = params['created_at'];
        this.updated_at = params['updated_at'];
    }
}