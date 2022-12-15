<template>
    <el-scrollbar>
    <div class="main-frame">
        <el-row type="flex" justify="center">
            <el-col :span="7" style="margin-top: 20px">
                <el-card shadow="always" style="margin-bottom: 20px;">
                    <el-row justify="center" align="middle">
                        <el-col :span="10">
                            <el-avatar v-if="this.$store.state.role.name === 'regulator'" :src="avatars.regulator"
                                       shape="square" style="width: 120px; height: 120px" alt />
                            <el-avatar v-else-if="this.$store.state.role.name === 'manufacturer'" :src="avatars.manufacturer"
                                       shape="square" style="width: 120px; height: 120px" alt />
                            <el-avatar v-else-if="this.$store.state.role.name === 'distributor'" :src="avatars.distributor"
                                       shape="square" style="width: 120px; height: 120px" alt />
                            <el-avatar v-else-if="this.$store.state.role.name === 'provider'" :src="avatars.provider"
                                       shape="square" style="width: 120px; height: 120px" alt />
                            <el-avatar v-else-if="this.$store.state.role.name === 'consumer'" :src="avatars.consumer"
                                       shape="square" style="width: 120px; height: 120px" alt />
                            <el-avatar v-else-if="this.$store.state.role.name === 'doctor'" :src="avatars.doctor"
                                       shape="square" style="width: 120px; height: 120px" alt />
                        </el-col>
                        <el-col :span="1"></el-col>
                        <el-col :span="10">
                            <el-row>
                                <el-col style="text-align: center">
                                    <strong style="font-size: x-large">{{this.$store.state.user.displayName}}</strong>
                                </el-col>
                            </el-row>
                            &nbsp;
                            <el-row>
                                <el-col style="text-align: center">
                                    <strong style="font-size: large">{{this.$store.state.role.name}}</strong>
                                </el-col>
                            </el-row>
                            &nbsp;
                            <el-row>
                                <el-col style="text-align: center">
                                    <el-button type="primary" size="small"
                                               @click="permsInfoDialogVisible = !permsInfoDialogVisible">
                                        Permissions
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-col>
                    </el-row>
                    <el-divider/>
                    <el-row justify="start" align="middle">
                        <el-col :span="24">
                            <el-row style="margin-bottom: 5px">
                                <el-col>
                                    <strong>Department:&nbsp;</strong>
                                    <el-link v-if="this.$store.state.role.name !== 'consumer' && this.$store.state.role.name !== 'doctor'"
                                             style="font-size: medium; color: deepskyblue"
                                             @click="deptInfoDialogVisible = !deptInfoDialogVisible">
                                        {{this.$store.state.dept.name}}
                                    </el-link>
                                    <span v-if="this.$store.state.role.name === 'consumer'">consumer</span>
                                    <span v-if="this.$store.state.role.name === 'doctor'">doctor</span>
                                </el-col>
                            </el-row>
                            <el-row style="margin-bottom: 5px">
                                <el-col>
                                    <strong>Email:&nbsp;</strong>
                                    {{this.$store.state.user.email}}
                                </el-col>
                            </el-row>
                            <el-row style="margin-bottom: 5px">
                                <el-col>
                                    <strong>Phone:&nbsp;</strong>
                                    {{this.$store.state.user.phone}}
                                </el-col>
                            </el-row>
                            <el-row style="margin-bottom: 5px">
                                <el-col>
                                    <strong>Browser:&nbsp;</strong>
                                    {{this.$store.state.loginUser.browser}}
                                </el-col>
                            </el-row>
                            <el-row style="margin-bottom: 5px">
                                <el-col>
                                    <strong>OS:&nbsp;</strong>
                                    {{this.$store.state.loginUser.os}}
                                </el-col>
                            </el-row>
                            <el-row style="margin-bottom: 5px">
                                <el-col>
                                    <strong>UUID:&nbsp;</strong>
                                    {{this.$store.state.loginUser.uuid}}
                                </el-col>
                            </el-row>
                        </el-col>
                    </el-row>
                </el-card>

                <el-card shadow="always">
                    <el-row>
                        <el-col>
                            <strong>Login IP:&nbsp;</strong>
                            {{this.$store.state.loginUser.loginIp}}
                        </el-col>
                    </el-row>
                    <el-divider/>
                    <el-row>
                        <el-col>
                            <strong>IP Location:&nbsp;</strong>
                            {{this.$store.state.loginUser.loginLocation}}
                        </el-col>
                    </el-row>
                </el-card>
            </el-col>

            <el-col :span="1"></el-col>

            <el-col :span="14" style="margin-top: 20px">
                <el-row type="flex" justify="space-between"
                        style="margin-bottom: 20px">
                    <el-col :span="7">
                        <el-card shadow="always" :body-style="{ padding: '0px' }">
                            <div style="display: flex; justify-content: center;
                                        align-items: center;width: 100%; height: 70px">
                                <div style="width: 30%; height: 70px; background-color: palevioletred"></div>
                                <div style="width: 70%; height: 70px; text-align: center">
                                    <div style="font-size: xx-large; color: #20a0ff;
                                                font-weight: bolder;margin-top: 5px">
                                        {{blockchainInfo.height}}
                                    </div>
                                    <div>
                                        Blockchain Height
                                    </div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="7">
                        <el-card shadow="always" :body-style="{ padding: '0px' }">
                            <div style="display: flex; justify-content: center;
                                        align-items: center;width: 100%; height: 70px">
                                <div style="width: 30%; height: 70px; background-color: darkseagreen"></div>
                                <div style="width: 70%; height: 70px; text-align: center">
                                    <div style="font-size: large; color: #20a0ff;
                                                font-weight: bolder;margin-top: 5px">
                                        {{ blockchainInfo.channelId }}
                                    </div>
                                    <div>
                                        Channel ID
                                    </div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="7">
                        <el-card shadow="always" :body-style="{ padding: '0px' }">
                            <div style="display: flex; justify-content: center;
                                        align-items: center;width: 100%; height: 70px">
                                <div style="width: 30%; height: 70px; background-color: lightsteelblue"></div>
                                <div style="width: 70%; height: 70px; text-align: center">
                                    <div style="font-size: xx-large; color: #20a0ff;
                                                font-weight: bolder;margin-top: 5px">
                                        {{ blockchainInfo.allBlockHashes.length }}
                                    </div>
                                    <div>
                                        TX Number
                                    </div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-card shadow="always" :body-style="{ padding: '5px'}"
                        style="width: 100%; background-color: whitesmoke; text-align: center">
                    <el-scrollbar>
                        <div style="max-height: 415px">
                            <div v-for="(hash, index) in blockchainInfo.allBlockHashes"
                                 style="margin-top: 5px; margin-bottom: 5px">
                                <div style="text-align: center">Hash: {{hash}}</div>
                                <div style="text-align: center">Block:&nbsp;{{blockchainInfo.height - index}}</div>
                                <div v-if="index !== blockchainInfo.height - 1">
                                    ------------------------------------------------------
                                    <el-icon :size="20"><Bottom /></el-icon>
                                    ------------------------------------------------------
                                </div>
                            </div>
                        </div>
                    </el-scrollbar>
                </el-card>
            </el-col>
        </el-row>

        <el-row v-if="serverInfo.cpu !== '' && serverInfo.mem !== '' && serverInfo.jvm !== ''"
                type="flex" justify="start" style="margin-top: 15px; margin-bottom: 15px">
            <el-col :span="1"></el-col>
            <el-col :span="7">
                <el-card shadow="always" style="height: 280px">
                    <template #header>
                        <div style="display: flex; justify-content: space-between; align-items: center">
                            <span>CPU</span>
                            <span>Core Number: {{serverInfo.cpu.coreNum}}</span>
                        </div>
                    </template>
                    <div style="display: flex; justify-content: space-evenly; align-items: center">
                        <span>Free: {{serverInfo.cpu.freeRate.toFixed(2)}}%</span>
                        <span>Waiting: {{serverInfo.cpu.waitRate.toFixed(2)}}%</span>
                    </div>
                    <div style="display: flex; justify-content: space-evenly; align-items: center">
                        <span>Used: {{serverInfo.cpu.usedRate.toFixed(2)}}%</span>
                        <span>System: {{serverInfo.cpu.systemRate.toFixed(2)}}%</span>
                    </div>&nbsp;
                    <div style="text-align: center">
                        <el-progress type="circle" :percentage="serverInfo.cpu.usage" color="green">
                            <template #default="{percentage}">
                                <span style="font-size: medium; font-weight: bolder">
                                    {{percentage}}%
                                </span>
                            </template>
                        </el-progress>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="1"></el-col>
            <el-col :span="6">
                <el-card shadow="always" style="height: 280px">
                    <template #header>
                        <div style="display: flex; justify-content: space-between; align-items: center">
                            <span>Memory</span>
                            <span>Total: {{serverInfo.mem.total.toFixed(2)}}Gb</span>
                        </div>
                    </template>
                    <div style="display: flex; justify-content: space-evenly; align-items: center">
                        <span>Free: {{(serverInfo.mem.free.toFixed(2))}}Gb</span>
                        <span>Used: {{serverInfo.mem.used.toFixed(2)}}Gb</span>
                    </div>&nbsp;
                    <div style="text-align: center; margin-top: 15px">
                        <el-progress type="circle" :percentage="serverInfo.mem.usage" color="blue">
                            <template #default="{percentage}">
                                <span style="font-size: medium; font-weight: bolder">
                                    {{percentage}}%
                                </span>
                            </template>
                        </el-progress>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="1"></el-col>
            <el-col :span="7">
                <el-card shadow="always" style="height: 280px">
                    <template #header>
                        <div style="display: flex; justify-content: space-between; align-items: center">
                            <span>JVM</span>
                            <span>Total: {{serverInfo.jvm.total}}Mb</span>
                        </div>
                    </template>
                    <div style="text-align: center">
                        <div>
                            {{serverInfo.jvm.name}}
                        </div>
                        <div>
                            Start-up Time: {{serverInfo.jvm.startDateTime}}
                        </div>
                        &nbsp;
                        <div>
                            <el-progress type="circle" :percentage="serverInfo.jvm.usage" color="yellow">
                                <template #default="{percentage}">
                                <span style="font-size: medium; font-weight: bolder">
                                    {{percentage}}%
                                </span>
                                </template>
                            </el-progress>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <el-dialog v-model="permsInfoDialogVisible"
                   title="User Permissions"
                   width="40%">
            <div v-for="(perm, index) in this.$store.state.perms"
                 class="dept-info" >
                <strong>{{index + 1}}:&nbsp;</strong>
                {{perm.name}}
            </div>
        </el-dialog>
        <el-dialog v-model="deptInfoDialogVisible"
                       title="Department Information"
                       width="40%">
            <div class="dept-info">
                <strong>Name: </strong>
                {{this.$store.state.dept.name}}
            </div>
            <div class="dept-info">
                <strong>Type: </strong>
                {{this.$store.state.dept.type}}
            </div>
            <div class="dept-info">
                <strong>Leader: </strong>
                {{this.$store.state.dept.leader}}
            </div>
            <div class="dept-info">
                <strong>Email: </strong>
                {{this.$store.state.dept.email}}
            </div>
            <div class="dept-info">
                <strong>Address: </strong>
                {{this.$store.state.dept.address}}
            </div>
        </el-dialog>
    </div>
    </el-scrollbar>
</template>

<script>
import requests from "@/utils/requests";
import regulatorIcon from "@/assets/img/avatar/regulator.png";
import manufacturerIcon from "@/assets/img/avatar/manufacturer.png";
import distributorIcon from "@/assets/img/avatar/distributor.png";
import providerIcon from "@/assets/img/avatar/provider.png";
import consumerIcon from "@/assets/img/avatar/consumer.png";
import doctorIcon from "@/assets/img/avatar/doctor.png";

export default {
    name: "Dashboard",

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
            blockchainInfo: {
                height: "",
                channelId: "",
                allBlockHashes: [],
            },
            serverInfo: {
                cpu: "",
                mem: "",
                jvm: "",
            },
            permsInfoDialogVisible: false,
            deptInfoDialogVisible: false,
        }
    },

    created() {
        this.getBasicInfo();
        this.getAllBlockHashes();
        this.getServerInfo();
    },

    methods: {
        getBasicInfo() {
            requests.create.get("/blockchain/info/getBasicInfo")
                    .then(res => {
                        let result = res.data;
                        if (result.code === 1) {
                            this.blockchainInfo.height = result.height;
                            this.blockchainInfo.channelId = result.channelId;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        getAllBlockHashes() {
            requests.create.get("/blockchain/info/getAllBlockHashes")
                    .then(res => {
                        let result = res.data;
                        if (result.code === 1) {
                            this.blockchainInfo.allBlockHashes = result.data;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            this.$message.error(result.msg);
                        }
                    });
        },
        getServerInfo() {
            requests.create.get("/server/login/getServerInfo")
                    .then(res => {
                        let result = res.data;
                        if (result.code === 1) {
                            this.serverInfo.cpu = result.cpu;
                            this.serverInfo.mem = result.mem;
                            this.serverInfo.jvm = result.jvm;
                            this.$message.success(result.msg);
                        } else {
                            console.error("The server info cannot be achieved right now");
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
        overflow: auto;
    }

    .dept-info {
        font-size: large;
    }
</style>