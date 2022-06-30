<template>
    <div>
        <h1>Alarms</h1>
        <br>

        <b-table striped hover :items="alarms" :fields="alarmFields">
        </b-table>

        <b-form inline style="margin-left:25px;">
            <label>Entity:</label>
            <b-form-select id="selectedDeviceName"
                        name="selectedDeviceName"
                        placeholder="Device Name"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        :options="deviceNameOptions"
                        v-model="selectedDeviceName"
                        style="margin-left:10px;">
            </b-form-select>

            <b-form-input id="message"
                        name="message"
                        placeholder="Message trigger pattern"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="message">
            </b-form-input>

            <b-form-input id="description"
                        name="description"
                        placeholder="Alarm description"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="description">
            </b-form-input>

            <b-button @click="onSubmit" class="mb-2 mr-sm-2 mb-sm-0">Create new alarm</b-button>
        </b-form>

        <b-modal ref="error-modal" hide-footer title="Error">
            <div class="d-block text-center">
                <p>{{ this.errorMessage }}</p>
            </div>
            <b-button class="mt-3" variant="outline-danger" block @click="hideErrorModal">Close</b-button>
        </b-modal>

        <b-modal ref="success-modal" hide-footer title="Success">
            <div class="d-block text-center">
                <p>Successfully created new alarm.</p>
            </div>
            <b-button class="mt-3" variant="outline-success" block @click="hideSuccessModal">Close</b-button>
        </b-modal>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                alarms: [],
                alarmFields: [
                    {
                        key: 'id',
                        headerTitle: 'ID',
                        label: 'ID',
                        sortable: true
                    },
                    {
                        key: 'description',
                        headerTitle: 'Description',
                        label: 'Description',
                        sortable: true
                    },
                    {
                        key: 'type',
                        headerTitle: 'Alarm type',
                        label: 'Alarm type',
                        sortable: true
                    },
                    {
                        key: 'messageTriggerPattern',
                        headerTitle: 'Trigger pattern',
                        label: 'Trigger pattern',
                        sortable: true
                    },
                    {
                        key: 'deviceTrigger.name',
                        headerTitle: 'Trigger device',
                        label: 'Trigger device',
                        sortable: true
                    },
                ],
                deviceNameOptions: [],
                selectedDeviceName: '',
                message: '',
                description: '',
                errorMessage: '',
            }
        },
        methods: {
            loadAllAlarms() {
                this.axios.get(`/api/alarms/all`, {
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                })
                .then(response => {
                    this.alarms = response.data;

                    this.axios.get(`/api/devices/all-names`, {
                        headers: {
                            Authorization: "Bearer " + sessionStorage.getItem("token"),
                        },
                    })
                    .then(response => {
                        this.deviceNameOptions = response.data;
                    })
                    .catch(error => {
                        console.log(error);
                    })
                })
                .catch(error => {
                    console.log(error);
                })
            },

            onSubmit() {
                let body = {
                    description: this.description,
                    deviceName: this.selectedDeviceName,
                    triggerPattern: this.message
                };

                this.axios.post(`/api/alarms/create-new-message-alarm`, body, {
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                })
                .then(response => {
                    this.showSuccessModal();
                    this.loadAllAlarms();
                })
                .catch(error => {
                    this.errorMessage = error.response.data.message;
                    this.showErrorModal();
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
            this.loadAllAlarms();
        }
    }
</script>
