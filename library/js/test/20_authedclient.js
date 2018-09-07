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
    let cli, acli, useed;

    before('prepare authedclient for tests', async function() {
        cli = pc.connect('http://localhost:8080');
        useed = mkseed();

        await cli.create_user({
            'sid': useed, 'name': useed, 'pass': useed
        });

        acli = await cli.login({
            'sid': useed, 'pass': useed
        });
    })

    describe('#ping()', function() {
        it('should return true if server is valid', async function() {
            const res = acli.ping();
            assert.ok(res);
        });
    });

    describe('#login()', function() {
        it('was tested in client test', function() {
            assert.ok(true);
        });
    });

    // =========================================================================

    describe('#create_user()', function() {
        it('was tested in client test', function() {
            assert.ok(true);
        });
    });

    describe('#search_user()', function() {
        const seed = mkseed();

        before(async function() {
            await acli.create_user({
                'sid': seed, 'name': seed, 'pass': seed
            });
        });

        it('should return user if user exists', async function() {
            const res = await acli.search_user({ 'sid': seed });

            assert.ok(res instanceof user);
        });

        it('should return null if user doesn\'t exist', async function() {
            const res = await acli.search_user({ 'sid': 'deadbeef' });

            assert.ok(res === null);
        });
    });

    describe('#get_user()', function() {
        const seed = mkseed();
        let id;
        
        before(async function() {
            await acli.create_user({
                'sid': seed, 'name': seed, 'pass': seed
            });

            const res = await acli.search_user({ 'sid': seed })
            id = res.id;
        });

        it('should return user if user exists', async function() {
            const res = await acli.get_user({ 'id': id });

            assert.ok(res instanceof user);
        });

        it('should return null if user doesn\'t exist', async function() {
            const res = await acli.get_user({ 'id': 'deadbeef' });

            assert.ok(res === null);
        });
    });

    describe('#delete_user()', function() {
        const seed = mkseed();
        let id, tacli;

        before(async function() {
            await acli.create_user({
                'sid': seed, 'name': seed, 'pass': seed
            });

            tacli = await acli.login({
                'sid': seed, 'pass': seed
            });

            const res = await acli.search_user({ 'sid': seed });
            id = res.id;
        });

        it('should return true if user exists', async function() {
            const res = await tacli.delete_user({ 'id': id });

            assert.ok(res === true);
        });

        it('should return false if user doesn\'t exist', async function() {
            const res = await tacli.delete_user({ 'id': '2f8ce12c-3326-416b-b18e-448023575ed3' });

            assert.ok(res === false);
        });
    });

    // =========================================================================

    describe('#create_group()', function() {
        const seed = mkseed();

        it('should return group if group doesn\'t exist', async function() {
            const res = await acli.create_group({
                'sid': seed,
                'name': seed
            });

            assert.ok(res instanceof group);
        });

        it('should return group if group doesn\'t exist', async function() {
            const res = await acli.create_group({
                'sid': seed,
                'name': seed
            });

            assert.ok(res === null);
        });
    });

    describe('#search_group()', function() {
        const seed = mkseed();

        before(async function() {
            await acli.create_group({
                'sid': seed,
                'name': seed
            });
        })

        it('should return group if group doesn\'t exist', async function() {
            const res = await acli.search_group({
                'sid': seed
            });

            assert.ok(res instanceof group);
        });

        it('should return group if group doesn\'t exist', async function() {
            const res = await acli.search_group({
                'sid': 'deadbeef'
            });

            assert.ok(res === null);
        });
    });

    describe('#get_group()', function() {
        const seed = mkseed();
        let id;
        
        before(async function() {
            await acli.create_group({
                'sid': seed, 'name': seed, 'pass': seed
            });

            const res = await acli.search_group({ 'sid': seed })
            id = res.id;
        });

        it('should return group if group exists', async function() {
            const res = await acli.get_group({ 'id': id });

            assert.ok(res instanceof group);
        });

        it('should return null if group doesn\'t exist', async function() {
            const res = await acli.get_group({ 'id': 'deadbeef' });

            assert.ok(res === null);
        });
    })

    describe('#delete_group()', function() {
        const seed = mkseed();
        let id;

        before(async function() {
            await acli.create_group({
                'sid': seed,
                'name': seed
            });

            const res = await acli.search_group({ 'sid': seed });
            id = res.id;
        })

        it('should return group if group doesn\'t exist', async function() {
            const res = await acli.delete_group({ 'id': id });

            assert.ok(res === true);
        });

        it('should return group if group doesn\'t exist', async function() {
            const res = await acli.delete_group({ 'id': id });

            assert.ok(res === false);
        });
    });

    // =========================================================================

    describe('#create_user_relation()', function() {
        const seed = mkseed();
        let user1, user2;

        before(async function() {
            user1 = await acli.search_user({
                'sid': useed
            });

            user2 = await acli.create_user({
                'sid': seed,
                'name': seed,
                'pass': seed
            });
        });

        it('should return user_relation if relation doesn\'t exist', async function() {
            const res = await acli.create_user_relation(user1, user2);

            assert.ok(res instanceof user_relation);
        });

        it('should return null if relation exists', async function() {
            const res = await acli.create_user_relation(user1, user2);

            assert.ok(res === null);
        });

        it('should return null if token is invalid', async function() {
            const res = await acli.create_user_relation(user2, user1);

            assert.ok(res === null);
        });
    });

    describe('#get_user_relation()', function() {
        const seed = mkseed();
        let user1, user2;

        before(async function() {
            user1 = await acli.search_user({
                'sid': useed
            });

            user2 = await acli.create_user({
                'sid': seed,
                'name': seed,
                'pass': seed
            });

            await acli.create_user_relation(user1, user2);
        });

        it('should return user_relation if relation exists', async function() {
            const res = await acli.get_user_relation(user1, user2);

            assert.ok(res instanceof user_relation);
        });

        it('should return null if relation doesn\'t exist', async function() {
            const res = await acli.get_user_relation(user2, user1);

            assert.ok(res === null);
        });
    });

    describe('#delete_user_relation()', function() {
        const seed = mkseed();
        let user1, user2;

        before(async function() {
            user1 = await acli.search_user({
                'sid': useed
            });

            user2 = await acli.create_user({
                'sid': seed,
                'name': seed,
                'pass': seed
            });

            await acli.create_user_relation(user1, user2);
        });

        it('should return true if relation exists', async function() {
            const res = await acli.delete_user_relation(user1, user2);

            assert.ok(res === true);
        });

        it('should return false if relation doesn\'t exist', async function() {
            const res = await acli.delete_user_relation(user1, user2);

            assert.ok(res === false);
        });
    });

    // =========================================================================

    describe('#create_group_relation()', function() {
        const seed = mkseed();
        let user, group;

        before(async function() {
            user = await acli.search_user({
                'sid': useed
            });

            group = await acli.create_group({
                'sid': seed,
                'name': seed
            });
        });

        it('should return group_relation if relation doesn\'t exist', async function() {
            const res = await acli.create_group_relation(user, group);

            assert.ok(res instanceof group_relation);
        });

        it('should return null if relation exists', async function() {
            const res = await acli.create_group_relation(user, group);

            assert.ok(res === null);
        });
    });

    describe('#get_group_relation()', function() {
        const seed1 = mkseed(), seed2 = mkseed();
        let user, group1, group2;

        before(async function() {
            user = await acli.search_user({
                'sid': useed
            });

            group1 = await acli.create_group({
                'sid': seed1,
                'name': seed1
            });

            group2 = await acli.create_group({
                'sid': seed2,
                'name': seed2
            });

            await acli.create_group_relation(user, group1);
        });

        it('should return group_relation if relation exists', async function() {
            const res = await acli.get_group_relation(user, group1);

            assert.ok(res instanceof group_relation);
        });

        it('should return null if relation doesn\'t exist', async function() {
            const res = await acli.get_group_relation(user, group2);

            assert.ok(res === null);
        });
    });

    describe('#delete_user_relation()', function() {
        const seed = mkseed();
        let user, group;

        before(async function() {
            user = await acli.search_user({
                'sid': useed
            });

            group = await acli.create_group({
                'sid': seed,
                'name': seed
            });

            await acli.create_group_relation(user, group);
        });

        it('should return true if relation exists', async function() {
            const res = await acli.delete_group_relation(user, group);

            assert.ok(res === true);
        });

        it('should return false if relation doesn\'t exist', async function() {
            const res = await acli.delete_group_relation(user, group);

            assert.ok(res === false);
        });
    });
});