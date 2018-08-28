const assert = require('assert');

const pc = require('..');
const user = require('../src/user');
const group = require('../src/group');

describe('authedclient', function() {

    let cli, acli, useed;

    before('prepare authedclient for tests', async function() {
        cli = pc.connect('http://localhost:8080');
        useed = Date.now().toString();

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
        const seed = Date.now().toString();

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
        const seed = Date.now().toString();
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
        const seed = Date.now().toString();
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
        const seed = Date.now().toString();

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
        const seed = Date.now().toString();

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
        const seed = Date.now().toString();
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
        const seed = Date.now().toString();
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
});