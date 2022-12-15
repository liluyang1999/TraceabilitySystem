<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-row type="flex" justify="start" align="middle">
                <el-col :span="5">
                    <el-card shadow="always" :body-style="{ padding: '0px' }" style="height: 80px">
                        <div style="display: flex; flex-direction: row; justify-content: center; align-items: start">
                            <div style="width: 30%; height: 80px; background-color: antiquewhite;">
                                <div style="width: 80%; height: 80%; margin-left: 10%; margin-top: 20%">
                                    <HomeFilled/>
                                </div>
                            </div>
                            <div style="display: flex; flex-direction: column; justify-content: space-evenly; align-items: start;
                                        margin-left: 15px; width: 70%; height: 80px;">
                                <div style="font-size: large">
                                    <strong>Inventory</strong>
                                </div>
                                <div>
                                    <strong>Total:&nbsp;</strong>{{calculateTotal}}
                                </div>
                                <el-button :disabled="myInventory == null"
                                           type="primary" plain size="small"
                                           @click="drawerFlag = !drawerFlag">
                                    View Details
                                </el-button>
                            </div>
                        </div>
                    </el-card>
                    <el-drawer v-model="drawerFlag"
                               direction="rtl"
                               modal
                               :close-on-click-modal="false"
                               :close-on-press-escape="true">
                        <template #header>
                            <div style="text-align: center">
                                <strong style="font-size: xx-large; text-align: center">STOCK</strong>
                            </div>
                        </template>
                        <template #default>
                            <el-scrollbar>
                                <div v-for="(value, key) in myInventory.stock">
                                    <el-divider/>
                                    <div>
                                        Supplies ID:&nbsp;{{key}}
                                    </div>
                                    <div>
                                        Supplies Name:&nbsp;{{allSupplies.filter(each => each.id === key)[0].name}}
                                    </div>
                                    <div>
                                        Remaining:&nbsp;{{value}}
                                    </div>
                                </div>
                            </el-scrollbar>
                        </template>
                    </el-drawer>
                </el-col>
                <el-col :span="1"></el-col>
                <el-col :span="2">
                    <el-button type="primary"
                               @click="showDistributeProductsDialog">
                        New Distribution
                    </el-button>
                </el-col>
            </el-row>

            <div class="table-frame">
                <el-table :data="myDistributions"
                          :scrollbar-always-on="true"
                          border stripe>
                    <el-table-column prop="status" label="Status" width="110px">
                        <template #default="scope">
                            <el-tag v-if="scope.row.status === 'delivered'" type="warning">delivered</el-tag>
                            <el-tag v-if="scope.row.status === 'received'" type="success">received</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="distributionId" label="Distribution ID" width="180px"/>
                    <el-table-column prop="orderId" label="Order ID" width="180px"/>
                    <el-table-column prop="distributorName" label="Distributor Name" width="180px"/>
                    <el-table-column prop="providerName" label="Provider Name" width="180px"/>
                    <el-table-column prop="suppliesUnit.supplies.id" label="Supplies ID" width="180px"/>
                    <el-table-column prop="suppliesUnit.supplies.name" label="Supplies Name" width="180px"/>
                    <el-table-column prop="suppliesUnit.quantity" label="Quantity" width="110px"/>
                    <el-table-column prop="deliverDate" label="Deliver Date" width="180px"/>
                    <el-table-column prop="driver" label="Driver" width="110px"/>
                    <el-table-column prop="contact" label="Contact" width="110px"/>
                    <el-table-column prop="receiveDate" label="Receive Date" width="180px"/>
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
                                           @click="showUpdateDistributionDialog(scope.row)"
                                           :disabled="scope.row.status === 'received'">
                                    <el-icon><Edit/></el-icon>
                                    Edit
                                </el-button>
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="removeDistribution(scope.row)">
                                    <template #reference>
                                        <el-button type="primary" size="large" link
                                                   :disabled="scope.row.status === 'received'">
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
                       :close-on-click-modal="false">
                <el-form :model="targetDistribution" label-position="left">
                    <el-form-item prop="orderId" label="Order ID" label-width="100px">
                        <el-input v-model="targetDistribution.orderId"
                                   placeholder="Please input order id"
                                   clearable
                                   :disabled="dialog.button === 'Update'"/>
                    </el-form-item>
                    <el-form-item prop="providerName" label="Provider" label-width="100px">
                        <el-select v-model="targetDistribution.providerId"
                                   placeholder="Choose provider"
                                   :disabled="dialog.button === 'Update'">
                            <el-option v-for="(provider, index) in allProviders"
                                       :label="provider.name"
                                       :key="index" :value="provider.id"
                                       :disabled="dialog.button === 'Update'"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="suppliesUnit" label="Supplies" label-width="100px">
                        <el-select v-model="targetDistribution.suppliesUnit.supplies.id"
                                   placeholder="Choose Supplies"
                                   :disabled="dialog.button === 'Update'">
                            <el-option v-for="(supplies, index) in allSupplies"
                                       :label="supplies.name"
                                       :key="index" :value="supplies.id"
                                       :disabled="dialog.button === 'Update'"/>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="Quantity" label-width="100px">
                        <el-input-number v-model="quantity" :min="1"
                                         :disabled="dialog.button === 'Update'"/>
                    </el-form-item>
                    <el-form-item prop = "deliverDate" label="Deliver Date" label-width="100px">
                        <el-date-picker v-model="targetDistribution.deliverDate"
                                        type="datetime"
                                        value-format="YYYY-MM-DD HH:mm:ss"
                                        placeholder="Please select deliver date" clearable/>
                    </el-form-item>
                    <el-form-item prop="driver" label="Driver" label-width="100px">
                        <el-input v-model="targetDistribution.driver"
                                  placeholder="Please input driver name" clearable/>
                    </el-form-item>
                    <el-form-item prop="contact" label="Contact" label-width="100px">
                        <el-input v-model="targetDistribution.contact"
                                  placeholder="Please input contact" clearable>
                            <template #prepend>+44</template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <div style="width: 100%;text-align: center">
                            <el-popconfirm
                                    title="Confirm to upload?"
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    @confirm="handleTargetDistribution">
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
    name: "DistributeProducts",

    data() {
        return {
            targetDistribution: { distributionId: "", orderId: "", distributorId: "", distributorName: "", providerId: "", providerName: "", suppliesUnit: {supplies: {id: "", name: "", isOtc: "", remarks: "", txRecord: {txId: "", txTimestamp: ""}}, quantity: ""}, status: "", deliverDate: "", driver: "", contact: "", receiveDate: "", txRecords: [] },
            quantity: 1,

            searchDistributionId: "",
            searchOrderId: "",

            allDistributions: [],
            allSupplies: [],
            allDistributors: [],
            allProviders: [],

            total: 0,
            targetInventory: null,
            allInventories: [],

            drawerFlag: false,
            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    computed: {
        myDistributions() {
            return this.allDistributions.filter(each => each.distributorId === this.$store.state.dept.id).reverse();
        },
        myInventory() {
            this.targetInventory = this.allInventories.filter(
                    each => each.distributorId === this.$store.state.dept.id)[0];
            return this.targetInventory;
        },
        calculateTotal() {
            let result = this.allInventories.filter(each => each.distributorId === this.$store.state.dept.id);
            if (result.length > 0) {
                let stock = result[0].stock;
                let total = 0;
                for (let key in stock) {
                    total = total + +stock[key];
                }
                return total.toString();
            } else {
                return "Computing...";
            }
        },
    },

    created() {
        this.getAllDistributorsAndProviders()
        this.getAllDistributions();
        this.getAllInventories();
        this.getAllSupplies();
        this.searchDistributionId = "";
        this.searchOrderId = "";
    },

    methods: {
        handleTargetDistribution() {
            let targetDistribution = this.targetDistribution;
            if (targetDistribution.orderId.length !== 0
                    && targetDistribution.providerId.toString().length !== 0
                    && targetDistribution.suppliesUnit.supplies.id.length !== 0
                    && targetDistribution.deliverDate != null
                    && targetDistribution.driver.length !== 0
                    && targetDistribution.contact.length !== 0) {
                let length = targetDistribution.contact.length;
                if (length > 0 && !(length >= 10 && length <= 11)) {
                    this.$message.error("Length of contact should be 10 to 11");
                    return;
                }

                if (this.dialog.button === "Distribute") {
                    this.loadingFlag = true;
                    if (this.myInventory !== null) {
                        let key = this.targetDistribution.suppliesUnit.supplies.id;
                        let remaining = parseInt(this.myInventory.stock[key]);
                        let quantity = this.quantity;
                        // alert("Check remaining: " + remaining);
                        // alert("Check quantity: " + quantity)
                        if (remaining === Number.NaN || remaining < quantity) {
                            this.loadingFlag = false;
                            this.$message.error("This medical supplies is out of stock");
                            return;
                        }
                    }
                    this.targetDistribution.distributionId = null;
                    this.targetDistribution.txRecords = null;
                    this.targetDistribution.distributorId = this.$store.state.dept.id;
                    this.targetDistribution.distributorName = this.$store.state.dept.name;
                    this.targetDistribution.providerName = this.allProviders.filter(
                            each => each.id === this.targetDistribution.providerId)[0].name;

                    let supplies = this.allSupplies.filter(each => each.id === this.targetDistribution.suppliesUnit.supplies.id)[0];
                    this.targetDistribution.suppliesUnit.supplies = JSON.parse(JSON.stringify(supplies));
                    this.targetDistribution.suppliesUnit.quantity = this.quantity.toString();
                    this.distributeProducts();
                } else {
                    this.loadingFlag = true;
                    this.updateDistribution();
                }
            } else {
                this.$message.error("Input information cannot be empty");
            }
        },
        distributeProducts() {
            requests.create.post("/blockchain/distributor/distributeProducts", this.targetDistribution)
                    .then( res => {
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
        updateDistribution() {
            requests.create.post("/blockchain/distributor/updateDistribution", this.targetDistribution)
                    .then( res => {
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
        removeDistribution(distribution) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/distributor/removeDistribution", { },
                    { params: {distributionId: distribution.distributionId} })
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
            this.getAllDistributions();
            this.getAllInventories();
            this.searchDistributionId = "";
            this.searchOrderId = "";
        },

        showDistributeProductsDialog() {
            Object.keys(this.targetDistribution).forEach( key => {
                if (key !== "suppliesUnit" && key !== "txRecords") {
                    this.targetDistribution[key] = "";
                }
            });
            this.targetDistribution.suppliesUnit.supplies.id = "";
            this.quantity = 1;

            this.dialog.visible = true;
            this.dialog.title = "New Distribution";
            this.dialog.button = "Distribute";
        },
        showUpdateDistributionDialog(distribution) {
            this.targetDistribution = JSON.parse(JSON.stringify(distribution));
            this.quantity = parseInt(this.targetDistribution.suppliesUnit.quantity);

            this.dialog.visible = true;
            this.dialog.title = "Update Distribution";
            this.dialog.button = "Update";
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
        getAllInventories() {
            requests.create.get("/blockchain/distributor/getAllInventories")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allInventories = result.data;
                        } else {
                            this.allInventories = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllDistributorsAndProviders() {
            requests.create.get("/server/mgmt/getAllDepts")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allDistributors = result.data.filter(
                                    each => each.type === "distributor");
                            this.allProviders = result.data.filter(
                                    each => each.type === "provider");
                        } else {
                            this.allDistributors = [];
                            this.allProviders = [];
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
        top: 2%;
        width: 90%;
        height: 90%;
    }

    .table-frame {
        width: 100%;
        position: absolute;
        top: 90px;
        overflow: auto;
        max-height: 88%;
    }
</style>