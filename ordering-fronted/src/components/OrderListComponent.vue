<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px;"><h1>주문 목록</h1></div>
        <table class="table">
            <thead>
                <tr>
                    <th>주문 ID</th>
                    <th>이메일</th>
                    <th>주문상태</th>
                    <th v-if="isAdmin">ACTION</th>
                    <th>생성시간</th>
                </tr>
            </thead>
            <tbody>
                <template v-for="order in orderList" :key="order.id">
                    <tr @click="toggleOrder(order.id)" style="cursor: pointer" >
                        <td>{{order.id}}</td>
                        <td>{{order.email}}</td>
                        <td>{{order.orderStatus}}</td>
                        <td><button v-if="order.orderStatus == 'ORDERED'" @click.stop="cancelOrder(order.id)">CANCEL</button></td>
                        <td>{{order.createdTime}}</td>
                    </tr>
                    <tr v-if="visibleOrder.has(order.id)">
                        <td colspan="5">
                            <span v-for="orderItem in order.orderItems" :key="orderItem.id">
                                {{ orderItem.itemName }} {{ orderItem.count }} 개
                            </span>
                        </td>
                    </tr>
                </template>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    props: ['isAdmin','apiUrl'],
    data(){
        return {
            orderList:[],
            visibleOrder: new Set(),
        }
        
    },
    async created() {
        try {
            const token = localStorage.getItem('token')
            console.log(token);
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}${this.apiUrl}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })

            this.orderList = response.data.result;

        } catch(error) {
            const error_message = error.response
            if(error_message) {
                console.log(error_message.data.error_message)
            } else {
                console.log(error);
                alert("Login faild")
            }
        }
    },
    methods: {
        toggleOrder(orderId) {
            if(this.visibleOrder.has(orderId)){
                this.visibleOrder.delete(orderId)
            } else {
                this.visibleOrder.add(orderId)
            }
        },
        async cancelOrder(orderId){
            if(confirm("정말 삭제하시겠습니까?")) {
                try {
                    const token = localStorage.getItem('token')
                    const url = `${process.env.VUE_APP_API_BASE_URL}/${orderId}/cancel`;
                    await axios.delete(url,{
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    })
                    const order = this.orderList.find(order => order.id === orderId);
                    order.orderStatus = "CANCELED";
                } catch(error) {
                    alert("주문 취소가 실패했습니다.");
                }
                
            }
            
            
        }
    },
}
</script>
