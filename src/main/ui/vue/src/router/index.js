import Vue from 'vue'
import VueRouter from 'vue-router'
import CreateCSR from '../components/CreateCSR'
import UnregisteredPage from '../views/UnregisteredPage'
import Login from '../views/Login'
import Logout from '../views/Logout'
import AdminPage from '../views/AdminPage'
import AllCSRs from '../components/AllCSRs'
import NewUser from '../components/NewUser'

Vue.use(VueRouter)

// const Role = {
// 	Admin: 'ROLE_ADMIN',
// 	...
// }

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
			},
			{
				path: "AllCSRs",
				name: "AllCSRs",
				component: AllCSRs,
			},
			{
				path: "NewUser",
				name: "NewUser",
				component: NewUser,
			}
		],
		// meta: {
		// 	roles: [Role.Admin]
		// },
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
// router.beforeEach((to, from, next) => {
// 	const { roles } = to.meta;
// 	if (roles) {
// 		// treba koristiti session storage
// 		const userRole = JSON.parse(atob(localStorage.getItem('token').split('.')[1])).role;
// 		if(roles.length && !roles.includes(userRole)){
// 			return next({path: 'Login'});
// 		}
// 	}
// 	next();
// });
