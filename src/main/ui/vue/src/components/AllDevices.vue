<template>
    <div>
        <h1>Your devices</h1>
        <br>
        <b-table striped hover :items="devices" :fields="deviceFields">
        </b-table>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                devices: [],
                deviceFields: [
                    {
                        key: 'name',
                        headerTitle: 'Name',
                        label: 'Name',
                        sortable: true
                    },
                    {
                        key: 'period',
                        headerTitle: 'Update period',
                        label: 'Update period',
                        sortable: true
                    },
                    {
                        key: 'filter',
                        headerTitle: 'Message filter',
                        label: 'Message filter'
                    }
                ]
            }
        },
        methods: {
            loadAllDevices() {
                this.axios.get(`/api/user/all-devices-for-role`, {
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
        },

        mounted() {
            this.loadAllDevices();
        }
    }
</script>
