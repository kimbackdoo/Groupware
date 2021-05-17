var SettingBanIPDetailMainPage;
SettingBanIPDetailMainPage = Vue.component("setting-banIP-detail-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/ban-ip/main-detail.html")).data,
        "data": function () {
            return {
                "banIP_detail": {
                    "panels": {
                        "list": [0]
                    },
                },
                "ipTypeLIst" : [
                    "IPv4", "IPv6"
                ],
                "detailData" : {
                    "id" : null,
                    "ipAddress" : null,
                    "ipType" : null,
                    "name" : null,
                    "description" : null,
                    "createdDate" : null,
                    "lastModifiedDate" : null,
                },
            };
        },
        "watch": {

        },
        "methods": {
            "setBanIPDeatil": async function (queryId) {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            return  metaGroupware.api.common.banIP.getBanIP(queryId);
                        })
                        .then(function (response) {
                            self.detailData = response.data;
                        })
                        .then(function () { resolve(); });
                });
            },
            "registerNewBanIP" : async function () {
                var self = this;

                if(self.detailData.ipAddress == null || self.detailData.ipAddress == "")
                {
                    await metaGroupware.alert("IP 주소를 반드시 입력하세요.");
                    return;
                }


                if(self.detailData.ipType == null || self.detailData.ipType == "")
                {
                    await metaGroupware.alert("IP 유형을 반드시 입력하세요.");
                    return;
                }

                if(self.detailData.name == null || self.detailData.name == "")
                {
                    await metaGroupware.alert("요약을 반드시 입력하세요.");
                    return;
                }

                if(self.detailData.description == null || self.detailData.description == "")
                {
                    await metaGroupware.alert("상세 설명을 반드시 입력하세요.");
                    return;
                }
                if (await metaGroupware.confirm("저장 하시겠습니까?")) {

                    var params = {
                        "ipAddress" : self.detailData.ipAddress,
                        "ipType" : self.detailData.ipType,
                        "name" : self.detailData.name,
                        "description" :  self.detailData.description,
                    };

                    new Promise(function (resolve, reject) {
                        Promise.resolve()
                            .then(function () {
                                return metaGroupware.api.common.banIP.createBanIP(params);
                            })
                            .then(function () { resolve(); });
                    });
                    await metaGroupware.alert('저장에 성공했습니다.');
                    this.$router.push({
                        "path": "/settings/ban-ip",
                    });
                }
            },
            "updateBanIP" : async function () {
                var self = this;

                if(self.detailData.ipAddress == null || self.detailData.ipAddress == "")
                {
                    await metaGroupware.alert("IP 주소를 반드시 입력하세요.");
                    return;
                }


                if(self.detailData.ipType == null || self.detailData.ipType == "")
                {
                    await metaGroupware.alert("IP 유형을 반드시 입력하세요.");
                    return;
                }
                if(self.detailData.ipType == 'IPv4' && !(/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/g.test(self.detailData.ipAddress)))
                {
                    await metaGroupware.alert("IPv4 형식에 맞지 않습니다.");
                    return;
                }

                if(self.detailData.ipType == 'IPv6' && !(/^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$/g.test(self.detailData.ipAddress)))
                {
                    await metaGroupware.alert("IPv6 형식에 맞지 않습니다.");
                    return;
                }

                if(self.detailData.name == null || self.detailData.name == "")
                {
                    await metaGroupware.alert("요약을 반드시 입력하세요.");
                    return;
                }

                if(self.detailData.description == null || self.detailData.description == "")
                {
                    await metaGroupware.alert("상세 설명을 반드시 입력하세요.");
                    return;
                }

                if (await metaGroupware.confirm("수정 하시겠습니까?")) {

                    var params = {
                        "id" : self.detailData.id,
                        "ipAddress" : self.detailData.ipAddress,
                        "ipType" : self.detailData.ipType,
                        "name" : self.detailData.name,
                        "description" :  self.detailData.description,

                    };
                    new Promise(function (resolve, reject) {
                        Promise.resolve()
                            .then(function () {
                                 return metaGroupware.api.common.banIP.modifyBanIP( params.id, params);
                            })
                            .then(function () { resolve();  });
                    });

                    await metaGroupware.alert('수정에 성공했습니다.');
                    this.$router.push({
                        "path": "/settings/ban-ip",
                    });
                }
            },
        },
        "mounted": function () {
            var queryId = this.$route.query.id;
            if(queryId != undefined && queryId!= null)
            {
                Promise.resolve()
                    .then(this.setBanIPDeatil(queryId),);
            }
        },
    });
});