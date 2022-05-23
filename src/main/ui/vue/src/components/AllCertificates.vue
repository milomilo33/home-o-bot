<template>
    <div>
        <h1>All certificates</h1>
        <br>
        <b-table striped hover :items="certificates" :fields="certificateFields">
            <template #cell(actions)="row">
                <button class="btn btn-success" @click="unrevokeCertificate(row)" v-if="row.item.status.includes('certificateHold')"
                    :ref="'genbtn' + row.index">Unrevoke</button>
                <button class="btn btn-danger" @click="revokeCertificate(row)" v-if="row.item.status === 'Valid'"
                    :ref="'delbtn' + row.index">Revoke</button>
            </template>
        </b-table>

        <b-modal ref="success-modal" hide-footer title="Success">
            <div class="d-block text-center">
                <p>Success!</p>
            </div>
            <b-button class="mt-3" variant="outline-success" block @click="hideSuccessModal">Close</b-button>
        </b-modal>

        <b-modal
        id="modal-prevent-closing"
        ref="modal"
        title="Reason of revocation"
        @show="resetModal"
        @hidden="resetModal"
        @ok="handleOk"
        >
        <form ref="form" @submit.stop.prevent="handleSubmit">
            <b-form-group
            label="Reason"
            label-for="reason-input"
            invalid-feedback="Reason is required"
            >
            <b-form-select
                id="reason-input"
                v-model="reasonInput"
                :options="reasonOptions"
                required
            ></b-form-select>
            </b-form-group>
        </form>
        </b-modal>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                reasonOptions: ["unspecified",
                                "keyCompromise",
                                "cACompromise",
                                "affiliationChanged",
                                "superseded",
                                "cessationOfOperation",
                                "certificateHold",
                                "removeFromCRL",
                                "privilegeWithdrawn",
                                "aACompromise"],
                reasonInput: '',
                selectedRow: {},
                certificates: [],
                certificateFields: [
                    {
                        key: 'CN',
                        headerTitle: 'Common Name',
                        label: 'Common',
                        sortable: true
                    },
                    {
                        key: 'OU',
                        headerTitle: 'Organizational Unit',
                        label: 'Organizational Unit',
                        sortable: true
                    },
                    {
                        key: 'O',
                        headerTitle: 'Organization',
                        label: 'Organization'
                    },
                    {
                        key: 'L',
                        headerTitle: 'Location',
                        label: 'Location'
                    },
                    {
                        key: 'ST',
                        headerTitle: 'State',
                        label: 'State'
                    },
                    {
                        key: 'C',
                        headerTitle: 'Country',
                        label: 'Country'
                    },
                    {
                        key: 'start',
                        headerTitle: 'Valid from',
                        label: 'Valid from'
                    },
                    {
                        key: 'end',
                        headerTitle: 'Valid until',
                        label: 'Valid until'
                    },
                    {
                        key: 'status',
                        headerTitle: 'Status',
                        label: 'Status'
                    },
                    'actions'
                ]
            }
        },
        methods: {
            revokeCertificate(row) {
                this.selectedRow = row;
                this.$bvModal.show("modal-prevent-closing");
            },

            unrevokeCertificate(row) {
                let CN = row.item.CN;
                this.axios.get(`/api/certificates/unrevoke?CN=${CN}`, /*{
                                    headers: {
                                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                                    },
                                }*/)
                                .then(response => {
                                    this.showSuccessModal();
                                    this.loadAllCertificates();
                                })
                                .catch(error => {
                                    console.log(error);
                                })
            },

            loadAllCertificates() {
                this.axios.get(`/api/certificates`, /*{
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                }*/)
                .then(response => {
                    this.certificates = response.data;
                    
                    // date conversion
                    this.certificates.forEach((obj) => {
                        console.log(obj["start"]);
                        obj["start"][1]--;
                        obj["end"][1]--;
                        obj["start"] = new Date(...obj["start"]).toLocaleDateString();
                        obj["end"] = new Date(...obj["end"]).toLocaleDateString();
                    });
                })
                .catch(error => {
                    console.log(error);
                })
            },

            hideSuccessModal() {
                this.$refs['success-modal'].hide()
            },

            showSuccessModal() {
                this.$refs['success-modal'].show()
            },

            resetModal() {
                this.reasonInput = ''
            },
            handleOk(bvModalEvent) {
                // Prevent modal from closing
                bvModalEvent.preventDefault()
                // Trigger submit handler
                this.handleSubmit()
            },
            handleSubmit() {
                // Exit when the form isn't valid
                if (this.reasonInput.trim() === '') {
                    return;
                }

                let CN = this.selectedRow.item.CN;
                this.axios.get(`/api/certificates/revoke?CN=${CN}&reason=${encodeURIComponent(this.reasonInput)}`, /*{
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                }*/)
                .then(response => {
                    this.showSuccessModal();
                    this.loadAllCertificates();
                })
                .catch(error => {
                    console.log(error);
                })

                // Hide the modal manually
                this.$nextTick(() => {
                this.$bvModal.hide('modal-prevent-closing')
                })
            }
        },

        mounted() {
            this.loadAllCertificates();
        }
    }
</script>
