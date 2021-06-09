var FileUploadDialogComponent;
FileUploadDialogComponent = Vue.component('signFile-upload-dialog-component', async function(resolve) { resolve({
        "template": (await axios.get("/vue/dialog/signFile-upload/main.html")).data,
        "props": {
            "dialog": {
                "type": Object,
                "default": {
                    "visible": false,
                    "title": "파일 등록",
                    "data": {}
                }
            }
        },
        "data": function() {
            return {
                "dataUpload":{
                    "selectedFile" : null,
                    "selectedFileName" : null,
                    "selectedFile2" : null,
                    "selectedFileName2" : null
                },
            }
        },
        "watch": {
            "dialog.visible": {
                "handler": function (n) {
                    if(!n) {
                        this.fileClear();
                    }
                }
            }
        },
        "methods": {
            "onButtonClick" : function (){
                this.isSelecting = true
                window.addEventListener('focus', () => {
                    this.isSelecting=false
                }, {once: true})
                this.$refs.uploader.click()
            },
            "onButtonClick2" : function (){
                this.isSelecting2 = true
                window.addEventListener('focus', () => {
                    this.isSelecting2=false
                }, {once: true})
                this.$refs.uploader2.click()
            },
            "onFileChanged": function(e) {
                this.dataUpload.selectedFile = e.target.files[0];
                if(this.dataUpload.selectedFile != undefined && this.dataUpload.selectedFile != null){
                    this.dataUpload.selectedFileName = this.dataUpload.selectedFile.name;
                }else{
                    this.dataUpload.selectedFileName=null;
                }
            },
            "onFileChanged2": function(e) {
                this.dataUpload.selectedFile2 = e.target.files[0];
                if(this.dataUpload.selectedFile2 != undefined && this.dataUpload.selectedFile2 != null){
                    this.dataUpload.selectedFileName2 = this.dataUpload.selectedFile2.name;
                }else{
                    this.dataUpload.selectedFileName2=null;
                }
            },
            "onFileUpload" : async function() {
                var self = this;

                if(self.dataUpload.selectedFile != null && self.dataUpload.selectedFile.size > 0
                        || self.dataUpload.selectedFile2 != null && self.dataUpload.selectedFile2.size > 0) {
                    this.$emit("upload", self.dataUpload);
                    this.dialog.visible = false;
                } else {
                    await ito.alert('파일을 선택해주세요.');
                }
            },
            "fileClear": function() {
                document.getElementById("hiddenFile").value = "";
                document.getElementById("hiddenFile2").value = "";
                this.dataUpload.selectedFileName = null;
                this.dataUpload.selectedFile = null;
                this.dataUpload.selectedFileName2 = null;
                this.dataUpload.selectedFile2 = null;
            },
            "clear": function() {
                this.dialog.visible = false;
            },
        },
    });
});