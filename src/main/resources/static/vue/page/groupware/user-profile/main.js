var UserProfilePage = Vue.component('user-profile-page', function(resolve, reject){
    axios.get("/vue/page/groupware/user-profile/main.html").then(function(response){
        resolve({
            "template": response.data,
            "data" : function(){
                return {
                    "dataUpload":{
                        "signFile" : null,
                        "signFileName" : null,
                        "sealFile" : null,
                        "sealFileName" : null
                    },
                    "user":{
                        "panels":{
                            "list":[0]
                        }
                    },
                    "data": {},
                    "userSeal":{},
                }
            },
            "watched":{

            },
            "methods":{
                "onSignButtonClick": async function(){
                    this.isSelecting = true
                    window.addEventListener('focus', () => {
                        this.isSelecting=false
                    }, {once: true})
                    this.$refs.signUploader.click()
                },
                "onSealButtonClick": async function(){
                    this.isSelecting2 = true
                    window.addEventListener('focus', () => {
                        this.isSelecting2=false
                    }, {once: true})
                    this.$refs.sealUploader.click()
                },
                "setUserProfile":async function(){
                    var self =this;

                    var userId = await store.state.app.user.id;
                    var value = (await metaGroupware.api.common.user.getUser(userId)).data;
                    console.log(value);

                    //userId로 조회해서 가져오기
                    // -> 이미지 파일은 이름만 가져오고 업데이트 치기
                    var sealInfo = (await metaGroupware.api.common.userSeal.getUserSeal(userId)).data;
                    this.userSeal = _.cloneDeep(sealInfo)
                    console.log(sealInfo)

                    if(value !== undefined){
                        self.data = _.cloneDeep(value);
                        this.dataUpload.sealFileName = sealInfo.sealName;
                        this.dataUpload.signFileName = sealInfo.signName;
                    }
                },
                "saveProfile": async function(){
                    var self = this;
                    var form = new FormData();
                    var userId = await store.state.app.user.id;


                    await metaGroupware.confirm("저장하시겠습니까?");
                    store.commit("app/SET_LOADING", true);

                    await metaGroupware.api.common.user.modifyUser(userId, this.data);
                    if(this.dataUpload.signlFile !== undefined )

                    form.append("signMultiFile" , this.dataUpload.signlFile);
                    form.append("sealMultiFile" , this.dataUpload.sealFile);
                    form.append("sealName", this.dataUpload.sealFileName);
                    form.append("signName", this.dataUpload.signFileName);
                    form.append("userId", userId);
                    form.append("id", this.userSeal.id);
                    await metaGroupware.api.common.userSeal.modifyUserSeal(form);

                    store.commit("app/SET_LOADING", false);
                    await metaGroupware.alert("수정이 완료되었습니다 ^3^; ");



                },
                "onSignFileChanged": function(e) {
                    this.dataUpload.signFile = e.target.files[0];
                    if(this.dataUpload.signFile != undefined && this.dataUpload.signFile != null){
                        this.dataUpload.signFileName = this.dataUpload.signFile.name;
                    }else{
                        this.dataUpload.signFileName=null;
                    }
                },
                "onSealFileChanged": function(e) {
                    this.dataUpload.sealFile = e.target.files[0];
                    if(this.dataUpload.sealFile != undefined && this.dataUpload.sealFile != null){
                        this.dataUpload.sealFileName = this.dataUpload.sealFile.name;
                    }else{
                        this.dataUpload.sealFileName=null;
                    }
                },
                "profileClear": function() {
                    this.data={};
                    document.getElementById("signHiddenFile").value = "";
                    document.getElementById("sealHiddenFile").value = "";
                    this.dataUpload.signFile = null;
                    this.dataUpload.signFileName = null;
                    this.dataUpload.sealFile = null;
                    this.dataUpload.sealFileName = null;
                },

            },
            "mounted": async function(){
                var self = this;
                await self.setUserProfile();
            }
        });
    });
});
