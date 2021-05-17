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
               vacation = (await metaGroupware.api.common.vacation.getVacationInfo(vacationId)).data;
               userName = (await metaGroupware.api.common.user.getUser(vacation.userId)).data.username;

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
               userId = (await metaGroupware.api.common.vacation.getVacationInfo(vacationId)).data.userId;
               userName = (await metaGroupware.api.common.user.getUser(userId)).data.username;

               if(await metaGroupware.confirm("승인하시겠습니까?")) {
                   switch (role) {
                       case "ROLE_EMPLOYEE":
                           await metaGroupware.api.common.approval.modifyApproval(vacationId, {"teamLeader": "T"});
                           await metaGroupware.api.common.mailSend.getMailSend({
                               "to": "dbwlgna98@naver.com",
                               "subject": userName + "님의 휴가신청서",
                               "text": "<a href=http://localhost:81/groupware/approval?vacationId=" + vacationId + "&role=ROLE_TEAMLEADER>" +
                                   "http://localhost:81/groupware/approval" +
                                   "</a>"
                           });
                           break;
                       case "ROLE_TEAMLEADER":
                           await metaGroupware.api.common.approval.modifyApproval(vacationId, {"director": "T"});
                           await metaGroupware.api.common.mailSend.getMailSend({
                               "to": "dbwlgna98@naver.com",
                               "subject": userName + "님의 휴가신청서",
                               "text": "<a href=http://localhost:81/groupware/approval?vacationId=" + vacationId + "&role=ROLE_DIRECTOR>" +
                                            "http://localhost:81/groupware/approval" +
                                        "</a>"
                           });
                           break;
                       case "ROLE_DIRECTOR":
                           await metaGroupware.api.common.approval.modifyApproval(vacationId, {
                               "president": "T",
                               "aprrovalDate": moment().format("YYYY-MM-DD")
                           });
                           await metaGroupware.api.common.mailSend.getMailSend({
                               "to": "kdk7121743@naver.com",
                               "subject": userName + "님의 휴가신청서",
                               "text": userName + "님의 휴가가 최종 승인되었습니다.",
                           });
                           break;
                   }
                   await metaGroupware.alert("승인되었습니다.");
               }
           },
           "rejectApproval": async function() {
               if(await metaGroupware.confirm("거절하시겠습니까?")) {
                   await metaGroupware.alert("거절되었습니다.");
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