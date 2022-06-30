<template>
    <div>
        <h1>Logs</h1>
        <br>
        <b-table striped hover :items="logs" :fields="logFields">
            <template #cell(showFullLog)="row">
                <button class="btn btn-success" @click="showFullLogModal(row)">Show full log</button>
            </template>
            <template #cell(showChangedProperties)="row">
                <button class="btn btn-primary" @click="showChangedPropertiesModal(row)">Show</button>
            </template>
        </b-table>

        <b-modal ref="full-log-modal" hide-footer title="Error" size="xl">
            <div class="d-block text-center">
                <p>{{ this.fullLog }}</p>
            </div>
            <b-button class="mt-3" variant="outline-primary" block @click="hideFullLogModal">Close</b-button>
        </b-modal>
        
        <b-modal ref="changed-properties-modal" hide-footer title="Success" size="xl">
            <div class="d-block text-center">
                <p>{{ this.changedProperties }}</p>
            </div>
            <b-button class="mt-3" variant="outline-primary" block @click="hideChangedPropertiesModal">Close</b-button>
        </b-modal>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                changedProperties: '',
                fullLog: '',
                selectedRow: {},
                logs: [],
                logFields: [
                    {
                        key: 'globalId.entity',
                        headerTitle: 'Entity',
                        label: 'Entity',
                        sortable: true
                    },
                    {
                        key: 'type',
                        headerTitle: 'Action type',
                        label: 'Action type',
                        sortable: true
                    },
                    {
                        key: 'commitMetadata.author',
                        headerTitle: 'Author',
                        label: 'Author'
                    },
                    {
                        key: 'commitMetadata.commitDate',
                        headerTitle: 'Timestamp',
                        label: 'Timestamp'
                    },
                    {
                        key: 'showChangedProperties',
                        headerTitle: 'Changed properties',
                        label: 'Changed properties'
                    },
                    {
                        key: 'showFullLog',
                        headerTitle: 'Full log',
                        label: 'Full log'
                    }
                ]
            }
        },
        methods: {
            loadAllLogs() {
                this.axios.get(`/api/logs/all/snapshots`, {
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                })
                .then(response => {
                    this.logs = response.data;
                    
                    console.log(response.data);
                })
                .catch(error => {
                    console.log(error);
                })
            },

            hideFullLogModal() {
                this.$refs['full-log-modal'].hide()
            },

            hideChangedPropertiesModal() {
                this.$refs['changed-properties-modal'].hide()
            },

            showFullLogModal(row) {
                this.fullLog = JSON.stringify(row.item);
                this.$refs['full-log-modal'].show()
            },

            showChangedPropertiesModal(row) {
                console.log(row.item);
                this.changedProperties = JSON.stringify(row.item.changedProperties);
                console.log(row.item.changedProperties);
                this.$refs['changed-properties-modal'].show()
            },
        },

        mounted() {
            this.loadAllLogs();
        }
    }
</script>
