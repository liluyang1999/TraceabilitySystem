<template>
    <div class="main-frame"
         element-loading-text="Uploading..."
         v-loading.fullscreen.lock = "loadingFlag">
        <el-scrollbar>
            <el-row style="display:flex; justify-content: left; align-items: center; margin-top: 15px">
                <el-col :span="1"></el-col>
                <el-col :span="4">
                    <el-input v-model="searchName"
                              placeholder="Name"
                              prefix-icon="OfficeBuilding"
                              @input="searchDeptsByName"
                              clearable/>
                </el-col>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <el-col :span="3">
                    <el-select v-model="searchType"
                               placeholder="Type"
                               @change="searchDeptsByType">
                        <el-option label="All" value="all"/>
                        <el-option v-for="role in roles"
                                    :label="role.name"
                                    :value="role.name"/>
                    </el-select>
                </el-col>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <el-col :span="2">
                    <el-button type="primary" icon="DocumentAdd" @click="showRegisterDeptDialog">
                        New Department
                    </el-button>
                </el-col>
            </el-row>

            <el-row>
                <el-col :span="1"></el-col>
                <el-col :span="22">
                    <el-row >
                    <el-col v-for="dept in selectedDepts" :span="7" style="margin-top: 20px; margin-right: 30px">
                        <el-card  shadow="always">
                            <template #header>
                                <div style="display: flex; justify-content: space-between; align-items: center">
                                    <strong>{{dept.name}}</strong>
                                    <el-icon style="cursor: pointer"
                                             @click="showUpdateDeptDialog(dept)">
                                        <Edit/>
                                    </el-icon>
                                </div>
                            </template>
                            <div style="display: flex; flex-direction: column; justify-content: center; align-items: start">
                                <span>
                                    <strong>Id:</strong> {{dept.id}}
                                </span>
                                <span>
                                    <strong>Type:</strong> {{dept.type}}
                                </span>
                                <span>
                                    <strong>Leader:</strong> {{dept.leader}}
                                </span>
                                <span>
                                    <strong>Email:</strong> {{dept.email}}
                                </span>
                                <span>
                                    <strong>Address:</strong> {{dept.address}}
                                </span>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                </el-col>
                <el-col :span="1"></el-col>
            </el-row>

            <el-dialog v-model="dialog.visible"
               :title="dialog.title" :modal="true"
               :close-on-click-modal="false">
                <div style="margin: 0 50px">
                    <el-form :model="targetDept" label-position="left">
                        <el-form-item label="Name" label-width="80px" prop="name">
                            <el-input prefix-icon="EditPen" v-model="targetDept.name" placeholder="department name" clearable/>
                        </el-form-item>
                        <el-form-item label="Type" label-width="80px" prop="type">
                            <el-select v-model="targetDept.type" placeholder="Department Type"
                                       :disabled="dialog.button === 'Update'">
                                <el-option v-for="role in roles"
                                           :label="role.name"
                                           :key="role.id"
                                           :value="role.name"
                                           :disabled="dialog.button === 'Update'"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Leader" label-width="80px" prop="leader">
                            <el-input prefix-icon="EditPen" v-model="targetDept.leader" placeholder="leader name" clearable/>
                        </el-form-item>
                        <el-form-item label="Email" label-width="80px" prop="email">
                            <el-input prefix-icon="EditPen" v-model="targetDept.email" placeholder="department email" clearable/>
                        </el-form-item>
                        <el-form-item label="Address" label-width="80px" prop="address">
                            <el-input prefix-icon="EditPen" v-model="targetDept.address" placeholder="department address" clearable/>
                        </el-form-item>
                        <el-form-item>
                            <div style="width: 100%; text-align: center">
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="handleTargetDept">
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
        </el-scrollbar>
    </div>
</template>

<script>
import requests from "@/utils/requests";

export default {
    name: "RegisterDepartment",

    data() {
        return {
            searchName: "",
            searchType: "",

            targetDept: { id: "", name: "", type: "", leader: "", email: "", address: "", creator: "", updater: "" },
            selectedDepts: [],
            depts: [],
            roles: [],

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    created() {
        this.getAllRoles();
        this.getAllDepts();
    },

    methods: {
        handleTargetDept() {
            let targetDept = this.targetDept;
            if (targetDept.name.length !== 0 && targetDept.type.length !== 0
                    && targetDept.leader.length !== 0 && targetDept.email.length !== 0
                    && targetDept.address.length !== 0) {
                this.loadingFlag = true;
                if (this.dialog.button === "Register") {
                    this.targetDept.creator = this.$store.state.user.username;
                    this.registerDept();
                } else if (this.dialog.button === "Update") {
                    this.targetDept.updater = this.$store.state.user.username;
                    this.updateDept();
                }
            } else {
                this.$message.error("The input information can't be empty");
            }
        },
        registerDept() {
            requests.create.post("/server/mgmt/registerDept", this.targetDept)
                    .then(res => {
                        this.loadingFlag = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.refreshData();
                            this.dialog.visible = false;
                            this.$message.success(result.msg);
                        } else if (result.code ===2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        updateDept() {
            requests.create.post("/server/mgmt/updateDept", this.targetDept)
                    .then(res => {
                        this.loadingFlag = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.refreshData();
                            this.dialog.visible = false;
                            this.$message.success(result.msg);
                        } else if (result.code ===2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        refreshData() {
            this.getAllDepts();
            this.searchName = "";
            this.searchType = "";
        },

        searchDeptsByName() {
            this.searchType = "";
            if (this.searchName === "") {
                this.selectedDepts = this.depts;
            } else {
                this.selectedDepts = this.depts.filter(dept => dept.name.toLowerCase().search(this.searchName.toLowerCase()) !== -1);
            }
        },
        searchDeptsByType() {
            this.searchName = "";
            if (this.searchType === "all") {
                this.selectedDepts = this.depts;
            } else {
                this.selectedDepts = this.depts.filter(dept => dept.type === this.searchType);
            }
        },

        showRegisterDeptDialog() {
            Object.keys(this.targetDept).forEach(key => {this.targetDept[key] = ""})
            this.dialog.visible = true;
            this.dialog.title = "Register New Department";
            this.dialog.button = "Register";
        },
        showUpdateDeptDialog(dept) {
            this.targetDept = JSON.parse(JSON.stringify(dept));
            this.dialog.visible = true;
            this.dialog.title = "Update Department";
            this.dialog.button = "Update";
        },

        getAllRoles() {
            requests.create.get("/server/mgmt/getAllRoles")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.roles = result.data.filter(each => each.name !== "consumer" && each.name !== "doctor");
                        } else {
                            this.roles = [];
                        }
                        console.log(result.msg);
                    });
        },
        getAllDepts() {
            requests.create.get("/server/mgmt/getAllDepts")
                    .then(res => {
                        let result = res.data;
                        if (result.data !== null) {
                            this.depts = result.data;
                            this.selectedDepts = this.depts;
                        } else {
                            this.selectedDepts = this.depts = [];
                        }
                        console.log(result.msg);
                    });
        },
    }
}
</script>

<style scoped>
    .main-frame {
        width: 100%;
        height: 100%;
        background-color: ghostwhite;
        overflow: auto;
    }
</style>

