<template>
    <div class="main-frame">
        <el-row type="flex" justify="center">
            <el-col :span="7" style="margin-top: 100px">
                <el-card shadow="always"
                     style="height: 250px"
                     v-loading="updateInfoLoading"
                     element-loading-text="Updating...">
                    <template #header>
                        <div style="display: flex; justify-content: space-between; align-items: center">
                            <strong>Information</strong>
                            <el-popconfirm
                                title="Confirm to update information?"
                                confirm-button-text="Yes"
                                cancel-button-text="No"
                                @confirm="updateUserInfo">
                                <template #reference>
                                    <el-button type="primary">Update</el-button>
                                </template>
                            </el-popconfirm>
                        </div>
                    </template>
                    <el-form  :model="updateInfo" label-width="80px" label-position="left">
                        <el-form-item label="Nickname">
                            <el-input v-model="updateInfo.displayName" prefix-icon="User" clearable/>
                        </el-form-item>
                        <el-form-item label="Email">
                            <el-input v-model="updateInfo.email" prefix-icon="Message" clearable/>
                        </el-form-item>
                        <el-form-item label="Phone">
                            <el-input v-model="updateInfo.phone" prefix-icon="Iphone" clearable/>
                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>

            <el-col :span="3" style="margin-top: 100px"></el-col>

            <el-col :span="7" style="margin-top: 100px">
                <el-card shadow="always" style="height: 250px"
                       v-loading="updatePasswordLoading"
                       element-loading-text="Updating...">
                    <template #header>
                        <div style="display: flex; justify-content: space-between; align-items: center">
                            <strong>Password</strong>
                            <el-popconfirm
                                  title="Confirm to update password?"
                                  confirm-button-text="Yes"
                                  cancel-button-text="No"
                                  @confirm="updateUserPassword">
                                <template #reference>
                                    <el-button type="primary">Update</el-button>
                                </template>
                            </el-popconfirm>
                        </div>
                    </template>
                    <el-form :model="updatePassword" label-width="120px" label-position="left" style="margin-top: 10px">
                        <el-form-item label="Old Password">
                          <el-input v-model="updatePassword.oldPassword" type="password" prefix-icon="Key" show-password clearable/>
                        </el-form-item>
                        <el-form-item label="New Password">
                          <el-input v-model="updatePassword.newPassword" type="password" prefix-icon="Key" show-password clearable/>
                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import requests from "@/utils/requests";
import router from "@/router";

export default {
    name: "UpdateAccount",

    data() {
        return {
            updateInfoLoading: false,
            username: "",
            updateInfo: {
                displayName: "",
                email: "",
                phone: "",
            },

            updatePasswordLoading: false,
            updatePassword: {
                oldPassword: "",
                newPassword: "",
            }
        }
    },

    created() {
        this.username = this.$store.state.user.username;
        this.updateInfo.displayName = this.$store.state.user.displayName;
        this.updateInfo.email = this.$store.state.user.email;
        this.updateInfo.phone = this.$store.state.user.phone;
    },

    methods: {
        updateUserInfo() {
            this.updateInfoLoading = true;
            requests.create.post("/server/user/updateUserInfo", { },
                    { params: { "username": this.username,
                                        "displayName": this.updateInfo.displayName,
                                        "email": this.updateInfo.email,
                                        "phone": this.updateInfo.phone } })
                    .then(res => {
                        this.updateInfoLoading = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.$store.state.user.displayName = this.updateInfo.displayName;
                            this.$store.state.user.email = this.updateInfo.email;
                            this.$store.state.user.phone = this.updateInfo.phone;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        } else if (result.code === 3) {
                            router.push("/login");
                            this.$message.error(result.msg);
                        }
                    });
        },

        updateUserPassword() {
            this.updatePasswordLoading = true;
            requests.create.post("/server/user/updateUserPassword", { },
                    { params: { "username": this.username,
                                        "oldPassword": this.updatePassword.oldPassword,
                                        "newPassword": this.updatePassword.newPassword } })
                    .then(res => {
                        this.updatePasswordLoading = false;
                        let result = res.data;
                        if (result.code === 1) {
                            this.updatePassword.oldPassword = "";
                            this.updatePassword.newPassword = "";
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        } else if (result.code === 3) {
                            router.push("/login");
                            this.$message.error(result.msg);
                        }
                    });
        },
    },
}
</script>

<style scoped>
    .main-frame {
        width: 100%;
        height: 100%;
        background-color: floralwhite;
    }
</style>
