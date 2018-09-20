
const assert = require('assert');

const pc = require('..');
const user = require('../src/user');
const group = require('../src/group');
const user_relation = require('../src/user_relation');
const group_relation = require('../src/group_relation');

function mkseed() {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < 32; i++)
    text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

describe('authedclient', function() {
    let cli;
    let acli1;
    let seed1, seed2, seed3;
    let user1, user2, user3;

    before(async function() {
        cli = pc.connect('http://localhost:8080');
        seed1 = mkseed();
        seed2 = mkseed();
        seed3 = mkseed();
        gseed = mkseed();

        user1 = await cli.create_user({
            'sid': seed1, 'name': seed1, 'pass': seed1
        });

        user2 = await cli.create_user({
            'sid': seed2, 'name': seed2, 'pass': seed2
        });

        user3 = await cli.create_user({
            'sid': seed3, 'name': seed3, 'pass': seed3
        });

        acli1 = await cli.login({
            'sid': seed1, 'pass': seed1
        });
    });

    describe('#get_user_relations()', function() {
        it('should return true if message was sent', async function() {
            const res = await acli1.get_user_relations();
            assert.ok(res.length === 0);
        });

        it('should return true if message was sent', async function() {
            await acli1.create_user_relation(user1, user2);
            const res = await acli1.get_user_relations();
            assert.ok(res.length === 1);
        });

        it('should return true if message was sent', async function() {
            await acli1.create_user_relation(user1, user3);
            const res = await acli1.get_user_relations();
            assert.ok(res.length === 2);
        });
    });

});
