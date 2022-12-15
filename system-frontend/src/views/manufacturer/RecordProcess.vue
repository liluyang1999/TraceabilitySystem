<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <div v-if="confirmedOrdersFlag">
                <el-row type="flex" justify="start" align="middle">
                    <el-col :span="6">
                        <el-select v-model="searchDistributorName"
                                   placeholder="Select Distributor"
                                   @change="searchConfirmedOrdersByDistributorName">
                            <el-option label="All" key="0" value="all"/>
                            <el-option v-for="(distributor, index) in allDistributors"
                                       :label="distributor.name"
                                       :key="index + 1" :value="distributor.name"/>
                        </el-select>
                    </el-col>
                    &nbsp;&nbsp;
                    <el-col :span="6">
                        <el-select v-model="searchSuppliesName"
                                   placeholder="Select Supplies"
                                   @change="searchConfirmedOrdersBySuppliesName">
                            <el-option label="All" key="0" value="all"/>
                            <el-option v-for="(supplies, index) in allSupplies"
                                       :label="supplies.name"
                                       :key="index + 1" :value="supplies.name"/>
                        </el-select>
                    </el-col>
                </el-row>

                <div v-for="order in myConfirmedOrders" class="description-frame">
                    <el-descriptions :title="'Order ID: ' + order.orderId"
                                 :column="4"
                                 border>
                        <template #extra>
                            <el-button type="primary" @click="showProcessTable(order)">
                                Production Progress
                            </el-button>
                            <el-button type="primary" @click="showTransferProductsDialog(order)">
                                Transfer
                            </el-button>
                        </template>
                        <el-descriptions-item>
                            <template #label>
                                <div class="description-item">
                                    <el-icon><Calendar /></el-icon>
                                    Request Date
                                </div>
                            </template>
                            {{order.requestDate}}
                        </el-descriptions-item>
                        <el-descriptions-item style="text-align: center">
                            <template #label>
                                <div class="description-item">
                                    <el-icon><View /></el-icon>
                                    Status
                                </div>
                            </template>
                            <el-tag type="success">{{order.status}}</el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="description-item">
                                    <el-icon><Goods /></el-icon>
                                    Supplies Name
                                </div>
                            </template>
                            {{order.suppliesUnit.supplies.name}}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="description-item">
                                    <el-icon><Histogram /></el-icon>
                                    Quantity
                                </div>
                            </template>
                            {{order.suppliesUnit.quantity}}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="description-item">
                                    <el-icon><OfficeBuilding /></el-icon>
                                    Manufacturer
                                </div>
                            </template>
                            {{order.manufacturerName}}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="description-item">
                                    <el-icon><OfficeBuilding /></el-icon>
                                    Distributor
                                </div>
                            </template>
                            {{order.distributorName}}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template #label>
                                <div class="description-item">
                                    <el-icon><Tickets /></el-icon>
                                    License ID
                                </div>
                            </template>
                            {{order.licenseId}}
                        </el-descriptions-item>
                    </el-descriptions>
                    <el-divider/>
                </div>
            </div>

            <div v-if="progressTableFlag"
                    class="table-frame">
                <div>
                    <el-button type="primary" link plain @click="showConfirmedOrderList">
                        <el-icon><ArrowLeft/></el-icon>
                        Back
                    </el-button>
                </div>
                <el-table :data="mySelectedProcesses"
                          :scrollbar-always-on="true"
                          stripe border>
                    <el-table-column prop="processId" label="Process ID" width="150px"/>
                    <el-table-column prop="distributorId" label="Distributor ID" width="150px"/>
                    <el-table-column prop="orderId" label="Order ID" width="180px"/>
                    <el-table-column prop="manufacturerId" label="Manufacturer ID" width="150px"/>
                    <el-table-column prop="manufacturerName" label="Manufacturer Name" width="180px"/>
                    <el-table-column prop="recordDate" label="Record Date" width="200px"/>
                    <el-table-column prop="status" label="Status" width="120px"/>
                    <el-table-column prop="remarks" label="Remarks" width="180px"/>
                    <el-table-column label="TxRecords" width="150px">
                        <template #default="scope">
                            <el-button type="success" @click="viewTxRecords(scope.row.txRecords)" plain>
                                view
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" label="Operation" width="110px">
                        <template #default="scope">
                            <div style="text-align: center">
                                <el-button link type="primary" size="large"
                                         @click="showUpdateProcessDialog(scope.row)">
                                    <el-icon><Edit/></el-icon>
                                    Edit
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
                &nbsp;
                <div style="width: 100%; text-align: center">
                    <el-button style="width: 50%" type="primary" plain
                                @click="showRecordProcessDialog">
                        Record New Process
                    </el-button>
                </div>
            </div>

            <el-dialog v-model="recordProcessDialog.visible"
                       :title="recordProcessDialog.title"
                       :modal="true"
                       :close-on-click-modal="false">
                <div style="margin: 0 50px">
                    <el-form :model="targetProcess" label-position="left" label-width="100px">
                        <div>
                            <strong>Order ID:&nbsp;</strong>
                            {{targetProcess.orderId}}
                        </div>
                        &nbsp;
                        <div>
                            <strong>Manufacturer:&nbsp;</strong>
                            {{targetProcess.manufacturerName}}
                        </div>
                        &nbsp;
                        <el-form-item label="Status" prop="status" label-width="100px" >
                            <el-input prefix-icon="EditPen" v-model="targetProcess.status"
                                      placeholder="Please input the current process status" clearable/>
                        </el-form-item>
                        <el-form-item label="Remarks" label-width="100px" prop="remarks">
                            <el-input prefix-icon="EditPen" v-model="targetProcess.remarks"
                                      placeholder="Please write down the relevant remarks" clearable/>
                        </el-form-item>
                    </el-form>
                    <div style="text-align: center">
                        <el-popconfirm
                                title="Confirm to upload?"
                                confirm-button-text="Yes"
                                cancel-button-text="No"
                                @confirm="handleTargetProcess">
                            <template #reference>
                                <el-button type="primary">{{recordProcessDialog.button}}</el-button>
                            </template>
                        </el-popconfirm>
                    </div>
                </div>
            </el-dialog>
            <el-dialog v-model="transferProductsDialog.visible"
                       :title="transferProductsDialog.title"
                       :modal="true"
                       :close-on-click-modal="false">
                <el-form :model="targetConfirmedOrder" label-position="left">
                    <el-form-item label="TransferDate" label-width="100px" prop="transferDate">
                        <el-date-picker v-model="targetConfirmedOrder.transferDate"
                                        value-format="YYYY-MM-DD HH:mm:ss"
                                        type="datetime"
                                        placeholder="Please select transfer date"/>
                    </el-form-item>
                    <el-form-item label="Carrier" label-width="100px" prop="carrier">
                        <el-input prefix-icon="EditPen" v-model="targetConfirmedOrder.carrier"
                                  placeholder="Please input carrier Name" clearable/>
                    </el-form-item>
                    <el-form-item label="Contact" label-width="100px" prop="contact">
                        <el-input prefix-icon="EditPen" v-model="targetConfirmedOrder.contact"
                                  placeholder="Please input carrier contact" clearable>
                            <template #prepend>+44</template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <div style="width: 100%; text-align: center">
                            <el-popconfirm
                                    title="Confirm to upload?"
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    @confirm="transferProducts">
                                <template #reference>
                                    <el-button type="primary">{{transferProductsDialog.button}}</el-button>
                                </template>
                            </el-popconfirm>
                        </div>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>
    </div>
</template>

<script>
import requests from "@/utils/requests";
import {ElMessageBox} from "element-plus";

export default {
    name: "RecordProcess",

    data() {
        return {
            targetProcess: { processId: "", distributorId: "", orderId: "", manufacturerId: "", manufacturerName: "", recordDate: "", status: "", remarks: "", txRecords: [] },
            selectedProcesses: [],
            allProcesses: [],

            targetConfirmedOrder: { orderId: "", requestDate: "", distributorId: "", distributorName: "", manufacturerId: "", manufacturerName: "", suppliesUnit: {supplies: {id: "", name: "", isOtc: "", remarks: "", txRecord: {txId: "", txTimestamp: ""}}, quantity: ""}, status: "", licenseId: "", transferDate: "", carrier: "", contact: "", receiveDate: "", txRecords: [] },
            selectedConfirmedOrders: [],
            allConfirmedOrders: [],

            searchDistributorName: "",
            allDistributors: [],

            searchSuppliesName: "",
            allSupplies: [],

            confirmedOrdersFlag: true,
            progressTableFlag: false,
            loadingFlag: false,
            recordProcessDialog: {
                  visible: false,
                  title: "",
                  button: "",
            },
            transferProductsDialog: {
                visible: false,
                title: "",
                button: "",
            },
      }
    },

    created() {
        this.getAllConfirmedOrders();
        this.getAllDistributors();
        this.getAllSupplies();
        this.getAllProcesses();
    },

    computed: {
        myConfirmedOrders() {
            return this.selectedConfirmedOrders.filter(each => each.manufacturerId === this.$store.state.dept.id).reverse();
        },
        mySelectedProcesses() {
            return this.selectedProcesses.filter(each => (each.manufacturerId === this.targetConfirmedOrder.manufacturerId)
                                                            && (each.orderId === this.targetConfirmedOrder.orderId));
        },
    },

    methods: {
        handleTargetProcess() {
            let targetProcess = this.targetProcess;
            if (targetProcess.status.length !== 0 && targetProcess.remarks.length !== 0) {
                this.loadingFlag = true;
                if (this.recordProcessDialog.button === "Record") {
                    this.recordProcess();
                } else {
                    this.updateProcess();
                }
            } else {
                this.$message.error("Input information cannot be empty");
            }
        },
        recordProcess() {
            requests.create.post("/blockchain/manufacturer/recordProcess", this.targetProcess)
                    .then(res => {
                        this.loadingFlag = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.refreshData();
                            this.recordProcessDialog.visible = false;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        updateProcess() {
            requests.create.post("/blockchain/manufacturer/updateProcess", this.targetProcess)
                    .then(res => {
                        this.loadingFlag = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.refreshData();
                            this.recordProcessDialog.visible = false;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        refreshData() {
            this.getAllProcesses();
            this.searchDistributorName = "";
            this.searchSuppliesName = "";
        },
        transferProducts() {
            let targetConfirmedOrder = this.targetConfirmedOrder;
            if ((targetConfirmedOrder.transferDate != null && targetConfirmedOrder.carrier.length !== 0)
                    && targetConfirmedOrder.contact.length !== 0) {
                let length = targetConfirmedOrder.contact.length;
                if (length > 0 && !(length >= 10 && length <= 11)) {
                    this.$message.error("Length of contact should be 10 to 11");
                    return;
                }

                this.loadingFlag = true;
                requests.create.post("/blockchain/manufacturer/transferProducts", this.targetConfirmedOrder)
                        .then(res => {
                            this.loadingFlag = false;
                            let result = res.data;
                            if (result.code === 1) {
                                this.getAllConfirmedOrders();
                                this.searchDistributorName = "";
                                this.searchSuppliesName = "";
                                this.transferProductsDialog.visible = false;
                                this.$message.success(result.msg);
                            } else if (result.code === 2) {
                                this.$message.error(result.msg);
                            }
                        });
            } else {
                this.$message.error("Input information cannot be empty");
            }
        },

        showRecordProcessDialog() {
            Object.keys(this.targetProcess).forEach(key => this.targetProcess[key] = "");
            this.targetProcess.processId = null;
            this.targetProcess.txRecords = null;
            this.targetProcess.orderId = this.targetConfirmedOrder.orderId;
            this.targetProcess.distributorId = this.targetConfirmedOrder.distributorId;
            this.targetProcess.manufacturerId = this.targetConfirmedOrder.manufacturerId;
            this.targetProcess.manufacturerName = this.targetConfirmedOrder.manufacturerName;
            this.recordProcessDialog.visible = true;
            this.recordProcessDialog.title = "Record New Process";
            this.recordProcessDialog.button = "Record";
        },
        showUpdateProcessDialog(process) {
            this.targetProcess = JSON.parse(JSON.stringify(process));
            this.recordProcessDialog.visible = true;
            this.recordProcessDialog.title = "Update Process";
            this.recordProcessDialog.button = "Update";
        },
        showTransferProductsDialog(order) {
            this.targetConfirmedOrder = JSON.parse(JSON.stringify(order));
            this.transferProductsDialog.visible = true;
            this.transferProductsDialog.title = "Transfer Products";
            this.transferProductsDialog.button = "Transfer";
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

        showProcessTable(order) {
            this.targetConfirmedOrder = JSON.parse(JSON.stringify(order));
            this.confirmedOrdersFlag = false;
            this.progressTableFlag = true;
        },
        showConfirmedOrderList() {
            this.progressTableFlag = false;
            this.confirmedOrdersFlag = true;
        },

        searchConfirmedOrdersByDistributorName() {
            this.searchSuppliesName = "";
            if (this.searchDistributorName === "all") {
                this.selectedConfirmedOrders = this.allConfirmedOrders;
            } else {
                this.selectedConfirmedOrders = this.allConfirmedOrders.filter(
                        each => each.distributorName === this.searchDistributorName);
            }
        },
        searchConfirmedOrdersBySuppliesName() {
            this.searchDistributorName = "";
            if (this.searchSuppliesName === "all") {
                this.selectedConfirmedOrders = this.allConfirmedOrders;
            } else {
                this.selectedConfirmedOrders = this.allConfirmedOrders.filter(
                        each => each.suppliesUnit.supplies.name === this.searchSuppliesName);
            }
        },

        getAllConfirmedOrders() {
            requests.create.get("/blockchain/distributor/getOrdersByStatus", { params: { status: "confirmed" } })
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allConfirmedOrders = result.data;
                            this.selectedConfirmedOrders = this.allConfirmedOrders;
                        } else {
                            this.selectedConfirmedOrders = this.allConfirmedOrders = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllDistributors() {
            requests.create.get("/server/mgmt/getAllDepts")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allDistributors = result.data.filter(each => each.type === "distributor");
                        } else {
                            this.allDistributors = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllSupplies() {
            requests.create.get("/blockchain/regulator/getAllSupplies")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allSupplies = result.data;
                        } else {
                            this.allSupplies = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllProcesses() {
            requests.create.get("/blockchain/manufacturer/getAllProcesses")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allProcesses = result.data;
                            this.selectedProcesses = this.allProcesses;
                        } else {
                            this.selectedProcesses = this.allProcesses = [];
                        }
                        console.log(result.msg);
                    });
        },
    }
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
        height: 90%;
    }

    .description-frame {
        margin: 15px 0;
    }

    .description-item {
        display: flex;
        align-items: center;
    }

    .table-frame {
        width: 100%;
        height: 90%;
        max-height: 90%;
        position: absolute;
        top: 15px;
        overflow: auto;
    }
</style>