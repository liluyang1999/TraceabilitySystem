<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-row type="flex" justify="start" align="middle">
                <el-col :span="5">
                    <el-input v-model="searchSuppliesName"
                              placeholder="Supplies Name"
                              prefix-icon="FirstAidKit"
                              @input="searchSuppliesByName"
                              clearable/>
                </el-col>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <el-col :span="7">
                    <el-radio-group v-model="selectedType" @change="searchSuppliesByType">
                        <el-radio-button :label="0" border>All</el-radio-button>
                        <el-radio-button :label="1" border>Non-prescription</el-radio-button>
                        <el-radio-button :label="2" border>Prescription</el-radio-button>
                    </el-radio-group>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary" @click="showReleaseSuppliesDialog">
                        New Supplies
                    </el-button>
                </el-col>
            </el-row>

            <el-row style="margin-bottom: 35px;">
                <el-col v-for="supplies in selectedSupplies" :span="7"
                        style="margin-top: 20px; margin-right: 30px">
                    <el-card shadow="always" :body-style="{ padding: '0px'}">
                        <div style="display: flex; flex-direction: column; justify-content: center; align-items: start">
                            <img src="../../assets/img/supplies-otc.png" v-if="supplies.isOtc === '1'"
                                 style="width: 100%; height: 150px" alt/>
                            <img src="../../assets/img/supplies-non-otc.png" v-if="supplies.isOtc === '0'"
                                 style="width: 100%; height: 150px" alt/>
                            <div style="width: 100%; height: 145px; margin: 5px">
                                <div>
                                    <strong>ID:&nbsp;</strong>
                                    <span style="font-size: small">
                                        {{supplies.id}}
                                    </span>
                                </div>
                                <div style="font-size: medium">
                                    <strong>Name:&nbsp;</strong> {{supplies.name}}
                                </div>
                                <div style="font-size: medium">
                                    <strong>Type:&nbsp;</strong>
                                    <el-tag type="success" v-if="supplies.isOtc === '1'" size="small" style="margin: 3px">
                                        Non-prescription
                                    </el-tag>
                                    <el-tag type="warning" v-if="supplies.isOtc === '0'" size="small" style="margin: 3px">
                                        Prescription
                                    </el-tag>
                                </div>
                                <div style="font-size: medium">
                                    <strong>TxRecord:&nbsp;</strong>
                                    <el-button type="primary" plain
                                               size="small"
                                               @click="viewTxRecords([supplies.txRecord])">
                                        View
                                    </el-button>
                                    <el-popconfirm
                                            title="Confirm to cancel this supplies?"
                                            confirm-button-text="Yes"
                                            cancel-button-text="No"
                                            @confirm="removeSupplies(supplies.id)">
                                        <template #reference>
                                            <el-button type="primary" size="small" plain>
                                                Cancel
                                            </el-button>
                                        </template>
                                    </el-popconfirm>
                                </div>
                                <div style="font-size: small">
                                    <strong>Remarks:&nbsp;</strong>
                                    {{supplies.remarks}}
                                </div>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>

            <el-dialog v-model="dialog.visible"
                       :title="dialog.title"
                       :modal="true"
                       :close-on-click-modal="false">
                <div style="margin: 0 50px">
                    <el-form :model="targetSupplies" label-position="left">
                        <el-form-item label="Name" label-width="100px" prop="name">
                            <el-input prefix-icon="EditPen" v-model="targetSupplies.name"
                                      placeholder="Supplies Name" clearable/>
                        </el-form-item>
                        <el-form-item label="Type" label-width="100px" prop="isOtc">
                            <el-radio-group v-model="targetSupplies.isOtc">
                                <el-radio label="1" border>
                                    Non-prescription
                                </el-radio>
                                <el-radio label="0" border>
                                    Prescription
                                </el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="Remarks" label-width="100px" prop="remarks">
                            <el-input prefix-icon="EditPen" v-model="targetSupplies.remarks"
                                      placeholder="Instructions about the Supplies" clearable/>
                        </el-form-item>
                        <el-form-item>
                            <div style="width: 100%; text-align: center">
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="releaseSupplies">
                                    <template #reference>
                                        <el-button type="primary">
                                            {{dialog.button}}
                                        </el-button>
                                    </template>
                                </el-popconfirm>
                            </div>
                        </el-form-item>
                    </el-form>
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script>
import requests from "@/utils/requests";
import {ElMessageBox} from "element-plus";

export default {
    name: "ManageSupplies",

    data() {
        return {
            targetSupplies: {name: "", isOtc: "", remarks: ""},
            selectedSupplies: [],
            allSupplies: [],

            searchSuppliesName: "",
            selectedType: 0,

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            }
        }
    },

    created() {
        this.getAllSupplies();
    },

    methods: {
        releaseSupplies() {
            let targetSupplies = this.targetSupplies;
            if (targetSupplies.name.length !== 0
                    && targetSupplies.isOtc.length !== 0
                    && targetSupplies.remarks.length !== 0) {
                this.loadingFlag = true;
                requests.create.post("/blockchain/regulator/releaseSupplies", this.targetSupplies)
                        .then(res => {
                            this.loadingFlag = false;
                            let result = res.data;
                            if (result.code === 1) {
                                this.refreshData();
                                this.dialog.visible = false;
                                this.$message.success(result.msg);
                            } else if (result.code === 2) {
                                this.$message.error(result.msg);
                            }
                        });
            } else {
                this.$message.error("Input information cannot be empty");
            }
        },
        removeSupplies(suppliesId) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/regulator/removeSupplies", { },
                    { params: {suppliesId: suppliesId} })
                    .then(res => {
                        this.loadingFlag = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.refreshData();
                            this.dialog.visible = false;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        refreshData() {
            this.getAllSupplies();
            this.searchSuppliesName = "";
            this.selectedType = 0;
        },

        viewTxRecords(txRecords) {
            let title = "Fabric Transactions History"
            let content = "";
            for (let index in txRecords) {
                let record = txRecords[index];
                content = content + `<div>Tx ID:&nbsp;${txRecords[index].txId.substring(0, 32)}</div>`
                        + `<div>Timestamp:&nbsp;${txRecords[index].txTimestamp}</div>&nbsp`;
            }
            ElMessageBox.alert(content, title, { confirmButtonText: "OK", dangerouslyUseHTMLString: true}).catch(() => {});
        },

        showReleaseSuppliesDialog() {
            Object.keys(this.targetSupplies).forEach(key => {this.targetSupplies[key] = ""})
            this.dialog.visible = true;
            this.dialog.title = "New Medical Supplies";
            this.dialog.button = "Release";
        },

        searchSuppliesByName() {
            this.selectedType = 0;
            if (this.searchSuppliesName === "") {
                this.selectedSupplies = this.allSupplies;
            } else {
                this.selectedSupplies = this.allSupplies.filter(each =>
                        each.name.toLowerCase().search(this.searchSuppliesName.toLowerCase()) !== -1);
            }
        },
        searchSuppliesByType() {
            this.searchSuppliesName = "";
            if (this.selectedType === 0) {
                this.selectedSupplies = this.allSupplies;
            } else if (this.selectedType === 1) {
                this.selectedSupplies = this.allSupplies.filter(each => each.isOtc === "1");
            } else if (this.selectedType === 2) {
                this.selectedSupplies = this.allSupplies.filter(each => each.isOtc === "0");
            }
        },

        getAllSupplies() {
            requests.create.get("/blockchain/regulator/getAllSupplies")
                .then(res => {
                    let result = res.data;
                    if (result.data !== null) {
                        this.allSupplies = result.data;
                        this.selectedSupplies = this.allSupplies;
                    } else {
                        this.selectedSupplies = this.allSupplies = [];
                    }
                    console.log(result.msg);
                });
        },
    },
}
</script>

<style scoped>
    .outer-frame {
        width: 100%;
        height: 100%;
        background-color: floralwhite;
        overflow: auto;
    }

    .inner-frame {
        position: relative;
        left: 5%;
        top: 3%;
        width: 90%;
    }
</style>