<template>
    <div>
        <h1>Manage real estate owners</h1>
        <br>
        <b-table striped hover :items="allRealEstate" :fields="allRealEstateFields">
            <template #cell(owner.username)="row">
                <b-form-select
                    type="text"
                    v-model="row.item.owner.username"
                    :options="usernameOptions"
                />
            </template>
        </b-table>

        <button class="btn btn-success" @click="updateOwners()">Update all</button>

        <b-modal ref="error-modal" hide-footer title="Error">
            <div class="d-block text-center">
                <p>{{ this.errorMessage }}</p>
            </div>
            <b-button class="mt-3" variant="outline-danger" block @click="hideErrorModal">Close</b-button>
        </b-modal>

        <b-modal ref="success-modal" hide-footer title="Success">
            <div class="d-block text-center">
                <p>Successfully updated real estate owners.</p>
            </div>
            <b-button class="mt-3" variant="outline-success" block @click="hideSuccessModal">Close</b-button>
        </b-modal>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                errorMessage: '',
                usernameOptions: [],
                allRealEstate: [],
                allRealEstateFields: [
                    {
                        key: 'id',
                        headerTitle: 'ID',
                        label: 'ID',
                        sortable: true
                    },
                    {
                        key: 'name',
                        headerTitle: 'Name',
                        label: 'Name',
                        sortable: true
                    },
                    {
                        key: 'owner.username',
                        headerTitle: 'Owner',
                        label: 'Owner'
                    }
                ]
            }
        },
        methods: {
            updateOwners() {
                let body = [];
                this.allRealEstate.forEach((realEstate) => {
                    body.push({
                        realEstateId: realEstate.id,
                        ownerUsername: realEstate.owner.username
                    });
                });

                this.axios.post(`/api/real-estate/update-owners`, body, {
                                    headers: {
                                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                                    },
                                })
                                .then(response => {
                                    this.showSuccessModal();
                                    this.loadAllRealEstateAndOwners();
                                })
                                .catch(error => {
                                    this.errorMessage = error.response.data.message;
                                    this.showErrorModal();
                                    console.log(error);
                                })
            },

            loadAllRealEstateAndOwners() {
                this.axios.get(`/api/real-estate/all`, {
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                })
                .then(response => {
                    this.allRealEstate = response.data;
                    console.log(response.data);
                    this.allRealEstate.forEach((realEstate) => {
                        if (!realEstate.owner) {
                            realEstate.owner = {username: ''};
                        }
                    });

                    this.axios.get(`/api/user/all-owners`, {
                        headers: {
                            Authorization: "Bearer " + sessionStorage.getItem("token"),
                        },
                    })
                    .then(response => {
                        console.log(response.data);
                        let owners = response.data;
                        this.usernameOptions = [''];
                        owners.forEach((owner) => {
                            this.usernameOptions.push(owner.username);
                        });
                    })
                })
                .catch(error => {
                    console.log(error);
                })
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
        },

        mounted() {
            this.loadAllRealEstateAndOwners();
        }
    }
</script>
