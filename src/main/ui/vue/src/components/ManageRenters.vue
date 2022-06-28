<template>
    <div>
        <h1>Manage my real estate</h1>
        <br>
        <b-table striped hover :items="allRealEstate" :fields="allRealEstateFields">
            <template #cell(renters)="row">
                <b-form-select
                    type="text"
                    v-model="row.item.renters"
                    :options="usernameOptions"
                    multiple
                />
            </template>
        </b-table>

        <button class="btn btn-success" @click="updateRenters()">Update all</button>

        <b-modal ref="error-modal" hide-footer title="Error">
            <div class="d-block text-center">
                <p>{{ this.errorMessage }}</p>
            </div>
            <b-button class="mt-3" variant="outline-danger" block @click="hideErrorModal">Close</b-button>
        </b-modal>

        <b-modal ref="success-modal" hide-footer title="Success">
            <div class="d-block text-center">
                <p>Successfully updated your real estate renters.</p>
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
                        key: 'renters',
                        headerTitle: 'Renters',
                        label: 'Renters'
                    }
                ]
            }
        },
        methods: {
            updateRenters() {
                let body = [];
                this.allRealEstate.forEach((realEstate) => {
                    body.push({
                        realEstateId: realEstate.id,
                        renterUsernames: realEstate.renters
                    });
                });

                this.axios.post(`/api/real-estate/update-renters`, body, {
                                    headers: {
                                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                                    },
                                })
                                .then(response => {
                                    this.showSuccessModal();
                                    console.log(response.data);
                                    this.loadAllRealEstateAndRenters();
                                })
                                .catch(error => {
                                    this.errorMessage = error.response.data.message;
                                    this.showErrorModal();
                                    console.log(error);
                                })
            },

            loadAllRealEstateAndRenters() {
                this.axios.get(`/api/real-estate/owned-real-estate`, {
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                })
                .then(response => {
                    this.allRealEstate = response.data;

                    this.allRealEstate.forEach((realEstate) => {
                        let renterUsernames = [];
                        realEstate.renters.forEach((renter) => {
                            renterUsernames.push(renter.username);
                        });
                        realEstate.renters = renterUsernames;
                    });

                    this.axios.get(`/api/user/all-renters`, {
                        headers: {
                            Authorization: "Bearer " + sessionStorage.getItem("token"),
                        },
                    })
                    .then(response => {
                        let renters = response.data;
                        this.usernameOptions = [];
                        renters.forEach((renter) => {
                            this.usernameOptions.push(renter.username);
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
            this.loadAllRealEstateAndRenters();
        }
    }
</script>
