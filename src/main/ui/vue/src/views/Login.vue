<template>
<div class="justify-content-center login">
  <b-alert v-model="showSuccessAlert" dismissible fade variant="danger">
      Bad credentials.
    </b-alert>
  <b-card title="Login">
    <b-form>
      <b-form-input
        id="input-1"
        v-model="username"
        placeholder="Username"
        required
      >
      </b-form-input>

      <b-form-input
        id="input-2"
        v-model="password"
        placeholder="Password"
        required
        type="password"
      >
      </b-form-input>
      <div class="mt-2">
        <b-button variant="primary" type="button" v-on:click="login()"
          >Login</b-button
        >
      </div>
    </b-form>
  </b-card>
</div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      username: "",
      password: "",
      showSuccessAlert: false,
    };
  },

  methods: {
    login() {
      var _this = this;
      if (this.username.trim() == "" && this.password.trim() == "") {
        _this.showSuccessAlert = true;
        return;
      }
      this.axios
        .post("/api/auth/login", {
          username: this.username,
          password: this.password,
        })
        .then((response) => {
          sessionStorage.setItem("token", response.data.accessToken);
          this.findUserRole();
        })
          .catch((error) => {
          console.log(error);
          _this.showSuccessAlert = true;
        });
    },

    findUserRole() {
      var userRole = JSON.parse(
        atob(sessionStorage.getItem("token").split(".")[1])
      ).role;
      if (userRole == "ROLE_ADMIN") {
        this.$router.push("AdminPage");
      }
    },
  },
}
</script>

<style scoped>
.login {
  max-width: 40rem;
  background-color: #ffffff;
  margin: auto;
  margin-top: 100px;
  margin-bottom: 200px;
  padding: 20px;
}
</style>