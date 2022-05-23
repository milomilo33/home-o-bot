<template>
    <b-container>
        <h1>Create certificate from CSR</h1>
        <b-form>
            <b-form-input id="commonName"
                        name="commonName"
                        placeholder="Common Name (unique)"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="commonName"
                        disabled>
            </b-form-input>
            <br>
            <b-form-input id="organizationalUnit"
                        name="organizationalUnit"
                        placeholder="Organizational Unit"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="organizationalUnit">
            </b-form-input>
            <br>
            <b-form-input id="organization"
                        name="organization"
                        placeholder="Organization"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="organization">
            </b-form-input>
            <br>
            <b-form-input id="location"
                        name="location"
                        placeholder="Location"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="location">
            </b-form-input>
            <br>
            <b-form-input id="state"
                        name="state"
                        placeholder="State"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="state">
            </b-form-input>
            <br>
            <b-form-select id="country"
                        name="country"
                        placeholder="Country"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="country"
                        :options="countryOptions">
            </b-form-select>
            <br>

            <label></label>
            <label for="dd"> Valid from: </label>
            <div>
            <b-form-datepicker
                v-model="dateFrom"
                :min="minDate"
                locale="en"
                class="mb-2"
                required
            ></b-form-datepicker>
            </div>
            <label></label>
            <label for="dd"> Valid until: </label>
            <b-form-datepicker
                v-model="dateTo"
                :min="minDate"
                locale="en"
                class="mb-2"
                required
            ></b-form-datepicker>

            <br>
            <b-button @click="onSubmit" class="mb-2 mr-sm-2 mb-sm-0">Create certificate</b-button>
        </b-form>

        <b-modal ref="error-modal" hide-footer title="Error">
            <div class="d-block text-center">
                <p>{{ this.errorMessage }}</p>
            </div>
            <b-button class="mt-3" variant="outline-danger" block @click="hideErrorModal">Close</b-button>
        </b-modal>
        
        <b-modal ref="success-modal" hide-footer title="Success">
            <div class="d-block text-center">
                <p>Certificate successfully created.</p>
            </div>
            <b-button class="mt-3" variant="outline-success" block @click="hideSuccessModal">Close</b-button>
        </b-modal>

    </b-container>
</template>

<script>
    import { countryList } from '../utilities/util'

    export default {
        data() {
            return {
                commonName: this.csr.CN,
                organizationalUnit: this.csr.OU,
                organization: this.csr.O,
                location: this.csr.L,
                state: this.csr.ST,
                country: this.csr.C,
                errorMessage: '',
                countryOptions: countryList,
                minDate: new Date(),
                dateFrom: new Date(),
                dateTo: new Date(new Date().setFullYear(new Date().getFullYear() + 5))
            }
        },
        methods: {
            onSubmit() {
                let body = {
                    CN: this.commonName,
                    OU: this.organizationalUnit,
                    O: this.organization,
                    L: this.location,
                    ST: this.state,
                    C: this.country,
                    start: new Date(this.dateFrom).toISOString(),
                    end: new Date(this.dateTo).toISOString()
                };

                this.axios.post(`/api/certificates/create`, body, {
                        headers: {
                            Authorization: "Bearer " + sessionStorage.getItem('token'),
                        },
                    })
                .then(() => {
                    this.showSuccessModal();
                })
                .catch(error => {
                    this.errorMessage = error.response.data.message;
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
        },

        props: {
            csr: Object
        },
    }
</script>
