var SettingMenuMainPage;
SettingMenuMainPage = Vue.component("setting-menu-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/menu/main.html")).data,
    "data": function () {
        return {
            "data": {
                "treeMenuList": [],
                "menu": {"show": null}
            },
            "treeview": {
                "menuList": {
                    "items": [],
                    "open": [],
                    "active": []
                }
            },
            "autocomplete": {
                "parentId": {
                    "items": []
                }
            },
            "btn": {
                "saveMenu": {
                    "loading": false
                },
                "removeMenu": {
                    "loading": false
                }
            }
        };
    },
    "watch": {
        "data.treeMenuList": {
            "handler": function (newValue, oldValue) {
                this.treeview.menuList.items = this.flatArrayToNestedArray(newValue, "id", "parentId");
                this.treeview.menuList.open = _.cloneDeep(newValue);
                this.autocomplete.parentId.items = _.cloneDeep(newValue);

            }
        },
        "treeview.menuList.active": {
            "handler": function (newValue, oldValue) {
                this.data.menu = newValue[0] ? _.cloneDeep(newValue[0]) : {"show": null};
            }
        }
    },
    "methods": {
        "loadTreeMenuList": async function () {
            var treeMenuList;
            treeMenuList = (await metaGroupware.api.util.menu.getTreeMenuList({
                "page": 1,
                "rowSize": 100000000,
                "sort": ["rankingPath,asc"]
            })).data.items;
            this.data.treeMenuList = treeMenuList;
        },
        "saveMenu": async function () {
            var menu;
            menu = this.data.menu;
            this.btn.saveMenu.loading = true;
            if (await metaGroupware.confirm("저장 하시겠습니까?")) {
                if (menu.id) {
                    menu = (await metaGroupware.api.common.menu.modifyMenu(menu.id, menu)).data;
                } else {
                    menu = (await metaGroupware.api.common.menu.createMenu(menu)).data;
                }
                await metaGroupware.alert("저장 되었습니다.");
                await this.loadTreeMenuList();
                this.treeview.menuList.active = [menu];
            }
            this.btn.saveMenu.loading = false;
        },
        "removeMenu": async function () {
            var menu;
            menu = this.data.menu;
            this.btn.removeMenu.loading = true;
            if (await metaGroupware.confirm("삭제 하시겠습니까?")) {
                await metaGroupware.api.common.menu.removeMenu(menu.id);
                await metaGroupware.alert("삭제 되었습니다.");
                await this.loadTreeMenuList();
                this.treeview.menuList.active = [];
            }
            this.btn.removeMenu.loading = false;
        },
        "flatArrayToNestedArray": function (flatArray, id, parentId, children) {
            var nestedArray,
                map,
                flat,
                i;
            flatArray = _.cloneDeep(flatArray);
            children = children ? children : "children";
            nestedArray = [];
            map = {};
            for (i = 0; i < flatArray.length; i++) {
                map[flatArray[i][id]] = i;
                flatArray[i][children] = [];
            }
            for (i = 0; i < flatArray.length; i++) {
                flat = flatArray[i];
                if (flat[parentId]) {
                    flatArray[map[flat[parentId]]][children].push(flat);
                } else {
                    nestedArray.push(flat);
                }
            }
            return nestedArray;
        }
    },
    "mounted": async function () {
        Promise.all([
            this.loadTreeMenuList()
        ]);
    }
}); });