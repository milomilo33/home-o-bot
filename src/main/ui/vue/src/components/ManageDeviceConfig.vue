<template>
    <div>
        <h1>Update config of devices</h1>
        <br>
        <b-table striped hover :items="devices" :fields="deviceFields">
            <template #cell(path)="row">
                <b-form-input
                    type="text"
                    v-model="row.item.path"
                />
            </template>
            <template #cell(period)="row">
                <b-form-input
                    type="text"
                    v-model="row.item.period"
                />
            </template>
            <template #cell(filter)="row">
                <b-form-input
                    type="text"
                    v-model="row.item.filter"
                />
            </template>
        </b-table>

        <button class="btn btn-success" @click="updateConfig()">Update all</button>

        <b-modal ref="error-modal" hide-footer title="Error">
            <div class="d-block text-center">
                <p>{{ this.errorMessage }}</p>
            </div>
            <b-button class="mt-3" variant="outline-danger" block @click="hideErrorModal">Close</b-button>
        </b-modal>

        <b-modal ref="success-modal" hide-footer title="Success">
            <div class="d-block text-center">
                <p>Successfully updated config.</p>
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
                devices: [],
                deviceFields: [
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
                        key: 'path',
                        headerTitle: 'Path (location)',
                        label: 'Path (location)'
                    },
                    {
                        key: 'period',
                        headerTitle: 'Period',
                        label: 'Period'
                    },
                    {
                        key: 'filter',
                        headerTitle: 'Filter',
                        label: 'Filter'
                    }
                ]
            }
        },
        methods: {
            updateConfig() {
                this.axios.post(`/api/devices/update-config`, this.devices, {
                                    headers: {
                                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                                    },
                                })
                                .then(response => {
                                    this.showSuccessModal();
                                    this.convertTimeArrayToString(response.data);
                                    this.devices = response.data;
                                })
                                .catch(error => {
                                    this.errorMessage = error.response.data.message;
                                    this.showErrorModal();
                                    console.log(error);
                                })
            },

            loadAllDevices() {
                this.axios.get(`/api/devices/all`, {
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                })
                .then(response => {
                    this.convertTimeArrayToString(response.data);
                    this.devices = response.data;
                })
                .catch(error => {
                    console.log(error);
                })
            },

            convertTimeArrayToString(devices) {
                devices.forEach((device) => {
                    if (device.period) {
                        let timeString = '';
                        device.period.forEach((timeStrip) => {
                            let timeStripStr = timeStrip.toString();
                            if (timeStripStr.length == 1) {
                                timeStripStr = "0" + timeStripStr;
                            }
                            timeString += timeStripStr + ":";
                        });
                        timeString = timeString.slice(0, -1);
                        device.period = timeString;
                    }
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

        mounted() {
            this.loadAllDevices();
        }
    }
</script>
