var MainPage;
MainPage = Vue.component("main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/main/main.html")).data,
        "data": function () {
            return {
            };
        },
        "methods": {
        },
        "mounted": function () {
        },
    });
});