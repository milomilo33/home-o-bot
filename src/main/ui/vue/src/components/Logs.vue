<template>
    <div>
        <h1>Logs</h1>
        <br>

        <b-form inline style="margin-left:25px;">
            <label>Entity:</label>
            <b-form-select id="entity"
                        name="entity"
                        placeholder="Entity"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        :options="entityOptions"
                        v-model="entity"
                        style="margin-left:10px;">
            </b-form-select>

            <label>Type:</label>
            <b-form-select id="type"
                        name="type"
                        placeholder="Type"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        :options="typeOptions"
                        v-model="type"
                        style="margin-left:10px;">
            </b-form-select>

            <b-form-input id="author"
                        name="author"
                        placeholder="Author"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="author">
            </b-form-input>

            <b-form-input id="fullLogRegex"
                        name="fullLogRegex"
                        placeholder="Regex full log"
                        class="mb-2 mr-sm-2 mb-sm-0"
                        v-model="fullLogRegex">
            </b-form-input>

            <b-button @click="onSubmit" class="mb-2 mr-sm-2 mb-sm-0">Search</b-button>
        </b-form>

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
                entity: '',
                entityOptions: [],
                type: '',
                typeOptions: [],
                author: '',
                fullLogRegex: '',
                changedProperties: '',
                fullLog: '',
                selectedRow: {},
                logs: [],
                logsCopy: [],
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
                        label: 'Author',
                        sortable: true
                    },
                    {
                        key: 'commitMetadata.commitDate',
                        headerTitle: 'Timestamp',
                        label: 'Timestamp',
                        sortable: true
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
                    this.logsCopy = this.logs;
                    this.populateSearchOptions();
                    
                    console.log(response.data);
                })
                .catch(error => {
                    console.log(error);
                })
            },

            populateSearchOptions() {
                this.entityOptions = [''];
                this.typeOptions = [''];

                this.logs.forEach((logObj) => {
                    this.entityOptions.push(logObj.globalId.entity);
                    this.typeOptions.push(logObj.type)
                });

                this.entityOptions = this.entityOptions.filter(this.onlyUnique);
                this.typeOptions = this.typeOptions.filter(this.onlyUnique);
            },

            onlyUnique(value, index, self) {
                return self.indexOf(value) === index;
            },

            onSubmit() {
                this.logs = this.logsCopy.filter((logObj) => {
                    let [entityFilter, typeFilter, regexFilter, authorFilter] = [true, true, true, true];
                    if (this.entity !== '') {
                        entityFilter = this.entity === logObj.globalId.entity;
                    }
                    if (this.type !== '') {
                        typeFilter = this.type === logObj.type;
                    }
                    if (this.fullLogRegex !== '') {
                        // let regexStr = this.fullLogRegex.replace(/[|\\{}()[\]^$+*?.]/g, '\\$&');
                        let regex = new RegExp(this.fullLogRegex);
                        regexFilter = regex.test(JSON.stringify(logObj));
                    }
                    if (this.author !== '') {
                        if (logObj.commitMetadata.author) {
                            authorFilter = logObj.commitMetadata.author.toLowerCase().includes(this.author.toLowerCase());
                        }
                        else authorFilter = false;
                    }
                    return entityFilter && typeFilter && regexFilter && authorFilter;
                });
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
