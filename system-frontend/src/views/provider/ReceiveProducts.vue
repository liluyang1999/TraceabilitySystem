<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-table :data="myDistributions" :scrollbar-always-on="true" border stripe>
                    <el-table-column label="Receive Date" width="180px">
                        <template #default="scope">
                            <span v-if="scope.row.status === 'delivered'">
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
                            <div>
                                <el-tag v-if="scope.row.status === 'delivered'" type="warning">
                                    {{scope.row.status}}
                                </el-tag>
                                <el-tag v-if="scope.row.status === 'received'" type="success">
                                    {{scope.row.status}}
                                </el-tag>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="distributionId" label="Distribution ID" width="180px"/>
                    <el-table-column prop="orderId" label="Order ID" width="180px"/>
                    <el-table-column prop="distributorName" label="Distributor Name" width="180px"/>
                    <el-table-column prop="providerName" label="Provider Name" width="180px"/>
                    <el-table-column prop="suppliesUnit.supplies.id" label="Supplies ID" width="180px"/>
                    <el-table-column prop="suppliesUnit.supplies.name" label="Supplies Name" width="180px"/>
                    <el-table-column prop="suppliesUnit.quantity" label="Quantity" width="110px"/>
                    <el-table-column prop="deliverDate" label="Deliver Date" width="200px"/>
                    <el-table-column prop="driver" label="Driver" width="110px"/>
                    <el-table-column prop="contact" label="Contact" width="110px"/>
                    <el-table-column label="Tx Records" width="110px">
                        <template #default="scope">
                            <div>
                                <el-button type="success" plain
                                           @click="viewTxRecords(scope.row.txRecords)">
                                    view
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
import requests from "@/utils/requests";
import {ElMessageBox} from "element-plus";

export default {
    name: "ReceiveProducts",

    data() {
        return {
            targetDistribution: { distributionId: "", orderId: "", distributorId: "", distributorName: "", providerId: "", providerName: "", suppliesUnit: {supplies: {id: "", name: "", isOtc: "", remarks: "", txRecord: {txId: "", txTimestamp: ""}}, quantity: ""}, status: "", deliverDate: "", driver: "", contact: "", receiveDate: "", txRecords: [] },
            allDistributions: [],
            loadingFlag: false,
        }
    },

    created() {
        this.getAllDistributions();
    },

    computed: {
        myDistributions() {
            return this.allDistributions.filter(each => each.providerId === this.$store.state.dept.id).reverse();
        }
    },

    methods: {
        receiveProducts(distribution) {
            this.loadingFlag = true;
            this.targetDistribution = JSON.parse(JSON.stringify(distribution));
            requests.create.post("/blockchain/provider/receiveProducts", this.targetDistribution)
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
            this.getAllDistributions();
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
        height: 90%;
        overflow: auto;
    }
</style>
