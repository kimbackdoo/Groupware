var GroupwareApprovalPage;
GroupwareApprovalPage = Vue.component('groupware-approval-page', async function(resolve) {
   resolve({
       "template": (await axios.get("/vue/page/groupware/approval/main.html")).data,
       "data": function() {
           return {
               "vacation": {},
           };
       },
       "methods": {
           "init": async function() {
               await this.loadVacation();
           },
           "loadVacation": async function() {
               let userName, vacation, vacationId = this.$route.query.vacationId;
               vacation = (await ito.api.app.vacation.getVacationInfo(vacationId)).data;
               userName = (await ito.api.common.user.getUser(vacation.userId)).data.username;

               this.vacation = vacation;
               this.vacation.name = userName;
               switch (this.vacation.type) {
                   case "M": this.vacation.type="월차"; break;
                   case "N": this.vacation.type="연차"; break;
                   case "O": this.vacation.type="반차"; break;
                   case "S": this.vacation.type="병가"; break;
                   case "E": this.vacation.type="기타"; break;
               }
               this.vacation.term = vacation.sterm + " ~ " + vacation.eterm;
           },
           "saveApproval": async function() {
               let userId, userName, vacationId, role = this.$route.query.role;
               vacationId = this.$route.query.vacationId;
               userId = (await ito.api.app.vacation.getVacationInfo(vacationId)).data.userId;
               userName = (await ito.api.common.user.getUser(userId)).data.username;

               if(await ito.confirm("승인하시겠습니까?")) {
                   switch (role) {
                       case "ROLE_EMPLOYEE":
                           await ito.api.app.approval.modifyApproval(vacationId, {"teamLeader": "T"});
                           await ito.api.app.mailSend.getMailSend({
                               "to": "dbwlgna98@naver.com",
                               "subject": userName + "님의 휴가신청서",
                               "text": "<a href=http://localhost:81/groupware/approval?vacationId=" + vacationId + "&role=ROLE_TEAMLEADER>" +
                                   "http://localhost:81/groupware/approval" +
                                   "</a>"
                           });
                           break;
                       case "ROLE_TEAMLEADER":
                           await ito.api.app.approval.modifyApproval(vacationId, {"director": "T"});
                           await ito.api.app.mailSend.getMailSend({
                               "to": "dbwlgna98@naver.com",
                               "subject": userName + "님의 휴가신청서",
                               "text": "<a href=http://localhost:81/groupware/approval?vacationId=" + vacationId + "&role=ROLE_DIRECTOR>" +
                                            "http://localhost:81/groupware/approval" +
                                        "</a>"
                           });
                           break;
                       case "ROLE_DIRECTOR":
                           await ito.api.app.approval.modifyApproval(vacationId, {
                               "president": "T",
                               "aprrovalDate": moment().format("YYYY-MM-DD")
                           });
                           await ito.api.app.mailSend.getMailSend({
                               "to": "kdk7121743@naver.com",
                               "subject": userName + "님의 휴가신청서",
                               "text": userName + "님의 휴가가 최종 승인되었습니다.",
                           });
                           break;
                   }
                   await ito.alert("승인되었습니다.");
               }
           },
           "rejectApproval": async function() {
               if(await ito.confirm("거절하시겠습니까?")) {
                   await ito.alert("거절되었습니다.");
                   this.$router.push({
                       "path": "/groupware/main"
                   });
               }
           }
       },
       "mounted": function() {
           this.init();
       }
   });
});