<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-row type="flex" justify="start" align="middle">
                <el-col :span="1"></el-col>
                <el-col :span="6">
                    <el-input v-model="searchSuppliesName"
                              placeholder="Please input supplies name"
                              prefix-icon="FirstAidKit"
                              @input="searchPrescriptionsBySuppliesName"
                              clearable/>
                </el-col>
                <el-col :span="1"></el-col>
                <el-col :span="3">
                    <el-button type="primary" @click="showReleasePrescriptionDialog">
                        New Prescription
                    </el-button>
                </el-col>
            </el-row>

            <div class="table-frame">
                <el-table :data="selectedPrescriptions"
                          :scrollbar-always-on="true"
                          border stripe>
                    <el-table-column prop="prescriptionId" label="Prescription ID" width="180px"/>
                    <el-table-column prop="releaseDate" label="Release Date" width="180px"/>
                    <el-table-column prop="doctorUsername" label="Doctor Username" width="150px"/>
                    <el-table-column prop="patientUsername" label="Patient Username" width="150px"/>
                    <el-table-column prop="suppliesUnit.supplies.id" label="Supplies ID" width="180px"/>
                    <el-table-column prop="suppliesUnit.supplies.name" label="Supplies Name" width="180px"/>
                    <el-table-column prop="suppliesUnit.quantity" label="Quantity" width="110px"/>
                    <el-table-column label="TxRecord" width="150px">
                        <template #default="scope">
                            <el-button type="success" @click="viewTxRecords([scope.row.txRecord])" plain>
                                view
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" label="Operation" width="110px">
                        <template #default="scope">
                            <el-popconfirm
                                    title="Confirm to remove?"
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    @confirm="removePrescription(scope.row.prescriptionId)">
                                <template #reference>
                                    <el-button type="primary" size="large" link>
                                        <el-icon><Delete /></el-icon>
                                        Remove
                                    </el-button>
                                </template>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <el-dialog v-model="dialog.visible"
                       :title="dialog.title"
                       :modal="true"
                       :close-on-click-modal="false"
                       width="500px">
                <div style="margin: 0 50px">
                    <el-form :model="targetPrescription" label-position="left">
                        <el-form-item label="Patient" label-width="80px" prop="patientUsername">
                            <el-select v-model="targetPrescription.patientUsername" placeholder="Choose Patient">
                                <el-option v-for="(consumer, index) in allConsumers"
                                           :label="consumer.username"
                                           :key="index" :value="consumer.username"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Supplies" label-width="80px">
                            <el-select v-model="targetPrescription.suppliesUnit.supplies.id" placeholder="Choose Supplies">
                                <el-option v-for="(supplies, index) in allPrescriptionSupplies"
                                           :label="supplies.name"
                                           :key="index" :value="supplies.id"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Quantity" label-width="80px">
                            <el-input-number v-model="quantity" :min="1"/>
                        </el-form-item>
                        <el-form-item>
                            <div style="text-align: center">
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="releasePrescription">
                                    <template #reference>
                                        <el-button type="primary">{{dialog.button}}</el-button>
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
    name: "ManagePrescriptions",

    data() {
        return {
            targetPrescription: { prescriptionId: "", releaseDate: "", doctorUsername: "", patientUsername: "",
                                    suppliesUnit: { supplies: {id: "", name: "", isOtc: "", remarks: "", txRecord: {txId: "", txTimestamp: ""} }, quantity: ""},
                                    txRecord: {txId: "", txTimestamp: ""} },
            selectedPrescriptions: [],
            allPrescriptions: [],
            quantity: 1,

            searchSuppliesName: "",
            allPrescriptionSupplies: [],
            allConsumers: [],

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    created() {
        this.getAllPrescriptions();
        this.getAllPrescriptionSupplies();
        this.getAllConsumers();
    },

    methods: {
        releasePrescription() {
            let targetPrescription = this.targetPrescription;
            if (targetPrescription.patientUsername.length !== 0
                    && targetPrescription.suppliesUnit.supplies.id.length !== 0) {
                this.loadingFlag = true;
                this.targetPrescription.prescriptionId = null;
                this.targetPrescription.txRecord = null;
                this.targetPrescription.doctorUsername = this.$store.state.user.username;

                let supplies = this.allPrescriptionSupplies.filter(each => each.id === this.targetPrescription.suppliesUnit.supplies.id)[0];
                this.targetPrescription.suppliesUnit.supplies = JSON.parse(JSON.stringify(supplies));
                this.targetPrescription.suppliesUnit.quantity = this.quantity.toString();

                requests.create.post("/blockchain/doctor/releasePrescription", this.targetPrescription)
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
                this.$message.error("Please choose the patient and supplies");
            }
        },
        removePrescription(prescriptionId) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/doctor/removePrescription", { },
                    { params: { prescriptionId: prescriptionId } })
                    .then( res => {
                        this.loadingFlag = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.refreshData();
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        refreshData() {
            this.getAllPrescriptions();
            this.searchSuppliesName = "";
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

        showReleasePrescriptionDialog() {
            Object.keys(this.targetPrescription).forEach( key => {
                if (key !== "suppliesUnit" && key !== "txRecord") {
                    this.targetPrescription[key] = ""
                }
            });
            this.targetPrescription.suppliesUnit.supplies.id = "";
            this.quantity = 1;

            this.dialog.visible = true;
            this.dialog.title = "Release Prescription";
            this.dialog.button = "Release";
        },

        searchPrescriptionsBySuppliesName() {
            if (this.searchSuppliesName === "") {
                this.selectedPrescriptions = this.allPrescriptions;
            } else {
                this.selectedPrescriptions = this.allPrescriptions.filter(
                        each => each.suppliesUnit.supplies.name.toLowerCase().search(this.searchSuppliesName.toLowerCase()) !== -1);
            }
        },

        getAllPrescriptions() {
            requests.create.get("/blockchain/doctor/getAllPrescriptions")
                    .then( res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allPrescriptions = result.data;
                            this.selectedPrescriptions = this.allPrescriptions;
                        } else {
                            this.selectedPrescriptions = this.allPrescriptions = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllPrescriptionSupplies() {
            requests.create.get("/blockchain/regulator/getAllSupplies")
                    .then( res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allPrescriptionSupplies = result.data.filter(each => each.isOtc === "0");
                        } else {
                            this.allPrescriptionSupplies = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllConsumers() {
            requests.create.post("/server/user/getUsersByRoleName", { },
                    { params: { roleName: "consumer" } })
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allConsumers = result.data;
                        } else {
                            this.allConsumers = [];
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
        background-color: ghostwhite;
    }

    .inner-frame {
        width: 88%;
        height: 80%;
        position: absolute;
        left: 5%;
        top: 10%;
    }

    .table-frame {
        width: 100%;
        position: absolute;
        top: 12%;
        overflow: auto;
        max-height: 90%;
    }
</style>