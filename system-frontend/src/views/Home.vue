<template>
    <div style="width: 100%; height: 100%">
      <my-header/>
      <my-sidebar/>
      <div class="content-box" :class="{ 'content-collapse': isCollapsed }">
        <my-tags/>
        <div class="content">
            <router-view v-slot="{ Component }">
                <transition name="move" mode="out-in">
                  <component :is="Component" />
                </transition>
            </router-view>
        </div>
      </div>
    </div>
</template>

<script>
import myHeader from "../components/Header";
import mySidebar from "../components/Sidebar";
import myTags from "../components/Tags.vue";
import requests from "@/utils/requests";
import router from "@/router";

export default {
    name: "Home",

    components: {
          myHeader,
          mySidebar,
          myTags,
      },

    created() {
        this.getLoginUser();
        this.getUserInfo();
    },

    computed: {
          tagsList() {
              return this.$store.state.tagsList.map(item => item.name);
          },
          isCollapsed() {
              return this.$store.state.isCollapsed;
          }
    },

    methods: {
        getLoginUser() {
            requests.create.post("/server/login/getLoginInfo")
                    .then(res => {
                        let result = res.data;
                        if (result.code === 1){
                            this.$store.state.loginUser = result.data;
                            this.$message.success(result.msg);
                        } else if (result.code === 2) {
                            localStorage.removeItem("token");
                            router.push("/login");
                            this.$message.error(result.msg);
                        }
                    });
        },

        getUserInfo() {
            requests.create.post("/server/user/getUserInfo")
                .then(res => {
                    let result = res.data;
                    if (result.code === 1){
                        this.$store.state.user = result.user;
                        this.$store.state.role = result.role;
                        this.$store.state.perms = result.perms;
                        if (result.role.name !== "consumer" && result.role.name !== "doctor") {
                            this.$store.state.dept = result.dept;
                        }
                        this.$message.success(result.msg);
                    } else if (result.code === 2) {
                        this.$message.error(result.msg);
                    } else if (result.code === 3) {
                        localStorage.removeItem("token");
                        router.push("/login");
                        this.$message.error(result.msg);
                    }
                });
        },
    }
};
</script>
