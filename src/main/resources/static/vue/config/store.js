var store;
store = new Vuex.Store({
    "modules": {
        /* app */
        "app": {
            "namespaced": true,
            "state": function () {
                return {
                    "token": null,
                    "user": null,
                    "person": null,
                    "roleList": null,
                    "menuList": null,
                    "treeMenuList": null,
                    "alert": {
                        "value": false,
                        "message": "",
                        "callback": function () {}
                    },
                    "confirm": {
                        "value": false,
                        "message": "",
                        "oktext" :  '확인',
                        "canceltext" : '취소',
                        "callback": function () {}
                    },
                    "showMenuPathName" : "dd",

                    "loading" : {
                    	"zIndex" : 9999,
                    	"overlay" : false
                    },
                };
            },
            "getters": {
            },
            "mutations": {
                "SET_TOKEN": function (state, payload) {
                    state.token = payload;
                },
                "SET_USER": function (state, payload) {
                    state.user = payload;
                },
                "SET_PERSON": function (state, payload) {
                    state.person = payload;
                },
                "SET_ROLE_LIST": function (state, payload) {
                    state.roleList = payload;
                },
                "SET_MENU_LIST": function (state, payload) {
                    state.menuList = payload;
                },
                "SET_TREE_MENU_LIST": function (state, payload) {
                    state.treeMenuList = payload;
                },
                "SET_ALERT": function (state, payload) {
                    state.alert = payload;
                },
                "SET_CONFIRM": function (state, payload) {
                    state.confirm = payload;
                },
                "SET_LOADING": function (state, payload) {
                    state.loading.overlay = payload;
                }
            },
            "actions": {
            }
        }
    }
});