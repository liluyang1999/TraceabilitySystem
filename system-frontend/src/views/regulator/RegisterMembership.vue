<template>
    <div class="outer-frame"
         element-loading-text="Uploading..."
         v-loading.fullscreen.lock = "loadingFlag">
        <div class="inner-frame">
            <el-row type="flex" justify="space-around" align="middle">
                <el-col :span="5">
                    <el-input v-model="searchUsername"
                              placeholder="Username"
                              prefix-icon="UserFilled"
                              @input="searchUsersByUsername"
                              clearable/>
                </el-col>
                &nbsp;&nbsp;&nbsp;
                <el-col :span="15">
                    <el-radio-group v-model="searchDeptType" @change="searchUsersByDeptType">
                        <el-radio-button label="All"/>
                        <el-radio-button v-for="role in roles" :label="role.name"/>
                    </el-radio-group>
                </el-col>
                <el-col :span="3">
                    <el-button type="primary" @click="showRegisterMembershipDialog">
                        New Membership
                    </el-button>
                </el-col>
            </el-row>

            <div class="table-frame">
                <el-table :data="selectedUsers"
                          :scrollbar-always-on="true"
                          border stripe>
                    <el-table-column prop="id" label="User ID" width="80px"/>
                    <el-table-column prop="username" label="Username" width="150px"/>
                    <el-table-column label="Status" width="110px">
                        <template #default="scope">
                            <el-tag type="success" v-if="scope.row.status === 0">
                                Enabled
                            </el-tag>
                            <el-tag type="warning" v-if="scope.row.status === 1">
                                Disabled
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="Department" width="130px">
                        <template #default="scope">
                            <el-tag v-if="scope.row.deptId === -1">consumer</el-tag>
                            <el-tag v-else-if="scope.row.deptId === -2">doctor</el-tag>
                            <el-button v-else type="primary" plain size="small"
                                       @click="viewDepartment(scope.row.deptId)">
                                view
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="displayName" label="Nickname" width="110px"/>
                    <el-table-column prop="email" label="Email" width="200px"/>
                    <el-table-column prop="phone" label="Phone" width="120px"/>
                    <el-table-column prop="createTime" label="Create Time" width="180px"/>
                    <el-table-column prop="creator" label="Creator" width="120px"/>
                    <el-table-column prop="updateTime" label="Update Time" width="180px"/>
                    <el-table-column prop="updater" label="Updater" width="120px"/>
                    <el-table-column fixed="right" label="Operation" width="110px">
                        <template #default="scope">
                            <div style="text-align: center">
                                <el-button type="primary" size="large" link
                                           @click="showUpdateMembershipDialog(scope.row)"
                                           :disabled="scope.row.id === this.$store.state.user.id">
                                    <el-icon><Edit /></el-icon>
                                    Edit
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <el-dialog v-model="dialog.visible"
                           :title="dialog.title"
                           :modal="true"
                           :close-on-click-modal="false">
                <div style="margin: 0 50px">
                    <el-form :model="targetUser" label-position="left">
                        <el-form-item label="Department" label-width="100px" prop="deptId">
                            <el-select v-model="targetUser.deptId" placeholder="Choose"
                                       :disabled="dialog.button === 'Update'"
                                       @change="checkDeptType">
                                <el-option v-for="(dept, index) in depts"
                                           :disabled="dialog.button === 'Update'"
                                           :label="dept.name"
                                           :key="index" :value="dept.id"/>
                                <el-option :disabled="dialog.button === 'Update'"
                                           label="Consumer"
                                           :key="depts.length" :value="-1"/>
                                <el-option :disabled="dialog.button === 'Update'"
                                           label="Doctor"
                                           :key="depts.length + 1" :value="-2"/>
                            </el-select>
                            &nbsp;&nbsp;&nbsp;
                            <span>Type: {{this.deptType}}</span>
                        </el-form-item>
                        <el-form-item label="Username" label-width="100px" prop="name">
                            <el-input prefix-icon="EditPen" v-model="targetUser.username"
                                      :disabled="dialog.button === 'Update'"
                                      placeholder="Username" clearable/>
                        </el-form-item>
                        <el-form-item label="Password" label-width="100px" prop="password">
                            <el-input prefix-icon="EditPen" v-model="targetUser.password"
                                      placeholder="Password" type="password" clearable show-password/>
                        </el-form-item>
                        <el-form-item label="Nickname" label-width="100px" prop="displayName">
                            <el-input prefix-icon="EditPen" v-model="targetUser.displayName"
                                      placeholder="Nickname" clearable/>
                        </el-form-item>
                        <el-form-item label="Email" label-width="100px" prop="email">
                            <el-input prefix-icon="EditPen" v-model="targetUser.email"
                                      placeholder="User Email" clearable/>
                        </el-form-item>
                        <el-form-item label="Phone" label-width="100px" prop="phone">
                            <el-input prefix-icon="EditPen" v-model="targetUser.phone"
                                      placeholder="User Phone" clearable>
                                <template #prepend>+44</template>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="Status" label-width="100px" prop="status">
                            <el-radio-group v-model="targetUser.status">
                                <el-radio :label="0" border>Enabled</el-radio>
                                <el-radio :label="1" border>Disabled</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item>
                            <div style="width: 100%; text-align: center">
                                <el-popconfirm
                                        title="Confirm to upload?"
                                        confirm-button-text="Yes"
                                        cancel-button-text="No"
                                        @confirm="handleTargetUser">
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
import {ElMessageBox} from "element-plus";

export default {
    name: "RegisterMembership",

    data() {
        return {
            targetUser: { id: '', username: '', password: '', displayName: '', deptId: '', email: '', phone: '', status: '', createTime: '', creator: '', updateTime: '', updater: '' },
            deptType: "",

            selectedUsers: [],
            users: [],
            roles: [],
            depts: [],

            searchUsername: "",
            searchDeptType: "All",

            loadingFlag: false,
            dialog: {
                visible: false,
                title: "",
                button: "",
            },
        }
    },

    created() {
        this.getAllMemberships();
        this.getAllRoles();
        this.getAllDepts();
    },

    methods: {
        handleTargetUser() {
            let targetUser = this.targetUser;
            if (targetUser.deptId.length !==0
                    && targetUser.username.length !== 0 && targetUser.password.length !== 0
                    && targetUser.displayName.length !== 0 && targetUser.status.length !== 0) {
                let length = this.targetUser.phone.length;
                if (length > 0 && !(length >= 10 && length <= 11)) {
                    this.$message.error("Length of mobile number should be 10 to 11");
                    return;
                }
                this.loadingFlag = true;
                if (this.dialog.button === "Register") {
                    this.targetUser.creator = this.$store.state.user.username;
                    this.registerMembership();
                } else if (this.dialog.button === "Update") {
                    this.targetUser.updater = this.$store.state.user.username;
                    this.updateMembership();
                }
            } else {
                this.$message.error("The input information can't be empty");
            }
        },
        registerMembership() {
                requests.create.post("/server/mgmt/registerMembership", this.targetUser)
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
        updateMembership() {
            requests.create.post("/server/mgmt/updateMembership", this.targetUser)
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
            this.getAllMemberships();
            this.searchUsername = "";
            this.searchDeptType = "";
        },

        showRegisterMembershipDialog() {
            Object.keys(this.targetUser).forEach( key => {this.targetUser[key] = ""})
            this.dialog.visible = true;
            this.dialog.title = "Register Membership";
            this.dialog.button = "Register";
        },
        showUpdateMembershipDialog(user) {
            this.targetUser = JSON.parse(JSON.stringify(user));
            this.dialog.visible = true;
            this.dialog.title = "Update Membership";
            this.dialog.button = "Update";
            this.checkDeptType();
        },

        searchUsersByUsername() {
            this.searchDeptType = "All";
            if (this.searchUsername === "") {
                this.selectedUsers = this.users;
            } else {
                this.selectedUsers = this.users.filter(user => user.username.toLowerCase().search(this.searchUsername.toLowerCase()) !== -1);
            }
        },
        searchUsersByDeptType() {
            this.searchUsername = "";
            let type = this.searchDeptType;
            if (type === "All") {
                this.selectedUsers = this.users;
            } else if (type === "consumer") {
                this.selectedUsers = this.users.filter(user => user.deptId === -1);
            } else if (type === "doctor") {
                this.selectedUsers = this.users.filter(user => user.deptId === -2);
            } else {
                let depts = this.depts.filter(each => each.type === type);
                this.selectedUsers = [];
                for (let index in this.users) {
                    let dept = depts.filter(each => each.id === this.users[index].deptId);
                    if (dept.length > 0) {
                        this.selectedUsers.push(this.users[index]);
                    }
                }
            }
        },
        checkDeptType() {
            if (this.targetUser.deptId === -1) {
                this.deptType = "consumer";
            } else if (this.targetUser.deptId === -2) {
                this.deptType = "doctor";
            } else {
                this.deptType = this.depts.filter(
                        each => each.id === this.targetUser.deptId)[0].type;
            }
        },
        viewDepartment(deptId) {
            if (this.depts.length !== 0) {
                let department = this.depts.filter(each => each.id === deptId)[0]
                let title = "Department Information"
                let content = "";
                content = content + `<div>Name: ${department.name}</div>`
                        + `<div>Type: ${department.type}</div>`
                        + `<div>Leader: ${department.leader}</div>`
                        + `<div>Email: ${department.email}</div>`
                        + `<div>Address: ${department.address}</div>`;
                ElMessageBox.alert(content, title, {confirmButtonText: "OK", dangerouslyUseHTMLString: true});
            } else {
                this.$message.error("No department can be found");
            }
        },

        getAllMemberships() {
            requests.create.get("/server/mgmt/getAllMemberships")
                .then(res => {
                    let result = res.data;
                    if (result.data !== null) {
                        this.users = result.data;
                        this.selectedUsers = this.users;
                    } else {
                        this.selectedUsers = this.users = [];
                    }
                    console.log(result.msg);
                });
        },
        getAllRoles() {
            requests.create.get("/server/mgmt/getAllRoles")
                .then(res => {
                    let result = res.data;
                    if (result.data !== null) {
                        this.roles = result.data;
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
                    } else {
                        this.depts = [];
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
        top: 10%;
        overflow: auto;
        max-height: 90%;
    }
</style>