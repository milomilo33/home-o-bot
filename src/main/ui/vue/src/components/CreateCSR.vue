<template>
    <b-container>
        <h1>Certificate Signing Request</h1>
        <b-form>
            <b-form-input id="commonName"
                        name="commonName"
                        placeholder="Common Name (unique)"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="commonName">
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
    import { countryList } from '../utilities/util'

    export default {
        data() {
            return {
                commonName: '',
                organizationalUnit: '',
                organization: '',
                location: '',
                state: '',
                country: '',
                errorMessage: '',
                countryOptions: countryList
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
                };

                this.axios.post(`/api/csr/create`, body)
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
        }
    }
</script>
