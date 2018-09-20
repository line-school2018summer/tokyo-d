module.exports = class {
    constructor(params) {
        this.id = params['id'];
        this.sid = params['sid'];
        this.name = params['name'];
        this.owner = params['owner'];
        this.created_at = params['created_at'];
        this.updated_at = params['updated_at'];
    }
}