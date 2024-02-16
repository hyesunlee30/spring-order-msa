import OrderList from '@/views/OrderList'
import OrderCart from '@/views/OrderCart'


export const orderRoutes = [
    {
        path: '/orders/manage',
        name: 'OrderList',
        component: OrderList,
    },
    {
        path: '/ordercart',
        name: 'OrderCart',
        component: OrderCart,
    },

]