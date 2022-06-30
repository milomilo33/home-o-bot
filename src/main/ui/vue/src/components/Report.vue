<template>
    <div>
        <h1>Report</h1>
        <br>

        <b-form inline style="margin-left:25px;">
            <label></label>
            <label for="dd"> Start time: </label>
            <div>
            <b-form-datepicker
                v-model="dateFrom"
                locale="en"
                class="mb-2"
                style="margin-left: 10px;"
                required
            ></b-form-datepicker>
            </div>
            <label></label>
            <label for="dd" style="margin-left: 10px;"> End: </label>
            <b-form-datepicker
                v-model="dateTo"
                locale="en"
                class="mb-2"
                style="margin-left: 10px;"
                required
            ></b-form-datepicker>

            <b-button @click="onSubmit" class="mb-2 mr-sm-2 mb-sm-0" style="margin-left: 10px;">Get report</b-button>
        </b-form>

        <br>

        <b-table striped hover :items="report" :fields="reportFields">
        </b-table>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                report: [],
                reportFields: [
                    {
                        key: 'deviceName',
                        headerTitle: 'Device Name',
                        label: 'Device Name',
                        sortable: true
                    },
                    {
                        key: 'realEstateName',
                        headerTitle: 'Device location',
                        label: 'Device location',
                        sortable: true
                    },
                    {
                        key: 'numOfTriggeredAlarms',
                        headerTitle: 'Total alarms triggered in this period',
                        label: 'Total alarms triggered in this period',
                        sortable: true
                    },
                ],
                minDate: new Date(),
                dateFrom: new Date(),
                dateTo: new Date(new Date().setDate(new Date().getDate() + 1))
            }
        },
        methods: {

            onSubmit() {
                let body = {
                    start: new Date(this.dateFrom).toISOString(),
                    end: new Date(this.dateTo).toISOString()
                };

                this.axios.post(`/api/alarms/get-report`, body, {
                        headers: {
                            Authorization: "Bearer " + sessionStorage.getItem('token'),
                        },
                    })
                .then((response) => {
                    this.report = response.data;
                })
                .catch(error => {
                    console.log(error);
                }); 
            },

        },

        mounted() {

        }
    }
</script>
