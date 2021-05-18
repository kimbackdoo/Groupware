var SignUpMainPage;
SignUpMainPage = Vue.component("sign-up-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/sign-up/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {
                        "department": null,
                        "teamName": null,
                        "position": null,
                    },
                    "role": {
                        "id": null
                    },
                },
                "text": {
                    "icon": "mdi-eye-off-outline",
                    "type": "password"
                },
                "btn": {
                    "saveAccount": {
                        "loading": false
                    },
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
                        } else if (value.indexOf(moment(this.data.user.birthDate).format("MMDD")) !== -1) {
                            message = '생년월일 포함';
                        } else if (value.indexOf(this.data.user.username) !== -1) {
                            message = '아이디 포함';
                        } else {
                            flag = true;
                        }
                        return flag || message;
                    },
                    "email": (value) => {
                        let message, flag;
                        flag = false;
                        if(!(/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i.test(value))) {
                            message = '유효한 이메일이 아닙니다.';
                        } else {
                            flag = true;
                        }
                        return flag || message;
                    },
                },
                "select": {
                    "department": {
                        "items": [
                            {"text": "개발부", "value": "develop"},
                            {"text": "서비스부", "value": "service"}
                        ]
                    },
                    "teamName": {
                        "items": []
                    },
                    "position": {
                        "items": []
                    }
                }
            };
        },
        "watch": {
            "data.user.department": {
                "handler": function(value) {
                    this.select.teamName.items = []; this.data.user.teamName = null;
                    this.select.position.items = []; this.data.user.position = null;
                    switch (value) {
                        case "develop": this.select.teamName.items.push({"text": "Temp", "value": "temp"}); break;
                        case "service": this.select.teamName.items.push({"text": "운영팀", "value": "operation"}, {"text": "기획팀", "value": "plan"}); break;
                    }
                }
            },
            "data.user.teamName": {
                "handler": function(value) {
                    this.select.position.items = []; this.data.user.position = null;
                    if (value === "temp") {
                        this.select.position.items.push({"text": "연구원", "value": "engineer"});
                    } else if (value === "operation" || value === "plan"){
                        this.select.position.items.push({"text": "팀장", "value": "teamLeader"});
                    }
                }
            },
        },
        "methods": {
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
            "existUsername": async function (username) {
                return (await metaGroupware.auth.idExists({"username": username})).data > 0 ? true : false;
            },
            "saveAccount": async function () {
                let user, validate;
                user = this.data.user;
                this.btn.saveAccount.loading = true;
                validate = this.$refs.form.validate();
                if (!validate) {
                    await metaGroupware.alert("유효한 값을 작성해주세요.");
                } else if (await this.existUsername(user.username)) {
                    await metaGroupware.alert("동일한 아이디가 존재합니다.");
                } else if (await metaGroupware.confirm("회원가입 하시겠습니까?")) {
                    await metaGroupware.auth.signUp({"userDto": user});
                    await metaGroupware.alert("회원가입 되었습니다.");
                    this.$router.push({"path": "/sign-in"});
                }
                this.btn.saveAccount.loading = false;
            },
            "showPassword": function () {
                if(this.text.icon == 'mdi-eye-off-outline'){
                    this.text.icon = 'mdi-eye-outline';
                    this.text.type = 'text';
                } else {
                    this.text.icon = 'mdi-eye-off-outline';
                    this.text.type = 'password';
                }
            },
        },
        "mounted": async function () {
            console.log(this.data.user);
        }
    });
});