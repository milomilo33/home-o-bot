<template>
    <div>
        <h1>Triggered alarms</h1>
        <br>

        <b-table striped hover :items="triggeredAlarms" :fields="triggeredAlarmFields">
        </b-table>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                alarms: [],
                triggeredAlarms: [],
                triggeredAlarmFields: [
                    {
                        key: 'description',
                        headerTitle: 'Alarm description',
                        label: 'Alarm description',
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
                    {
                        key: 'triggerMessage',
                        headerTitle: 'Trigger message',
                        label: 'Trigger message',
                        sortable: true
                    },
                    {
                        key: 'triggeredAt',
                        headerTitle: 'Triggered at',
                        label: 'Trigger at',
                        sortable: true
                    },
                ],
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

                    this.triggeredAlarms = [];
                    this.alarms.forEach((alarm) => {
                        alarm.allTriggered.forEach((alarmTriggered) => {
                            let obj = {
                                description: alarm.description,
                                type: alarm.type,
                                messageTriggerPattern: alarm.messageTriggerPattern,
                                deviceTrigger: alarm.deviceTrigger,
                                triggerMessage: alarmTriggered.valueTriggeredOn,
                                triggeredAt: alarmTriggered.dateTriggered,
                            };

                            obj["triggeredAt"][1]--;
                            obj["triggeredAt"].pop();
                            obj["triggeredAt"] = new Date(...obj["triggeredAt"]);

                            this.triggeredAlarms.push(obj);
                        });
                    });
                })
                .catch(error => {
                    console.log(error);
                })
            },

        },

        mounted() {
            this.loadAllAlarms();
        }
    }
</script>
