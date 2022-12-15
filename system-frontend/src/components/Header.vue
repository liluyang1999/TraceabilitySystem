<template>
    <el-row class="header-main">
        <el-col :span="2">
            <div class="header-collapse-button" @click="changeCollapse">
                <el-icon v-if="!isCollapsed" :size="25"><Fold /></el-icon>
                <el-icon v-else :size="25"><Expand /></el-icon>
            </div>
        </el-col>

        <el-col :span="5">
            <div class="header-user-info">
                <div>
                    <span>Username: {{username}}</span>
                </div>
                <div>
                    <span>Role: {{roleName}}</span>
                </div>
                <div v-if="this.$store.state.role.name !== 'consumer'
                        && this.$store.state.role.name !== 'doctor'">
                    <span>Department: {{deptName}}</span>
                </div>
            </div>
        </el-col>

        <el-col :span="12">
            <div class="header-title">
                Medical Supplies Supervision System
            </div>
        </el-col>

        <el-col :span="5">
            <div class="header-user-profile">
                <el-avatar :src="avatars.regulator" v-if="this.$store.state.role.name === 'regulator'"/>
                <el-avatar :src="avatars.manufacturer" v-else-if="this.$store.state.role.name === 'manufacturer'"/>
                <el-avatar :src="avatars.distributor" v-else-if="this.$store.state.role.name === 'distributor'"/>
                <el-avatar :src="avatars.provider" v-else-if="this.$store.state.role.name === 'provider'"/>
                <el-avatar :src="avatars.consumer" v-else-if="this.$store.state.role.name === 'consumer'"/>
                <el-avatar :src="avatars.doctor" v-else-if="this.$store.state.role.name === 'doctor'"/>
                &nbsp;&nbsp;
                <el-dropdown trigger="click" @command="handleCommand">
                    <span style="color: white; cursor: pointer">
                        {{displayName}}
                        <el-icon><CaretBottom /></el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <a href="http://192.168.48.128:7017" target="_blank">
                                <el-dropdown-item>
                                    Blockchain Explorer
                                </el-dropdown-item>
                            </a>
                            <el-dropdown-item divided command="logout">
                                Logout
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
                &nbsp;&nbsp;&nbsp;
            </div>
      </el-col>
    </el-row>
</template>

<script>
import router from "@/router";
import requests from "@/utils/requests";
import regulatorIcon from "@/assets/img/avatar/regulator.png";
import manufacturerIcon from "@/assets/img/avatar/manufacturer.png";
import distributorIcon from "@/assets/img/avatar/distributor.png";
import providerIcon from "@/assets/img/avatar/provider.png";
import consumerIcon from "@/assets/img/avatar/consumer.png";
import doctorIcon from "@/assets/img/avatar/doctor.png";

export default {
    data() {
        return {
            avatars: {
                regulator: regulatorIcon,
                manufacturer: manufacturerIcon,
                distributor: distributorIcon,
                provider: providerIcon,
                consumer: consumerIcon,
                doctor: doctorIcon,
            },
        }
    },

    computed: {
        isCollapsed() {
            return this.$store.state.isCollapsed;
        },
        displayName() {
            return this.$store.state.user.displayName;
        },
        username() {
            return this.$store.state.user.username;
        },
        roleName() {
            return this.$store.state.role.name;
        },
        deptName() {
            return this.$store.state.dept.name;
        }
    },

    methods: {
        changeCollapse() {
            this.$store.commit("handleCollapse", !this.isCollapsed);
        },

        handleCommand(command) {
        if (command === "logout") {
            requests.create.post("/server/logout/handleLogout")
                .then(res => {
                    localStorage.removeItem("token");
                    router.push("/login");
                    this.$message.success("Logout Success");
                });
        }
      }
    },
};
</script>

<style scoped>
    .header-main {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      background-color: #242f42;
      width: 100%;
      height: 70px;
      font-size: 20px;
      color: white;
    }

    .header-collapse-button {
      text-align: center;
      cursor: pointer;
      line-height: 70px;
      background: rgb(40,52,70);
    }

    .header-user-info {
      width: 100%;
      height: 100%;
      text-align: left;
      font-size: medium;
    }

    .header-title {
      width: 100%;
      height: 100%;
      text-align: center;
      font-size: x-large;
    }

    .header-user-profile {
      display: flex;
      flex-direction: row;
      justify-content: right;
      align-items: center;
      width: 100%;
      height: 100%;
      text-align: center;
    }
</style>
