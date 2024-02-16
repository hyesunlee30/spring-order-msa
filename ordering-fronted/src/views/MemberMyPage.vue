<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px;"><h1>회원 정보</h1></div>
        <table class="table">
            <tr><td>이름</td>
                <td>{{memberinfo.name}}</td>
            </tr>
            <tr>
                <td>이메일</td>
                <td>{{memberinfo.email}}</td>
            </tr>
            <tr><td>도시</td>
                <td>{{memberinfo.city}}</td>
            </tr>
            <tr><td>상세주소</td>
                <td>{{memberinfo.street}}</td>
            </tr>
            <tr><td>우편번호</td>
                <td>{{memberinfo.zipcod}}</td>
            </tr>
        </table>    
    </div>
    <OrderListComponent
    :isAdmin="false"
    apiUrl="/member/myorders"
    />
</template>

<script>
import OrderListComponent from '@/components/OrderListComponent.vue';
import axios from 'axios';

export default {
    components:{OrderListComponent},
    data(){
        return {
            memberinfo: {}
        }
    },
    created() {
        this.fetchMember()
    },
    methods: {
        async fetchMember() {
            

            try {
                const url =`${process.env.VUE_APP_API_BASE_URL}/member/myInfo`
                const token = localStorage.getItem('token')
                const response = await axios.get(url, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                })
                this.memberinfo = response.data;
            } catch(error) {
                const error_message = error.response
                if(error_message) {
                    alert(error_message.data.error_message)
                } else {
                    console.log(error);
                    alert("입력값 확인 필요")
                }
                
            }
        }
    }
}
</script>
