<template>
    <div class="tags-main" v-if="showTags">
        <ul>
            <li class="tags-li"
                v-for="(item,index) in tagsList"
                :class="{'active': isActive(item.path)}"
                :key="index">
                <router-link :to="item.path" class="tags-li-title">{{item.title}}</router-link>
                <el-icon @click="closeTags(index)"><Close /></el-icon>
            </li>
        </ul>

        <div class="tags-close-box">
            <el-dropdown @command="handleTags">
                <el-button type="primary">
                    Options
                  <el-icon><CollectionTag /></el-icon>
                </el-button>
                <template #dropdown>
                    <el-dropdown-menu size="small">
                        <el-dropdown-item command="others">Close Others</el-dropdown-item>
                        <el-dropdown-item command="all" divided>Close All</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
export default {
    created() {
        this.$store.commit("closeAllTags");
        this.setTags(this.$route);
    },

    computed: {
        tagsList() {
            return this.$store.state.tagsList;
        },
        showTags() {
            return this.tagsList.length > 0;
        }
    },

    watch: {
        $route(newValue) {
            this.setTags(newValue);
        }
    },

    methods: {
        isActive(path) {
            return path === this.$route.fullPath;
        },

        closeTags(index) {
            const delItem = this.tagsList[index];
            this.$store.commit("delTagsItem", { index });
            const item = this.tagsList[index] ? this.tagsList[index] : this.tagsList[index - 1];
            if (item) {
                delItem.path === this.$route.fullPath &&
                    this.$router.push(item.path);
            } else {
                this.$router.push("/dashboard");
            }
        },

        setTags(route) {
            const isExist = this.tagsList.some(item => {
                return item.path === route.fullPath;
            });
            if (!isExist) {
                if (this.tagsList.length >= 8) {
                    this.$store.commit("delTagsItem", { index: 0 });
                }
                this.$store.commit("setTagsItem", {
                    name: route.name,
                    title: route.meta.title,
                    path: route.fullPath
                });
            }
        },

        handleTags(command) {
            if (command === "others") {
              this.closeOther();
            } else if (command === "all") {
              this.closeAll();
            }
        },

        closeAll() {
          this.$store.commit("closeAllTags");
          this.$router.push("/dashboard");
        },

        closeOther() {
            const curItem = this.tagsList.filter(item => {
                return item.path === this.$route.fullPath;
            });
            this.$store.commit("closeOtherTags", curItem);
        },
    },
};
</script>


<style>
.tags-main {
    position: relative;
    height: 30px;
    overflow: hidden;
    background: #fff;
    padding-right: 120px;
    box-shadow: 0 5px 10px #ddd;
}

.tags-main ul {
    box-sizing: border-box;
    width: 100%;
    height: 100%;
}

.tags-li {
    float: left;
    margin: 3px 5px 2px 3px;
    border-radius: 3px;
    font-size: 12px;
    overflow: hidden;
    cursor: pointer;
    height: 23px;
    line-height: 23px;
    border: 1px solid #e9eaec;
    background: #fff;
    padding: 0 5px 0 12px;
    vertical-align: middle;
    color: #666;
    -webkit-transition: all 0.3s ease-in;
    -moz-transition: all 0.3s ease-in;
    transition: all 0.3s ease-in;
}

.tags-li:not(.active):hover {
    background: #f8f8f8;
}

.tags-li.active {
    color: #fff;
}

.tags-li-title {
    float: left;
    max-width: 150px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    margin-right: 5px;
    color: #666;
}

.tags-li.active .tags-li-title {
    color: #fff;
}

.tags-close-box {
    position: absolute;
    right: 0;
    top: 0;
    box-sizing: border-box;
    padding-top: 1px;
    text-align: center;
    width: 110px;
    height: 30px;
    background: #fff;
    box-shadow: -3px 0 15px 3px rgba(0, 0, 0, 0.1);
    z-index: 10;
}
</style>
