<template>
    <div class="outer-frame"
         element-loading-text="Uploading into Blockchain..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-row type="flex" justify="start" align="middle">
                <el-col :span="5">
                    <el-select v-model="searchSuppliesName"
                               placeholder="Choose Supplies"
                               @change="searchLicensesBySuppliesName">
                        <el-option label="All" value="All"/>
                        <el-option v-for="supplies in allSupplies"
                                   :label="supplies.name"
                                   :value="supplies.name"/>
                    </el-select>
                </el-col>
                &nbsp;&nbsp;&nbsp;
                <el-col :span="5">
                    <el-select v-model="searchManufacturerName"
                               placeholder="Choose Manufacturer"
                               @change="searchLicensesByManufacturerName">
                        <el-option label="All" value="All"/>
                        <el-option v-for="manufacturer in allManufacturers"
                                   :label="manufacturer.name"
                                   :value="manufacturer.name"/>
                    </el-select>
                </el-col>
                &nbsp;&nbsp;&nbsp;
                <el-col :span="1">
                    <el-button type="primary" @click="showReleaseLicenseDialog">
                        New License
                    </el-button>
                </el-col>
            </el-row>

            <div class="table-frame">
                <el-table :data="selectedLicenses" :scrollbar-always-on="true" stripe border>
                    <el-table-column prop="txRecord.txId" label="Transaction ID" width="200px"/>
                    <el-table-column prop="licenseId" label="License ID" width="180px"/>
                    <el-table-column prop="regulatorName" label="Regulator" width="150px"/>
                    <el-table-column prop="manufacturerName" label="Manufacturer" width="180px"/>
                    <el-table-column prop="suppliesId" label="Supplies Id" width="180px"/>
                    <el-table-column prop="suppliesName" label="Supplies Name" width="180px"/>
                    <el-table-column prop="issueDate" label="Issue Date" width="180px"/>
                    <el-table-column prop="expireDate" label="Expire Date" width="180px"/>
                    <el-table-column fixed="right" label="Operation" width="100px">
                        <template #default="scope">
                            <el-popconfirm
                                    title="Confirm to cancel this license?"
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    @confirm="removeLicense(scope.row.licenseId)">
                                <template #reference>
                                    <div style="text-align: center">
                                        <el-button type="primary" link>
                                            <el-icon><Delete/></el-icon>
                                            Cancel
                                        </el-button>
                                    </div>
                                </template>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <el-dialog v-model="dialog.visible"
                       :title="dialog.title"
                       :modal="true"
                       width="500px"
                       :close-on-click-modal="false">
                <div style="margin: 0 50px">
                    <el-form :model="targetLicense" label-position="left" label-width="130px">
                        <el-form-item label="Manufacturer" prop="manufacturerName">
                            <el-select v-model="targetLicense.manufacturerName" placeholder="Choose Manufacturer">
                                <el-option v-for="manufacturer in allManufacturers"
                                           :label="manufacturer.name"
                                           :value="manufacturer.name"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Supplies" prop="suppliesName">
                            <el-select v-model="targetLicense.suppliesName" placeholder="Choose Supplies">
                                <el-option v-for="supplies in allSupplies"
                                           :label="supplies.name"
                                           :value="supplies.name"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Issue Date" prop="issueDate">
                            <el-date-picker
                                    v-model="targetLicense.issueDate"
                                    type="datetime"
                                    value-format="YYYY-MM-DD HH:mm:ss"
                                    placeholder="Issue Date"/>
                        </el-form-item>
                        <el-form-item label="Expire Date" prop="expireDate">
                            <el-date-picker
                                    v-model="targetLicense.expireDate"
                                    type="datetime"
                                    value-format="YYYY-MM-DD HH:mm:ss"
                                    placeholder="Expire Date"/>
                        </el-form-item>
                        <el-form-item>
                            <div style="text-align: center">
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="releaseLicense">
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

export default {
    name: "ManageLicenses",

    data() {
        return {
            targetLicense: { regulatorId: 0, regulatorName: "", manufacturerId: 0, manufacturerName: "", suppliesId: "", suppliesName: "", issueDate: "", expireDate: "" },
            selectedLicenses: [],
            allLicenses: [],
            allManufacturers: [],
            allSupplies: [],

            searchSuppliesName: "",
            searchManufacturerName: "",

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    created() {
        this.getAllSupplies();
        this.getAllManufacturers();
        this.getAllLicenses();
        this.searchSuppliesName = "";
        this.searchManufacturerName = "";
    },

    methods: {
        releaseLicense() {
            let targetLicense = this.targetLicense;
            if (targetLicense.manufacturerName.length !== 0
                    && targetLicense.suppliesName.length !== 0
                    && targetLicense.issueDate !== null && targetLicense.issueDate.length !== 0
                    && targetLicense.expireDate !== null && targetLicense.expireDate.length !== 0) {
                this.loadingFlag = true;
                let issueDate = new Date(targetLicense.issueDate);
                let expireDate = new Date(targetLicense.expireDate);
                if (issueDate > expireDate) {
                    this.loadingFlag = false;
                    this.$message.error("Expire date should be after issue date");
                    return;
                }

                this.targetLicense.regulatorId = this.$store.state.dept.id;
                this.targetLicense.regulatorName = this.$store.state.dept.name;

                let manufacturer = this.allManufacturers.filter(
                        each => each.name === this.targetLicense.manufacturerName)[0];
                this.targetLicense.manufacturerId = manufacturer.id;

                let supplies = this.allSupplies.filter(
                        each => each.name === this.targetLicense.suppliesName)[0];
                this.targetLicense.suppliesId = supplies.id;

                requests.create.post("/blockchain/regulator/releaseLicense", this.targetLicense)
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
        removeLicense(licenseId) {
            this.loadingFlag = true;
            requests.create.post("/blockchain/regulator/removeLicense", { },
                    {params: {licenseId: licenseId}})
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
            this.getAllLicenses();
            this.searchSuppliesName = "";
            this.searchManufacturerName = "";
        },

        searchLicensesBySuppliesName() {
            this.searchManufacturerName = "";
            if (this.searchSuppliesName === "All") {
                this.selectedLicenses = this.allLicenses;
            } else {
                this.selectedLicenses = this.allLicenses.filter(
                        license => license.suppliesName === this.searchSuppliesName);
            }
        },
        searchLicensesByManufacturerName() {
            this.searchSuppliesName = "";
            if (this.searchManufacturerName === "All") {
                this.selectedLicenses = this.allLicenses;
            } else {
                this.selectedLicenses = this.allLicenses.filter(
                        license => license.manufacturerName === this.searchManufacturerName);
            }
        },

        showReleaseLicenseDialog() {
            Object.keys(this.targetLicense).forEach(key => {this.targetLicense[key] = ""})
            this.dialog.visible = true;
            this.dialog.title = "New License";
            this.dialog.button = "Release";
        },

        getAllLicenses() {
            requests.create.get("/blockchain/regulator/getAllLicenses")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null)  {
                            this.allLicenses = result.data;
                            this.selectedLicenses = this.allLicenses;
                        } else {
                            this.selectedLicenses = this.allLicenses = [];
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
        getAllManufacturers() {
            requests.create.get("/server/mgmt/getAllDepts")
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
        position: absolute;
        top: 10%;
        width: 100%;
        max-height: 85%;
        overflow: auto;
    }
</style>