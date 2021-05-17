var GroupwareMainPage;
GroupwareMainPage = Vue.component('groupware-main-page', async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/groupware/main/main.html")).data,
        "data": function() {
            return {
                "calendar": {
                    "panels": {
                        "list": [0]
                    },
                    "selectedEvent": {},
                    "selectedElement": null,
                    "selectedOpen": false,
                    "events": [],
                    "names": [],
                    "move": '',
                },
                "dialogVacation": {
                    "visible": false,
                    "title": "휴가 신청서",
                    "data": {}
                }
            };
        },
        "methods": {
            "prevCalendar": function() {
                this.$refs.calendar.prev();
            },
            "nextCalendar": function() {
                this.$refs.calendar.next();
            },
            "showEvent": async function({nativeEvent, event}) {
                let self = this;
                if(this.calendar.selectedOpen) {
                    self.calendar.selectedOpen = false;
                    await setTimeout(()=>{
                        self.calendar.selectedOpen = true;
                    },10);
                }else {
                    self.calendar.selectedOpen = true;
                }
                event.term = event.start + " ~ " + event.end;
                self.calendar.selectedEvent = event;
                self.calendar.selectedElement = nativeEvent.target;
                nativeEvent.stopPropagation();
            },
            "loadCalendar": async function({start, end}) {
                if(start !== undefined && end !== undefined) {
                    await this.getVacationList({start, end});
                    await this.getNoticeList({start});
                }
            },
            "getVacationList": async function({start, end}) {
                let userName, color, step, vacationList;
                vacationList = (await metaGroupware.api.common.vacation.getVacationList({
                    "rowSize": 10000000,
                    "sterm": start.date,
                    "eterm": end.date
                })).data.items;

                this.calendar.events = [];
                for(let i=0; i<vacationList.length; i++) {
                    step = (await metaGroupware.api.common.approval.getApproval(vacationList[i].id)).data.step;
                    if(step !== 3) continue;

                    userName = (await metaGroupware.api.common.user.getUser(vacationList[i].userId)).data.username;
                    switch(vacationList[i].type) {
                        case "M":
                            color="green";
                            vacationList[i].type = "월차";
                            break;
                        case "O":
                            color="deep-purple";
                            vacationList[i].type = "반차";
                            break;
                        case "S":
                            color="grey darken-1";
                            vacationList[i].type = "병가";
                            break;
                        case "E":
                            color="orange";
                            vacationList[i].type = "기타";
                            break;
                    }

                    this.calendar.events.push({
                        "name": userName,
                        "type": vacationList[i].type,
                        "start": vacationList[i].sterm,
                        "end": vacationList[i].eterm,
                        "color": color,
                        "sep": "휴가"
                    });
                }
            },
            "getNoticeList": async function ({start}) {
                let noticeList;
                noticeList = (await metaGroupware.api.common.notice.getNoticeList({
                    "rowSize": 10000000,
                    "postingDate": start.date,
                })).data.items;

                for(let i=0; i<noticeList.length; i++) {
                    this.calendar.events.push({
                        "name": noticeList[i].title,
                        "contents": noticeList[i].contents,
                        "start": noticeList[i].postingDate,
                        "end": noticeList[i].postingDate,
                        "color": "red",
                        "sep": "공지사항"
                    });
                }
            },
            "saveNotice": async function(data) {
                let vacation, roleValue;
                data.userId = store.state.app.user.id;

                if(await metaGroupware.confirm("저장하시겠습니까?")) {
                    vacation = (await metaGroupware.api.common.vacation.createVacation(data)).data;
                    roleValue = (await metaGroupware.api.common.roleUser.getRoleUserList({"userId": data.userId})).data.items[0].role.value;
                    await metaGroupware.api.common.mailSend.getMailSend({
                        "to": "dbwlgna98@naver.com",
                        "subject": data.name + "님의 휴가신청서",
                        "text": "<a href=http://localhost:81/groupware/approval?vacationId=" + vacation.id + "&role=" + roleValue + ">" +
                                    "http://localhost:81/groupware/approval" +
                                "</a>"
                    });
                    await metaGroupware.alert("저장되었습니다.");
                    await this.loadCalendar({});
                }
            },
            "registerNotice": function() {
                this.$router.push({
                    "path": "/groupware/notices/details"
                });
            },
            "expenditureDownload": async function() {
                await metaGroupware.api.common.expenditureDownload.downloadExpenditureXlsx();
            },
            "openVacationDialog": function() {
                this.dialogVacation.data = {};
                this.dialogVacation.visible = true;
            }
        },
    });
});