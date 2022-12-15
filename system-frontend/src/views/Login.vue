<template>
    <div class="login-wrapper">
        <div class="login-frame">
            <div class="login-title">
              Medical Supplies Supervision System
            </div>

            <el-row type="flex" justify="center">
              <el-col :span="10" class="login-tips">Please Select Account Type</el-col>
            </el-row>
            <el-row type="flex" justify="center" style="margin-bottom: 5px">
              <el-col :span="16" style="text-align: center">
                <el-radio-group v-model="account.deptType">
                  <el-radio-button label="1">Regulator</el-radio-button>
                  <el-radio-button label="2">Manufacturer</el-radio-button>
                  <el-radio-button label="3">Distributor</el-radio-button>
                </el-radio-group>
              </el-col>
            </el-row>
            <el-row type="flex" justify="center" style="margin-bottom: 10px">
              <el-col :span="16" style="text-align: center">
                <el-radio-group v-model="account.deptType">
                  <el-radio-button label="4">Provider</el-radio-button>
                  <el-radio-button label="5">Consumer</el-radio-button>
                  <el-radio-button label="6">Doctor</el-radio-button>
                </el-radio-group>
              </el-col>
            </el-row>

            <el-form :model="account" ref="loginForm" class="login-data">
              <el-row type="flex" justify="center">
                <el-col :span="2">
                  <el-icon :size="25"><User/></el-icon>
                </el-col>
                <el-col :span="18">
                  <el-form-item prop="username">
                    <el-input
                        v-model="account.username"
                        type="text"
                        prefix-icon="user"
                        placeholder="Username"
                        clearable/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="2">
                  <el-icon :size="25"><Key/></el-icon>
                </el-col>
                <el-col :span="18">
                  <el-form-item prop="password">
                      <el-input
                          v-model="account.password"
                          type="password"
                          prefix-icon="key"
                          placeholder="Password"
                          show-password
                          clearable/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="2">
                    <el-icon :size="25"><Postcard /></el-icon>
                </el-col>
                <el-col :span="8">
                  <el-form-item prop="verCode">
                    <el-input
                        v-model="account.verCode"
                        prefix-icon="Postcard"
                        placeholder="Captcha"/>
                  </el-form-item>
                </el-col>
                <el-col :span="1"></el-col>
                <el-col :span="9">
                  <img :src="captchaUrl" alt="" @click="getCaptcha"/>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="7">
                  <el-checkbox v-model="remember">
                    <span style="color:black">remember me</span>
                  </el-checkbox>
                </el-col>
              </el-row>
              <div class="login-btn">
                  <el-button type="primary" @click="handleLogin">
                    Log in
                  </el-button>
              </div>
            </el-form>
        </div>
    </div>
</template>

<script>
import requests from "@/utils/requests.js";
import router from "@/router.js";

export default {
    data() {
        return {
            remember: true,
            captchaUrl: "",
            account: {
                username: "",
                password: "",
                verCode: "",
                deptType: "1",
            },
        };
    },

    beforeCreate() {
        let token = localStorage.getItem("token");
        if (token != null) {
            router.push("/dashboard");
        }
    },

    created() {
        this.getCaptcha();
        let deptType = localStorage.getItem("deptType");
        if (deptType != null) {
            this.account.deptType = deptType;
            this.account.username = localStorage.getItem("username");
        }
    },

    methods: {
        handleLogin() {
            if (!(this.account.username == null || this.account.username === "")
                  && !(this.account.password == null || this.account.password === "")
                  && !(this.account.verCode == null || this.account.verCode === "")) {
                  requests.create.post("/server/login/authenticate", {
                      username: this.account.username,
                      password: this.account.password,
                      deptType: this.mapDeptType(),
                      verCode: this.account.verCode,
                  }).then((res) => {
                      let result = res.data;
                      if (result.code === 1) {
                          this.handleRemember();
                          localStorage.setItem("token", "bearer:" + result.token);
                          router.push("/dashboard");
                          this.$message.success(result.msg);
                      } else {
                        this.$message.error(result.msg)
                      }
                  });
            } else {
              this.$message.error("The account or captcha shouldn't be empty");
            }
        },

        getCaptcha() {
          this.captchaUrl = "http://localhost:9000/server/captcha/getImage?" + Math.random();
        },

        handleRemember() {
            if (this.remember === true) {
                localStorage.setItem("deptType", this.account.deptType);
                localStorage.setItem("username", this.account.username);
            } else {
                localStorage.removeItem("deptType");
                localStorage.removeItem("username");
            }
        },

        mapDeptType() {
            switch (this.account.deptType) {
                case "1": return "regulator";
                case "2": return "manufacturer";
                case "3": return "distributor";
                case "4": return "provider";
                case "5": return "consumer";
                case "6": return "doctor";
            }
        },
    },
};
</script>

<style scoped>
    .login-wrapper {
      background-image: url(@/assets/img/login-bg.png);
      background-size: cover;
      width: 100%;
      height: 100%;
      position: relative;
    }

    .login-frame {
      position: absolute;
      left: 45%;
      top: 45%;
      width: 500px;
      margin: -190px 0 0 -175px;
      border-radius: 5px;
      background: rgba(255, 255, 255, 0.5);
      overflow: hidden;
    }

    .login-title {
        width: 100%;
        line-height: 50px;
        text-align: center;
        font-size: x-large;
        font-weight: bold;
        color: #ffffff;
        border-bottom: 1px solid #ddd;
        text-shadow: 2px 2px 2px #242f42;
    }

    .login-data {
        padding: 10px 50px;
    }

    .login-btn {
        text-align: center;
        width: 100%;
        height: 36px;
        margin-bottom: 10px;
    }

    .login-tips {
        text-align: center;
        font-size: 12px;
        line-height: 30px;
        color: black;
    }
</style>