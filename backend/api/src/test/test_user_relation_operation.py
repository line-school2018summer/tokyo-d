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


def create_user_relation(token, uid):
    res = requests.post(_f('relations/users'), json={'id': uid}, headers={'Authorization': 'Bearer ' + token})

    return res.status_code == 200


def get_user_relation(token, uid):
    res = requests.get(_f('relations/users/' + uid), headers={'Authorization': 'Bearer ' + token})

    return res.status_code == 302


def delete_user_relation(token, uid):
    res = requests.delete(_f('relations/users/' + uid), headers={'Authorization': 'Bearer ' + token})

    return res.status_code == 200


def test_user_registration():
    user1 = {
        'sid': 'proelbtn1',
        'name': 'proelbtn1',
        'pass': 'proelbtn1',
    }
    user2 = {
        'sid': 'proelbtn2',
        'name': 'proelbtn2',
        'pass': 'proelbtn2',
    }

    assert create_user(user1)
    assert create_user(user2)

    u1 = search_user(user1['sid'])
    assert u1 is not None
    u2 = search_user(user2['sid'])
    assert u2 is not None
    
    t1 = get_token(user1)
    assert t1 is not None 
    t2 = get_token(user2)
    assert t2 is not None 

    assert create_user_relation(t1, u2['id'])
    assert create_user_relation(t2, u1['id'])

    assert get_user_relation(t1, u2['id'])
    assert get_user_relation(t2, u1['id'])

    assert delete_user_relation(t1, u2['id'])
    assert delete_user_relation(t2, u1['id'])

    assert delete_user(t1, u1['id'])
    assert delete_user(t2, u2['id'])

