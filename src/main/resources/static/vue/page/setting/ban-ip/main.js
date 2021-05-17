var SettingBanIPMainPage;
SettingBanIPMainPage = Vue.component("setting-banIp-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/ban-ip/main.html")).data,
        "data": function () {
            return {
                "banIp": {
                    "panels": {
                        "list": [0]
                    },
                    "selected" : [],
                    "dataTable": {
                        "headers": [
                            {"text": "IP 주소", "sortable": false, "value": "ipAddress"},
                            {"text": "IP 유형", "sortable": false, "value": "ipType"},
                            {"text": "IP 요약", "sortable": false, "value": "name"},
                            {"text": "IP 상세 설명", "sortable": false, "value": "description"},
                            {"text": "생성한 날짜", "sortable": false, "value": "createdDate"},
                            {"text": "수정한 시간", "sortable": false, "value": "lastModifiedDate"},
                        ],
                        "items": [],
                        "loading": false,
                        "serverItemsLength": 0,
                        "page": 1,
                        "itemsPerPage": 10
                    },
                    "pagination": {
                        "length": 10,
                        "totalVisible": 10
                    },
                }
            };
        },

        "watch": {
            "banIp.dataTable.page": {
                "handler": function (newValue, oldValue) {
                    this.setBanIPList();
                },
                "deep": true
            }
        },
        "methods": {
            "handleClick" : function(value) {
                this.$router.push({
                    "path": "/settings/ban-ip/detail",
                    "query": {
                        "id": value.id
                    }
                });
            },
            "setBanIPList": async function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            var params = {
                                    "page": self.banIp.dataTable.page,
                                    "rowSize": self.banIp.dataTable.itemsPerPage
                                };
                            self.banIp.dataTable.loading = true;
                            return metaGroupware.api.common.banIP.getBanIPList(params);
                        })
                        .then(function (response) {

                            var data = response.data;
                            self.banIp.dataTable.items = data.items;
                            self.banIp.dataTable.serverItemsLength = data.totalRows;
                            self.banIp.dataTable.loading = false;
                        })
                        .then(function () { resolve(); });
                });
            },

            "deleteBanIPList" : async function() {
                var self = this;

                if (await metaGroupware.confirm("삭제 하시겠습니까?")) {
                    var selectedBanIP = self.banIp.selected;

              
          
                    var idList = [];
                    
                    var idList = selectedBanIP.map( e => e.id);
                    
  

                    (await metaGroupware.api.common.banIP.removeBanIPList(idList));


                    await metaGroupware.alert('삭제에 성공했습니다.');
                    this.setBanIPList();
                }

            },

        },

        "mounted": function () {
            Promise.resolve()
                .then(this.setBanIPList);
        },

    });
});