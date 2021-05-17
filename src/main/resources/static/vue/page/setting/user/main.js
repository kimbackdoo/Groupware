var SettingUserMainPage;
SettingUserMainPage = Vue.component("setting-user-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/user/main.html")).data,
    "data": function () {
        return {
            "data": {
                "userList": [],
                "person":{},
                "user": {},
                "role": {
                    "id": null
                }
            },
			"role" : {
                    "roleSelected" : [],
                    "dataTable" : {
                        "headers": [
                            {"text": "역할 명", "sortable": false, "value": "name"},
                            {"text": "역할 설명", "sortable": false, "value": "description"},
                        ],
                        "items": [],
                    }
            },
            "text": {
                "icon": "mdi-eye-off-outline",
                "type": "password"
            },
            "select": {
                "roleList": []
            },
            "btn": {
                "saveAccount": {
                    "loading": false
                }
            },
            "statusNameList": {
                "T": "사용",
                "F": "회원가입",
                "D": "삭제"
            },
            "rules": {
                "required": value => !!value || '필수',
                "password": (value) => {
                    let message, flag;
                    flag = false;
                    if(value == null || value.length < 8) {
                        message = '비밀번호 8자 이상';
                    } else if (!/(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/g.test(value)) {
                        message = '영문자 숫자 특수문자 조합';
                    } else if (/(\w)\1\1/.test(value)){
                        message = '동일한 3자리 문자';
                    } else if (!this.stck(value, 3)) {
                        message = '연속된 3자리 문자';
                    } else if (value.indexOf(moment(this.data.person.birthDate).format("MMDD")) !== -1) {
                        message = '생년월일 포함';
                    } else if (value.indexOf(this.data.user.username) !== -1) {
                        message = '아이디 포함';
                    } else {
                        flag = true;
                    }
                    return flag || message;
                },
                "email": value => /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/.test(value) || '형식이 맞지 않습니다.'
            }
        };
    },
    "methods": {
        "loadUserList": async function () {
            var userPersonList;
            userPersonList = (await metaojt.api.common.userPerson.getUserPersonList({
                "page": 1,
                "rowSize": 100000000
            })).data.items;

            this.data.userList = userPersonList;
        },
        "loadRoleList": async function () {
            var roleList;
            roleList = (await metaojt.api.common.role.getRoleList({
                "page": 1,
                "rowSize": 100000000
            })).data.items;
            this.select.roleList = [{"id": null, "name": "선택"}, ...roleList];
			
			this.role.dataTable.items = roleList;
        },
        "clickUser": async function (i) {
            let roleList;
            this.data.person = this.data.userList[i].person;
            this.data.user = this.data.userList[i].user;
            roleList = (await metaojt.api.common.roleUser.getRoleUserList({"userId": this.data.user.id})).data.items;
			this.role.roleSelected = [];
			for(var i=0; i<roleList.length; i++)
				this.role.roleSelected.push(roleList[i].role);
        },
        "existUsername": async function (username) {
            return (await metaojt.api.common.user.getUserList({"username": username, "rowSize": 1})).data.items.length > 0 && !this.data.user.id ? true : false;
        },
		"saveAccount": async function () {
            let user, person, selectedRoleList, account, validate;
            user = this.data.user;
            person = this.data.person;
			selectedRoleList = this.role.roleSelected.map(e=>e.id);
			
            this.btn.saveAccount.loading = true;
            validate = this.$refs.form.validate();
            if (!validate) {
                await metaojt.alert("유효한 값을 작성해주세요.");
            } else if (await this.existUsername(user.username)) {
                await metaojt.alert("동일한 아이디가 존재합니다.");
            } else if (await metaojt.confirm("저장 하시겠습니까?")) {
                if (user.id && person.id) {
                    await metaojt.api.app.account.modifyAccount({"userDto": user, "personDto": person, "roleId": selectedRoleList});
                } else {
                    account = (await metaojt.api.app.account.createAccount({"userDto": user, "personDto": person, "roleId": selectedRoleList})).data;
                }
                await metaojt.alert("저장 되었습니다.");
                await this.loadUserList();
            }
            this.btn.saveAccount.loading = false;
		},
        "saveNewRole": async function (user, selectedRoleList) {
            let validate;
            this.btn.saveAccount.loading = true;
            validate = this.$refs.form.validate();
            if (!validate) {
                await metaojt.alert("유효한 값을 작성해주세요.");
            } else if (await metaojt.confirm("저장 하시겠습니까?")) {
	
				
				await metaojt.api.common.roleUser.updateRoleUser({"userId": user.id, "roleId": selectedRoleList.id});
                await metaojt.alert("저장 되었습니다.");
                await this.loadUserList();
            }
            this.btn.saveAccount.loading = false;
        },
        "stck": function (str, limit) {
            var o, d, p, n = 0, l = limit == null ? 4 : limit;
            for (var i = 0; i < str.length; i++) {
                var c = str.charCodeAt(i);
                if (i > 0 && (p = o - c) > -2 && p < 2 && (n = p == d ? n + 1 : 0) > l - 3)
                    return false;
                d = p;
                o = c;
            }
            return true;
        },
        "showPassword": function () {
            if(this.text.icon == 'mdi-eye-off-outline'){
                this.text.icon = 'mdi-eye-outline';
                this.text.type = 'text';
            } else {
                this.text.icon = 'mdi-eye-off-outline';
                this.text.type = 'password';
            }
        }

    },
    "mounted": async function () {
        Promise.all([
            this.loadUserList(),
            this.loadRoleList()
        ]);
    }
}); });