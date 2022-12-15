<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-row type="flex" justify="start" align="middle">
                <el-col :span="5">
                    <el-select v-model="searchManufacturerName"
                               @change="searchOrdersByManufacturerName"
                               placeholder="Choose Manufacturer">
                        <el-option label="All" key="0" value="all"/>
                        <el-option v-for="(manufacturer, index) in allManufacturers"
                                   :label="manufacturer.name"
                                   :key="index + 1" :value="manufacturer.name"/>
                    </el-select>
                </el-col>
                &nbsp;&nbsp;
                <el-col :span="5">
                    <el-select v-model="searchSuppliesName"
                               @change="searchOrdersBySuppliesName"
                               placeholder="Choose Supplies">
                        <el-option label="All" key="0" value="all"/>
                        <el-option v-for="(supplies, index) in allSupplies"
                                   :label="supplies.name"
                                   :key="index + 1" :value="supplies.name"/>
                    </el-select>
                </el-col>
                &nbsp;&nbsp;
                <el-col :span="4">
                    <el-select v-model="searchStatus"
                               @change="searchOrdersByStatus"
                               placeholder="Status">
                        <el-option label="All" key="0" value="all"/>
                        <el-option label="Requested" key="1" value="requested"/>
                        <el-option label="Confirmed" key="2" value="confirmed"/>
                        <el-option label="Transferred" key="3" value="transferred"/>
                        <el-option label="Received" key="4" value="received"/>
                    </el-select>
                </el-col>
                &nbsp;&nbsp;&nbsp;
                <el-col :span="2">
                    <el-button type="primary"
                               @click="showSendOrderDialog">
                        New Order
                    </el-button>
                </el-col>
            </el-row>

            <div class="table-frame">
                <el-table :data="myOrders"
                          :scrollbar-always-on="true"
                          border stripe >
                    <el-table-column label="Receive Date" width="180px">
                        <template #default="scope">
                            <span v-if="scope.row.status === 'requested'">
                                Waiting for confirmation
                            </span>
                            <span v-if="scope.row.status === 'confirmed'">
                                In Manufacturing
                            </span>
                            <span v-if="scope.row.status === 'transferred'">
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="receiveProducts(scope.row)">
                                    <template #reference>
                                        <el-button type="primary">Receive</el-button>
                                    </template>
                                </el-popconfirm>
                            </span>
                            <span v-if="scope.row.status === 'received'">
                                {{scope.row.receiveDate}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="Status" width="110px">
                        <template #default="scope">
                            <el-tag v-if="scope.row.status !== 'received'" type="warning">
                                {{scope.row.status}}
                            </el-tag>
                            <el-tag v-if="scope.row.status === 'received'" type="success">
                                {{scope.row.status}}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="orderId" label="Order ID" width="180px"/>
                    <el-table-column prop="requestDate" label="Request Date" width="180px"/>
                    <el-table-column prop="distributorName" label="Distributor Name" width="180px"/>
                    <el-table-column prop="manufacturerName" label="Manufacturer Name" width="180px"/>
                    <el-table-column label="Supplies Unit" width="150px">
                        <template #default="scope">
                            <el-button type="success" plain
                                        @click="viewSuppliesUnit(scope.row.suppliesUnit)">
                                view
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="licenseId" label="License ID" width="200px"/>
                    <el-table-column prop="transferDate" label="Transfer Date" width="180px"/>
                    <el-table-column prop="carrier" label="Carrier" width="110px"/>
                    <el-table-column prop="contact" label="Contact" width="110px"/>
                    <el-table-column label="Tx Records" width="110px">
                        <template #default="scope">
                            <el-button type="success" plain
                                       @click="viewTxRecords(scope.row.txRecords)">
                                view
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" label="Operation" width="160px">
                        <template #default="scope">
                            <div style="text-align: center">
                                <el-button type="primary" link size="large"
                                           @click="showUpdateOrderDialog(scope.row)"
                                           :disabled="scope.row.status !== 'requested'">
                                    <el-icon><Edit/></el-icon>
                                    Edit
                                </el-button>
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="removeOrder(scope.row)">
                                    <template #reference>
                                        <el-button type="primary" link size="large"
                                                   :disabled="scope.row.status !== 'requested'">
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
                       width="400px">
                <el-form :model="targetOrder" label-position="left">
                        <el-form-item label="Manufacturer" label-width="100px" prop="deptId">
                            <el-select v-model="targetOrder.manufacturerId" placeholder="Choose Manufacturer"
                                       :disabled="dialog.button === 'Update'">
                                <el-option v-for="(manufacturer, index) in allManufacturers"
                                           :disabled="dialog.button === 'Update'"
                                           :label="manufacturer.name"
                                           :key="index" :value="manufacturer.id"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Supplies" label-width="100px">
                            <el-select v-model="targetOrder.suppliesUnit.supplies.id" placeholder="Choose Supplies"
                                       :disabled="dialog.button === 'Update'">
                                <el-option v-for="(supplies, index) in allSupplies"
                                           :disabled="dialog.button === 'Update'"
                                           :label="supplies.name"
                                           :key="index" :value="supplies.id"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Quantity" label-width="100px">
                            <el-input-number v-model="quantity" :min="1"/>
                        </el-form-item>
                        <el-form-item>
                            <div style="width: 100%; text-align: center">
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="handleTargetOrder">
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
import router from "@/router";

export default {
    name: "ManageOrders",

    data() {
        return {
            targetOrder: { orderId: "", requestDate: "", distributorId: "", distributorName: "",
                            manufacturerId: "", manufacturerName: "",
                            suppliesUnit: { supplies: {id: "", name: "", isOtc: "", remarks: "", txRecord: {txId: "", txTimestamp: ""} }, quantity: ""},
                            status: "", licenseId: "", transferDate: "", carrier: "", contact: "", receiveDate: "", txRecords: [],
            },
            quantity: 1,

            searchStatus: "",
            selectedOrders: [],
            allOrders: [],

            searchManufacturerName: "",
            allManufacturers: [],

            searchSuppliesName: "",
            allSupplies: [],

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    created() {
        this.getAllManufacturers();
        this.getAllSupplies();
        this.getAllOrders();
    },

    computed: {
        myOrders() {
            return this.selectedOrders = this.selectedOrders.filter(each => each.distributorId === this.$store.state.dept.id).reverse();
        },
    },

    methods: {
        handleTargetOrder() {
            if (this.dialog.button === "Send")  {
                if (this.targetOrder.manufacturerId.toString().length !== 0
                        && this.targetOrder.suppliesUnit.supplies.id.length !== 0) {
                    this.loadingFlag = true;
                    this.targetOrder.orderId = null;
                    this.targetOrder.txRecords = null;
                    this.targetOrder.distributorId = this.$store.state.dept.id;
                    this.targetOrder.distributorName = this.$store.state.dept.name;

                    this.targetOrder.manufacturerName = this.allManufacturers.filter(each => each.id === this.targetOrder.manufacturerId)[0].name;
                    let supplies = this.allSupplies.filter(each => each.id === this.targetOrder.suppliesUnit.supplies.id)[0];
                    this.targetOrder.suppliesUnit.supplies = JSON.parse(JSON.stringify(supplies));
                    this.targetOrder.suppliesUnit.quantity = this.quantity.toString();
                    this.sendOrder();
                } else {
                    this.$message.error("Please choose the manufacturer and supplies");
                }
            } else {
                this.loadingFlag = true;
                this.targetOrder.suppliesUnit.quantity = this.quantity.toString();
                this.updateOrder();
            }
        },
        sendOrder() {
            requests.create.post("/blockchain/distributor/sendOrder", this.targetOrder)
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
        updateOrder() {
            requests.create.post("/blockchain/distributor/updateOrder", this.targetOrder)
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
        removeOrder(order) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/distributor/removeOrder", { },
                    { params: {orderId: order.orderId} })
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
        receiveProducts(order) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/distributor/receiveProducts", order)
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
            this.getAllOrders();
            this.searchManufacturerName = "";
            this.searchSuppliesName = "";
            this.searchStatus = "";
        },

        showSendOrderDialog() {
            Object.keys(this.targetOrder).forEach( key => {
                if (key !== "suppliesUnit" && key !== "txRecords") {
                    this.targetOrder[key] = ""
                }
            });
            this.targetOrder.suppliesUnit.supplies.id = "";
            this.quantity = 1;

            this.dialog.visible = true;
            this.dialog.title = "Send Order";
            this.dialog.button = "Send";
        },
        showUpdateOrderDialog(order) {
            this.targetOrder = JSON.parse(JSON.stringify(order));
            this.quantity = parseInt(this.targetOrder.suppliesUnit.quantity);
            this.dialog.visible = true;
            this.dialog.title = "Update Order";
            this.dialog.button = "Update";
        },

        searchOrdersByManufacturerName() {
            this.searchSuppliesName = "";
            this.searchStatus = "";
            if (this.searchManufacturerName === "all") {
                this.selectedOrders = this.allOrders;
            } else {
                this.selectedOrders = this.allOrders.filter(
                        each => each.manufacturerName === this.searchManufacturerName);
            }
        },
        searchOrdersBySuppliesName() {
            this.searchManufacturerName = "";
            this.searchStatus = "";
            if (this.searchSuppliesName === "all") {
                this.selectedOrders = this.allOrders;
            } else {
                this.selectedOrders = this.allOrders.filter(
                        each => each.suppliesUnit.supplies.name === this.searchSuppliesName);
            }
        },
        searchOrdersByStatus() {
            this.searchManufacturerName = "";
            this.searchSuppliesName = "";
            if (this.searchStatus === "all") {
                this.selectedOrders = this.allOrders;
            } else {
                this.selectedOrders = this.allOrders.filter(each => each.status === this.searchStatus);
            }
        },

        viewSuppliesUnit(suppliesUnit) {
            let title = "Supplies Information"
            let content = "";
            content = content + `<div>ID: ${suppliesUnit.supplies.id}</div>`
                        + `<div>Name: ${suppliesUnit.supplies.name}</div>`
                        + `<div>Quantity: ${suppliesUnit.quantity}</div>`;
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

        getAllOrders() {
            requests.create.post("/blockchain/distributor/getAllOrders", { },
                    {params: {distributorId: this.$store.state.dept.id}})
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allOrders = result.data;
                            this.selectedOrders = this.allOrders;
                        } else {
                            this.selectedOrders = this.allOrders = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllManufacturers() {
            requests.create.post("/server/mgmt/getAllDepts")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allManufacturers = result.data.filter(each => each.type === "manufacturer");
                        } else {
                            this.allManufacturers = [];
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
        top: 3%;
        width: 90%;
        height: 90%;
    }

    .table-frame {
        width: 100%;
        position: absolute;
        top: 45px;
        max-height: 95%;
        overflow: auto;
    }
</style>