var MainLayout;
MainLayout = Vue.component("main-layout", async function (resolve) { resolve({
    "template": (await axios.get("/vue/layout/main.html")).data,
    "data": function () {
        return {
            "drawer": undefined
        };
    },
    "computed": {
        "name": function () {
            var name,
                user,
                person;
            name = "";
            user = _.cloneDeep(store.state.app.user);
            person = _.cloneDeep(store.state.app.person);
            if (user && person) {
                name = person
                        ? person.name
                        : user.username;
                name += " 님";
            }
            return name;
        }
    },
    "methods": {
        "logout": async function () {
            var token;
            token = metaojt.auth.getToken();
            await metaojt.auth.logout(token);
            this.$router.replace("/sign-in");
        }
    }
}); });