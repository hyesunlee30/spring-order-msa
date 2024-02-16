import {createRouter, createWebHistory} from 'vue-router';
import ItemList from '@/views/ItemList'
import MemberList from '@/views/MemberList'
import MemberCreate from '@/views/MemberCreate'
import LoginComponent from '@/views/LoginComponent'
//export default인경우에는 {} 필요없고, 여러개 요소가 있을 경우에는 {} 필요
import { memberRoutes } from './memberRouter';
import { orderRoutes } from './orderRouter';
import { itemRoutes } from './itemRouter';
import BasicComponent from '@/components/BasicComponent'

const routes = [
    {
        path: '/',
        name: 'HOME',
        component: ItemList,
    },{
        path: '/basic',
        name: 'basic',
        component: BasicComponent,
    },
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
        path: '/login',
        name: 'Login',
        component: LoginComponent,
    },
    ...memberRoutes, //주로 배열요소를 다른 배열요소에 합할때 사용
    ...orderRoutes,
    ...itemRoutes
]

const router = createRouter({
    //veu router는 내부적으로 히스토리 관리를 제공
    //1) 전에 있던 히스토리
    history: createWebHistory(),
    routes
});

export default router;