<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-row type="flex" justify="start" align="middle">
                <el-col :span="6">
                    <el-select v-model="searchDistributorName"
                               placeholder="Select Distributor"
                               @change="searchRequestedOrdersByDistributorName">
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
                               @change="searchRequestedOrdersBySuppliesName">
                        <el-option label="All" key="0" value="all"/>
                        <el-option v-for="(supplies, index) in allSupplies"
                                   :label="supplies.name"
                                   :key="index + 1" :value="supplies.name"/>
                    </el-select>
                </el-col>
            </el-row>

            <el-row style="margin-bottom: 50px;">
                <el-col :span="7"
                        v-for="order in myRequestedOrders"
                        style="margin-top: 20px; margin-right: 30px">
                    <el-card shadow="always">
                        <template #header>
                            <div>
                                <strong>Order ID:&nbsp;</strong>
                                {{order.orderId}}
                            </div>
                        </template>
                        <div style="display: flex; flex-direction: column; justify-content: space-evenly; align-items: start">
                            <div>
                                <strong>Distributor:&nbsp;</strong>
                                {{order.distributorName}}
                            </div>
                            <div>
                                <strong>Supplies ID:&nbsp;</strong>
                                {{order.suppliesUnit.supplies.id}}
                            </div>
                            <div>
                                <strong>Supplies Name:&nbsp;</strong>
                                {{order.suppliesUnit.supplies.name}}
                            </div>
                            <div>
                                <strong>Quantity:&nbsp;</strong>
                                {{order.suppliesUnit.quantity}}
                            </div>&nbsp;
                            <div>
                                <strong>Status: </strong>
                                <el-tag v-if="order.status === 'requested'"
                                        type="success">
                                    In Request
                                </el-tag>
                            </div>&nbsp;
                            <div>
                                <strong>TxRecord:&nbsp;</strong>
                                <el-button type="success"
                                           size="small"
                                           plain
                                           @click="viewTxRecords(order.txRecords)">
                                    view
                                </el-button>
                            </div>&nbsp;
                            <div>
                                <el-popconfirm
                                        title="Confirm to accept this order?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="confirmOrder(order)">
                                    <template #reference>
                                        <el-button type="primary">
                                            Confirm
                                        </el-button>
                                    </template>
                                </el-popconfirm>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import requests from "@/utils/requests";
import {ElMessageBox} from "element-plus";

export default {
    name: "ConfirmOrders",

    data() {
        return {
            searchDistributorName: "",
            searchSuppliesName: "",

            selectedRequestedOrders: [],
            allRequestedOrders: [],
            allDistributors: [],
            allSupplies: [],
            allLicenses: [],

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    created() {
        this.getAllRequestedOrders();
        this.getAllDistributors();
        this.getAllSupplies();
        this.getAllLicenses();
    },

    computed: {
        myRequestedOrders() {
            return this.selectedRequestedOrders.filter(each => each.manufacturerId === this.$store.state.dept.id).reverse();
        },
    },

    methods: {
        confirmOrder(order) {
            let curDate = new Date();
            let license = this.allLicenses.filter(each =>
                    ((each.manufacturerId === order.manufacturerId) && (each.suppliesId) === order.suppliesUnit.supplies.id)
                        && curDate <= new Date(each.expireDate));
            if (license.length > 0) {
                this.loadingFlag = true;
                order.licenseId = license[0].licenseId;
                requests.create.post("/blockchain/manufacturer/confirmOrder", order)
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
            } else {
                this.$message.error("Don't have the license, please contact the regulatory agency.");
            }
        },
        refreshData() {
            this.getAllRequestedOrders();
            this.searchDistributorName = "";
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

        searchRequestedOrdersByDistributorName() {
            this.searchSuppliesName = "";
            if (this.searchDistributorName === "all") {
                this.selectedRequestedOrders = this.allRequestedOrders;
            } else {
                this.selectedRequestedOrders = this.allRequestedOrders.filter(
                        each => each.distributorName === this.searchDistributorName);
            }
        },
        searchRequestedOrdersBySuppliesName() {
            this.searchDistributorName = "";
            if (this.searchSuppliesName === "all") {
                this.selectedRequestedOrders = this.allRequestedOrders;
            } else {
                this.selectedRequestedOrders = this.allRequestedOrders.filter(
                        each => each.suppliesUnit.supplies.name === this.searchSuppliesName);
            }
        },

        getAllRequestedOrders() {
            requests.create.get("/blockchain/distributor/getOrdersByStatus", { params: { status: "requested" } })
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allRequestedOrders = result.data;
                            this.selectedRequestedOrders = this.allRequestedOrders;
                        } else {
                            this.selectedRequestedOrders = this.allRequestedOrders = [];
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
        getAllLicenses() {
            requests.create.get("/blockchain/regulator/getAllLicenses")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.allLicenses = result.data;
                        } else {
                            this.allLicenses = [];
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