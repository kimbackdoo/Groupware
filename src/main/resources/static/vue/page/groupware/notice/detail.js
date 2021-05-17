var GroupwareNoticeDetailPage;
GroupwareNoticeDetailPage = Vue.component('roupware-notice-detail-page', async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/groupware/notice/detail.html")).data,
        "data": function() {
            return {
                "data": {
                    "notice": {}
                }
            };
        },
        "methods": {
            "setNotice": async function() {
                let noticeId=this.$route.query.id, notice;

                if(noticeId !== undefined && noticeId !== null) {
                    notice = (await metaGroupware.api.common.notice.getNotice(noticeId)).data;
                    this.data.notice = notice;
                }
            },
            "saveNotice": async function() {
                let userId = store.state.app.user.id,
                    notice = _.cloneDeep(this.data.notice);

                delete notice.createdDate;
                delete notice.lastModifiedDate;

                if(await metaGroupware.confirm("저장하시겠습니까?")) {
                    if(notice.id !== undefined && notice.id !== null) {
                        await metaGroupware.api.common.notice.modifyNotice(notice.id, notice);
                    } else {
                        notice.userId = userId;
                        await metaGroupware.api.common.notice.createNotice(notice);
                    }
                    await metaGroupware.alert("저장했습니다.");
                    this.$router.push({"path": "/groupware/notices"});
                }
            },
            "initializeNotice": function() {
                this.data.notice = [];
            }
        },
        "mounted": async function() {
            await this.setNotice();
        }
    });
});
