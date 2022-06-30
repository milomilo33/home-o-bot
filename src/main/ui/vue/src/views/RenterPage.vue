<template>
  <div>
    <b-navbar fixed="top" toggleable="lg" type="light" variant="light">
    <b-navbar-brand href="/RenterPage">Home Page</b-navbar-brand>
    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
    <b-collapse id="nav-collapse" is-nav>    
      <b-navbar-nav>
        <b-nav-item href="/RenterPage/AllDevices">My devices</b-nav-item>
        <b-nav-item href="/RenterPage/AllDeviceMessages">My device messages</b-nav-item>
        <b-nav-item href="/RenterPage/Report">Report</b-nav-item>
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
