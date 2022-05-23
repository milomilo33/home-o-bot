<template>
    <div>
        <h1>All pending CSRs</h1>
        <br>
        <b-table striped hover :items="csrs" :fields="csrFields">
            <template #cell(action)="row">
                <button class="btn btn-success" @click="generateCertificate(row)"
                    :ref="'genbtn' + row.index">Generate certificate</button>
                <button class="btn btn-dark" @click="rejectCSR(row)"
                    :ref="'delbtn' + row.index">Reject</button>
            </template>
        </b-table>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                csrs: [],
                csrFields: [
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
                    'actions'
                ]
            }
        },
        methods: {
            rejectCSR(row) {
                let CN = row.item.CN;
                this.axios.get(`/api/csr/reject/${CN}`, /*{
                                    headers: {
                                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                                    },
                                }*/)
                                .then(response => {
                                    this.loadAllCSRs();
                                })
                                .catch(error => {
                                    console.log(error);
                                })
            },

            loadAllCSRs() {
                this.axios.get(`/api/csr`, /*{
                    headers: {
                        Authorization: "Bearer " + sessionStorage.getItem("token"),
                    },
                }*/)
                .then(response => {
                    this.csrs = response.data;
                })
                .catch(error => {
                    console.log(error);
                })
            },

            generateCertificate(row) {
                console.log("open page for certificate creation");
            }
        },

        mounted() {
            this.loadAllCSRs();
        }
    }
</script>
