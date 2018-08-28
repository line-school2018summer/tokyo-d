const assert = require('assert');

const pc = require('..');
const client = require('../src/client');

describe('pusherchat', function() {
    describe('#connect()', function() {
        it('should return client', function() {
            const cli = pc.connect('http://localhost:8080');
            assert.ok(cli instanceof client);
        });
    });
});
