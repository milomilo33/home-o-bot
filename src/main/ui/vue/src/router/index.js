import Vue from 'vue'
import VueRouter from 'vue-router'
import CreateCSR from '../components/CreateCSR'
import UnregisteredPage from '../views/UnregisteredPage'
import Login from '../views/Login'
import Logout from '../views/Logout'
import AdminPage from '../views/AdminPage'
import OwnerPage from '../views/OwnerPage'
import RenterPage from '../views/RenterPage'
import AllCSRs from '../components/AllCSRs'

import NewUser from '../components/NewUser'

import CreateCertificate from '../components/CreateCertificate'
import AllCertificates from '../components/AllCertificates'
import ManageDeviceConfig from '../components/ManageDeviceConfig'
import ManageOwners from '../components/ManageOwners'
import ManageRenters from '../components/ManageRenters'
import AllDevices from '../components/AllDevices'
import AllDeviceMessages from '../components/AllDeviceMessages'



Vue.use(VueRouter)

const Role = {
	Admin: 'ROLE_ADMIN',
	Owner: 'ROLE_OWNER',
	Renter: 'ROLE_RENTER'
}

const routes = [
	{
		path: "/",
		name: UnregisteredPage,
		component: UnregisteredPage,
		children: [
			{
				path: "CreateCSR",
				name: "CreateCSRUnregistered",
				component: CreateCSR,
			}
		]
	},
	{
		path: "/Login",
		name: "Login",
		component: Login
	},
	{
		path: "/Logout",
		name: "Logout",
		component: Logout
	},
	{
		path: "/AdminPage",
		name: "AdminPage",
		component: AdminPage,
		children: [
			{
				path: "CreateCSR",
				name: "CreateCSRAdmin",
				component: CreateCSR,
				meta: {
					roles: [Role.Admin]
				},
			},
			{
				path: "AllCSRs",
				name: "AllCSRs",
				component: AllCSRs,
				meta: {
					roles: [Role.Admin]
				},
			},
			{
				path: "NewUser",
				name: "NewUser",
				component: NewUser,
				meta: {
					roles: [Role.Admin]
				},
			},
			{
				path: "CreateCertificate",
				name: "CreateCertificate",
				component: CreateCertificate,
				props: true,
				meta: {
					roles: [Role.Admin]
				},
			},
			{
				path: "AllCertificates",
				name: "AllCertificates",
				component: AllCertificates,
				meta: {
					roles: [Role.Admin]
				},
			},
			{
				path: "ManageDeviceConfig",
				name: "ManageDeviceConfig",
				component: ManageDeviceConfig,
				meta: {
					roles: [Role.Admin]
				},
			},
			{
				path: "ManageOwners",
				name: "ManageOwners",
				component: ManageOwners,
				meta: {
					roles: [Role.Admin]
				},
			},
		],
		meta: {
			roles: [Role.Admin]
		},
	},
	{
		path: "/OwnerPage",
		name: "OwnerPage",
		component: OwnerPage,
		children: [
			{
				path: "ManageRenters",
				name: "ManageRenters",
				component: ManageRenters,
				meta: {
					roles: [Role.Owner]
				},
			},
			{
				path: "AllDevices",
				name: "AllDevicesOwner",
				component: AllDevices,
				meta: {
					roles: [Role.Owner]
				},
			},
			{
				path: "AllDeviceMessages",
				name: "AllDeviceMessagesOwner",
				component: AllDeviceMessages,
				meta: {
					roles: [Role.Owner]
				},
			},
		],
		meta: {
			roles: [Role.Owner]
		},
	},
	{
		path: "/RenterPage",
		name: "RenterPage",
		component: RenterPage,
		children: [
			{
				path: "AllDevices",
				name: "AllDevicesRenter",
				component: AllDevices,
				meta: {
					roles: [Role.Renter]
				},
			},
			{
				path: "AllDeviceMessages",
				name: "AllDeviceMessagesRenter",
				component: AllDeviceMessages,
				meta: {
					roles: [Role.Renter]
				},
			},
		],
		meta: {
			roles: [Role.Renter]
		},
	},
	{
		path: '*',
		redirect: "/Login"
	}

]

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

export default router
router.beforeEach((to, from, next) => {
	const { roles } = to.meta;
	if (roles) {
		const userRole = JSON.parse(atob(sessionStorage.getItem('token').split('.')[1])).role;
		if(roles.length && !roles.includes(userRole)){
			return next({path: 'Login'});
		}
	}
	next();
});
