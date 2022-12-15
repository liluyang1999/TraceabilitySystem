<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <div style="width: 100%; text-align: center">
                <el-button style="width: 50%"
                           type="primary"
                           @click="showSellProductsDialog"
                           plain>
                    New Sale
                </el-button>
            </div>
            <div class="table-frame">
                <el-table :data="mySales"
                      :scrollbar-always-on="true"
                      border stripe>
                <el-table-column prop="saleId" label="Sale ID" width="180px"/>
                <el-table-column prop="username" label="Consumer Username" width="180px"/>
                <el-table-column prop="suppliesUnit.supplies.id" label="Supplies ID" width="180px"/>
                <el-table-column prop="suppliesUnit.supplies.name" label="Supplies Name" width="180px"/>
                <el-table-column prop="suppliesUnit.quantity" label="Quantity" width="110px"/>
                <el-table-column prop="providerName" label="Provider" width="150px"/>
                <el-table-column prop="purchaseDate" label="Purchase Date" width="180px"/>
                <el-table-column prop="distributionId" label="Distribution ID" width="180px"/>
                <el-table-column prop="orderId" label="Order ID" width="180px"/>
                <el-table-column label="TxRecords" width="150px">
                    <template #default="scope">
                        <el-button type="success" @click="viewTxRecords(scope.row.txRecords)" plain>
                            view
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column label="Operation" fixed="right" width="160px">
                    <template #default="scope">
                        <div style="text-align: center">
                            <el-button type="primary" size="large" link
                                       @click="showUpdateSaleDialog(scope.row)">
                                <el-icon><Edit /></el-icon>
                                Edit
                            </el-button>
                            <el-popconfirm
                                    title="Confirm to upload?"
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    @confirm="removeSale(scope.row)">
                                <template #reference>
                                    <el-button type="primary" size="large" link
                                               :disabled="myTraceTasks.filter(each => each.sale.saleId === scope.row.saleId).length > 0">
                                        <el-icon><Delete/></el-icon>
                                        Remove
                                    </el-button>
                                </template>
                            </el-popconfirm>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            </div>
            <el-dialog v-model="dialog.visible"
                       :title="dialog.title"
                       :modal="true"
                       :close-on-click-modal="false"
                       width="500px">
                <el-form :model="targetSale" label-position="left">
                    <el-form-item prop="username" label="Consumer" label-width="110px">
                        <el-select v-model="targetSale.username"
                                   placeholder="Choose consumer"
                                   :disabled="dialog.button === 'Update'">
                            <el-option v-for="(consumer, index) in allConsumers"
                                       :disabled="dialog.button === 'Update'"
                                       :label="consumer.username"
                                       :key="index" :value="consumer.username"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="suppliesUnit" label="Supplies" label-width="110px">
                        <el-select v-model="targetSale.suppliesUnit.supplies.id"
                                   placeholder="Choose Supplies"
                                   :disabled="dialog.button === 'Update'">
                            <el-option v-for="(supplies, index) in allSupplies"
                                       :label="supplies.name"
                                       :key="index" :value="supplies.id"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="Quantity" label-width="110px">
                        <el-input-number v-model="quantity" :min="1"/>
                    </el-form-item>
                    <el-form-item prop = "purchaseDate" label="Purchase Date" label-width="110px">
                        <el-date-picker v-model="targetSale.purchaseDate"
                                        type="datetime"
                                        value-format="YYYY-MM-DD HH:mm:ss"
                                        placeholder="Please select purchase date"/>
                    </el-form-item>
                    <el-form-item prop="distributionId" label="Distribution ID" label-width="110px">
                        <el-input v-model="targetSale.distributionId"
                                  placeholder="Please input distribution id"
                                  :disabled="dialog.button === 'Update'" clearable/>
                    </el-form-item>
                    <el-form-item>
                        <div style="width: 100%; text-align: center">
                            <el-popconfirm
                                    title="Confirm to upload?"
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    @confirm="handleTargetSale">
                                <template #reference>
                                    <el-button type="primary">{{dialog.button}}</el-button>
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
    name: "SellProducts",
    data() {
        return {
            targetSale: { saleId: "", username: "", suppliesUnit: {supplies: {id: "", name: "", isOtc: "", remarks: "", txRecord: {txId: "", txTimestamp: ""}}, quantity: ""}, providerId: "", providerName: "", purchaseDate: "", distributionId: "", orderId: "", txRecords: [] },
            quantity: 0,

            allSales: [],
            allDistributions: [],
            allOrders: [],
            allSupplies: [],
            allConsumers: [],
            allPrescriptions: [],
            allTraceTasks: [],

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    created() {
        this.getAllSales();
        this.getAllDistributions();
        this.getAllOrders();
        this.getAllSupplies();
        this.getAllConsumers();
        this.getAllPrescriptions();
        this.getAllTraceTasks();
    },

    computed: {
        mySales() {
            return this.allSales.filter(each => each.providerId === this.$store.state.dept.id).reverse();
        },
        myTraceTasks() {
            return this.allTraceTasks;
        },
    },

    methods: {
        handleTargetSale() {
            let targetSale = this.targetSale;
            if (targetSale.username.length !== 0
                    && targetSale.suppliesUnit.supplies.id.length !== 0
                    && targetSale.distributionId.length !== 0
                    && targetSale.purchaseDate !== null && targetSale.purchaseDate.length !== 0) {

                let supplies = this.allSupplies.filter(
                        each => each.id === targetSale.suppliesUnit.supplies.id)[0];

                if (supplies.isOtc === "0") {
                    let prescription = this.allPrescriptions.filter(
                            each => each.patientUsername === targetSale.username
                                    && each.suppliesUnit.supplies.id === supplies.id);

                    if (prescription.length === 0) {
                        this.loadingFlag = false;
                        this.$message.error("This is prescription medical supplies, but no prescription can be found");
                        return;
                    }

                    let limitNum = parseInt(prescription[0].suppliesUnit.quantity);
                    let purchaseNum = this.quantity;
                    if (limitNum < purchaseNum) {
                        this.loadingFlag = false;
                        this.$message.error("The number of purchased medical supplies exceeds the prescription limit, maximum: " + limitNum);
                        return;
                    }
                }

                if (this.dialog.button === "Sell") {
                    this.loadingFlag = true;

                    this.targetSale.suppliesUnit.supplies = JSON.parse(JSON.stringify(supplies));
                    this.targetSale.suppliesUnit.quantity = this.quantity.toString();

                    let distribution = this.allDistributions.filter(
                            each => each.distributionId === this.targetSale.distributionId && each.status === "received");

                    if (distribution.length > 0) {
                        if (distribution[0].suppliesUnit.supplies.id !== this.targetSale.suppliesUnit.supplies.id
                            || parseInt(distribution[0].suppliesUnit.quantity) < parseInt(this.targetSale.suppliesUnit.quantity)) {
                            // alert(parseInt(distribution[0].suppliesUnit.quantity))
                            // alert(parseInt(this.targetSale.suppliesUnit.quantity))
                            this.loadingFlag = false;
                            this.$message.error("Purchased supplies do not match the distributed ones");
                            return;
                        }

                        this.targetSale.saleId = null;
                        this.targetSale.txRecords = null;
                        this.targetSale.providerId = this.$store.state.dept.id;
                        this.targetSale.providerName = this.$store.state.dept.name;
                        this.targetSale.orderId = this.allDistributions.filter(
                                each => each.distributionId === this.targetSale.distributionId)[0].orderId;
                        this.sellProducts();
                    } else {
                        this.loadingFlag = false;
                        this.$message.error("No distribution can be found or it has not been received yet");
                    }
                } else {
                    this.loadingFlag = true;
                    this.targetSale.suppliesUnit.quantity = this.quantity.toString();
                    this.updateSale();
                }
            } else {
                this.$message.error("Input information cannot be empty");
            }
        },
        sellProducts() {
            requests.create.post("/blockchain/provider/sellProducts", this.targetSale)
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
        updateSale() {
            requests.create.post("/blockchain/provider/updateSale", this.targetSale)
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
        removeSale(sale) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/provider/removeSale", { },
                    { params: {saleId: sale.saleId} })
                    .then(res => {
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
            this.getAllSales();
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

        showSellProductsDialog() {
            Object.keys(this.targetSale).forEach( key => {
                if (key !== "suppliesUnit" && key !== "txRecords") {
                    this.targetSale[key] = "";
                }
            });
            this.targetSale.suppliesUnit.supplies.id = "";
            this.quantity = 1;
            this.dialog.visible = true;
            this.dialog.title = "Sell Products";
            this.dialog.button = "Sell";
        },
        showUpdateSaleDialog(sale) {
            this.targetSale = JSON.parse(JSON.stringify(sale));
            this.quantity = parseInt(this.targetSale.suppliesUnit.quantity);
            this.dialog.visible = true;
            this.dialog.title = "Update Sale";
            this.dialog.button = "Update";
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
        getAllDistributions() {
            requests.create.get("/blockchain/distributor/getAllDistributions")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allDistributions = result.data;
                        } else {
                            this.allDistributions = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllOrders() {
            requests.create.get("/blockchain/distributor/getAllOrders")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allOrders = result.data;
                        } else {
                            this.allOrders = [];
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
        getAllPrescriptions() {
            requests.create.get("/blockchain/doctor/getAllPrescriptions")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allPrescriptions = result.data;
                        } else {
                            this.allPrescriptions = [];
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
    },
}
</script>

<style scoped>
    .outer-frame {
        width: 100%;
        height: 100%;
        background-color: floralwhite;
    }

    .inner-frame {
        position: relative;
        left: 5%;
        top: 3%;
        width: 90%;
        height: 90%;
    }

    .table-frame {
        width: 100%;
        position: absolute;
        top: 10%;
        overflow: auto;
        max-height: 85%;
    }
</style>