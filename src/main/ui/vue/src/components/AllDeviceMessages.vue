<template>
    <div>
        <h1>Messages from your devices</h1>
        <br>

        <b-form inline style="margin-left:25px;">
            <label>Device name:</label>
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
                        placeholder="Message"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="message">
            </b-form-input>

            <label style="margin-left:10px;">Real estate name:</label>
            <b-form-select id="selectedRealEstateName"
                        name="selectedRealEstateName"
                        placeholder="Real Estate Name"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="selectedRealEstateName"
                        :options="realEstateNameOptions"
                        style="margin-left:10px;">
            </b-form-select>

            <b-button @click="onSubmit" class="mb-2 mr-sm-2 mb-sm-0">Search</b-button>
        </b-form>

        <br>

        <b-table striped hover :items="messages" :fields="messageFields">
        </b-table>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                messages: [],
                messageFields: [
                    {
                        key: 'deviceName',
                        headerTitle: 'Device Name',
                        label: 'Device Name',
                        sortable: true
                    },
                    {
                        key: 'message',
                        headerTitle: 'Message',
                        label: 'Message',
                        sortable: true
                    },
                    {
                        key: 'realEstateName',
                        headerTitle: 'Real Estate Name',
                        label: 'Real Estate Name',
                        sortable: true
                    },
                    {
                        key: 'period',
                        headerTitle: 'Time updated',
                        label: 'Time updated',
                        sortable: true
                    },
                ],
                devices: [],
                deviceNameOptions: [],
                selectedDeviceName: '',
                message: '',
                selectedRealEstateName: '',
                realEstateNameOptions: [],
                messagesCopy: []
            }
        },
        methods: {
            loadAllDevicesAndMessages() {
                this.axios.get(`/api/user/all-devices-for-role`, {
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                })
                .then(response => {
                    this.convertTimeArrayToString(response.data);
                    this.devices = response.data;

                    this.messages = [];
                    this.devices.forEach((device) => {
                        device.messages.forEach((message) => {
                            let obj = {
                                deviceName: device.name,
                                message,
                                realEstateName: device.realEstate.name,
                                period: device.period
                            };

                            this.messages.push(obj);
                        });
                    });

                    this.messagesCopy = this.messages;
                    this.populateSearchOptions();
                })
                .catch(error => {
                    console.log(error);
                })
            },

            populateSearchOptions() {
                this.deviceNameOptions = [''];
                this.realEstateNameOptions = [''];

                this.messages.forEach((messageObj) => {
                    this.deviceNameOptions.push(messageObj.deviceName);
                    this.realEstateNameOptions.push(messageObj.realEstateName)
                });

                this.deviceNameOptions = this.deviceNameOptions.filter(this.onlyUnique);
                this.realEstateNameOptions = this.realEstateNameOptions.filter(this.onlyUnique);
            },

            onlyUnique(value, index, self) {
                return self.indexOf(value) === index;
            },

            onSubmit() {
                this.messages = this.messagesCopy.filter((messageObj) => {
                    let [deviceNameFilter, realEstateNameFilter] = [true, true];
                    if (this.selectedDeviceName !== '') {
                        deviceNameFilter = this.selectedDeviceName === messageObj.deviceName;
                    }
                    if (this.selectedRealEstateName !== '') {
                        realEstateNameFilter = this.selectedRealEstateName === messageObj.realEstateName;
                    }
                    return deviceNameFilter && realEstateNameFilter && messageObj.message.toLowerCase().includes(this.message.toLowerCase());
                });
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
            this.loadAllDevicesAndMessages();
        }
    }
</script>
