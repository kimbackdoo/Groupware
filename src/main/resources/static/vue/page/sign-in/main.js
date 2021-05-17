var SignInMainPage;
SignInMainPage = Vue.component("sign-in-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/sign-in/main.html")).data,
    "data": function () {
        return {
            "data": {
                "username": null,
                "password": null
            },
            "btn": {
                "signIn": {
                    "loading": false
                }
            }
        };
    },
    "methods": {
        /* 로그인 */
        "login": async function () {
            this.btn.signIn.loading = true;
            if(!this.data.username) {
                alert("아이디를 입력해주세요.");
            } else if (!this.data.password) {
                alert("비밀번호를 입력해주세요.");
            } else {
                try {
                    await metaojt.auth.login(this.data.username, this.data.password);
                    this.$router.replace("/");
                } catch (e) {
                    alert(e.response.data.message);
                }
            }
            this.btn.signIn.loading = false;
        }
    }
}); });