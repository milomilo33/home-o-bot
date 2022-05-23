<template>
    <b-container>
        <h1>Certificate Signing Request</h1>
        <b-form>
            <b-form-input id="username"
                        name="username"
                        placeholder="Username"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="username">
            </b-form-input>
            <br>
            <b-form-input id="password"
                        name="password"
                        placeholder="Password (must be at least 8 characters long, have at least one of each number, lower case character, upper case character and special character)"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="password">
            </b-form-input>
            <br>
            <b-form-input id="firstname"
                        name="firstname"
                        placeholder="First Name"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="firstname">
            </b-form-input>
            <br>
            <b-form-input id="lastname"
                        name="lastname"
                        placeholder="Last Name"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="lastname">
            </b-form-input>
            <br>
            <b-form-input id="email"
                        name="email"
                        placeholder="Email"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="email">
            </b-form-input>
            <br>
            <b-form-select id="role"
                        name="role"
                        placeholder="Role"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="role"
                        :options="roles">
            </b-form-select>
            <br>
            <br>
            <b-button @click="onSubmit" class="mb-2 mr-sm-2 mb-sm-0">Create request</b-button>
        </b-form>

        <b-modal ref="error-modal" hide-footer title="Error">
            <div class="d-block text-center">
                <p>{{ this.errorMessage }}</p>
            </div>
            <b-button class="mt-3" variant="outline-danger" block @click="hideErrorModal">Close</b-button>
        </b-modal>
        
        <b-modal ref="success-modal" hide-footer title="Success">
            <div class="d-block text-center">
                <p>Request successfully submitted.</p>
            </div>
            <b-button class="mt-3" variant="outline-success" block @click="hideSuccessModal">Close</b-button>
        </b-modal>

    </b-container>
</template>

<script>

    export default {
        data() {
            return {
                username: '',
                password: '',
                firstname: '',
                lastname: '',
                email: '',
                role: '',
                errorMessage: '',
                roles: ['ROLE_ADMIN', 'ROLE_OWNER', 'ROLE_RENTER']
            }
        },
        methods: {
            onSubmit() {
                let body = {
                    username: this.username,
                    password: this.password,
                    firstname: this.firstname,
                    lastname: this.lastname,
                    email: this.email,
                    role: this.role,
                };

                this.errorMessage = ""
                if (body.username === "") {
                    this.errorMessage = "Username can not be empty";
                    this.showErrorModal();
                }
                let repw = new RegExp("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[#?!@$ %^&*-]).{8,}$");
                let reemail = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;   
                if (!repw.test(body.password)){
                    this.errorMessage = "Invalid password format";
                    this.showErrorModal();
                }
                if (body.firstname === "") {
                    this.errorMessage = "First name can not be empty";
                    this.showErrorModal();
                }
                if (body.lastname === "") {
                    this.errorMessage = "Last name can not be empty";
                    this.showErrorModal();
                }
                if (!reemail.test(body.email)) {
                    this.errorMessage = "Provide valid email";
                    this.showErrorModal();
                }

                if (this.errorMessage !== ""){
                    return
                }

                this.axios.post(`http://localhost:8443/api/auth/signup`, body, {
                        headers: {
                            Authorization: "Bearer " + sessionStorage.getItem('token'),
                        },
                    })
                .then(() => {
                    this.showSuccessModal();
                })
                .catch(error => {
                    this.errorMessage = error.response.data;
                    this.showErrorModal();
                });
            },

            hideErrorModal() {
                this.$refs['error-modal'].hide()
            },

            hideSuccessModal() {
                this.$refs['success-modal'].hide()
            },

            showErrorModal() {
                this.$refs['error-modal'].show()
            },

            showSuccessModal() {
                this.$refs['success-modal'].show()
            },
        }
    }
</script>
