var DataTableCustomComponent;
DataTableCustomComponent = Vue.component("data-table-custom-component", async function (resolve) { resolve({
    "template": (await axios.get("/vue/component/data-table-custom-component.html")).data,
    "props": {
        "headers": {
            "type": Object,
            "default": {
                "page": 1,
                "itemsPerPage": 10,
                "pageStart": 0,
                "pageStop": 10,
                "pageCount": 1,
                "itemsLength": 10
            }
        },
        "items": {
            "type": Array,
            "default": []
        },
        "itemClass": {
            "type": String,
            "default": ""
        },
        "totalRows": {
            "type": Number,
            "default": 0
        },
        "loading": {
            "type": Boolean,
            "default": false
        },
        "importHide": {
            "type": Boolean,
            "default": false
        },
        "addHide": {
            "type": Boolean,
            "default": false
        },
        "removeHide": {
            "type": Boolean,
            "default": false
        },
        "showSelect": {
            "type": Boolean,
            "default": false
        },
        "itemKey": {
            "type": String,
            "default": "id"
        },
        "countTitle": {
            "type": Object,
            "default": {
                "front": "전체",
                "end": "건"
            }
        },
        "cell": {
            "type": Object,
            "default": {}
        },
        "downloadHide": {
            "type": Boolean,
            "default": false
        },
        "search": {
            "type": Boolean,
            "default": false
        }
    },
    "data": function () {
        return {
            "paginationList": [
                {"text": "5개 보기", "value": 5},
                {"text": "10개 보기", "value": 10},
                {"text": "15개 보기", "value": 15},
                {"text": "20개 보기", "value": 20},
                {"text": "25개 보기", "value": 25},
                {"text": "30개 보기", "value": 30},
            ],
            "data": [],
            "options": {
                "page": 1,
                "itemsPerPage": 10,
                "sortBy": [],
                "sortDesc": [],
                "groupBy": [],
                "groupDesc": [],
                "multiSort": true,
                "mustSort": false
            }
        };
    },
    "watch": {
        "options": {
            "handler": function (n,o) {
                this.$emit("reload", n);
            },
            "deep": true
        },
        "loading": {
            "handler": function (n,o) {
                this.data = [];
            },
            "deep": true
        },
        "search": {
            "handler": function (n,o) {
                if(n) {
                    if(this.options.page !== 1) {
                        this.options.page = 1;
                    }else {
                        this.$emit("reload", this.options);
                    }
                }
            }
        }
    },
    "computed": {
        "disabled": function () {
            return this.loading || this.totalRows === 0 ? true : false;
        },
        "count": function () {
            var finalCount=null, temp;
            var alterCount = this.totalRows;
            while(alterCount > 1000){
                temp = alterCount % 1000;
                alterCount = Math.floor(alterCount / 1000);
                finalCount = ","+String(temp);
            }
            if(finalCount != null) finalCount = String(alterCount) + finalCount;
            else finalCount = this.totalRows;
            return this.countTitle.front + " " + finalCount + this.countTitle.end;
        }
    },
    "methods": {
        "clickRow": function (e) {
            this.$emit("click:row", e);
        },
        "excelImport": function () {
            this.$emit("import");
        },
        "remove": function () {
            this.$emit("remove", this.data);
        },
        "add": function () {
            this.$emit("add");
        },
        "clickIcon": function (item, header) {
            const clicked = { item, header };
            this.$emit("click:icon", clicked);
        },
        "changeAutocomplete": function (id, header, item) {
            const selected = { id, header, item };
            this.$emit("change:autocomplete", selected);
        },
        "clickButton": function (item, header) {
            const clicked = {item, header};
            this.$emit("click:button", clicked);
        },
        "inputTextField": function (value, item, header) {
            const clicked = {value, item, header};
            this.$emit("input:textField", clicked);
        },
        "download": function(){
            this.$emit("download")
        }
    },
}); });