import requests

def _f(path):
    return 'http://localhost:8080/' + path

def test_create_user():
    user = {
        'sid': 'proelbtn',
        'name': 'proelbtn',
        'pass': 'proelbtn',
    }

    res1 = requests.post(_f('users'), json=user)
    assert res1.status_code == 200

    res2 = requests.get(_f('search/users/' + user['sid']))
    assert res2.status_code == 302

    res_user = res2.json()

    assert user['sid'] == res_user['sid']
    assert user['name'] == res_user['name']

    res_user_id = res_user['id']

    res3 = requests.delete(_f('users/' + res_user_id))
    assert res3.status_code == 200
