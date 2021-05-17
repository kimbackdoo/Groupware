var GroupwareNoticePage;
GroupwareNoticePage = Vue.component('groupware-notice-page', async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/groupware/notice/main.html")).data,
        "data": function() {
            return {
                "notice": {
                    "panels": {
                        "list": [0]
                    }
                },
                "dataTable": {
                    "headers": [
                        {"text": "제목", "value": "title", "width": 120, "align": "center", cellClass:"text-truncate"},
                        {"text": "내용", "value": "contents", "width": 120, "align": "center", cellClass:"text-truncate"},
                        {"text": "공지일자", "value": "createdDate", "width": 120, "align": "center", cellClass:"text-truncate"}
                    ],
                    "cell": {
                        "icon": {
                            "edit": {
                                "title": "mdi-pencil"
                            }
                        }
                    },
                    "items": [],
                    "totalRows": 0,
                    "loading": false,
                },
            };
        },
        "methods": {
            "init": async function() {
                this.loadNoticeList();
            },
            "loadNoticeList": async function(options) {
                let self = this, noticeList;

                self.dataTable.loading = true;
                noticeList = (await ito.api.app.notice.getNoticeList({
                    "page": options !== undefined ? options.page : 1,
                    "rowSize": options !== undefined ? options.itemsPerPage : 10,
                    "sort": options !== undefined ? ito.util.sort(options.sortBy, options.sortDesc) : [],
                })).data;

                self.dataTable.totalRows = noticeList.totalRows;
                self.dataTable.items = noticeList.items;
                self.dataTable.loading = false;
            },
            "setNotice": async function(value) {
                this.$router.push({
                    "path": "/groupware/notices/details",
                    "query": {
                        "id": value.id
                    }
                });
            },
            "removeNoticeList": async function(data) {
                if(data.length === 0) {
                    await ito.alert("삭제할 데이터를 선택해주세요.");
                } else if(await ito.confirm("삭제하시겠습니까?")) {
                    await ito.api.app.notice.removeNoticeList(data.map(e=>e.id));
                    await ito.alert("삭제했습니다.");
                    await this.loadNoticeList();
                }
            },
        },
        "mounted": function() {
            this.init();
        }
    });
});