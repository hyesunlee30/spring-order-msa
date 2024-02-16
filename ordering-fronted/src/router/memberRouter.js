import MemberList from '@/views/MemberList'
import MemberCreate from '@/views/MemberCreate'
import MemberOrders from '@/views/MemberOrders'
import MemberMyPage from '@/views/MemberMyPage'

export const memberRoutes = [
    {
        path: '/members',
        name: 'MemberList',
        component: MemberList,
    },
    {
        path: '/member/create',
        name: 'MemberCreate',
        component: MemberCreate,
    },
    {
        path: '/member/:id/orders',
        name: 'MemberOrders',
        component: MemberOrders,
        props: true
    },
    {
        path: '/mypage',
        name: 'MemberMyPage',
        component: MemberMyPage,
        props: true
    },
]

