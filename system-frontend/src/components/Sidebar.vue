<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-menu"
            mode="vertical"
            :default-active="currentRoute"
            :collapse="isCollapsed"
            background-color="#324157"
            text-color="#bfcbd9"
            active-text-color="#20a0ff"
            unique-opened
            router>
            <el-menu-item :index="dashboard.path" :key="dashboard.name">
                <el-icon><HomeFilled /></el-icon>
                <template #title>
                    <span style="font-size: medium">
                        {{dashboard.name}}
                    </span>
                </template>
            </el-menu-item>
            <div v-for="perm in perms">
                <el-menu-item :index="perm.path" :key="perm.path">
                    <el-icon v-if="role.name === 'regulator'"><Management /></el-icon>
                    <el-icon v-else-if="role.name === 'manufacturer'"><List /></el-icon>
                    <el-icon v-else-if="role.name === 'distributor'"><Van /></el-icon>
                    <el-icon v-else-if="role.name === 'provider'"><Shop /></el-icon>
                    <el-icon v-else><UserFilled /></el-icon>
                    <template #title>
                        <span style="font-size: medium">
                            {{perm.name}}
                        </span>
                    </template>
                </el-menu-item>
            </div>
            <el-menu-item :index="updateAccount.path" :key="updateAccount.name">
                <el-icon><Lock /></el-icon>
                <template #title>
                    <span style="font-size: medium">
                        {{updateAccount.name}}
                    </span>
                </template>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script>
export default {
    data() {
        return {
            dashboard: {
                path: "/dashboard",
                name: "Dashboard",
            },
            updateAccount: {
                path: "/update-account",
                name: "Update Account",
            },
        }
    },

    computed: {
        currentRoute() {
            return this.$route.path;
        },
        isCollapsed(){
            return this.$store.state.isCollapsed;
        },
        role() {
            return this.$store.state.role;
        },
        perms() {
            return this.$store.state.perms;
        },
    },
};
</script>

<style scoped>
    .sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    overflow-y: scroll;
    }

    .sidebar::-webkit-scrollbar {
    width: 0;
    }

    .sidebar-menu:not(.el-menu--collapse) {
    width: 250px;
    }

    .sidebar > ul {
    height: 100%;
    }
</style>
