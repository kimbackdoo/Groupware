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
        "page": {
            "type": Number,
            "default": 1
        },
        "itemsPerPage": {
            "type": 10,
            "default": 10
        },
        "sortBy": {
            "type": Array,
            "default": []
        },
        "sortDesc": {
            "type": Array,
            "default": []
        },
        "groupBy": {
            "type": Array,
            "default": []
        },
        "groupDesc": {
            "type": Array,
            "default": []
        },
        "multiSort": {
            "type": Boolean,
            "default": false
        },
        "mustSort": {
            "type": Boolean,
            "default": false
        },
        "loading": {
            "type": Boolean,
            "default": false
        },
        "downloadHide": {
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
                {"text": "전체 보기", "value": null}
            ]
        };
    },
    "watch": {
        "page": {
            "handler": function (n,o) {
                this.$emit("update:page", n);
            }
        },
        "itemsPerPage": {
            "handler": function (n,o) {
                this.$emit("update:itemsPerPage", n);
                this.$emit("update:page", 1);
            }
        },
        "sortBy": {
            "handler": function (n,o) {
                this.$emit("update:sortBy", n);
            }
        },
        "sortDesc": {
            "handler": function (n,o) {
                this.$emit("update:sortDesc", n);
            }
        },
        "groupBy": {
            "handler": function (n,o) {
                this.$emit("update:groupBy",n);
            }
        },
        "groupDesc": {
            "handler": function (n,o) {
                this.$emit("update:groupDesc", n);
            }
        },
        "totalRows": {
            "handler": function (n,o) {
                this.paginationList[this.paginationList.length - 1].value = n;
            },
            "deep": true
        }
    },
    "computed": {
        "disabled": function () {
            return this.loading || this.totalRows === 0 ? true : false;
        }
    },
    "methods": {
        "clickRow": function (e) {
            this.$emit("click:row", e);
        },
        "download": function () {
            this.$emit("download");
        }
    },
}); });