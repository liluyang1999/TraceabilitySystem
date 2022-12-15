import {createRouter, createWebHistory} from "vue-router";
import Home from "./views/Home.vue";
import Login from "@/views/Login";
import Dashboard from "@/views/common/Dashboard";
import RegisterMembership from "@/views/regulator/RegisterMembership";
import RegisterDepartment from "@/views/regulator/RegisterDepartment";
import UpdateAccount from "@/views/common/UpdateAccount";
import ManageSupplies from "@/views/regulator/ManageSupplies";
import ManageLicenses from "@/views/regulator/ManageLicenses";
import RecordProcess from "@/views/manufacturer/RecordProcess";
import ConfirmOrders from "@/views/manufacturer/ConfirmOrders";
import ManageOrders from "@/views/distributor/ManageOrders";
import DistributeProducts from "@/views/distributor/DistributeProducts";
import ReceiveProducts from "@/views/provider/ReceiveProducts";
import SellProducts from "@/views/provider/SellProducts";
import ManagePrescriptions from "@/views/doctor/ManagePrescriptions";
import TraceSupplies from "@/views/consumer/TraceSupplies";

const routes = [
    {
        path: "/login",
        name: Login,
        meta: {
            title: "Login"
        },
        component: Login,
    },
    {
        path: "/",
        redirect: "/dashboard",
    },
    {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/dashboard",
                name: "Dashboard",
                meta: {
                    title: "Dashboard",
                },
                component: Dashboard,
            },
            {
                path: "/update-account",
                name: "UpdateAccount",
                meta: {
                    title: "Update Account",
                },
                component: UpdateAccount,
            },
            {
                path: "/regulator/register-membership",
                name: "RegisterMembership",
                meta: {
                    title: "Register Membership"
                },
                component: RegisterMembership,
            },
            {
                path: "/regulator/register-department",
                name: "RegisterDepartment",
                meta: {
                    title: "Register Department"
                },
                component: RegisterDepartment,
            },
            {
                path: "/regulator/manage-licenses",
                name: "ManageLicenses",
                meta: {
                    title: "Manage Licenses",
                },
                component: ManageLicenses,
            },
            {
                path: "/regulator/manage-supplies",
                name: "ManageSupplies",
                meta: {
                    title: "Manage Supplies",
                },
                component: ManageSupplies,
            },
            {
                path: "/manufacturer/confirm-orders",
                name: "ConfirmOrders",
                meta: {
                    title: "Confirm Orders",
                },
                component: ConfirmOrders,
            },
            {
                path: "/manufacturer/record-process",
                name: "RecordProcess",
                meta: {
                    title: "Record Process",
                },
                component: RecordProcess,
            },
            {
                path: "/distributor/manage-orders",
                name: "ManageOrders",
                meta: {
                    title: "Manage Orders",
                },
                component: ManageOrders,
            },
            {
                path: "/distributor/distribute-products",
                name: "DistributeProducts",
                meta: {
                    title: "Distribute Products",
                },
                component: DistributeProducts,
            },
            {
                path: "/provider/receive-products",
                name: "ReceiveProducts",
                meta: {
                    title: "Receive Products",
                },
                component: ReceiveProducts,
            },
            {
                path: "/provider/sell-products",
                name: "SellProducts",
                meta: {
                    title: "Sell Products",
                },
                component: SellProducts,
            },
            {
                path: "/consumer/trace-supplies",
                name: "TraceSupplies",
                meta: {
                    title: "Trace Supplies",
                },
                component: TraceSupplies,
            },
            {
                path: "/doctor/manage-prescriptions",
                name: "ManagePrescriptions",
                meta: {
                    title: "Manage Prescriptions",
                },
                component: ManagePrescriptions,
            },
        ]
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | Supervision-System`;
    let token = localStorage.getItem('token');
    if (to.path !== "/login" && token == null) {
        next("/login");
    } else {
        next();
    }
});

export default router;
