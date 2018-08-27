const assert = require('assert');

const pc = require('..');

describe('client', function() {
    const clia = pc.connect('http://localhost:8080');
    const clib = pc.connect('http://localhost:8081');

    describe('#ping()', function() {
        it('should return true if server is valid', async function() {
            assert.ok(await clia.ping());
        });
        it('should return false if server is invalid', async function() {
            assert.ok(!(await clib.ping()));
        });
    });
});
