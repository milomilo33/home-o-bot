<template>
  <div>
    <b-navbar fixed="top" toggleable="lg" type="light" variant="light">
    <b-navbar-brand href="/AdminPage">Home Page</b-navbar-brand>
    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
    <b-collapse id="nav-collapse" is-nav>    
      <b-navbar-nav>
        <b-nav-item href="/AdminPage/CreateCSR">Create request</b-nav-item>
        <b-nav-item href="/AdminPage/AllCSRs">All CSRs</b-nav-item>
        <b-nav-item href="/AdminPage/AllCertificates">All certificates</b-nav-item>
        <b-nav-item href="/AdminPage/NewUser">Create user</b-nav-item>
        <b-nav-item href="/AdminPage/ManageDeviceConfig">Device config</b-nav-item>
        <b-nav-item href="/AdminPage/ManageOwners">Manage real estate</b-nav-item>
        <b-nav-item-dropdown left>
            <template #button-content>
                Alarms
            </template>
            <b-dropdown-item href="/AdminPage/AlarmDefinitions">Alarm definitions</b-dropdown-item>
            <b-dropdown-item href="/AdminPage/TriggeredAlarms">Triggered alarms</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>

      <!-- Right aligned nav items -->
      <b-navbar-nav class="ml-auto">
        <b-nav-item right href="/Logout">Log out</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
    </b-navbar>

    <router-view>

    </router-view>
  </div>
</template>
