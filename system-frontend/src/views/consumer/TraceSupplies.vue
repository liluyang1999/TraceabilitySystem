<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div v-if="salesPageFlag" class="inner-frame1">
            <div v-for="sale in mySales" class="description-frame">
                <el-descriptions :title="'Sale ID: ' + sale.saleId" :column="2" border>
                    <template #extra>
                        <div>
                            <el-popconfirm
                                    v-if="myTraceTasks.filter(each => each.sale.saleId === sale.saleId).length === 0"
                                    title="Confirm the sales and start to trace?"
                                    confirm-button-text="Confirm"
                                    cancel-button-text="Cancel"
                                    @confirm="traceSupplies(sale)">
                                <template #reference>
                                    <el-button type="primary">
                                        Confirm Sale
                                    </el-button>
                                </template>>
                            </el-popconfirm>
                            <el-button v-else type="success" @click="traceSupplies(sale)">
                                Trace Supplies
                            </el-button>
                        </div>
                    </template>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><User /></el-icon>&nbsp;
                                Consumer
                            </div>
                        </template>
                        {{sale.username}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><Goods /></el-icon>&nbsp;
                                Supplies Name
                            </div>
                        </template>
                        {{sale.suppliesUnit.supplies.name}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><Histogram /></el-icon>&nbsp;
                                Quantity
                            </div>
                        </template>
                        {{sale.suppliesUnit.quantity}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><OfficeBuilding /></el-icon>&nbsp;
                                Provider
                            </div>
                        </template>
                        {{sale.providerName}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><Calendar /></el-icon>&nbsp;
                                Purchase Date
                            </div>
                        </template>
                        {{sale.purchaseDate}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><Van /></el-icon>&nbsp;
                                Distribution ID
                            </div>
                        </template>
                        {{sale.distributionId}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><List /></el-icon>&nbsp;
                                Order ID
                            </div>
                        </template>
                        {{sale.orderId}}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="description-item">
                                <el-icon><Notebook /></el-icon>&nbsp;
                                TxRecords
                            </div>
                        </template>
                        <el-button type="success" @click="viewTxRecords(sale.txRecords)" plain>
                            view
                        </el-button>
                    </el-descriptions-item>
                </el-descriptions>
                <el-divider/>
            </div>
        </div>


        <div v-if="traceTaskPageFlag" class="inner-frame2">
            <el-row>
                <el-col :span="2" style="text-align: center; height: 2%">
                    <el-button type="primary" link plain @click="showSalesPage">
                        <el-icon><ArrowLeft/></el-icon>
                        Back
                    </el-button>
                </el-col>
                <el-col :span="22" style="text-align: center; height: 2%; font-size: large">
                    <strong>Trace ID: {{this.targetTraceTask.traceId}}</strong>
                </el-col>
            </el-row>
            <el-row justify="space-evenly" style="margin-top: 10px">
                <el-col :span="8">
                    <el-card shadow="always" :body-style="{ padding: '5px'}" style="background-color: whitesmoke; height: 510px">
                        <el-scrollbar>
                                <div style="max-height: 500px">
                                    <el-row type="flex" justify="space-evenly" align="middle">
                                        <el-col :span="10">
                                            <img src="../../assets/img/hyperledger-fabric.png"
                                                 style="width: 100%; height: 130px;" alt />
                                        </el-col>
                                        <el-col :span="13" :offset="1">
                                            <div style="width: 100%; height: 130px; display: flex; flex-direction: column; justify-content: space-evenly">
                                                <strong>
                                                    {{this.channelId.toUpperCase()}}
                                                </strong>
                                                <strong>
                                                    Current Height:&nbsp;
                                                    {{this.height}}
                                                </strong>
                                                <strong>
                                                    Genesis Block:&nbsp;
                                                    {{this.genesisBlockRecord.dataHash}}
                                                </strong>
                                            </div>
                                        </el-col>
                                    </el-row>
                                    <div v-for="(blockRecord, index) in this.allBlockRecords" style="margin: 10px 5px">
                                        <div>
                                            Block Number:&nbsp;{{blockRecord.blockNumber}}
                                        </div>
                                        <div>
                                            Tx Count:&nbsp;{{blockRecord.txCount}}
                                        </div>
                                        <div>
                                            Data:&nbsp;{{blockRecord.dataHash}}
                                        </div>
                                        <div v-if="index !== this.allBlockRecords.length - 1">
                                            --------------------------------<el-icon :size="20"><Bottom /></el-icon>--------------------------------
                                        </div>
                                        <div v-if="index === this.allBlockRecords.length - 1">
                                            &nbsp;
                                        </div>
                                    </div>
                                </div>
                            </el-scrollbar>
                    </el-card>
                </el-col>
                <el-col :span="7">
                    <el-card shadow="always" style="background-color: whitesmoke; height: 510px">
                        <el-scrollbar>
                            <div style="max-height: 470px">
                                <div style="text-align: center; font-size: large">
                                    <strong>
                                        Username: {{this.targetTraceTask.username}}
                                    </strong>
                                </div>&nbsp;
                                <div>
                                    <div style="font-size: large">
                                        <strong>Medical Supplies</strong>
                                    </div>&nbsp;
                                    <div>
                                        <strong>ID:&nbsp;</strong>
                                        {{this.targetSupplies.id}}
                                    </div>
                                    <div>
                                        <strong>Name:&nbsp;</strong>
                                        {{this.targetSupplies.name}}
                                    </div>
                                    <div>
                                        <strong>Is Prescription:&nbsp;</strong>
                                        <el-tag v-if="this.targetSupplies.isOtc === '1'" type="success">No</el-tag>
                                        <el-tag v-else type="warning">Yes</el-tag>
                                    </div>
                                    <div>
                                        <strong>Remarks:&nbsp;</strong>
                                        <span style="font-size: small">
                                            {{this.targetSupplies.remarks}}
                                        </span>
                                    </div>
                                    <div>
                                        <strong>Tx Record:&nbsp;</strong>
                                        <el-button type="success" size="small" plain
                                                   @click="viewTxRecords([this.targetSupplies.txRecord])">
                                            view
                                        </el-button>
                                    </div>
                                </div>
                                <el-divider/>
                                <div>
                                    <div style="font-size: large">
                                        <strong>Sale</strong>
                                    </div>&nbsp;
                                    <div>
                                        <strong>ID:&nbsp;</strong>
                                        {{this.targetSale.saleId}}
                                    </div>
                                    <div>
                                        <strong>Vendor:&nbsp;</strong>
                                        {{this.targetSale.providerName}}
                                    </div>
                                    <div>
                                        <strong>Supplies Record:&nbsp;</strong>
                                        <el-button type="success" size="small" plain
                                                   @click="viewSupplies(this.targetSale.suppliesUnit.supplies)">
                                            view
                                        </el-button>
                                    </div>
                                    <div>
                                        <strong>Purchase Number:&nbsp;</strong>
                                        {{this.targetSale.suppliesUnit.quantity}}
                                    </div>
                                    <div>
                                        <strong>Date:&nbsp;</strong>
                                        {{this.targetSale.purchaseDate}}
                                    </div>
                                    <div>
                                        <strong>Tx Record:&nbsp;</strong>
                                        <el-button type="success" size="small" plain
                                                   @click="viewTxRecords(this.targetSale.txRecords)">
                                            view
                                        </el-button>
                                    </div>
                                </div>
                                <el-divider/>
                                <div>
                                    <div style="font-size: large">
                                        <strong>Distribution</strong>
                                    </div>&nbsp;
                                    <div>
                                        <strong>ID:&nbsp;</strong>
                                        {{this.targetDistribution.distributionId}}
                                    </div>
                                    <div>
                                        <strong>Departure:&nbsp;</strong>
                                        {{this.targetDistribution.distributorName}}
                                    </div>
                                    <div>
                                        <strong>Arrival:&nbsp;</strong>
                                        {{this.targetDistribution.providerName}}
                                    </div>
                                    <div>
                                        <strong>Deliver Date:&nbsp;</strong>
                                        {{this.targetDistribution.deliverDate}}
                                    </div>
                                    <div>
                                        <strong>Receive Date:&nbsp;</strong>
                                        {{this.targetDistribution.receiveDate}}
                                    </div>
                                    <div>
                                        <strong>Supplies Record:&nbsp;</strong>
                                        <el-button type="success" size="small" plain
                                                   @click="viewSupplies(this.targetDistribution.suppliesUnit.supplies)">
                                            view
                                        </el-button>
                                    </div>
                                    <div>
                                        <strong>Deliver Number:&nbsp;</strong>
                                        {{this.targetDistribution.suppliesUnit.quantity}}
                                    </div>
                                    <div>
                                        <strong>Driver:&nbsp;</strong>
                                        {{this.targetDistribution.driver}}
                                    </div>
                                    <div>
                                        <strong>Contact:&nbsp;</strong>
                                        {{this.targetDistribution.contact}}
                                    </div>
                                    <div>
                                        <strong>Status:&nbsp;</strong>
                                        {{this.targetDistribution.status}}
                                    </div>
                                    <div>
                                        <strong>Tx Record:&nbsp;</strong>
                                        <el-button type="success" size="small" plain
                                                   @click="viewTxRecords(this.targetDistribution.txRecords)">
                                            view
                                        </el-button>
                                    </div>
                                </div>&nbsp;
                                <div v-if="this.targetSupplies.isOtc === '0'">
                                    <el-divider/>
                                    <div style="font-size: large">
                                        <strong>Prescription</strong>
                                    </div>&nbsp;
                                    <div>
                                        <strong>ID:&nbsp;</strong>
                                        {{this.targetPrescription.prescriptionId}}
                                    </div>
                                    <div>
                                        <strong>Release Date:&nbsp;</strong>
                                        {{this.targetPrescription.releaseDate}}
                                    </div>
                                    <div>
                                        <strong>Doctor:&nbsp;</strong>
                                        {{this.targetPrescription.doctorUsername}}
                                    </div>
                                    <div>
                                        <strong>Patient:&nbsp;</strong>
                                        {{this.targetPrescription.patientUsername}}
                                    </div>
                                    <div>
                                        <strong>Supplies Record:&nbsp;</strong>
                                        <el-button type="success" size="small" plain
                                                   @click="viewSupplies(this.targetPrescription.suppliesUnit.supplies)">
                                            view
                                        </el-button>
                                    </div>
                                    <div>
                                        <strong>Number of Prescribing:&nbsp;</strong>
                                        {{this.targetPrescription.suppliesUnit.quantity}}
                                    </div>
                                    <div>
                                        <strong>Tx Record:&nbsp;</strong>
                                        <el-button type="success" size="small" plain
                                                   @click="viewTxRecords([this.targetPrescription.txRecord])">
                                            view
                                        </el-button>
                                    </div>
                                </div>
                            </div>
                        </el-scrollbar>
                    </el-card>
                </el-col>
                <el-col :span="8">
                    <el-card shadow="always" style="background-color: whitesmoke; height: 510px">
                        <el-scrollbar>
                            <div style="max-height: 470px">
                                <div style="text-align: center">
                                    <el-radio-group v-model="selectedType">
                                        <el-radio-button label="order">
                                            Order
                                        </el-radio-button>
                                        <el-radio-button label="processes">
                                            Processes
                                        </el-radio-button>
                                        <el-radio-button label="license">
                                            License
                                        </el-radio-button>
                                    </el-radio-group>
                                </div>&nbsp;

                                <div v-if="selectedType === 'order'">
                                    <div>
                                        <div style="font-size: large">
                                            <strong>Order</strong>
                                        </div>&nbsp;
                                        <div>
                                            <strong>ID:&nbsp;</strong>
                                            {{this.targetOrder.orderId}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Request Date: </strong>
                                            {{this.targetOrder.requestDate}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Receive Date: </strong>
                                            {{this.targetOrder.receiveDate}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Distributor: </strong>
                                            {{this.targetOrder.distributorName}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Manufacturer: </strong>
                                            {{this.targetOrder.manufacturerName}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Supplies ID: </strong>
                                            {{this.targetOrder.suppliesUnit.supplies.id}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Supplies Name: </strong>
                                            {{this.targetOrder.suppliesUnit.supplies.name}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Quantity: </strong>
                                            {{this.targetOrder.suppliesUnit.quantity}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Status: </strong>
                                            {{this.targetOrder.status}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Tx Record:&nbsp;</strong>
                                            <el-button type="success" plain
                                                       size="small"
                                                       @click="viewTxRecords(this.targetOrder.txRecords)">
                                                view
                                            </el-button>
                                        </div>
                                    </div>
                                    <el-divider/>
                                    <div>
                                        <div style="font-size: large">
                                            <strong>Transfer</strong>
                                        </div>&nbsp;
                                        <div>
                                            <strong>Transfer Date:&nbsp;</strong>
                                            {{this.targetOrder.transferDate}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Carrier:&nbsp;</strong>
                                            {{this.targetOrder.carrier}}
                                        </div>&nbsp;
                                        <div>
                                            <strong>Contact:&nbsp;</strong>
                                            {{this.targetOrder.contact}}
                                        </div>
                                    </div>
                                </div>

                                <div v-if="selectedType === 'processes'">
                                    <el-timeline>
                                        <el-timeline-item v-for="(process, index) in this.targetProcesses"
                                                          :timestamp="process.recordDate"
                                                          placement="top" center>
                                            <el-card shadow="always" :body-style="{ padding: '5px'}">
                                                <div>
                                                    <strong>ID:&nbsp;</strong>
                                                    {{process.processId}}
                                                </div>
                                                <div>
                                                    <strong>Manufacturer:&nbsp;</strong>
                                                    {{process.manufacturerName}}
                                                </div>
                                                <div>
                                                    <strong>Stage:&nbsp;</strong>
                                                    {{process.status}}
                                                </div>
                                                <div>
                                                    <strong>Remarks:&nbsp;</strong>
                                                    {{process.remarks}}
                                                </div>
                                                <div>
                                                    <strong>Tx Record:&nbsp;</strong>
                                                    <el-button type="success" plain
                                                               size="small"
                                                               @click="viewTxRecords(process.txRecords)">
                                                        view
                                                    </el-button>
                                                </div>
                                            </el-card>
                                        </el-timeline-item>
                                    </el-timeline>
                                </div>

                                <div v-if="selectedType === 'license'">
                                    <div style="font-size: large">
                                        <strong>License</strong>
                                    </div>&nbsp;
                                    <div>
                                        <strong>ID:&nbsp;</strong>
                                        {{this.targetLicense.licenseId}}
                                    </div>&nbsp;
                                    <div>
                                        <strong>Regulator:&nbsp;</strong>
                                        {{this.targetLicense.regulatorName}}
                                    </div>&nbsp;
                                    <div>
                                        <strong>Manufacturer:&nbsp;</strong>
                                        {{this.targetLicense.manufacturerName}}
                                    </div>&nbsp;
                                    <div>
                                        <strong>Supplies ID:&nbsp;</strong>
                                        {{this.targetLicense.suppliesId}}
                                    </div>&nbsp;
                                    <div>
                                        <strong>Supplies Name:&nbsp;</strong>
                                        {{this.targetLicense.suppliesName}}
                                    </div>&nbsp;
                                    <div>
                                        <strong>Issue Date:&nbsp;</strong>
                                        {{this.targetLicense.issueDate}}
                                    </div>&nbsp;
                                    <div>
                                        <strong>Expiry Date:&nbsp;</strong>
                                        {{this.targetLicense.expireDate}}
                                    </div>&nbsp;
                                    <div>
                                        <strong>Tx Record:&nbsp;</strong>
                                        <el-button type="success" plain
                                                   size="small"
                                                   @click="viewTxRecords([this.targetLicense.txRecord])">
                                            view
                                        </el-button>
                                    </div>
                                </div>
                            </div>
                        </el-scrollbar>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import {ElMessageBox} from "element-plus";
import requests from "@/utils/requests";

export default {
    name: "TraceSupplies",

    data() {
        return {
            targetTraceTask: {},
            targetSupplies: {},
            targetSale: {},
            targetDistribution: {},
            targetOrder: {},
            targetProcesses: [],
            targetLicense: {},
            targetPrescription: {},

            allTraceTasks: [],
            allSales: [],
            allPrescriptions: [],

            selectedType: "",

            genesisBlockRecord: {},
            allBlockRecords: [],

            height: 0,
            channelId: "",

            salesPageFlag: true,
            traceTaskPageFlag: false,
            loadingFlag: false,
        }
    },

    created() {
        this.getAllSales();
        this.getBasicInfo();
        this.getAllBlockRecords();
        this.getAllTraceTasks();
        this.getAllPrescriptions();
        this.selectedType = "order";
    },

    computed: {
        mySales() {
            return this.allSales.filter(each => each.username === this.$store.state.user.username).reverse();
        },
        myTraceTasks() {
            return this.allTraceTasks.filter(each => each.username === this.$store.state.user.username);
        }
    },

    methods: {
        traceSupplies(sale) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/consumer/traceSupplies", sale)
                    .then(res => {
                        this.loadingFlag = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.targetTraceTask = result.data;
                            this.targetSupplies = this.targetTraceTask.supplies;
                            this.targetSale = this.targetTraceTask.sale;
                            this.targetDistribution = this.targetTraceTask.distribution;
                            this.targetOrder = this.targetTraceTask.order;
                            this.targetProcesses = this.targetTraceTask.processes;
                            this.targetLicense = this.targetTraceTask.license;

                            if (this.targetSupplies.isOtc === "0") {
                                let result = this.allPrescriptions.filter(each => each.suppliesUnit.supplies.id === this.targetSupplies.id
                                                                                    && each.patientUsername === this.$store.state.user.username);
                                if (result.length > 0) {
                                    this.targetPrescription = result[0];
                                }
                            }

                            this.refreshData();
                            this.showTraceTaskPage();
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        refreshData() {
            this.getAllTraceTasks();
        },

        showSalesPage() {
            this.salesPageFlag = true;
            this.traceTaskPageFlag = false;
        },
        showTraceTaskPage() {
            this.salesPageFlag = false;
            this.traceTaskPageFlag = true;
            this.selectedType = "order";
        },

        viewSupplies(supplies) {
            let title = "Medical Supplies"
            let content = "";
            content = content + `<div>ID: ${supplies.id}</div>`
                    + `<div>Name: ${supplies.name}</div>`
                    + `<div>Is Non-prescription: ${supplies.isOtc}</div>`
                    + `<div>Timestamp: ${supplies.txRecord.txTimestamp}</div>`;
            ElMessageBox.alert(content, title, { confirmButtonText: "OK", dangerouslyUseHTMLString: true}).catch(() => {});
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

        getAllSales() {
            requests.create.get("/blockchain/provider/getAllSales")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allSales = result.data;
                        } else {
                            this.allSales = [];
                        }
                        console.log(result.msg);
                    });
        },
        getBasicInfo() {
            requests.create.get("/blockchain/info/getBasicInfo")
                    .then(res => {
                        let result = res.data;
                        if (result.code === 1) {
                            this.height = result.height;
                            this.channelId = result.channelId;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        getAllBlockRecords() {
            requests.create.get("/blockchain/info/getAllBlockRecords")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allBlockRecords = result.data;
                            this.genesisBlockRecord = this.allBlockRecords[this.allBlockRecords.length - 1];
                        } else {
                            this.allBlockRecords = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllTraceTasks() {
            requests.create.get("/blockchain/consumer/getAllTraceTasks")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allTraceTasks = result.data;
                        } else {
                            this.allTraceTasks = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllPrescriptions() {
            requests.create.get("/blockchain/doctor/getAllPrescriptions")
                    .then( res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allPrescriptions = result.data;
                        } else {
                            this.allPrescriptions = [];
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

    .inner-frame1 {
        position: relative;
        top: 15px;
        left: 5%;
        width: 90%;
    }

    .inner-frame2 {
        position: relative;
        left: 0.5%;
        top: 2%;
        width: 99%;
    }

    .description-frame {
        margin: 0 10px;
    }

    .description-item {
        display: flex;
        align-items: center;
    }
</style>