<template>
    <div class="container">
        <div class="page-header text-center" style="margin-top: 20px;">
            <h1>회원가입</h1>
        </div>
        <form  @submit.prevent="memberCreate">
            <div class="form-group">
                <label for="name">이름 :</label>
                <input type="text" v-model="name" class="form-control">
            </div>
            <div class="form-group">
                <label for="email">이메일 : </label>
                <input type="text" v-model="email" class="form-control">
            </div>
            <div class="form-group">
                <label for="password">비밀번호 :</label>
                <input type="password" v-model="password"  class="form-control">
            </div>
            <div class="form-group">
                <label for="city">도시 :</label>
                <input type="text" v-model="city"  class="form-control">
            </div>
            <div class="form-group">
                <label for="street">상세주소 :</label>
                <input type="text" v-model="street"  class="form-control">
            </div>
            <div class="form-group">
                <label for="zipcode">우편번호 :</label>
                <input type="text" v-model="zipcode"  class="form-control">
            </div>
            <div class="form-group">
                <p><button class="btn btn-primary mt-3" type="submit">회원가입</button></p>
            </div>
        </form>
    </div>
</template>

<script>

import axios from 'axios';


export default {
    data(){
        return{
            name: "",
            email: "",
            password: "",
            city: "",
            street: "",
            zipcode: ""
        }
    },
    methods: {
        async memberCreate() {
            try {
                //2가지 예외 가능성 : 200번대 상태값 token이 비어있는 경우
                const registerData = {
                    name:this.name,
                    email:this.email,
                    password:this.password,
                    city:this.city,
                    street:this.street,
                    zipcode:this.zipcode
                };
                const url = `${process.env.VUE_APP_API_BASE_URL}/member/create`;
                await axios.post(url, registerData)
                this.$router.push({name : "Login"})
                
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
    },
}
</script>
