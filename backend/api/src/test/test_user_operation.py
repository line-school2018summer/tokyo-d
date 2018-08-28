import requests

def _f(path):
    return 'http://localhost:8080/' + path

def create_user(params):
    assert 'sid' in params
    assert 'name' in params
    assert 'pass' in params

    res = requests.post(_f('users'), json=params)
    return res.status_code == 200


def search_user(sid):
    res = requests.get(_f('search/users/' + sid))

    return res.json() if res.status_code == 302 else None


def get_user(uid):
    res = requests.get(_f('users/' + uid))

    return res.json() if res.status_code == 200 else None


def get_token(params):
    assert 'sid' in params
    assert 'pass' in params

    res = requests.post(_f('token'), json=params)
    return res.json()['token'] if res.status_code == 200 else None


def delete_user(token, uid):
    res = requests.delete(_f('users/' + uid), headers={'Authorization': 'Bearer ' + token})

    return res.status_code == 200


def test_user_registration():
    user = {
        'sid': 'proelbtn',
        'name': 'proelbtn',
        'pass': 'proelbtn',
    }

    assert create_user(user)

    u = search_user(user['sid'])
    assert u is not None

    t = get_token(user)
    assert t is not None 

    assert delete_user(t, u['id'])

