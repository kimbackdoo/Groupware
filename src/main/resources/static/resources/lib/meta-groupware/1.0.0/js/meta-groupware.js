var metaGroupware;
metaGroupware = {
    "version": "1.0.0",
    "init": function () {
        axios.defaults.paramsSerializer = function (params) {
            return Qs.stringify(params, {"indices": false});
        }
    },
    "alert": function (message) {
        return new Promise(function (resolve, reject) {
            store.commit("app/SET_ALERT", {
                "value": true,
                "message": message,
                "callback": function () {
                    resolve();
                }
            });
        });
    },
    "confirm": function (message, oktext = '예', canceltext = '아니오') {
        return new Promise(function (resolve, reject) {
            store.commit("app/SET_CONFIRM", {
                "value": true,
                "message": message,
                "oktext" : oktext,
                "canceltext" : canceltext,
                "callback": function (result) {
                    resolve(result);
                }
            });
        });
    },
    "auth": {
        "login": async function (username, password) {
            var authorization,
                token;
            token = Basil.localStorage.get("token");
            if (token && await metaGroupware.auth.authenticated(token)) {
                authorization = "Bearer " + token;
            } else {
                authorization = (await axios({
                    "url": "/api/login",
                    "method": "post",
                    "data": {
                        "username": username,
                        "password": password
                    }
                })).headers.authorization;
                token = authorization.replace("Bearer ", "");
                Basil.localStorage.set("token", token);
            }
            axios.defaults.headers.Authorization = authorization;
        },
        "logout": async function (token) {
            await axios({
                "url": "/api/logout",
                "method": "post",
                "headers": {
                    "Authorization": "Bearer " + token
                }
            });
            Basil.localStorage.remove("token");
            delete axios.defaults.headers.Authorization;
        },
        "signUp": async function (data) {
             return await axios({
                "url": "/api/app/accounts/sign-up",
                "method": "post",
                "data": data,
            });
        },
        "idExists": async function (data) {
            return await axios({
                "url": "/api/common/users/id-exists",
                "method": "post",
                "data": data,
            });
        },
        "authenticated": async function (token) {
            if (token) {
                try {
                    await axios({
                        "url": "/api",
                        "method": "get",
                        "headers": {
                            "Authorization": token
                        }
                    });
                    return true;
                } catch (e) {
                    return false;
                }
            } else {
                return false;
            }
        },
        "authorize": async function (token) {
            var authentication, user;
            authentication = jwt_decode(token);
            store.commit("app/SET_TOKEN", token);
            store.commit("app/SET_USER", authentication.user);
            store.commit("app/SET_PERSON", authentication.person);
            store.commit("app/SET_ROLE_LIST", authentication.roleList);
            store.commit("app/SET_MENU_LIST", authentication.menuList);
            store.commit("app/SET_TREE_MENU_LIST", authentication.treeMenuList);
            axios.defaults.headers.Authorization = "Bearer " + token;
        },
        "unauthorize": function () {
            store.commit("app/SET_TOKEN", null);
            store.commit("app/SET_USER", null);
            store.commit("app/SET_PERSON", null);
            store.commit("app/SET_ROLE_LIST", null);
            store.commit("app/SET_MENU_LIST", null);
            store.commit("app/SET_TREE_MENU_LIST", null);
            delete axios.defaults.headers.Authorization;
            Basil.localStorage.remove("token");
        },
        "getToken": function () {
            return Basil.localStorage.get("token");
        }
    },
    "api": {
        "common": {
            "api": {
                "getApiList": function (params) { return axios({"url": "/api/common/apis", "method": "get", "params": params}); },
                "getApi": function (id) { return axios({"url": "/api/common/apis/" + id, "method": "get"}); },
                "createApiList": function (data) { return axios({"url": "/api/common/apis?bulk", "method": "post", "data": data}); },
                "createApi": function (data) { return axios({"url": "/api/common/apis", "method": "post", "data": data}); },
                "modifyApiList": function (data) { return axios({"url": "/api/common/apis", "method": "put", "data": data}); },
                "modifyApi": function (id, data) { return axios({"url": "/api/common/apis/" + id, "method": "put", "data": data}); },
                "removeApiList": function (data) { return axios({"url": "/api/common/apis", "method": "delete", "data": data}); },
                "removeApi": function (id) { return axios({"url": "/api/common/apis/"+ id, "method": "delete"}); }
            },
            "menu": {
                "getMenuList": function (params) { return axios({"url": "/api/common/menus", "method": "get", "params": params}); },
                "getMenu": function (id) { return axios({"url": "/api/common/menus/" + id, "method": "get"}); },
                "createMenuList": function (data) { return axios({"url": "/api/common/menus?bulk", "method": "post", "data": data}); },
                "createMenu": function (data) { return axios({"url": "/api/common/menus", "method": "post", "data": data}); },
                "modifyMenuList": function (data) { return axios({"url": "/api/common/menus", "method": "put", "data": data}); },
                "modifyMenu": function (id, data) { return axios({"url": "/api/common/menus/" + id, "method": "put", "data": data}); },
                "removeMenuList": function (data) { return axios({"url": "/api/common/menus", "method": "delete", "data": data}); },
                "removeMenu": function (id) { return axios({"url": "/api/common/menus/"+ id, "method": "delete"}); }
            },
            "person": {
                "getPersonList": function (params) { return axios({"url": "/api/common/people", "method": "get", "params": params}); },
                "getPerson": function (id) { return axios({"url": "/api/common/people/" + id, "method": "get"}); },
                "createPersonList": function (data) { return axios({"url": "/api/common/people?bulk", "method": "post", "data": data}); },
                "createPerson": function (data) { return axios({"url": "/api/common/people", "method": "post", "data": data}); },
                "modifyPersonList": function (data) { return axios({"url": "/api/common/people", "method": "put", "data": data}); },
                "modifyPerson": function (id, data) { return axios({"url": "/api/common/people/" + id, "method": "put", "data": data}); },
                "removePersonList": function (data) { return axios({"url": "/api/common/people", "method": "delete", "data": data}); },
                "removePerson": function (id) { return axios({"url": "/api/common/people/"+ id, "method": "delete"}); }
            },
            "role": {
                "getRoleList": function (params) { return axios({"url": "/api/common/roles", "method": "get", "params": params}); },
                "getRole": function (id) { return axios({"url": "/api/common/roles/" + id, "method": "get"}); },
                "createRoleList": function (data) { return axios({"url": "/api/common/roles?bulk", "method": "post", "data": data}); },
                "createRole": function (data) { return axios({"url": "/api/common/roles", "method": "post", "data": data}); },
                "modifyRoleList": function (data) { return axios({"url": "/api/common/roles", "method": "put", "data": data}); },
                "modifyRole": function (id, data) { return axios({"url": "/api/common/roles/" + id, "method": "put", "data": data}); },
                "removeRoleList": function (data) { return axios({"url": "/api/common/roles", "method": "delete", "data": data}); },
                "removeRoleAllDependencyList": function (data) { return axios({"url": "/api/common/roles/delete-all-dependency", "method": "delete", "data": data}); },
                "removeRole": function (id) { return axios({"url": "/api/common/roles/"+ id, "method": "delete"}); }
            },
            "roleApi": {
                "getRoleApiList": function (params) { return axios({"url": "/api/common/role-apis", "method": "get", "params": params}); },
                "getRoleApi": function (roleId, apiId) { return axios({"url": "/api/common/role-apis/" + roleId + "," + apiId, "method": "get"}); },
                "createRoleApiList": function (data) { return axios({"url": "/api/common/role-apis?bulk", "method": "post", "data": data}); },
                "createRoleApi": function (data) { return axios({"url": "/api/common/role-apis", "method": "post", "data": data}); },
                "modifyRoleApiList": function (data) { return axios({"url": "/api/common/role-apis", "method": "put", "data": data}); },
                "modifyRoleApi": function (roleId, apiId, data) { return axios({"url": "/api/common/role-apis/" + roleId + "," + apiId, "method": "put", "data": data}); },
                "removeRoleApiList": function (data) { return axios({"url": "/api/common/role-apis", "method": "delete", "data": data}); },
                "removeRoleApi": function (roleId, apiId) { return axios({"url": "/api/common/role-apis/"+ roleId + "," + apiId, "method": "delete"}); }
            },
            "roleMenu": {
                "getRoleMenuList": function (params) { return axios({"url": "/api/common/role-menus", "method": "get", "params": params}); },
                "getRoleMenu": function (roleId, menuId) { return axios({"url": "/api/common/role-menus/" + roleId + "," + menuId, "method": "get"}); },
                "createRoleMenuList": function (data) { return axios({"url": "/api/common/role-menus?bulk", "method": "post", "data": data}); },
                "createRoleMenu": function (data) { return axios({"url": "/api/common/role-menus", "method": "post", "data": data}); },
                "modifyRoleMenuList": function (data) { return axios({"url": "/api/common/role-menus", "method": "put", "data": data}); },
                "modifyRoleMenu": function (roleId, menuId, data) { return axios({"url": "/api/common/role-menus/" + roleId + "," + menuId, "method": "put", "data": data}); },
                "removeRoleMenuList": function (data) { return axios({"url": "/api/common/role-menus", "method": "delete", "data": data}); },
                "removeRoleMenu": function (roleId, menuId) { return axios({"url": "/api/common/role-menus/"+ roleId + "," + menuId, "method": "delete"}); }
            },
            "roleUser": {
                "getRoleUserList": function (params) { return axios({"url": "/api/common/role-users", "method": "get", "params": params}); },
                "getRoleUser": function (userId) { return axios({"url": "/api/common/role-users/" + roleId + "," + userId, "method": "get"}); },
                "createRoleUserList": function (data) { return axios({"url": "/api/common/role-users?bulk", "method": "post", "data": data}); },
                "createRoleUser": function (data) { return axios({"url": "/api/common/role-users", "method": "post", "data": data}); },
                "modifyRoleUserList": function (data) { return axios({"url": "/api/common/role-users", "method": "put", "data": data}); },
                "modifyRoleUser": function (roleId, userId, data) { return axios({"url": "/api/common/role-users/" + roleId + "," + userId, "method": "put", "data": data}); },
                "removeRoleUserList": function (data) { return axios({"url": "/api/common/role-users", "method": "delete", "data": data}); },
                "removeRoleUser": function (roleId, userId) { return axios({"url": "/api/common/role-users/"+ roleId + "," + userId, "method": "delete"}); },
                "updateRoleUser": function(data) {return axios({"url": "/api/common/role-users"+"/updateRoleUser", "method": "put"});  }
},
            "user": {
                "getUserList": function (params) { return axios({"url": "/api/common/users", "method": "get", "params": params}); },
                "getUser": function (id) { return axios({"url": "/api/common/users/" + id, "method": "get"}); },
                "createUserList": function (data) { return axios({"url": "/api/common/users?bulk", "method": "post", "data": data}); },
                "createUser": function (data) { return axios({"url": "/api/common/users", "method": "post", "data": data}); },
                "modifyUserList": function (data) { return axios({"url": "/api/common/users", "method": "put", "data": data}); },
                "modifyUser": function (id, data) { return axios({"url": "/api/common/users/" + id, "method": "put", "data": data}); },
                "removeUserList": function (data) { return axios({"url": "/api/common/users", "method": "delete", "data": data}); },
                "removeUser": function (id) { return axios({"url": "/api/common/users/"+ id, "method": "delete"}); },
                "modifyPassword": function (id, data) { return axios({"url": "/api/common/users/" + id + "/password", "method": "put", "data": data}); }
            },
            "userPerson": {
                "getUserPersonList": function (params) { return axios({"url": "/api/common/user-people", "method": "get", "params": params}); },
                "getUserPerson": function (userId) { return axios({"url": "/api/common/user-people/" + userId, "method": "get"}); },
                "createUserPersonList": function (data) { return axios({"url": "/api/common/user-people?bulk", "method": "post", "data": data}); },
                "createUserPerson": function (data) { return axios({"url": "/api/common/user-people", "method": "post", "data": data}); },
                "modifyUserPersonList": function (data) { return axios({"url": "/api/common/user-people", "method": "put", "data": data}); },
                "modifyUserPerson": function (userId, data) { return axios({"url": "/api/common/user-people/" + userId, "method": "put", "data": data}); },
                "removeUserPersonList": function (data) { return axios({"url": "/api/common/user-people", "method": "delete", "data": data}); },
                "removeUserPerson": function (userId) { return axios({"url": "/api/common/user-people/"+ userId, "method": "delete"}); }
            },
            "profile": {
                "getProfileList": function (params) { return axios({"url": "/api/common/profile", "method": "get", "params": params}); },
                "getProfile": function (id) { return axios({"url": "/api/common/profile/" + id, "method": "get"}); },
                "createProfileList": function (data) { return axios({"url": "/api/common/profile?bulk", "method": "post", "data": data}); },
                "createProfile": function (data) { return axios({"url": "/api/common/profile", "method": "post", "data": data}); },
                "modifyProfileList": function (data) { return axios({"url": "/api/common/profile", "method": "put", "data": data}); },
                "modifyProfile": function (id, data) { return axios({"url": "/api/common/profile/" + id, "method": "put", "data": data}); },
                "removeProfileList": function (data) { return axios({"url": "/api/common/profile", "method": "delete", "data": data}); },
                "removeProfile": function (id) { return axios({"url": "/api/common/profile/"+ id, "method": "delete"}); }
            },
            "banIP": {
                "getBanIPList": function (params) { return axios({"url": "/api/common/banIPs", "method": "get", "params": params}); },
                "getBanIP": function (id) { return axios({"url": "/api/common/banIPs/" + id, "method": "get"}); },
                "createBanIPList": function (data) { return axios({"url": "/api/common/banIPs?bulk", "method": "post", "data": data}); },
                "createBanIP": function (data) { return axios({"url": "/api/common/banIPs", "method": "post", "data": data}); },
                "modifyBanIPList": function (data) { return axios({"url": "/api/common/banIPs", "method": "put", "data": data}); },
                "modifyBanIP": function (id, data) { return axios({"url": "/api/common/banIPs/" + id, "method": "put", "data": data}); },
                "removeBanIPList": function (data) { return axios({"url": "/api/common/banIPs", "method": "delete", "data": data}); },
                "removeBanIP": function (id) { return axios({"url": "/api/common/banIPs/"+ id, "method": "delete"}); }
            },
        },
        "app": {
            "account": {
                "createAccount": function (data) { return axios({"url": "/api/app/accounts", "method": "post", "data": data}); },
                "modifyAccount": function (data) { return axios({"url": "/api/app/accounts", "method": "put", "data": data}); },
                "removeAccount": function (data) { return axios({"url": "/api/app/accounts", "method": "delete", "data": data}); },
            },
            "RoleAndRoleMenuAndRoleApi": {
                "createRoleAndRoleMenuAndRoleApi": function (data) { return axios({"url": "/api/app/role-menu-apis", "method": "post", "data": data}); },
                "modifyRoleAndRoleMenuAndRoleApi": function (data) { return axios({"url": "/api/app/role-menu-apis", "method": "put", "data": data}); },
            }
        },
        "util": {
            "menu": {
                "getTreeMenuList": function (params) { return axios({"url": "/api/util/menus/tree-menus", "method": "get", "params": params}); }
            }
        }
    },
    "util": {
        "sort": function (sortBy, sortDesc) {
            let str;
            str = [];
            if(sortBy.length > 0 && sortDesc.length > 0) {
                for (var i = 0;  i < sortBy.length; i++) {
                    str.push(sortBy[i] + "," + (sortDesc[i] ? "desc" : "asc"));
                }
            }
            return str;
        }
    }
};
metaGroupware.init();